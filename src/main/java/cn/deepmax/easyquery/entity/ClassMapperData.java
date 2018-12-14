package cn.deepmax.easyquery.entity;


import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassMapperData {

    String tableName;
    PropertyDescriptor primaryKeyDescriptor;
    List<String> beanFieldNameList = new ArrayList<>();
    Map<String,String> toFieldMapper = new ConcurrentHashMap<>(16);
    Map<String,String> toColumnMapper = new ConcurrentHashMap<>(16);


    public void addMapper(String fieldName, String columnName){
        toFieldMapper.put(columnName, fieldName);
        toColumnMapper.put(fieldName, columnName);
    }

    public void appendFieldName(String fieldName){
        beanFieldNameList.add(fieldName);
    }



}
