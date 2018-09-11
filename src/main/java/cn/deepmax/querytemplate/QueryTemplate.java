package cn.deepmax.querytemplate;


import cn.deepmax.pagehelper.PageInfo;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Including:
 * select list *4
 * select one   *4
 * select page  *4
 * select scalar
 * execute sql
 * batch operations
 * entity operations *3
 * transaction support
 */
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
     * @param converter
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql, Function<RowRecord,T> converter,Object... params);


    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<Map<String,Object>> selectPage(String sql, Integer pageNumber, Integer pageSize, Object... params);


    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<RowRecord> selectPageEx(String sql, Integer pageNumber, Integer pageSize, Object... params);

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
    <T> PageInfo<T> selectPage(String sql, Class<T> clazz, Integer pageNumber,Integer pageSize, Object... params);

    /**
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param converter
     * @param params
     * @param <T>
     * @return
     */
    <T> PageInfo<T> selectPage(String sql, Function<RowRecord,T> converter, Integer pageNumber, Integer pageSize, Object... params);

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
     * @param converter
     * @param params
     * @param <T>
     * @return
     */
    <T> T select(String sql, Function<RowRecord,T> converter, Object... params);


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
     * @param params
     * @return
     */
    int executeUpdate(String sql, Object... params);

    /**
     *
     * @param sql
     * @param paramList
     * @return
     */
    int[] executeBatch(String sql, List<List<Object>> paramList);

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
