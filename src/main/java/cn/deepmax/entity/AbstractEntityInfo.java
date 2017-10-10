package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntityInfo implements EntityInfo {

    private Map<String,List<String>> beanFieldNameCache = new HashMap<>();
    private Map<String,Map<String,String>> fieldNameToColumnNameMapCache = new HashMap<>();
    private Map<String,PropertyDescriptor> primaryKeyPropertyDescriptorCache = new HashMap<>();

    abstract Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz);
    abstract List<String> getBeanFieldNameList(Class<?> clazz);

    @Override
    public String getFullTableName(Class<?> clazz) {
        String catalogName = getCatalogName(clazz);
        if(catalogName==null || catalogName.length()==0){
            return getTableName(clazz);
        }else{
            String tableName = getTableName(clazz);
            if(tableName==null||tableName.length()==0){
                throw new EasyQueryException("Can't get table name of entity with type["+clazz.getName()+"] .");
            }
            return catalogName+"."+getTableName(clazz);
        }
    }

    @Override
    public Map<String, String> fieldNameToColumnNameMap(Class<?> clazz) {
        Map<String, String> map = fieldNameToColumnNameMapCache.get(clazz.getName());
        if(map==null){
            map = getFieldNameToColumnNameMap(clazz);
            fieldNameToColumnNameMapCache.put(clazz.getName(),map);
        }
        return map;
    }

    @Override
    public List<String> beanFieldNameList(Class<?> clazz) {
        List<String> list = beanFieldNameCache.get(clazz.getName());
        if(list==null){
            list = getBeanFieldNameList(clazz);
            beanFieldNameCache.put(clazz.getName(),list);
        }
        return list;
    }

    @Override
    public Class<?> getPrimaryKeyFieldType(Class<?> clazz) {
         return getPrimaryKeyFieldPropertyDescriptor(clazz).getPropertyType();
    }

    @Override
    public Object getPrimaryKeyFieldValue(Object object) {
        Class<?> clazz = object.getClass();
        PropertyDescriptor descriptor = getPrimaryKeyFieldPropertyDescriptor(clazz);
        return getPrimaryKeyFieldValue(object,descriptor.getReadMethod());
    }

    @Override
    public void setPrimaryKeyFieldValue(Object target, Object value) {
        Class<?> clazz = target.getClass();
        PropertyDescriptor descriptor = getPrimaryKeyFieldPropertyDescriptor(clazz);
        setPrimaryKeyFieldValue(target,value,descriptor.getWriteMethod());
    }

    private PropertyDescriptor getPrimaryKeyFieldPropertyDescriptor(Class<?> clazz){
        PropertyDescriptor descriptor = primaryKeyPropertyDescriptorCache.get(clazz.getName());
        if(descriptor!=null){
            return descriptor;
        }
        try {
            descriptor = new PropertyDescriptor(getPrimaryKeyFieldName(clazz),clazz);
            primaryKeyPropertyDescriptorCache.put(clazz.getName(),descriptor);
            return descriptor;
        } catch (IntrospectionException e) {
            throw new EasyQueryException(e);
        }
    }

    private Object getPrimaryKeyFieldValue(Object target, Method getter){
        try {
            return getter.invoke(target);
        } catch (IllegalAccessException e) {
            throw new EasyQueryException(e);
        } catch (InvocationTargetException e) {
            throw new EasyQueryException(e);
        }
    }

    /**
     * 只支持 主键类型为Integer int Long long BigInteger String
     * @param target
     * @param value
     * @param setter
     */
    private void setPrimaryKeyFieldValue(Object target,Object value, Method setter){
        Class<?> targetType = getPrimaryKeyFieldType(target.getClass());
        if(!targetType.isInstance(value)){
            value = getCompatibleValue(targetType,value);
        }
        try {
            setter.invoke(target,value);
        } catch (IllegalAccessException e) {
            throw new EasyQueryException(e);
        } catch (InvocationTargetException e) {
            throw new EasyQueryException(e);
        }
    }

    private Object getCompatibleValue(Class<?> targetType, Object value){
        String v = value.toString();
        if(targetType==int.class || targetType==Integer.class){
            return Integer.valueOf(v);
        }else if(targetType==long.class || targetType==Long.class){
            return Long.valueOf(v);
        }else if(targetType==BigInteger.class){
            return BigInteger.valueOf(Long.valueOf(v));
        }else if(targetType==String.class){
            return v;
        }else{
            throw new EasyQueryException("PrimaryKey of type["+targetType.getName()+"] is not supported.");
        }
    }

}
