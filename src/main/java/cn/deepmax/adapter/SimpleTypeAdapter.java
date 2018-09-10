package cn.deepmax.adapter;

import cn.deepmax.support.CacheDataSupport;
import cn.deepmax.util.BeanToMap;
import java.beans.PropertyDescriptor;
import java.util.List;

public class SimpleTypeAdapter extends CacheDataSupport<String,ClassFieldTypeData> implements TypeAdapter {


    @Override
    public ClassFieldTypeData load(String uniqueKey) throws Exception {
        Class<?> clazz = Class.forName(uniqueKey);
        ClassFieldTypeData data = new ClassFieldTypeData();
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            String fieldName = it.getName();
            data.fieldDatabaseTypeMap.put(fieldName, it.getPropertyType());
            data.fieldEntityTypeMap.put(fieldName, it.getPropertyType());
        }
        return data;
    }

    @Override
    public Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value) {
        Class<?> typeClass = loadThen(entityClass.getName(),(data)->data.fieldEntityTypeMap.get(fieldName));
        return getCompatibleValue(typeClass, value);
    }

    @Override
    public Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName,  Object value) {
        Class<?> typeClass = loadThen(entityClass.getName(),(data)->data.fieldDatabaseTypeMap.get(fieldName));
        return getCompatibleValue(typeClass, value);
    }


}
