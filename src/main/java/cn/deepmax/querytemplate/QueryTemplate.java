package cn.deepmax.querytemplate;


import cn.deepmax.pagehelper.PageInfo;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;
import java.util.List;
import java.util.Map;

public interface QueryTemplate {

    List<Map<String,Object>> selectList(String sql, Object... params);

    List<RowRecord> selectListEx(String sql, Object... params);

    <T> List<T> selectList(String sql, Class<T> clazz, Object... params);

    <T> List<RowRecord<T>> selectListEx(String sql, Class<T> clazz, Object... params);

    PageInfo<Map<String,Object>> selectPage(String sql,Integer pageNumber,Integer pageSize,Object... params);

    <T> PageInfo<T>  selectPage(String sql,Integer pageNumber,Integer pageSize,Class<T> clazz,Object... params);

    PageInfo<RowRecord> selectPageEx(String sql,Integer pageNumber,Integer pageSize,Object... params);

    <T> PageInfo<RowRecord<T>> selectPageEx(String sql,Class<T> clazz,Integer pageNumber,Integer pageSize,Object... params);

    Map<String,Object> select(String sql, Object... params);

    RowRecord selectEx(String sql, Object... params);

    <T> T select(String sql, Class<T> clazz, Object... params);

    <T> RowRecord<T> selectEx(String sql, Class<T> clazz, Object... params);

    <T> T selectScalar(String sql, Class<T> clazz, Object... params);

    int[] executeBatch(String sql, List<List<Object>> paramList);

    int executeUpdate(String sql,Object... params);

    <T> T get(Class<T> clazz,Object primary);

    /**
     * only support autoincrement primaryKey
     * @param obj
     * @return
     */
    Boolean save(Object obj);

    Boolean delete(Object obj);

    Transaction transaction();




}
