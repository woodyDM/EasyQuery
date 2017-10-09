package cn.deepmax.querytemplate;

import cn.deepmax.entity.EntityFactory;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.mapper.ColumnNameMapper;
import cn.deepmax.mapper.MapColumnNameMapper;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.model.Pair;
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

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);

    public DefaultQueryTemplate(Transaction transaction, EntityFactory entityFactory, boolean isShowSql) {
        this.transaction = transaction;
        this.entityFactory = entityFactory;
        this.isShowSql = isShowSql;
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
        return rawSelectEntity(sql,clazz,entityFactory,params);
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        ColumnNameMapper columnNameMapper = new MapColumnNameMapper(columnNameToFieldNameMap);
        return rawSelectEntity(sql,clazz,new EntityFactory(columnNameMapper),params);
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return rawSelectEntity(sql,clazz,new EntityFactory(columnNameMapper),params);
    }

    private <T> List<T> rawSelectEntity(String sql, Class<T> clazz,EntityFactory entityFactory, Object... params) {
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
        return null;
    }

    @Override
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return null;
    }

    @Override
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        return null;
    }
    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, Object... params) {
        return null;
    }


    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params) {
        return null;
    }

    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, Map<String, String> columnNameToFieldNameMap, Object... params) {
        return null;
    }



    @Override
    public <T> T selectScalar(String sql, Class<T> clazz, Object... params){
        return null;
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        } finally {
            closeResources(ps,rs,transaction);
        }
    }

    private void setPrepareStatementParams(PreparedStatement ps, Object... params){
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setObject(i+1,params[i]);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Setting preparestatement params error, check your parameters.",e);
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

    private Object handleUnique(List<?> list){
        if(list.size()==0){
            return null;
        }else if(list.size()>1){
            throw new EasyQueryException("Resultset is not unique.");
        }else{
            return list.get(0);
        }
    }
}
