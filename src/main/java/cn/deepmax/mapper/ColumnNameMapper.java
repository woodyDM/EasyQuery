package cn.deepmax.mapper;


import java.util.Map;

public interface ColumnNameMapper {

    /**
     * 将数据库名称转化成实体字段名称
     * @param columnName
     * @return
     */
    String toEntityPropertyName(String columnName);

    /**
     * 特殊映射规定
     * @param specialMap
     */
    void setSpecial(Map<Class,Map<String,String>> specialMap);

}
