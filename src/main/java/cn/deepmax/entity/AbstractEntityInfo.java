package cn.deepmax.entity;

import cn.deepmax.adapter.AbstractCacheableTypeAdapter;

import cn.deepmax.adapter.JpaAnnotatedTypeAdapter;
import cn.deepmax.adapter.SimpleTypeAdapter;
import cn.deepmax.adapter.TypeAdapter;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.support.CacheDataSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

/**
 * provide a abstract EntityInfo implementation to support cache.
 */
public abstract class AbstractEntityInfo extends CacheDataSupport<String, ClassMapperData> implements EntityInfo {


    public static Logger logger = LoggerFactory.getLogger(AbstractEntityInfo.class);
    protected  TypeAdapter typeAdapter = new JpaAnnotatedTypeAdapter();


    @Override
    public String getTableName(Class<?> clazz) {
        return checkLoadedThen(clazz, (data)-> data.tableName);
    }

    @Override
    public Map<String, String> fieldNameToColumnNameMap(Class<?> clazz) {
        return checkLoadedThen(clazz, (data)->data.toColumnMapper);
    }


    @Override
    public Map<String, String> columnNameToFieldNameMap(Class<?> clazz) {
        return checkLoadedThen(clazz,(data)->data.toFieldMapper);
    }


    @Override
    public List<String> beanFieldNameList(Class<?> clazz) {
        return checkLoadedThen(clazz,(data)->data.beanFieldNameList);
    }


    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return checkLoadedThen(clazz,(data)->data.primaryKeyDescriptor.getName());
    }

    @Override
    public Class<?> getPrimaryKeyFieldType(Class<?> clazz) {
        return checkLoadedThen(clazz,(data)->data.primaryKeyDescriptor.getPropertyType());
    }

    @Override
    public Object getPrimaryKeyFieldValue(Object object) {
        Objects.requireNonNull(object,"TargetObject is null.");
        Class<?> clazz = object.getClass();
        return checkLoadedThen(clazz,(data)->{
            PropertyDescriptor descriptor = data.primaryKeyDescriptor;
            return getPrimaryKeyFieldValue(object, descriptor.getReadMethod());
        });
    }

    private Object getPrimaryKeyFieldValue(Object target, Method getter){
        try {
            return getter.invoke(target);
        } catch (IllegalAccessException |InvocationTargetException e) {
            throw new EasyQueryException("unable to putIfAbsent primary key value.", e);
        }
    }

    @Override
    public void setPrimaryKeyFieldValue(Object target, Object value) {
        Objects.requireNonNull(target,"TargetObject is null.");
        Class<?> clazz = target.getClass();
        checkLoadedThen(clazz,(data)->{
            PropertyDescriptor descriptor = data.primaryKeyDescriptor;
            setPrimaryKeyFieldValue(target, value, descriptor.getWriteMethod());
            return null;
        });
    }

    private void setPrimaryKeyFieldValue(Object target,Object value, Method setter){

        Class<?> entityType = target.getClass();
        String idField = getPrimaryKeyFieldName(entityType);
        value = typeAdapter.getCompatibleFieldValue(entityType, idField, value);
        try {
            setter.invoke(target,value);
        } catch (IllegalAccessException  |InvocationTargetException e) {
            throw new EasyQueryException("unable to set primary key value.", e);
        }
    }



    /**
     * load if not loaded.
     * then action
     * @param clazz
     * @param function
     * @param <T>
     * @return
     */
    protected <T> T checkLoadedThen(Class<?> clazz, Function<ClassMapperData, T> function){
        String className = clazz.getName();
        return loadThen(className, function);
    }

    /**
     * do load action
     */
    @Override
    public ClassMapperData load(String className) throws Exception{
        Class<?> clazz = Class.forName(className);
        logger.debug("try to load class mapper data -->{}" , clazz.getName());
        String tableName = getTableNameInternal(clazz);
        String primaryKeyFieldName = getPrimaryKeyFieldNameInternal(clazz);
        PropertyDescriptor descriptor = null;
        try {
            descriptor = new PropertyDescriptor(primaryKeyFieldName, clazz);
        } catch (IntrospectionException e) {
            throw new EasyQueryException("Unable to create descriptor of class "+clazz.getName()+" primary field "+primaryKeyFieldName);
        }
        Map<String, String> toColumnMap = getFieldNameToColumnNameMap(clazz);
        if(!toColumnMap.containsKey(primaryKeyFieldName)){
            toColumnMap.put(primaryKeyFieldName, primaryKeyFieldName);
        }
        ClassMapperData data = new ClassMapperData();
        data.tableName = tableName;
        data.primaryKeyDescriptor = descriptor;
        toColumnMap.forEach((fieldName, columnName)->{
            data.addMapper(fieldName, columnName);
            data.appendFieldName(fieldName);
        });

        return data;
    }



    abstract String getPrimaryKeyFieldNameInternal(Class<?> clazz);
    abstract Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz);
    abstract String getTableNameInternal(Class<?> clazz);




}
