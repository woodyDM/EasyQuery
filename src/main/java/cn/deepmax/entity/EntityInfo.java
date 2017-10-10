package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.util.List;
import java.util.Map;

public interface EntityInfo {

    String getCatalogName(Class<?> clazz);

    String getTableName(Class<?> clazz);

    String getFullTableName(Class<?> clazz);

    /**
     * 包括主键名称
     * @param clazz
     * @return
     */
    Map<String,String> fieldNameToColumnNameMap(Class<?> clazz);

    List<String> beanFieldNameList(Class<?> clazz);

    Class<?> getPrimaryKeyFieldType(Class<?> clazz);

    String getPrimaryKeyFieldName(Class<?> clazz);

    Object getPrimaryKeyFieldValue(Object object);

    void setPrimaryKeyFieldValue(Object target,Object value);


}
