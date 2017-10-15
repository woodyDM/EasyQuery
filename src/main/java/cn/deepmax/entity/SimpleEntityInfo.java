package cn.deepmax.entity;

import cn.deepmax.mapper.NameMapper;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用NameMapper 的实体管理器,只支持 主键为 id
 *
 */
public class SimpleEntityInfo extends AbstractEntityInfo {


    private NameMapper toTableNameMapper;
    private NameMapper toColumnNameMapper;

    public SimpleEntityInfo(NameMapper toTableNameMapper, NameMapper toColumnNameMapper) {
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
     *
     * @param clazz
     * @return
     */
    @Override
    public String getCatalogName(Class<?> clazz) {
        return null;
    }

    @Override
    public String getTableName(Class<?> clazz) {
        return toTableNameMapper.convert(clazz,null);
    }
    @Override
    public String getPrimaryKeyFieldName(Class<?> clazz) {
        return "id";
    }


    @Override
    Map<String, String> getColumnNameToFieldNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(Map.Entry<String,String> entry:getFieldNameToColumnNameMap(clazz).entrySet()){
            map.put(entry.getValue(),entry.getKey());
        }
        return map;
    }

    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(String field:getBeanFieldNameList(clazz)){
            String columnName = toColumnNameMapper.convert(clazz,field);
            map.put(field,columnName);
        }
        return map;
    }

    /**
     * simpleEntityInfo requires all fields has corresponding database column.
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
