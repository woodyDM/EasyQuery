package cn.deepmax.entity;

import cn.deepmax.mapper.NameMapper;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * EntityInfo implements using NameMapper.
 *
 */
public class MappedEntityInfo extends AbstractEntityInfo {


    private NameMapper toTableNameMapper;
    private NameMapper toColumnNameMapper;

    public MappedEntityInfo(NameMapper toTableNameMapper, NameMapper toColumnNameMapper) {
        Objects.requireNonNull(toTableNameMapper,"toTableNameMapper is null");
        Objects.requireNonNull(toColumnNameMapper,"toColumnNameMapper is null");
        this.toTableNameMapper = toTableNameMapper;
        this.toColumnNameMapper = toColumnNameMapper;
    }

    public void setToTableNameMapper(NameMapper toTableNameMapper) {
        this.toTableNameMapper = toTableNameMapper;
    }

    public void setToColumnNameMapper(NameMapper toColumnNameMapper) {
        this.toColumnNameMapper = toColumnNameMapper;
    }

    /**
     * null database catalog returned.
     * using default jdbc database catalog.
     * @param clazz
     * @return
     */
    @Override
    public String getCatalogName(Class<?> clazz) {
        return null;
    }

    /**
     * getTableName only using target class information.
     * @param clazz
     * @return
     */
    @Override
    public String getTableName(Class<?> clazz) {
        return toTableNameMapper.convert(clazz,null);
    }

    /**
     * MappedEntityInfo only supports entity that primaryKey fieldName is "id"
     * @param clazz
     * @return
     */
    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return "id";
    }

    /**
     * using NameMapper to convert fieldName to columnName
     * @param clazz
     * @return
     */
    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(String fieldName:getBeanFieldNameList(clazz)){
            String columnName = toColumnNameMapper.convert(clazz,fieldName);
            map.put(fieldName,columnName);
        }
        return map;
    }

    /**
     * just reverse fieldNameToColumnNameMap
     * @param clazz
     * @return
     */
    @Override
    Map<String, String> getColumnNameToFieldNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(Map.Entry<String,String> entry:getFieldNameToColumnNameMap(clazz).entrySet()){
            map.put(entry.getValue(),entry.getKey());
        }
        return map;
    }

    /**
     * MappedEntityInfo requires all fields has corresponding database columns.
     * @param clazz
     * @return
     */
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
