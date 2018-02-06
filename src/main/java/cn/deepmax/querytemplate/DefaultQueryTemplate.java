package cn.deepmax.querytemplate;

import cn.deepmax.entity.EntityFactory;
import cn.deepmax.entity.SqlTranslator;
import cn.deepmax.util.TypeAdapter;
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
import java.util.function.Function;


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

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String,Object>> selectList(String sql, Object... params){
        return doSelect(sql,params).last;
    }


    @Override
    public <T> List<T> selectList(String sql, Function<RowRecord, T> converter, Object... params) {
        return selectList(sql, null ,converter, params);
    }

    @Override
    public  <T> List<T> selectList(String sql, Class<T> clazz, Object... params) {
        return selectList(sql, clazz ,map-> entityFactory.create(clazz,map), params);
    }

    private <T> List<T> selectList(String sql, Class<T> clazz, Function<RowRecord, T> converter, Object... params) {
        Pair<DbMetaData,List<Map<String,Object>>> pair = doSelect(sql,params);
        List<Map<String,Object>> results = pair.last;
        List<T> entityList = new ArrayList<>();
        for(Map<String,Object> it:results){
            RowRecord temp = new RowRecord(it);
            T re = converter.apply(temp);
            entityList.add(re);
        }
        if(config.isGenerateClass() && clazz!=null){
            generator.generateIfNecessary(pair.first, clazz);
        }
        return entityList;
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<RowRecord> selectListEx(String sql, Object... params) {
        List<Map<String,Object>> rawResults = doSelect(sql,params).last;
        List<RowRecord> results = new ArrayList<>();
        for(Map<String,Object> it:rawResults){
            RowRecord oneRecord = new RowRecord (it);
            results.add(oneRecord);
        }
        return results;
    }

    @Override
    public PageInfo<Map<String, Object>> selectPage(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        return doSelectPage(sql, null, pageNumber, pageSize,
                result-> selectList((String)result.get(0),(Object[])result.get(2)),
                params);
    }


    @Override
    public PageInfo<RowRecord> selectPageEx(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        return doSelectPage(sql, null, pageNumber, pageSize,
                result-> selectListEx((String)result.get(0), (Object[])result.get(2)),
                params);
    }

    @Override
    public <T> PageInfo<T> selectPage(String sql, Class<T> clazz,Integer pageNumber, Integer pageSize,  Object... params) {
        return doSelectPage(sql, clazz, pageNumber, pageSize,
                result-> selectList((String)result.get(0),(Class<T>) result.get(1),(Object[])result.get(2)),
                params);
    }

    @Override
    public <T> PageInfo<T> selectPage(String sql, Integer pageNumber, Integer pageSize, Function<RowRecord, T> converter, Object... params) {
        return doSelectPage(sql, null , pageNumber, pageSize,
                result-> selectList((String)result.get(0),(Class<T>)result.get(1), converter, (Object[])result.get(2)),
                params);
    }


    private <T> PageInfo<T>  doSelectPage(String sql, Class<T> clazz, Integer pageNumber, Integer pageSize, Function<List<Object>,List<T>> generator, Object... params) {
        String totalSql = pagePlugin.getSqlForTotalRow(sql);
        Long totalRow = selectScalar(totalSql, Long.class, params);
        PageInfo<T> pageInfo = new PageInfo<>(pageNumber, pageSize, totalRow);
        String dataSql = pagePlugin.getSqlForPagingData(sql,pageInfo.getStartRow(), pageSize);
        List<Object> generatorParams = new ArrayList<>();
        generatorParams.add(dataSql);
        generatorParams.add(clazz);
        generatorParams.add(params);
        List<T> data = generator.apply(generatorParams);
        pageInfo.setData(data);
        return pageInfo;
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public Map<String,Object> select(String sql, Object... params){
        List<Map<String,Object>> results = selectList(sql, params);
        return handleUnique(results);
    }

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    @Override
    public  <T> T select(String sql, Class<T> clazz, Object... params) {
        List<T> results = selectList(sql, clazz, params);
        return handleUnique(results);
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public RowRecord selectEx(String sql, Object... params) {
        List<RowRecord> results = selectListEx(sql,params);
        return handleUnique(results);
    }


    @Override
    public <T> T select(String sql, Class<T> clazz, Function<RowRecord, T> converter, Object... params) {
        List<T> results = selectList(sql, clazz, converter, params);
        return handleUnique(results);
    }

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
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

    /**
     *
     * @param clazz
     * @param primary
     * @param <T>
     * @return
     */
    @Override
    public <T> T get(Class<T> clazz,Object primary){
        Objects.requireNonNull(primary,"PrimaryKey value is null, unable to get entity of type["+clazz.getName()+"]");
        String sql = sqlTranslator.getSelectSQLInfo(clazz);
        return select (sql,clazz,primary);
    }

    /**
     *
     * @param obj
     * @return
     */
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

    /**
     *
     * @param obj
     * @return
     */
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
