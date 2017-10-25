package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class JpaEntityInfo extends AbstractEntityInfo {


    @Override
    List<String> getBeanFieldNameList(Class<?> clazz) {
        Map<String,String> map = fieldNameToColumnNameMap(clazz);
        Set<String> field = map.keySet();
        return new ArrayList<>(field);
    }

    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> fieldNameToColumnNameMap = new LinkedHashMap<>();
        PropertyDescriptor[] propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            Field tempField = null;
            String fieldName = propertyDescriptor.getName();
            getField(clazz,fieldName,tempField);
            Method getter = propertyDescriptor.getReadMethod();
            Column columnOnField =tempField.getAnnotation(Column.class);
            Column columnOnGetter =getter.getAnnotation(Column.class);
            String columnName = getColumnName(clazz,columnOnField,columnOnGetter);
            if(columnName!=null && columnName.length()!=0){
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
        if(columnOnField!=null && columnOnGetter==null){
            return columnOnField.name();
        }
        if(columnOnField==null && columnOnGetter!=null){
            return columnOnGetter.name();
        }
        return null;
    }

    @Override
    public String getCatalogName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if(table==null){
            throw new EasyQueryException("class "+clazz.getName()+" should be @Table annotated.");
        }else{
            String catalog = table.catalog();
            if(catalog.length()==0){
                return null;
            }else{
                return catalog;
            }
        }
    }

    @Override
    public String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if(table==null){
            throw new EasyQueryException("class "+clazz.getName()+" should be @Table annotated.");
        }else{
            return table.name();
        }
    }

    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        boolean found = false;
//        String pk =
//        PropertyDescriptor[] propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
//        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
//            Field tempField = null;
//            String fieldName = propertyDescriptor.getName();
//            getField(clazz,fieldName,tempField);
//            Method getter = propertyDescriptor.getReadMethod();
//            Id IdOnField =tempField.getAnnotation(Id.class);
//            Id IdOnGetter =getter.getAnnotation(Id.class);
//
//            if(IdOnField!=null || IdOnGetter!=null){
//                found = true;
//            }
//        }

    }







    private void getField(Class<?> clazz, String fieldName,Field field){
        Class<?> superClass = clazz.getSuperclass();
        if(superClass!=null){
            getField(superClass,fieldName,field);
        }
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
