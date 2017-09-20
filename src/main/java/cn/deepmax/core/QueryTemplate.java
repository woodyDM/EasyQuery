package cn.deepmax.core;

import cn.deepmax.entity.Pair;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class QueryTemplate {

    DataSource dataSource;
    private ResultSetHandler resultSetHandler;


    public QueryTemplate(DataSource dataSource) {
        Objects.requireNonNull(dataSource,"dataSource is empty.");
        resultSetHandler = new DefaultResultSetHandler();
        this.dataSource = dataSource;
    }

    public QueryTemplate(DataSource dataSource, ResultSetHandler resultSetHandler) {
        this.dataSource = dataSource;
        this.resultSetHandler = resultSetHandler;
    }

    private Connection getCurrentConnection(){
        return DataSourceUtils.getConnection(dataSource);
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

    private Pair<List<Map<String,Object>>,ResultSetMetaData> rawSelect(String sql, Object... params){
        Connection cn = getCurrentConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = cn.prepareStatement(sql);
            setPrepareStatementParams(ps,params);
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            List<Map<String,Object>> resultDate = resultSetHandler.handle(rs);
            return new Pair<>(resultDate,metaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(ps,rs);
        }
    }

    private void setPrepareStatementParams(PreparedStatement ps,Object... params){
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setObject(i+1,params[i]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void close(Statement statement,ResultSet resultSet){
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
    }

}
