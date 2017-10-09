package cn.deepmax.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SameColumnNameMapper implements ColumnNameMapper {

    /**
     *
     * @param clazz
     * @param columnName
     * @return
     */
    @Override
    public String toEntityFieldName(Class<?> clazz, String columnName) {
        return columnName;
    }


}
