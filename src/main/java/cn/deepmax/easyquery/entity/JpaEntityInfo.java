package cn.deepmax.easyquery.entity;

import cn.deepmax.easyquery.adapter.TypeAdapter;
import cn.deepmax.easyquery.exception.EasyQueryException;
import cn.deepmax.easyquery.util.BeanToMap;
import cn.deepmax.easyquery.util.BeanUtils;
import cn.deepmax.easyquery.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JpaEntityInfo extends AbstractEntityInfo {

    public static final Logger logger = LoggerFactory.getLogger(JpaEntityInfo.class);


    public JpaEntityInfo(TypeAdapter typeAdapter) {
        super(typeAdapter);
    }

    @Override
    String getPrimaryKeyFieldNameInternal(Class<?> clazz) {
        String pk = null;
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String fieldName = propertyDescriptor.getName();
            Field tempField = BeanUtils.getField(clazz,fieldName);
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
            Field field = BeanUtils.getField(clazz, fieldName);
            Method getter = propertyDescriptor.getReadMethod();
            if(isTransient(field, getter)){
                logger.debug("@Transient field found on field [{}] of class [{}]", fieldName, clazz.getName());
                continue;
            }
            Column columnOnField =field.getAnnotation(Column.class);
            Column columnOnGetter =getter.getAnnotation(Column.class);
            String columnName = getColumnName(fieldName, clazz, columnOnField, columnOnGetter);
            if(StringUtils.isNotEmpty(columnName)){
                logger.debug("@Column with columnName {} found on field [{}] of class {} ",columnName, fieldName, clazz.getName());
                fieldNameToColumnNameMap.put(fieldName,columnName);
            }
        }
        return fieldNameToColumnNameMap;
    }

    private boolean isTransient(Field field, Method getter){
        Transient tF = field.getAnnotation(Transient.class);
        Transient tG = getter.getAnnotation(Transient.class);
        return tF!=null || tG!=null;
    }

    private String getColumnName(String fieldName, Class<?> clazz,Column columnOnField,Column columnOnGetter){
        if(columnOnField!=null && columnOnGetter!=null){
            //check identity
            if(columnOnField.name().equals(columnOnGetter.name())){
                return getColumnNameIfEmpty(fieldName, columnOnField.name());
            }else{
                throw new EasyQueryException("Column name is not unique on field and getter in class["+clazz.getName()+"]");
            }
        }
        if(columnOnField!=null ){
            return getColumnNameIfEmpty(fieldName,columnOnField.name());
        }
        if(columnOnGetter!=null){
            return getColumnNameIfEmpty(fieldName, columnOnGetter.name());
        }
        return fieldName;
    }

    private String getColumnNameIfEmpty(String fieldName, String definedColumnName){
        if(StringUtils.isEmpty(definedColumnName)){
            return fieldName;
        }else{
            return definedColumnName;
        }
    }


}
