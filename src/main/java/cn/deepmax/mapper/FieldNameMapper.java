package cn.deepmax.mapper;

public interface FieldNameMapper {

    String toDatabaseColumnName(Class<?> clazz, String fieldName);

}
