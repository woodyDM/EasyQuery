package cn.deepmax.adapter;

import cn.deepmax.util.ForceTypeAdapter;

public interface TypeAdapter{


    Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Class<?> targetType,  Object value);


    Object getCompatibleDatabaseValue(Class<?> entityClass, String columnName, Class<?> targetType, Object value);


    default Object getCompatibleValue(Class<?> targetType,Object value){
        return ForceTypeAdapter.getCompatibleValue(targetType, value);
    }

}