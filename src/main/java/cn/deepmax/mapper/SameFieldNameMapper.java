package cn.deepmax.mapper;

public class SameFieldNameMapper implements FieldNameMapper {
    @Override
    public String toDatabaseColumnName(Class<?> clazz, String fieldName) {
        return fieldName;
    }
}
