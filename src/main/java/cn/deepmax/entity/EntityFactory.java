package cn.deepmax.entity;


import cn.deepmax.adapter.AbstractCacheableTypeAdapter;
import cn.deepmax.adapter.JpaAnnotatedTypeAdapter;
import cn.deepmax.adapter.SimpleTypeAdapter;
import cn.deepmax.adapter.TypeAdapter;
import cn.deepmax.util.BeanToMap;
import cn.deepmax.util.BeanUtils;
import cn.deepmax.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * factory to create entity.
 */
public class EntityFactory {


    private EntityInfo entityInfo;
    private TypeAdapter typeAdapter = new JpaAnnotatedTypeAdapter();

    public EntityFactory(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public void setEntityInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public void setTypeAdapter(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }

    /**
     * create entity
     * @param clazz     target entity type.
     * @param columnNameWithFieldValueMap    <columnName, value>
     * @param <T>
     * @return
     */
    public  <T> T create(Class<T> clazz, Map<String, Object> columnNameWithFieldValueMap) {
        if(clazz==null){
            return null;
        }
        T targetObj = BeanUtils.newInstance(clazz);
        Map<String,PropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(clazz);
        Map<String,String> columnNameToFieldMap = entityInfo.columnNameToFieldNameMap(clazz);
        for(Map.Entry<String,Object> entry:columnNameWithFieldValueMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = columnNameToFieldMap.get(key);

            if(StringUtils.isNotEmpty(fieldName)){
                PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(fieldName);
                if(propertyDescriptor!=null){
                    Method setterMethod = propertyDescriptor.getWriteMethod();
                    value = typeAdapter.getCompatibleFieldValue(clazz, fieldName, value);
                    setValue(targetObj, value, setterMethod);
                }
            }

        }
        return targetObj;
    }

    /**
     * return java type clazz, PropertyDescriptor
     * @param clazz
     * @return LinkedHashMap<String,PropertyDescriptor> key is fieldName, value is PropertyDescriptor
     */
    private Map<String,PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz){
        Map<String,PropertyDescriptor> propertyDescriptorMap = new LinkedHashMap<>();
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            propertyDescriptorMap.put(it.getName(),it);
        }
        return propertyDescriptorMap;
    }



    private void setValue(Object target,Object value,Method setter){
        try {
            setter.invoke(target,value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("can't set value of type "+value.getClass().getName() +" to entity "+target.getClass().getName()+" "+setter.getName());
        }
    }


}
