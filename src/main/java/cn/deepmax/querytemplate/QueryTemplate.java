package cn.deepmax.querytemplate;


import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;
import java.util.List;
import java.util.Map;

public interface QueryTemplate {

    List<Map<String,Object>> select(String sql, Object... params);

    <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Object... params);

    <T> List<T> selectEntity(String sql, Class<T> clazz, Object... params);

    Map<String,Object> selectOne(String sql, Object... params);

    <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Object... params);

    <T> T selectOneEntity(String sql, Class<T> clazz, Object... params);

    <T> T selectScalar(String sql, Class<T> clazz, Object... params);

    <T> T selectScalar(String sql, String columnName, Class<T> clazz, Object... params);

    int[] executeBatch(String sql, List<List<Object>> paramList);

    int executeUpdate(String sql,Object... params);

    <T> T get(Class<T> clazz,Object primary);

    /**
     * only support autoincrement pk
     * @param obj
     * @return
     */
    Boolean save(Object obj);

    Boolean delete(Object obj);

    Transaction transaction();
}
