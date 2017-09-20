package cn.deepmax.entityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultColumnNameMapper implements ColumnNameMapper {

    Map<Class, Map<String, String>> specialMap = new HashMap<>();

    /**
     * 将数据库名称转化成实体字段名称
     *
     * @param columnName
     * @return
     */
    @Override
    public String toEntityPropertyName(Class clazz,String columnName) {
        Objects.requireNonNull(columnName,"columnName is empty");

        return columnName;
    }

    /**
     * 特殊映射规定
     *
     * @param specialMap   <columName,propertyName>
     */
    @Override
    public void setSpecial(Map<Class, Map<String, String>> specialMap) {
        if(specialMap!=null){
            this.specialMap = specialMap;
        }
    }
}
