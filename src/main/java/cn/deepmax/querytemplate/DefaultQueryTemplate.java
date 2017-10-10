package cn.deepmax.querytemplate;

import cn.deepmax.entity.EntityFactory;
import cn.deepmax.entity.SqlTranslator;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.mapper.ColumnNameMapper;
import cn.deepmax.mapper.MapColumnNameMapper;
import cn.deepmax.model.Pair;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * 查询接口，
 * 所有的都未释放连接
 */
public class DefaultQueryTemplate implements QueryTemplate {

    private Transaction transaction;
    private EntityFactory entityFactory;
    private boolean isShowSql;
    private SqlTranslator sqlTranslator;

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);

    public DefaultQueryTemplate(Transaction transaction, EntityFactory entityFactory, boolean isShowSql, SqlTranslator sqlTranslator) {
        this.transaction = transaction;
        this.entityFactory = entityFactory;
        this.isShowSql = isShowSql;
        this.sqlTranslator = sqlTranslator;
    }


    @Override
    public void setColumnNameMapper(ColumnNameMapper columnNameMapper) {
        entityFactory = new EntityFactory(columnNameMapper);
    }

    @Override
    public Transaction transaction() {
        return transaction;
    }

    @Override
    public List<Map<String,Object>> select(String sql, Object... params){
        return doSelect(sql,params);
    }

    public <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Object... params){
        return select(sql, clazz, entityFactory.getColumnNameMapper(), entityFactory, params);
    }

    @Override
    public <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        ColumnNameMapper columnNameMapper = new MapColumnNameMapper(columnNameToFieldNameMap);
        return select(sql,clazz,columnNameMapper,params);
    }
    @Override
    public <T> List<RowRecord<T>> select(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        EntityFactory tempFactory = new EntityFactory(columnNameMapper);
        return select(sql, clazz, columnNameMapper, tempFactory, params);
    }


    private  <T> List<RowRecord<T>> select(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper,EntityFactory entityFactory, Object... params) {
        List<Map<String,Object>> rawResults = doSelect(sql,params);
        List<RowRecord<T>> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            T obj = entityFactory.create(clazz,it);
            RowRecord<T> oneRecord = new RowRecord<>(it,clazz,obj,columnNameMapper);
            results.add(oneRecord);
        }
        return results;
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, Object... params) {
        return selectEntity(sql,clazz,entityFactory,params);
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        ColumnNameMapper columnNameMapper = new MapColumnNameMapper(columnNameToFieldNameMap);
        return selectEntity(sql,clazz,new EntityFactory(columnNameMapper),params);
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return selectEntity(sql,clazz,new EntityFactory(columnNameMapper),params);
    }

    private <T> List<T> selectEntity(String sql, Class<T> clazz, EntityFactory entityFactory, Object... params) {
        List<Map<String,Object>> rawResults = doSelect(sql,params);
        List<T> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            T obj = entityFactory.create(clazz,it);
            results.add(obj);
        }
        return results;
    }

    @Override
    public Map<String,Object> selectOne(String sql, Object... params){
        List<Map<String,Object>> results = select(sql, params);
        return (Map<String,Object>) handleUnique(results);
    }

    @Override
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Object... params){
        return selectOne(sql,clazz,entityFactory.getColumnNameMapper(),entityFactory,params);
    }

    @Override
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        ColumnNameMapper mapper = new MapColumnNameMapper(columnNameToFieldNameMap);
        return selectOne(sql,clazz, mapper,params);
    }

    @Override
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return selectOne(sql, clazz, columnNameMapper, new EntityFactory(columnNameMapper),params);
    }

    private  <T> RowRecord<T> selectOne(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, EntityFactory  entityFactory,Object... params) {
        Map<String,Object> rawResult = selectOne(sql,params);
        if(rawResult==null){
            return null;
        }
        T obj = entityFactory.create(clazz,rawResult);
        return new RowRecord<>(rawResult,clazz, obj,  columnNameMapper);
    }

    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, Object... params) {
        return selectOneEntity(sql,clazz,entityFactory,params);
    }

    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        return selectOneEntity(sql,clazz,new EntityFactory(new MapColumnNameMapper(columnNameToFieldNameMap)),params);
    }

    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return selectOneEntity(sql,clazz,new EntityFactory(columnNameMapper),params);
    }

    private  <T> T selectOneEntity(String sql, Class<T> clazz, EntityFactory entityFactory, Object... params) {
        Map<String,Object> rawResult = selectOne(sql,params);
        if(rawResult==null){
            return null;
        }
        return entityFactory.create(clazz,rawResult);
    }

    private Object handleUnique(List<?> list){
        if(list.size()==0){
            return null;
        }else if(list.size()>1){
            throw new EasyQueryException("Resultset is not unique.");
        }else{
            return list.get(0);
        }
    }


    @Override
    public Boolean save(Object obj){
        Pair<String,List<Object>> info = sqlTranslator.getInsertSQL(obj);
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(info.first,Statement.RETURN_GENERATED_KEYS);
            setPrepareStatementParams(ps,info.last.toArray());
            if(isShowSql){
                logger.debug("[insert] "+info.first);
            }
            int effectRow= ps.executeUpdate();
            if(effectRow==0){
                return false;
            }
            rs = ps.getGeneratedKeys();
            return setNextId(rs,obj);
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,rs, transaction);
        }
    }


    private Boolean setNextId(ResultSet rs,Object target) throws SQLException {
        Object nextId=null;
        if(rs!=null){
            if(rs.next()){
                nextId = rs.getObject(1);
            }
        }
        if(nextId!=null){
            sqlTranslator.getEntityInfo().setPrimaryKeyFieldValue(target,nextId);
            return true;
        }
        return false;

    }

    @Override
    public void update(Object obj) {
        Pair<String,List<Object>> pair = sqlTranslator.getUpdateSQL(obj);
        executeUpdate(pair.first, pair.last.toArray());
    }

    @Override
    public <T> T selectScalar(String sql, Class<T> clazz, Object... params) {
        Map<String,Object> oneResult = selectOne(sql,params);
        if(oneResult==null){
            return null;
        }
        if(oneResult.size()!=1){
            throw new EasyQueryException("Result is not a scalar.");
        }
        String key = null;
        for(String onekey:oneResult.keySet()){
            key = onekey;
        }
        Object obj = oneResult.get(key);
        return clazz.cast(obj);
    }

    @Override
    public <T> T selectScalar(String sql, String columnName, Class<T> clazz, Object... params){
        Map<String,Object> oneResult = selectOne(sql,params);
        if(oneResult==null){
            return null;
        }
        Object obj = oneResult.get(columnName);
        return clazz.cast(obj);
    }

    /**
     * 批量update insert delete执行
     * @param sql
     * @param paramList
     * @return
     */
    @Override
    public int[] executeBatch(String sql, List<List<Object>> paramList) {
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        try {
            ps = cn.prepareStatement(sql);
            for(List<Object> params:paramList){
                setPrepareStatementParams(ps,params.toArray());
                ps.addBatch();
            }
            if(isShowSql){
                logger.debug("[executeBatch] "+sql);
            }
            return ps.executeBatch();
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,null, transaction);
        }
    }

    /**
     * 单个update insert delete执行
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int executeUpdate(String sql,Object... params){
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        try {
            ps = cn.prepareStatement(sql);
            setPrepareStatementParams(ps,params);
            if(isShowSql){
                logger.debug("[executeUpdate] "+sql);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,null, transaction);
        }
    }

    /**
     * 查询语句执行
     * @param sql
     * @param params
     * @return
     */
    private List<Map<String,Object>> doSelect(String sql, Object... params){
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(sql);
            setPrepareStatementParams(ps,params);
            if(isShowSql){
                logger.debug("[select] "+sql);
            }
            rs = ps.executeQuery();
            return ResultSetHandler.handle(rs);

        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,rs,transaction);
        }
    }

    private void setPrepareStatementParams(PreparedStatement ps, Object... params){
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setObject(i+1,params[i]);
            } catch (SQLException e) {
                throw new EasyQueryException("Setting preparestatement params error, check your parameters.",e);
            }
        }
    }

    private void closeResources(Statement statement, ResultSet resultSet,Transaction transaction){
        if(statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                //Quitely
            }
        }
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                //Quitely
            }
        }
        if(!transaction.isTransactionMode()){
            transaction.close();
        }
    }


}
