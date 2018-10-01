package cn.deepmax.adapter;

import cn.deepmax.support.CacheDataSupport;

import java.util.Objects;


public abstract class AbstractCacheableTypeAdapter extends CacheDataSupport<String,ClassFieldTypeData<Void>> implements TypeAdapter {

    @Override
    public Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value) {
        Class<?> typeClass = loadThen(entityClass.getName(),(data)->(Class<?>)data.getFieldType(fieldName));
        Objects.requireNonNull(typeClass, "Unable to find fieldType of class "+entityClass.getName()+
                " field :"+fieldName);
        return getCompatibleValue(typeClass, value);
    }

    @Override
    public Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName,  Object value) {
        Class<?> typeClass = loadThen(entityClass.getName(),(data)->(Class<?>)data.getDatabaseType(fieldName));
        Objects.requireNonNull(typeClass, "Unable to find databaseType of class "+entityClass.getName()+
                " field :"+fieldName);
        return getCompatibleValue(typeClass, value);
    }


}
