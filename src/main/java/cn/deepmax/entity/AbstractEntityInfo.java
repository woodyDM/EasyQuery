package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.util.TypeAdapter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * provide a abstract EntityInfo implementation  to support cache.
 */
public abstract class AbstractEntityInfo implements EntityInfo {

    private Map<String,PropertyDescriptor> primaryKeyPropertyDescriptorCache = new ConcurrentHashMap<>();
    private Map<String,List<String>> beanFieldNameCache = new ConcurrentHashMap<>();
    private Map<String,Map<String,String>> fieldNameToColumnNameMapCache = new ConcurrentHashMap<>();
    private Map<String,Map<String,String>> columnNameToFieldNameMapCache = new ConcurrentHashMap<>();
    private Map<String,String> fullTableNameCache = new ConcurrentHashMap<>();

    abstract List<String> getBeanFieldNameList(Class<?> clazz);
    abstract Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz);


    @Override
    public String getFullTableName(Class<?> clazz) {
        String fullTableName = fullTableNameCache.get(clazz.getName());
        if(fullTableName==null||fullTableName.length()==0){
            String catalogName = getCatalogName(clazz);
            if(catalogName==null || catalogName.length()==0){
                fullTableName = doGetTableName(clazz);
            }else{
                fullTableName = catalogName+"."+doGetTableName(clazz);
            }
            fullTableNameCache.put(clazz.getName(),fullTableName);
        }
        return fullTableName;
    }

    private String doGetTableName(Class<?> clazz){
        String tableName = getTableName(clazz);
        if(tableName==null||tableName.length()==0){
            throw new EasyQueryException("Unable to get table name of type["+clazz.getName()+"] .");
        }
        return tableName;
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
    public String fieldNameToColumnName(Class<?> clazz, String fieldName) {
        Map<String,String> map = fieldNameToColumnNameMap(clazz);
        if(map==null){
            return null;
        }else{
            return map.get(fieldName);
        }
    }

    @Override
    public Map<String, String> columnNameToFieldNameMap(Class<?> clazz) {
        Map<String, String> map = columnNameToFieldNameMapCache.get(clazz.getName());
        if(map==null){
            map = getColumnNameToFieldNameMap(clazz);
            columnNameToFieldNameMapCache.put(clazz.getName(),map);
        }
        return map;
    }

    @Override
    public String columnNameToFieldName(Class<?> clazz, String columnName) {
        Map<String,String> map = columnNameToFieldNameMap(clazz);
        if(map==null){
            return null;
        }else{
            return map.get(columnName);
        }
    }

    /**
     * just reverse fieldNameToColumnNameMap
     * @param clazz
     * @return
     */
    private Map<String, String> getColumnNameToFieldNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(Map.Entry<String,String> entry:fieldNameToColumnNameMap(clazz).entrySet()){
            map.put(entry.getValue(),entry.getKey());
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
        Objects.requireNonNull(object,"TargetObject is null.");
        Class<?> clazz = object.getClass();
        PropertyDescriptor descriptor = getPrimaryKeyFieldPropertyDescriptor(clazz);
        return getPrimaryKeyFieldValue(object,descriptor.getReadMethod());
    }

    @Override
    public void setPrimaryKeyFieldValue(Object target, Object value) {
        Objects.requireNonNull(target,"TargetObject is null.");
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

    private void setPrimaryKeyFieldValue(Object target,Object value, Method setter){
        Class<?> targetType = getPrimaryKeyFieldType(target.getClass());
        value = TypeAdapter.getCompatibleValue(targetType,value);
        try {
            setter.invoke(target,value);
        } catch (IllegalAccessException e) {
            throw new EasyQueryException(e);
        } catch (InvocationTargetException e) {
            throw new EasyQueryException(e);
        }
    }


}
