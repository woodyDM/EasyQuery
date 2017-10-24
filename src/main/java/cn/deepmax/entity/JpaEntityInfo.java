package cn.deepmax.entity;

import java.util.List;
import java.util.Map;

public class JpaEntityInfo extends AbstractEntityInfo {

    @Override
    List<String> getBeanFieldNameList(Class<?> clazz) {
        return null;
    }

    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
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
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return null;
    }
}
