package cn.deepmax.mapper;


import java.util.Map;

public interface ColumnNameMapper {

    /**
     * convert columnName from database to entity fieldName
     * @param clazz
     * @param columnName
     * @return
     */
    String toEntityFieldName(Class<?> clazz, String columnName);




}
