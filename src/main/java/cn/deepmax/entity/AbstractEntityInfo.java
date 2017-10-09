package cn.deepmax.entity;

import java.util.List;
import java.util.Map;

public class AbstractEntityInfo implements EntityInfo {

    @Override
    public List<String> beanFieldNameList(Class<?> clazz) {
        return null;
    }

    @Override
    public String getCatalogName(Class<?> clazz) {
        return null;
    }

    @Override
    public String getTableName(Class<?> clazz) {
        return null;
    }

    @Override
    public Map<String, String> fieldNameToColumnNameMap(Class<?> clazz) {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return null;
    }

    @Override
    public boolean hasPrimaryKeyField(Class<?> clazz) {
        return false;
    }
}
