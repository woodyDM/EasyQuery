package cn.deepmax.querytemplate;

import cn.deepmax.mapper.ColumnNameMapper;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface QueryTemplate {

    List<Map<String,Object>> select(String sql, Object... params);
    <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Object... params);
    <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Map<String,String> columnNameToFieldNameMap, Object... params);
    <T> List<RowRecord<T>> select(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params);

    <T> List<T> selectEntity(String sql, Class<T> clazz, Object... params);
    <T> List<T> selectEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params);
    <T> List<T> selectEntity(String sql, Class<T> clazz, Map<String,String> columnNameToFieldNameMap, Object... params);

    Map<String,Object> selectOne(String sql, Object... params);
    <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Object... params);
    <T> RowRecord<T> selectOne(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper, Object... params);
    <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Map<String,String> columnNameToFieldNameMap,Object... params);
    <T> T selectOneEntity(String sql, Class<T> clazz, Object... params);
    <T> T selectOneEntity(String sql, Class<T> clazz, ColumnNameMapper columnNameMapper,Object... params);
    <T> T selectOneEntity(String sql, Class<T> clazz, Map<String,String> columnNameToFieldNameMap,Object... params);

    <T> T selectScalar(String sql, Class<T> clazz, Object... params);
    <T> T selectScalar(String sql, String columnName, Class<T> clazz, Object... params);

    void setColumnNameMapper(ColumnNameMapper columnNameMapper);
    Transaction transaction();
    int[] executeBatch(String sql, List<List<Object>> paramList);
    int executeUpdate(String sql,Object... params);

    Boolean save(Object obj);
    void update(Object obj);

}
