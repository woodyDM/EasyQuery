package cn.deepmax.adapter;

import cn.deepmax.util.ForceTypeAdapter;

public interface TypeAdapter{


    Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value);


    Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName,  Object value);


    default Object getCompatibleValue(Class<?> targetType,Object value){
        return ForceTypeAdapter.getCompatibleValue(targetType, value);
    }

}