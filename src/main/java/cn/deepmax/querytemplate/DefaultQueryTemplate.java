package cn.deepmax.querytemplate;

import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.model.Pair;
import cn.deepmax.entity.EntityFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);
    private ResultSetHandler resultSetHandler;
    private Transaction transaction;
    private EntityFactory entityFactory;
    private boolean isShowSql;

    public DefaultQueryTemplate(ResultSetHandler resultSetHandler, Transaction transaction, EntityFactory entityFactory, boolean isShowSql) {

        this.resultSetHandler = resultSetHandler;
        this.transaction = transaction;
        this.entityFactory = entityFactory;
        this.isShowSql = isShowSql;

    }

    /**
     * 选出list
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Object... params){
        Pair<List<Map<String,Object>>,ResultSetMetaData> pair = rawSelect(sql,params);
        List<RowRecord<T>> list = new ArrayList<>();
        for(Map<String,Object> it:pair.first){
            RowRecord<T> temp = new RowRecord<>(it,clazz,pair.last);
            list.add(temp);
        }
        return list;
    }

    /**
     * 选出list
     * @param sql
     * @param params
     * @return
     */
    public List<Map<String,Object>> select(String sql, Object... params){
        return rawSelect(sql,params).first;
    }

    @Override
    public <T> List<T> selectEntity(String sql, Class<T> clazz, Object... params) {
        return null;
    }
    /**
     * 选出单个
     * @param sql
     * @param params
     * @return
     */
    public Map<String,Object> selectOne(String sql, Object... params){
        return null;
    }

    /**
     * 选出单个
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Object... params){
        return null;
    }
    @Override
    public <T> T selectOneEntity(String sql, Class<T> clazz, Object... params) {
        return null;
    }
    /**
     * 选向量
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public <T> T selectScalar(String sql, Class<T> clazz, Object... params){
        return null;
    }


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
            closeResources(ps,null,transaction);
        }
    }

    private Pair<List<Map<String,Object>>,ResultSetMetaData> rawSelect(String sql, Object... params){

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
            ResultSetMetaData metaData = rs.getMetaData();
            List<Map<String,Object>> resultDate = resultSetHandler.handle(rs);
            return new Pair<>(resultDate,metaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(ps,rs,transaction);
        }
    }

    @Override
    public Transaction transaction() {
        return transaction;
    }



    private void setPrepareStatementParams(PreparedStatement ps, Object... params){
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setObject(i+1,params[i]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void closeResources(Statement statement, ResultSet resultSet,Transaction transaction){
        if(statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!transaction.isTransactionMode()){
            transaction.close();
        }
    }

}
