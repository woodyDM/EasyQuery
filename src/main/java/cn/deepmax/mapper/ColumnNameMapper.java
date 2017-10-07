package cn.deepmax.mapper;


import java.util.Map;

public interface ColumnNameMapper {

    /**
     * 将数据库名称转化成实体字段名称
     * @param clazz
     * @param columnName
     * @return
     */
    String toEntityPropertyName(Class clazz,String columnName);




}
