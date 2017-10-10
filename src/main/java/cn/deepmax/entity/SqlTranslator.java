package cn.deepmax.entity;

import cn.deepmax.model.Pair;
import java.util.List;

/**
 * SqlTranslator supports QueryTemplates's entity operations, including
 * save,update,insert,delete
 * SqlTranslator requires an EntityInfo implement for working.
 */
public interface SqlTranslator {
    /**
     * get sql and sql array params for obj entity to insert.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getInsertSQL(Object obj);

    /**
     * get sql and sql array params for obj entity to update.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getUpdateSQL(Object obj);

    /**
     * get sql and sql array params for obj entity to select by primaryKey.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getSelectSQL(Object obj);

    /**
     * get sql and sql array params for obj entity to delete by primaryKey.
     * @param obj
     * @return
     */
    Pair<String,List<Object>> getDeleteSQL(Object obj);

    /**
     * return the EntityInfo implement.
     * @return
     */
    EntityInfo getEntityInfo();
}
