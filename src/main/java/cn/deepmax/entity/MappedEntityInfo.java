package cn.deepmax.entity;

import cn.deepmax.annotation.Ignore;
import cn.deepmax.mapper.table.LowerCaseTableNameMapper;
import cn.deepmax.mapper.NameMapper;
import cn.deepmax.mapper.column.UpperCaseColumnNameMapper;
import cn.deepmax.util.BeanToMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * EntityInfo implements using NameMapper.
 *
 */
public class MappedEntityInfo extends AbstractEntityInfo {


    private NameMapper toTableNameMapper;
    private NameMapper toColumnNameMapper;
    private static final Logger logger = LoggerFactory.getLogger(MappedEntityInfo.class);
    private String primaryKeyFieldName = "id";

    public MappedEntityInfo(NameMapper toTableNameMapper, NameMapper toColumnNameMapper) {
        Objects.requireNonNull(toTableNameMapper,"toTableNameMapper is null");
        Objects.requireNonNull(toColumnNameMapper,"toColumnNameMapper is null");
        this.toTableNameMapper = toTableNameMapper;
        this.toColumnNameMapper = toColumnNameMapper;
    }

    public MappedEntityInfo() {
        this.toTableNameMapper = new LowerCaseTableNameMapper();
        this.toColumnNameMapper = new UpperCaseColumnNameMapper();
    }

    public void setToTableNameMapper(NameMapper toTableNameMapper) {
        this.toTableNameMapper = toTableNameMapper;
    }

    public void setToColumnNameMapper(NameMapper toColumnNameMapper) {
        this.toColumnNameMapper = toColumnNameMapper;
    }

    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        this.primaryKeyFieldName = primaryKeyFieldName;
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
        return this.primaryKeyFieldName;
    }


    /**
     * using NameMapper to convert fieldName to columnName
     * @param clazz
     * @return
     */
    @Override
    Map<String, String> getFieldNameToColumnNameMap(Class<?> clazz) {
        Map<String,String> map = new LinkedHashMap<>();
        for(String fieldName:beanFieldNameList(clazz)){
            String columnName = toColumnNameMapper.convert(clazz,fieldName);
            map.put(fieldName,columnName);
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
        Set<String> ignoredFieldSet = new HashSet<>();
        getIgnoredFieldNames(clazz,ignoredFieldSet);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){

            String filedName = propertyDescriptor.getName();
            if(!"class".equals(filedName) && !ignoredFieldSet.contains(filedName)){
                filedNames.add(filedName);
            }
        }
        return filedNames;
    }

    private void getIgnoredFieldNames(Class<?> clazz,Set<String> result){
        Field[] fields = clazz.getDeclaredFields();
        for(Field it:fields){
            Ignore ann = it.getAnnotation(Ignore.class);
            if(ann!=null){
                logger.trace("Find ignore field [{}] in class [{}]",it.getName(),clazz.getName());
                result.add(it.getName());
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if(superClass!=null){
            getIgnoredFieldNames(superClass,result);
        }
    }

}
