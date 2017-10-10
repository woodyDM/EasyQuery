package cn.deepmax.entity;

import cn.deepmax.mapper.FieldNameMapper;
import cn.deepmax.mapper.SameFieldNameMapper;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleEntityInfo extends AbstractEntityInfo {

    private FieldNameMapper fieldNameMapper;

    public SimpleEntityInfo() {
        this.fieldNameMapper = new SameFieldNameMapper();
    }

    public SimpleEntityInfo(FieldNameMapper fieldNameMapper) {
        this.fieldNameMapper = fieldNameMapper;
    }

    public void setFieldNameMapper(FieldNameMapper fieldNameMapper) {
        this.fieldNameMapper = fieldNameMapper;
    }

    @Override
    public String getCatalogName(Class<?> clazz) {
        return null;
    }

    @Override
    public String getTableName(Class<?> clazz) {
        return "user";  //TODO 修改
    }

    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return "id";
    }

    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(String field:getBeanFieldNameList(clazz)){
            String columnName = fieldNameMapper.toDatabaseColumnName(clazz,field);
            map.put(field,columnName);
        }
        return map;
    }

    @Override
    List<String> getBeanFieldNameList(Class<?> clazz) {
        PropertyDescriptor[] propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        List<String> filedNames = new ArrayList<>();
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String filedName = propertyDescriptor.getName();
            if(!"class".equals(filedName)){
                filedNames.add(filedName);
            }
        }
        return filedNames;
    }

}
