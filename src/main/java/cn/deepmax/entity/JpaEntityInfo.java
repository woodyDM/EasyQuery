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
            String fieldName = propertyDescriptor.getName();
            if(!"class".equals(fieldName)){
                //if one field has propertyDescriptor, field is not null.
                Field field = getField(clazz,fieldName);
                Method getter = propertyDescriptor.getReadMethod();
                Column columnOnField =field.getAnnotation(Column.class);
                Column columnOnGetter =getter.getAnnotation(Column.class);
                String columnName = getColumnName(clazz,columnOnField,columnOnGetter);
                if(columnName!=null && columnName.length()!=0){
                    fieldNameToColumnNameMap.put(fieldName,columnName);
                }
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

        String pk = null;
        PropertyDescriptor[] propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String fieldName = propertyDescriptor.getName();
            if(!"class".equals(fieldName)){
                Field tempField =  getField(clazz,fieldName);
                Method getter = propertyDescriptor.getReadMethod();
                Id idOnField =tempField.getAnnotation(Id.class);
                Id idOnGetter =getter.getAnnotation(Id.class);

                if(idOnField!=null || idOnGetter!=null){
                    if(pk==null){
                        pk = fieldName;
                    }else{
                        throw new EasyQueryException("Duplicate @Id found in class["+clazz.getName()+"],check it and its superclass.");
                    }

                }

            }

        }
        if(pk==null){
            throw new EasyQueryException("@Id not found in class["+clazz.getName()+"],check it and its superclass.");
        }
        return pk;

    }







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
