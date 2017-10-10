package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.util.List;
import java.util.Map;

public interface EntityInfo {

    /**
     * entity corresponding database catalogName.
     * @param clazz
     * @return
     */
    String getCatalogName(Class<?> clazz);

    /**
     * entity corresponding database tableName.
     * @param clazz
     * @return
     */
    String getTableName(Class<?> clazz);

    /**
     * entity corresponding database tableName with catalogName
     * if catalogName is empty, return only tableName.
     * @param clazz
     * @return
     */
    String getFullTableName(Class<?> clazz);

    /**
     * entity fieldName to database columnName information,
     * fieldName including primarykey(PK) fieldName
     * @param clazz
     * @return
     */
    Map<String,String> fieldNameToColumnNameMap(Class<?> clazz);

    /**
     * entity fieldNames that are desired to persist
     * include primaryKey fieldName
     * @param clazz
     * @return
     */
    List<String> beanFieldNameList(Class<?> clazz);

    /**
     * entity primaryKey field java type
     * @param clazz
     * @return
     */
    Class<?> getPrimaryKeyFieldType(Class<?> clazz);

    /**
     * entity primaryKey fieldName
     * @param clazz
     * @return
     */
    String getPrimaryKeyFieldName(Class<?> clazz);

    /**
     * get object primaryKey value
     * @param object
     * @return
     */
    Object getPrimaryKeyFieldValue(Object object);

    /**
     * get object primaryKey value
     * @param target
     * @param value
     */
    void setPrimaryKeyFieldValue(Object target,Object value);


}
