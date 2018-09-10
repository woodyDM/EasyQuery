package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.adapter.PropertyMapper;
import cn.deepmax.util.BeanToMap;
import cn.deepmax.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class JpaEntityInfo extends AbstractEntityInfo {


    @Override
    String getPrimaryKeyFieldNameInternal(Class<?> clazz) {
        String pk = null;
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String fieldName = propertyDescriptor.getName();
            Field tempField =  getField(clazz,fieldName);
            Method getter = propertyDescriptor.getReadMethod();
            Id idOnField =tempField.getAnnotation(Id.class);
            Id idOnGetter =getter.getAnnotation(Id.class);
            if(idOnField!=null || idOnGetter!=null){
                if(pk==null){
                    pk = fieldName;
                }else{
                    throw new EasyQueryException("Duplicate @Id found in class["+clazz.getName()+"], check it and its superclass.");
                }
            }
        }
        if(StringUtils.isEmpty(pk)){
            throw new EasyQueryException("@Id not found in class["+clazz.getName()+"], check it and its superclass.");
        }
        return pk;
    }

    @Override
    Map<String, Class<? extends PropertyMapper<?, ?>>> getConverts(Class<?> clazz) {
        return null;
    }

    @Override
    String getTableNameInternal(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if(table==null){
            throw new EasyQueryException("class "+clazz.getName()+" should be @Table annotated.");
        }else{
            String schema = table.schema();
            String tableName = table.name();
            if(StringUtils.isEmpty(tableName)){
                throw new EasyQueryException("class "+clazz.getName()+"@Table name is missing.");
            }
            if(StringUtils.isEmpty(schema)){
                return tableName;
            }else{
                return schema+"."+tableName;
            }
        }
    }

    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> fieldNameToColumnNameMap = new LinkedHashMap<>();
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String fieldName = propertyDescriptor.getName();
            //if one field has propertyDescriptor, field is not null.
            Field field = getField(clazz, fieldName);
            Method getter = propertyDescriptor.getReadMethod();
            Column columnOnField =field.getAnnotation(Column.class);
            Column columnOnGetter =getter.getAnnotation(Column.class);
            String columnName = getColumnName(clazz, columnOnField, columnOnGetter);
            if(StringUtils.isNotEmpty(columnName)){
                fieldNameToColumnNameMap.put(fieldName,columnName);
            }
        }
        return fieldNameToColumnNameMap;
    }

    private String getColumnName(Class<?> clazz,Column columnOnField,Column columnOnGetter){
        if(columnOnField!=null && columnOnGetter!=null){
            //check identity
            if(columnOnField.name().equals(columnOnGetter.name())){
                return columnOnField.name();
            }else{
                throw new EasyQueryException("Column name is not unique on field and getter in class["+clazz.getName()+"]");
            }
        }
        if(columnOnField!=null ){
            return columnOnField.name();
        }
        if(columnOnGetter!=null){
            return columnOnGetter.name();
        }
        return null;
    }

    /**
     * find Field from class and its superclass.
     * @param clazz
     * @param fieldName
     * @return
     */
    private Field getField(Class<?> clazz, String fieldName){
        Field field=null;
        try {
            field = clazz.getDeclaredField(fieldName);

        } catch (NoSuchFieldException e) {
            //not found
        }
        if(field!=null){
            return field;
        }else{
            Class<?> superClass = clazz.getSuperclass();
            if(superClass==null){
                return null;
            }else{
                return getField(superClass,fieldName);
            }
        }
    }

}
