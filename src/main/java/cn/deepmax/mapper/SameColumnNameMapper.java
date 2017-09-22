package cn.deepmax.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SameColumnNameMapper implements ColumnNameMapper {

    /**
     * 将数据库名称转化成实体字段名称
     *
     * @param clazz
     * @param columnName
     * @return
     */
    @Override
    public String toEntityPropertyName(Class clazz, String columnName) {
        return columnName;
    }

    /**
     * 特殊映射规定
     *
     * @param clazz
     * @param entityPropertyName
     */
    @Override
    public String toColumnName(Class clazz, String entityPropertyName) {
        return entityPropertyName;
    }
}
