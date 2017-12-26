package cn.deepmax.querytemplate;


import cn.deepmax.pagehelper.PageInfo;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;
import java.util.List;
import java.util.Map;

public interface QueryTemplate {

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    List<Map<String,Object>> selectList(String sql, Object... params);

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    List<RowRecord> selectListEx(String sql, Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql, Class<T> clazz, Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> List<RowRecord<T>> selectListEx(String sql, Class<T> clazz, Object... params);

    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<Map<String,Object>> selectPage(String sql,Integer pageNumber,Integer pageSize,Object... params);

    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> PageInfo<T>  selectPage(String sql,Class<T> clazz,Integer pageNumber,Integer pageSize,Object... params);

    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<RowRecord> selectPageEx(String sql,Integer pageNumber,Integer pageSize,Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param pageNumber
     * @param pageSize
     * @param params
     * @param <T>
     * @return
     */
    <T> PageInfo<RowRecord<T>> selectPageEx(String sql,Class<T> clazz,Integer pageNumber,Integer pageSize,Object... params);

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    Map<String,Object> select(String sql, Object... params);

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    RowRecord selectEx(String sql, Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> T select(String sql, Class<T> clazz, Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> RowRecord<T> selectEx(String sql, Class<T> clazz, Object... params);

    /**
     *
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    <T> T selectScalar(String sql, Class<T> clazz, Object... params);

    /**
     *
     * @param sql
     * @param paramList
     * @return
     */
    int[] executeBatch(String sql, List<List<Object>> paramList);

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    int executeUpdate(String sql,Object... params);

    /**
     *
     * @param clazz
     * @param primary
     * @param <T>
     * @return
     */
    <T> T get(Class<T> clazz,Object primary);

    /**
     * only support autoincrement primaryKey
     * @param obj
     * @return
     */
    Boolean save(Object obj);

    /**
     *
     * @param obj
     * @return
     */
    Boolean delete(Object obj);

    /**
     *
     * @return
     */
    Transaction transaction();




}
