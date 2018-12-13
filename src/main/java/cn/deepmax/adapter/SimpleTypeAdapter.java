package cn.deepmax.adapter;

import cn.deepmax.util.BeanToMap;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * no type converted adapter.
 */
public class SimpleTypeAdapter extends AbstractCacheableTypeAdapter {

    @SuppressWarnings("unchecked")
    @Override
    public ClassFieldTypeData load(String uniqueKey) throws Exception {
        Class<?> clazz = Class.forName(uniqueKey);
        ClassFieldTypeData data = new ClassFieldTypeData();
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            String fieldName = it.getName();
            data.registerTypical(fieldName,it.getPropertyType());
        }
        return data;
    }
}
