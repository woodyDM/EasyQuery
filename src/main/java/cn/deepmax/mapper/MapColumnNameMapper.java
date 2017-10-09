package cn.deepmax.mapper;

import java.util.Map;

public class MapColumnNameMapper implements ColumnNameMapper {
    private Map<String,String> dbToPropertyMap;

    public MapColumnNameMapper(Map<String, String> columnNameToFieldNameMap) {
        this.dbToPropertyMap = columnNameToFieldNameMap;
    }

    /**
     *
     * @param clazz
     * @param columnName
     * @return
     */
    @Override
    public String toEntityFieldName(Class<?> clazz, String columnName) {
        return dbToPropertyMap.get(columnName);
    }
}
