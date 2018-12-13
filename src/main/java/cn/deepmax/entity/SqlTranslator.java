package cn.deepmax.entity;

import cn.deepmax.model.Pair;

import java.util.List;

/**
 * SqlTranslator supports QueryTemplates's entity operations, including
 * save,update,insert,delete
 * SqlTranslator requires an EntityInfo implementation for working.
 */
public interface SqlTranslator {
    /**
     * putIfAbsent sql and sql array params for obj entity to insert.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getInsertSQLInfo(Object obj);

    /**
     * putIfAbsent sql and sql array params for obj entity to update.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getUpdateSQLInfo(Object obj);

    /**
     * putIfAbsent sql for obj entity to selectList by primaryKey.
     * @param clazz
     * @return
     */
    String getSelectSQLInfo(Class<?> clazz);

    /**
     * putIfAbsent sql and primary key field value for obj entity to delete by primaryKey.
     * @param obj
     * @return
     */
    Pair<String,Object> getDeleteSQLInfo(Object obj);

    /**
     * return the EntityInfo implement.
     * @return
     */
    EntityInfo getEntityInfo();
}
