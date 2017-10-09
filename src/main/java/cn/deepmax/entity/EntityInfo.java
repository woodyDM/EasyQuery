package cn.deepmax.entity;

import java.util.List;
import java.util.Map;

public interface EntityInfo {

    String getCatalogName(Class<?> clazz);
    String getTableName(Class<?> clazz);
    Map<String,String> fieldNameToColumnNameMap(Class<?> clazz);
    List<String> beanFieldNameList(Class<?> clazz);
    String getPrimaryKeyFieldName(Class<?> clazz);
    boolean hasPrimaryKeyField(Class<?> clazz);

}
