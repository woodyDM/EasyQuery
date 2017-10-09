package cn.deepmax.entity;

import cn.deepmax.mapper.ColumnNameMapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityFactory {


    private ColumnNameMapper columnNameMapper;

    public EntityFactory(ColumnNameMapper columnNameMapper) {
        this.columnNameMapper = columnNameMapper;
    }

    public ColumnNameMapper getColumnNameMapper() {
        return columnNameMapper;
    }

    /**
     * 转化为实体
     * @param clazz         转化为实体的类型
     * @param columnNameWithFieldValueMap    <columnName, value> 需要为实体字段
     * @param <T>
     * @return
     */
    public  <T> T create(Class<T> clazz, Map<String, Object> columnNameWithFieldValueMap) {
        T obj = newInstance(clazz);
        Map<String,PropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(clazz);
        for(Map.Entry<String,Object> entry:columnNameWithFieldValueMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = columnNameMapper.toEntityFieldName(clazz,key);

            if(fieldName!=null && fieldName.length()!=0){
                PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(fieldName);
                if(propertyDescriptor!=null){
                    Method setter = propertyDescriptor.getWriteMethod();
                    Class<?> beanValueType = propertyDescriptor.getPropertyType();
                    setValue(obj, value, setter, beanValueType);
                }
            }
        }
        return obj;
    }


    private void setValue(Object target,Object value,Method setter,Class<?> beanValueType){
        if(isCompatibleType(value,beanValueType)){
            try {
                setter.invoke(target,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("can't set value of type "+value.getClass().getName() +" to entity "+target.getClass().getName()+" "+setter.getName());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("can't set value of type "+value.getClass().getName() +" to entity "+target.getClass().getName()+" "+setter.getName());
            }
        }else{
            throw new IllegalArgumentException("Target type is "+beanValueType.getName()+" with setter ["+setter.getName()+"], is not compatible with value type "+value.getClass().getName()+" from database ");
        }
    }

    private Map<String,PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz){
        BeanInfo beanInfo;
        try {
            Map<String,PropertyDescriptor> propertyDescriptorMap = new LinkedHashMap<>();
            beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor it:propertyDescriptors){
                propertyDescriptorMap.put(it.getName(),it);
            }
            return propertyDescriptorMap;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
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

    /** 是否类型一致
     * @param value The value to be passed into the setter method.
     * @param type The setter's parameter type (non-null)
     * @return boolean True if the value is compatible
     */
    private boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value) || matchesPrimitive(type, value.getClass())) {
            return true;
        }
        return false;
    }

    /**
     * @param targetType The primitive type to target.
     * @param valueType The value to match to the primitive type.
     * @return Whether <code>valueType</code> can be coerced (e.g. autoboxed) into <code>targetType</code>.
     */
    private boolean matchesPrimitive(Class<?> targetType, Class<?> valueType) {
        if (!targetType.isPrimitive()) {
            return false;
        }

        try {
            // see if there is a "TYPE" field.  This is present for primitive wrappers.
            Field typeField = valueType.getField("TYPE");
            Object primitiveValueType = typeField.get(valueType);
            if (targetType == primitiveValueType) {
                return true;
            }
        } catch (NoSuchFieldException e) {
            // lacking the TYPE field is a good sign that we're not working with a primitive wrapper.
            // we can't match for compatibility
        } catch (IllegalAccessException e) {
            // an inaccessible TYPE field is a good sign that we're not working with a primitive wrapper.
            // nothing to do.  we can't match for compatibility
        }
        return false;
    }

}
