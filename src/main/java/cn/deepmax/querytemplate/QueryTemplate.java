package cn.deepmax.querytemplate;

import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface QueryTemplate {


    <T> List<RowRecord<T>> select(String sql, Class<T> clazz, Object... params);
    <T> List<T> selectEntity(String sql, Class<T> clazz, Object... params);
    List<Map<String,Object>> select(String sql, Object... params);

    Map<String,Object> selectOne(String sql, Object... params);
    <T> RowRecord<T> selectOne(String sql, Class<T> clazz, Object... params);
    <T> T selectOneEntity(String sql, Class<T> clazz, Object... params);

    <T> T selectScalar(String sql, Class<T> clazz, Object... params);
    Transaction transaction();

    int execute(String sql,Object... params);
    boolean save(Object obj);
    boolean delete(Object obj);
}
