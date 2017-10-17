package cn.deepmax.entity;


import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * factory to create entity.
 */
public class EntityFactory {


    private EntityInfo entityInfo;

    public EntityFactory(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    /**
     * create entity
     * @param clazz     target entity type.
     * @param columnNameWithFieldValueMap    <columnName, value>
     * @param <T>
     * @return
     */
    public  <T> T create(Class<T> clazz, Map<String, Object> columnNameWithFieldValueMap) {
        T targetObj = newInstance(clazz);
        Map<String,PropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(clazz);
        for(Map.Entry<String,Object> entry:columnNameWithFieldValueMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = entityInfo.columnNameToFieldName(clazz,key);

            if(fieldName!=null && fieldName.length()!=0){
                PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(fieldName);
                if(propertyDescriptor!=null){
                    Method setterMethod = propertyDescriptor.getWriteMethod();
                    Class<?> beanFieldType = setterMethod.getParameterTypes()[0];
                    value = TypeAdapter.getCompatibleValue(beanFieldType,value);
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
        PropertyDescriptor[] propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            propertyDescriptorMap.put(it.getName(),it);
        }
        return propertyDescriptorMap;
    }

    private <T> T newInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("can't create new instance of type "+clazz.getName(),e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't create new instance of type "+clazz.getName(),e);
        }
    }

    private void setValue(Object target,Object value,Method setter){
        try {
            setter.invoke(target,value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't set value of type "+value.getClass().getName() +" to entity "+target.getClass().getName()+" "+setter.getName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("can't set value of type "+value.getClass().getName() +" to entity "+target.getClass().getName()+" "+setter.getName());
        }
    }


}
