package cn.deepmax.adapter;

import cn.deepmax.util.BeanToMap;

import java.beans.PropertyDescriptor;
import java.util.List;

public class SimpleTypeAdapter extends AbstractCacheableTypeAdapter {

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
}
