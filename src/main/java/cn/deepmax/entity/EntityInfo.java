package cn.deepmax.entity;


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
     * return a Map ,key is columnName from database and value is entity fieldName.
     * @param clazz
     * @return
     */
    Map<String,String> columnNameToFieldNameMap(Class<?> clazz);

    /**
     * get columnName of clazz and fieldName , possible to be null.
     * @param clazz
     * @param columnName
     * @return
     */
    String columnNameToFieldName(Class<?> clazz,String columnName);

    /**
     * entity fieldName to database columnName information,
     * @param clazz
     * @return
     */
    Map<String,String> fieldNameToColumnNameMap(Class<?> clazz);

    /**
     * get fieldName of clazz and ColumnName , possible to be null.
     * @param clazz
     * @param fieldName
     * @return
     */
    String fieldNameToColumnName(Class<?> clazz,String fieldName);

    /**
     * entity fieldNames that are desired to persist
     * include primaryKey fieldName
     * @param clazz
     * @return
     */
    List<String> beanFieldNameList(Class<?> clazz);

    /**
     * java type of entity primaryKey field .
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
     * get object primaryKey fieldValue
     * @param target notnull object
     * @param value
     */
    void setPrimaryKeyFieldValue(Object target,Object value);


}
