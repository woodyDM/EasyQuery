package cn.deepmax.entity;

import cn.deepmax.mapper.ColumnNameMapper;

import java.util.Map;

public class EntityFactory {


    private ColumnNameMapper columnNameMapper;

    public EntityFactory(ColumnNameMapper columnNameMapper) {
        this.columnNameMapper = columnNameMapper;
    }

    public  <T> T create(Class<T> clazz, Map<String, Object> columnNameWithPropertyValueMap) {
        return null;
    }
}
