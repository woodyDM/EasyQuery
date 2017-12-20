package cn.deepmax.querytemplate;

import cn.deepmax.entity.EntityFactory;
import cn.deepmax.entity.SqlTranslator;
import cn.deepmax.entity.TypeAdapter;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.generator.Generator;
import cn.deepmax.model.Config;
import cn.deepmax.model.DbMetaData;
import cn.deepmax.model.Pair;
import cn.deepmax.pagehelper.PageInfo;
import cn.deepmax.pagehelper.PagePlugin;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.*;

/**
 *
 *
 */
public class DefaultQueryTemplate implements QueryTemplate {

    private Transaction transaction;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private Config config;
    private Generator generator;
    private PagePlugin pagePlugin;
    private static final Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);


    public DefaultQueryTemplate(Transaction transaction, EntityFactory entityFactory, Config config, SqlTranslator sqlTranslator,PagePlugin pagePlugin) {
        Objects.requireNonNull(config);
        this.transaction = transaction;
        this.entityFactory = entityFactory;
        this.config = config;
        this.sqlTranslator = sqlTranslator;
        this.generator = new Generator(config);
        this.pagePlugin = pagePlugin;
    }


    @Override
    public Transaction transaction() {
        return transaction;
    }

    @Override
    public List<Map<String,Object>> selectList(String sql, Object... params){
        return doSelect(sql,params).last;
    }


    @Override
    public <T> List<RowRecord<T>> selectListEx(String sql, Class<T> clazz, Object... params) {
        Pair<DbMetaData,List<Map<String,Object>>> pair = doSelect(sql,params);
        List<Map<String,Object>> rawResults = pair.last;
        List<RowRecord<T>> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            T obj = entityFactory.create(clazz,it);
            if(it!=null) {
                RowRecord<T> oneRecord = new RowRecord<>(it,clazz,obj);
                results.add(oneRecord);
            }
        }
        if(config.isGenerateClass()){
            generator.generateIfNecessary(pair.first, clazz);
        }
        return results;
    }


    @Override
    public  <T> List<T> selectList(String sql, Class<T> clazz, Object... params) {
        Pair<DbMetaData,List<Map<String,Object>>> pair = doSelect(sql,params);
        List<Map<String,Object>> rawResults = pair.last;
        List<T> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            T obj = entityFactory.create(clazz,it);
            results.add(obj);
        }
        if(config.isGenerateClass()){
            generator.generateIfNecessary(pair.first, clazz);
        }
        return results;
    }

    @Override
    public List<RowRecord> selectListEx(String sql, Object... params) {
        List<Map<String,Object>> rawResults = doSelect(sql,params).last;
        List<RowRecord> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            if(it!=null){
                RowRecord oneRecord = new RowRecord<>(it,null,null);
                results.add(oneRecord);
            }
        }
        return results;
    }

    @Override
    public PageInfo<Map<String, Object>> selectPage(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        String totalSql = pagePlugin.getSqlForTotalRow(sql);
        Long totalRow = selectScalar(totalSql,Long.class);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(pageNumber, pageSize, totalRow);
        String dataSql = pagePlugin.getSqlForPagingData(sql,pageInfo.getStartRow(),pageSize);
        List<Map<String,Object>> data = selectList(dataSql,params);
        pageInfo.setData(data);
        return pageInfo;
    }

    @Override
    public PageInfo<RowRecord> selectPageEx(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        String totalSql = pagePlugin.getSqlForTotalRow(sql);
        Long totalRow = selectScalar(totalSql,Long.class);
        PageInfo<RowRecord> pageInfo = new PageInfo<>(pageNumber, pageSize, totalRow);
        String dataSql = pagePlugin.getSqlForPagingData(sql,pageInfo.getStartRow(),pageSize);
        List<RowRecord> data = selectListEx(dataSql,params);
        pageInfo.setData(data);
        return pageInfo;
    }

    @Override
    public <T> PageInfo<RowRecord<T>> selectPageEx(String sql, Class<T> clazz, Integer pageNumber, Integer pageSize, Object... params) {
        String totalSql = pagePlugin.getSqlForTotalRow(sql);
        Long totalRow = selectScalar(totalSql,Long.class);
        PageInfo<RowRecord<T>> pageInfo = new PageInfo<>(pageNumber, pageSize, totalRow);
        String dataSql = pagePlugin.getSqlForPagingData(sql,pageInfo.getStartRow(),pageSize);
        List<RowRecord<T>> data = selectListEx(dataSql,clazz,params);
        pageInfo.setData(data);
        return pageInfo;
    }

    @Override
    public Map<String,Object> select(String sql, Object... params){
        List<Map<String,Object>> results = selectList(sql, params);
        return handleUnique(results);
    }


    @Override
    public  <T> RowRecord<T> selectEx(String sql, Class<T> clazz, Object... params) {
        List<RowRecord<T>> result = selectListEx(sql, clazz, params);
        return handleUnique(result);
    }


    @Override
    public  <T> T select(String sql, Class<T> clazz, Object... params) {
        List<T> results = selectList(sql,clazz,params);
        return handleUnique(results);
    }

    @Override
    public RowRecord selectEx(String sql, Object... params) {
        Map<String,Object> rawResult = select(sql,params);
        if(rawResult==null){
            return null;
        }
        return new RowRecord<>(rawResult,null, null );
    }

    @Override
    public <T> T selectScalar(String sql, Class<T> clazz, Object... params) {
        Map<String,Object> oneResult = select(sql,params);
        if(oneResult==null){
            return null;
        }
        if(oneResult.size()!=1){
            throw new EasyQueryException("Result is not a scalar.");
        }
        String key = null;
        for(String oneKey:oneResult.keySet()){
            key = oneKey;
        }
        Object obj = oneResult.get(key);
        return (T)TypeAdapter.getCompatibleValue(clazz,obj);
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
            if(config.isShowSql()){
                logger.debug("[executeBatch] {}",sql);
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
            if(config.isShowSql()){
                logger.debug("[executeUpdate] {}",sql);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,null, transaction);
        }
    }

    @Override
    public <T> T get(Class<T> clazz,Object primary){
        Objects.requireNonNull(primary,"PrimaryKey value is null, unable to get entity of type["+clazz.getName()+"]");
        String sql = sqlTranslator.getSelectSQLInfo(clazz);
        return select (sql,clazz,primary);
    }

    @Override
    public Boolean save(Object obj){
        Objects.requireNonNull(obj,"Target is null, unable to save.");
        if(!isPrimaryKeyValueExist(obj)){
            return doSave(obj);
        }else{
            return doUpdate(obj);
        }
    }

    private Boolean doSave(Object obj){
        Pair<String,List<Object>> info = sqlTranslator.getInsertSQLInfo(obj);
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(info.first,Statement.RETURN_GENERATED_KEYS);
            setPrepareStatementParams(ps,info.last.toArray());
            if(config.isShowSql()){
                logger.debug("[insert] {}",info.first);
            }
            int effectRow= ps.executeUpdate();
            if(effectRow==0){
                return false;
            }
            rs = ps.getGeneratedKeys();
            return setEntityPrimaryKeyValue(rs,obj);
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps,rs, transaction);
        }
    }

    private Boolean doUpdate(Object obj) {
        Pair<String,List<Object>> pair = sqlTranslator.getUpdateSQLInfo(obj);
        int i =executeUpdate(pair.first, pair.last.toArray());
        return (i!=0);
    }

    @Override
    public Boolean delete(Object obj){
        Objects.requireNonNull(obj, "Deleted object should not be null");
        Pair<String, Object> pair = sqlTranslator.getDeleteSQLInfo(obj);
        int i =executeUpdate(pair.first, pair.last);
        return (i!=0);
    }

    private Boolean setEntityPrimaryKeyValue(ResultSet rs, Object target) throws SQLException {
        Object nextId=null;
        if(rs!=null){
            if(rs.next()){
                //test on mysql 5.6, nextId would be type of Long.class,
                // but still use EntityInfo to set its primaryKey value.
                nextId = rs.getObject(1);
            }
        }
        if(nextId!=null){
            sqlTranslator.getEntityInfo().setPrimaryKeyFieldValue(target,nextId);
            return true;
        }
        return false;
    }

    private Boolean isPrimaryKeyValueExist(Object obj){
        return (sqlTranslator.getEntityInfo().getPrimaryKeyFieldValue(obj)!=null);
    }

    private <T> T handleUnique(List<T> list){
        if(list.size()==0){
            return null;
        }else if(list.size()>1){
            throw new EasyQueryException("ResultSet is not unique, try using selectList method.");
        }else{
            return list.get(0);
        }
    }

    /**
     * 查询语句执行
     * @param sql
     * @param params
     * @return
     */
    private Pair<DbMetaData,List<Map<String,Object>>> doSelect(String sql, Object... params){
        Objects.requireNonNull(sql,"Sql should not be null");
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(sql);
            setPrepareStatementParams(ps,params);
            if(config.isShowSql()){
                logger.debug("[select] {}",sql);
            }
            rs = ps.executeQuery();
            return ResultSetHandler.handle(rs,config.isGenerateClass());

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
                throw new EasyQueryException("Setting prepareStatement params error, check your parameters.",e);
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
