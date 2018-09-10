package cn.deepmax.entity;


import cn.deepmax.adapter.PropertyMapper;

import java.util.List;
import java.util.Map;

public interface EntityInfo {


    /**
     * entity corresponding database tableName with catalogName
     * if catalogName is empty, return only tableName.
     * @param clazz
     * @return
     */
    String getTableName(Class<?> clazz);


    /**
     * entity fieldName to database columnName information,
     * @param clazz
     * @return
     */
    Map<String,String> fieldNameToColumnNameMap(Class<?> clazz);


    /**
     * return a Map ,key is columnLabelName from database and value is entity fieldName.
     * @param clazz
     * @return
     */
    Map<String,String> columnNameToFieldNameMap(Class<?> clazz);


    /**
     * Entity fieldNames that are desired to persist
     * including primaryKey fieldName
     * @param clazz
     * @return
     */
    List<String> beanFieldNameList(Class<?> clazz);



    /**
     * entity primaryKey fieldName
     * @param clazz
     * @return
     */
    String getPrimaryKeyFieldName(Class<?> clazz);

    /**
     * java type of entity primaryKey field .
     * @param clazz
     * @return
     */
    Class<?> getPrimaryKeyFieldType(Class<?> clazz);



    /**
     * get object primaryKey value
     * @param object
     * @return
     */
    Object getPrimaryKeyFieldValue(Object object);

    /**
     * set object primaryKey fieldValue
     * @param target notnull object
     * @param value
     */
    void setPrimaryKeyFieldValue(Object target,Object value);


}
