package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * used to convert a javabean to LinkedHashMap<String,Object>
 */
public class BeanToMap {

    public static Map<String,Object> convert(Object bean){
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptor(bean.getClass());
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        for(PropertyDescriptor descriptor:propertyDescriptors){
            String key = descriptor.getName();
            if(!"class".equals(key)){
                Method getter = descriptor.getReadMethod();
                Object value = getFieldValue(getter,bean);
                map.put(key, value);
            }
        }
        return map;
    }

    public static PropertyDescriptor[] getPropertyDescriptor(Class<?> clazz){
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new EasyQueryException(e);
        }
    }
    private static Object getFieldValue(Method getter,Object target){
        try {
            Object value = getter.invoke(target);
            return value;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }


}
