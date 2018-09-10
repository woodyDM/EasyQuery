package cn.deepmax.util;

import cn.deepmax.exception.EasyQueryException;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * used to convert a javabean to LinkedHashMap<String,Object>
 */
public class BeanToMap {

    /**
     * convert java bean to LinkedHashMap<String,Object>
     * @param bean
     * @return  LinkedHashMap<String,Object>
     */
    public static Map<String,Object> convert(Object bean){
        List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptor(bean.getClass());
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        for(PropertyDescriptor descriptor:propertyDescriptors){
            String key = descriptor.getName();
            Method getter = descriptor.getReadMethod();
            Object value = getFieldValue(getter,bean);
            map.put(key, value);
        }
        return map;
    }


    public static List<PropertyDescriptor> getPropertyDescriptor(Class<?> clazz){
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            return Arrays.stream(propertyDescriptors).filter((it)->!it.getName().equals("class")).collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new EasyQueryException("Unable to putIfAbsent PropertyDescriptor of class "+clazz.getName(), e);
        }
    }

    private static Object getFieldValue(Method getter,Object target){
        try {
            Object value = getter.invoke(target);
            return value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EasyQueryException("Unable to call ["+getter.getName()+"] to putIfAbsent value of object"+target.getClass().getName(), e);
        }
    }


}
