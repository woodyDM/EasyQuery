package cn.deepmax.adapter;

public class SimpleTypeAdapter implements TypeAdapter {

    @Override
    public Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Class<?> targetType, Object value) {
        return getCompatibleValue(targetType, value);
    }

    @Override
    public Object getCompatibleDatabaseValue(Class<?> entityClass, String columnName, Class<?> targetType, Object value) {
        return getCompatibleValue(targetType, value);
    }

}
