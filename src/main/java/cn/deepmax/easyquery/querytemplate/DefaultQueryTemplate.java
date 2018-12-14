package cn.deepmax.easyquery.querytemplate;

import cn.deepmax.easyquery.adapter.TypeAdapter;
import cn.deepmax.easyquery.exception.EasyQueryException;
import cn.deepmax.easyquery.model.Pair;
import cn.deepmax.easyquery.pagehelper.PageInfo;
import cn.deepmax.easyquery.pagehelper.PagePlugin;
import cn.deepmax.easyquery.resultsethandler.ResultSetHandler;
import cn.deepmax.easyquery.resultsethandler.RowRecord;
import cn.deepmax.easyquery.test.EntityFactory;
import cn.deepmax.easyquery.test.SqlTranslator;
import cn.deepmax.easyquery.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


/**
 * Default
 *
 */
public class DefaultQueryTemplate implements QueryTemplate {

    private Transaction transaction;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private PagePlugin pagePlugin;
    private boolean isShowSql;
    private TypeAdapter typeAdapter;

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);

    public DefaultQueryTemplate(Transaction transaction, EntityFactory entityFactory, SqlTranslator sqlTranslator,PagePlugin pagePlugin,boolean isShowSql ) {
        this.transaction = transaction;
        this.entityFactory = entityFactory;
        this.sqlTranslator = sqlTranslator;
        this.pagePlugin = pagePlugin;
        this.isShowSql = isShowSql;

    }

    public void setTypeAdapter(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }


    @Override
    public <T> List<T> selectList(SqlQuery<T> query) {
        return selectList(query.toSql(), query.getTargetClass(), query.toParameters());
    }

    @Override
    public List<Map<String,Object>> selectListMap(SqlQuery  query){
        return selectList(query.toSql(), query.toParameters());
    }

    @Override
    public  <T> List<T> selectList(String sql, Class<T> clazz, Object... params) {
        checkTargetClass(clazz, "selectList");
        List<T> result = selectList(sql, map -> entityFactory.create(clazz, map), params);
        return result;
    }


    @Override
    public <T> List<T> selectList(String sql, Function<RowRecord, T> converter, Object... params) {
        List<? extends Map<String,Object>> results = selectListInternal(sql, params);
        List<T> entityList = new ArrayList<>();
        for(Map<String,Object> it : results){
            RowRecord temp = (RowRecord)it;
            T re = converter.apply(temp);
            entityList.add(re);
        }
        return entityList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RowRecord> selectListEx(String sql, Object... params) {
        List<? extends Map<String, Object>> list = selectListInternal(sql, params);
        return (List<RowRecord>)list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String,Object>> selectList(String sql, Object... params){
        List<? extends Map<String, Object>> list = selectListInternal(sql, params);
        return (List<Map<String,Object>>)list;
    }

    private List<? extends Map<String,Object>> selectListInternal(String sql, Object... params){
        Objects.requireNonNull(sql,"Sql should not be null");
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(sql);
            setPrepareStatementParams(ps,params);
            if(isShowSql){
                logger.debug("[select] {}",sql);
            }
            rs = ps.executeQuery();
            return ResultSetHandler.handle(rs);
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        } finally {
            closeResources(ps, rs, transaction);
        }
    }



    @Override
    public PageInfo<Map<String, Object>> selectPage(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        return selectPageInternal(sql,  pageNumber, pageSize,
                innerSql-> selectList(innerSql, params),
                params);
    }


    @Override
    public PageInfo<RowRecord> selectPageEx(String sql, Integer pageNumber, Integer pageSize, Object... params) {
        return selectPageInternal(sql,  pageNumber, pageSize,
                innerSql-> selectListEx(innerSql, params),
                params);
    }

    @Override
    public <T> PageInfo<T> selectPage(String sql, Class<T> clazz,Integer pageNumber, Integer pageSize,  Object... params) {
        return selectPageInternal(sql,  pageNumber, pageSize,
                innerSql-> selectList(innerSql ,clazz, params),
                params);
    }

    @Override
    public <T> PageInfo<T> selectPage(String sql, Function<RowRecord, T> converter, Integer pageNumber, Integer pageSize,  Object... params) {
        return selectPageInternal(sql, pageNumber, pageSize,
                innerSql -> selectList(innerSql, converter, params),
                params);
    }



    private <T> PageInfo<T> selectPageInternal(String sql, Integer pageNumber, Integer pageSize, Function<String, List<T>> closure, Object... params) {
        String totalSql = pagePlugin.getSqlForTotalRow(sql);
        Long totalRow = selectScalar(totalSql, Long.class, params);
        PageInfo<T> pageInfo = new PageInfo<>(pageNumber, pageSize, totalRow);
        if(totalRow.equals(0L)){
            List<T> data = new ArrayList<>();
            pageInfo.setData(data);
        }else{
            String dataSql = pagePlugin.getSqlForPagingData(sql,pageInfo.getStartRow(), pageSize);
            List<T> data = closure.apply(dataSql);
            pageInfo.setData(data);
        }
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
        List<Map<String, Object>> results = selectList(sql, params);
        return handleUnique(results);
    }

    @Override
    public RowRecord selectEx(String sql, Object... params) {
        List<RowRecord> results = selectListEx(sql,params);
        return handleUnique(results);
    }


    @Override
    public  <T> T select(String sql, Class<T> clazz, Object... params) {
        List<T> results = selectList(sql, clazz, params);
        return handleUnique(results);
    }


    @Override
    public <T> T select(String sql, Function<RowRecord, T> converter, Object... params) {
        List<T> results = selectList(sql, converter, params);
        return handleUnique(results);
    }

    /**
     * @param query
     * @return
     */
    @Override
    public <T> T select(SqlQuery<T> query) {
        List<T> results = selectList(query);
        return handleUnique(results);
    }

    /**
     * @param query
     * @return
     */
    @Override
    public <T> T selectScalar(SqlQuery<T> query) {
        Class<T> targetClazz = query.getTargetClass();
        return selectScalar(query.toSql(), targetClazz, query.toParameters());
    }

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectScalar(String sql, Class<T> clazz, Object... params) {
        checkTargetClass(clazz, "selectScalar");
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
        return (T) typeAdapter.getCompatibleValue(clazz,obj);
    }



    /**
     * batch -> update insert delete
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
     * single -> update insert delete
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
     *  putIfAbsent operation.
     * @param clazz
     * @param primary
     * @param <T>
     * @return
     */
    @Override
    public <T> T get(Class<T> clazz,Object primary){
        Objects.requireNonNull(primary,"PrimaryKey value is null, unable to putIfAbsent entity of type["+clazz.getName()+"]");
        String sql = sqlTranslator.getSelectSQLInfo(clazz);
        return select (sql,clazz,primary);
    }

    /**
     * save operation
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

    /**
     * when obj primaryKey value not exist ,do save, and generate increment id..
     * @param obj
     * @return
     */
    private Boolean doSave(Object obj){
        Pair<String,List<Object>> info = sqlTranslator.getInsertSQLInfo(obj);
        Connection cn = transaction.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(info.first,Statement.RETURN_GENERATED_KEYS);
            setPrepareStatementParams(ps,info.last.toArray());
            if(isShowSql){
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

    /**
     * when obj primaryKey value exist ,do update.
     * @param obj
     * @return
     */
    private Boolean doUpdate(Object obj) {
        Pair<String,List<Object>> pair = sqlTranslator.getUpdateSQLInfo(obj);
        int i =executeUpdate(pair.first, pair.last.toArray());
        return (i!=0);
    }

    /**
     *  delete obj by its primaryKey value.
     *  if primaryKey value is null , raise an exception
     * @param obj
     * @return
     */
    @Override
    public Boolean delete(Object obj){
        Objects.requireNonNull(obj, "Deleted object should not be null");
        Pair<String, Object> pair = sqlTranslator.getDeleteSQLInfo(obj);
        int i = executeUpdate(pair.first, pair.last);
        return (i!=0);
    }

    @Override
    public Transaction transaction() {
        return transaction;
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

    /**
     * whethere the object primaryKey value exist.
     * @param obj
     * @return
     */
    private Boolean isPrimaryKeyValueExist(Object obj){
        return (sqlTranslator.getEntityInfo().getPrimaryKeyFieldValue(obj)!=null);
    }

    /**
     * used to support unique select.
     * @param list
     * @param <T>
     * @return null if not found. if found more than one result ,cause exception.
     */
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
     * To set parameters in prepareStatement
     * @param ps
     * @param params
     */
    private void setPrepareStatementParams(PreparedStatement ps, Object... params){
        for (int i = 0; i < params.length; i++) {
            try {

                ps.setObject(i+1,params[i]);
            } catch (SQLException e) {
                throw new EasyQueryException("Setting prepareStatement params error, check your parameters.",e);
            }
        }
    }

    /**
     * close used resources.
     * but in self-managed transaction mode, transaction will not close.
     * @param statement
     * @param resultSet
     * @param transaction
     */
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
        if(transaction.needClose()){
            transaction.close();
        }
    }

    /**
     * check
     * @param clazz
     * @param methodName
     */
    private void checkTargetClass(Class<?> clazz, String methodName){
        if(clazz==null){
            throw new IllegalArgumentException("targetClass must be set when using "+methodName+" method.");
        }
    }

}
