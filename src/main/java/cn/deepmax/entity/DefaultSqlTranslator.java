package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.Pair;
import java.util.*;

public class DefaultSqlTranslator implements SqlTranslator{

    private EntityInfo entityInfo;

    /**
     * sql cache Maps for entity, key is class.name  value is sql string.
     */
    private Map<String,String> insertCache = new HashMap<>();
    private Map<String,String> updateCache = new HashMap<>();
    private Map<String,String> selectCache = new HashMap<>();
    private Map<String,String> deleteCache = new HashMap<>();

    public DefaultSqlTranslator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    @Override
    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    @Override
    public Pair<String,List<Object>> getInsertSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        Map<String,Object> values = BeanToMap.convert(obj);
        String sql = getInsertSql(clazz);
        List<Object> fieldValueList = getEntityFieldValues(clazz,values);
        return new Pair<>(sql,fieldValueList);
    }

    /**
     * get insert sql from cache.
     * if not, create a insert sql and put it to cache.
     * @param clazz
     * @return
     */
    private String getInsertSql(Class<?> clazz){
        List<String> fieldNameList = entityInfo.beanFieldNameList(clazz);
        String sql = insertCache.get(clazz.getName());
        if(sql!=null){
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getFullTableName(clazz);
        String pk = entityInfo.getPrimaryKeyFieldName(clazz);
        Map<String,String> convertMap = entityInfo.fieldNameToColumnNameMap(clazz);
        sb.append("insert into ").append(tableName).append(" (");
        int counter = 0;
        for(String field:fieldNameList){
            if(!field.equals(pk)){
                String columnName = convertMap.get(field);
                if(columnName!=null && columnName.length()!=0){
                    sb.append(columnName).append(",");
                    counter++;
                }else{
                    throw new EasyQueryException("Entity["+clazz.getName()+"] filed "+field+" don't have columnName.");
                }
            }
        }
        deleteLastChar(sb,counter);
        sb.append(") values ( ");
        for (int i = 0; i < counter; i++) {
            sb.append("?,");
        }
        deleteLastChar(sb,counter);
        sb.append(") ");
        sql = sb.toString();
        insertCache.put(clazz.getName(),sql);
        return sql;
    }

    @Override
    public Pair<String,List<Object>> getUpdateSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        Map<String,Object> values = BeanToMap.convert(obj);
        String sql = getUpdateSQL(clazz);
        List<Object> fieldValueList = getEntityFieldValues(clazz,values);
        fieldValueList.add(entityInfo.getPrimaryKeyFieldValue(obj));    //add primary key value.
        return new Pair<>(sql,fieldValueList);
    }

    /**
     * get update sql from cache.
     * if not, create a update sql and put it to cache.
     * @param clazz
     * @return
     */
    private String getUpdateSQL(Class<?> clazz){
        String sql = updateCache.get(clazz.getName());
        if(sql!=null){
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(entityInfo.getFullTableName(clazz)).append(" set ");
        Map<String,String> fieldNameToColumnNameMap = entityInfo.fieldNameToColumnNameMap(clazz);
        String pkFieldName = entityInfo.getPrimaryKeyFieldName(clazz);
        int counter=0;
        for(String fieldName:entityInfo.beanFieldNameList(clazz)){
            String columnName =fieldNameToColumnNameMap.get(fieldName);
            if(columnName!=null && columnName.length()!=0 && !fieldName.equals(pkFieldName)){//exclude primarykey
                sb.append(columnName).append(" = ? ,");
                counter++;
            }
        }
        deleteLastChar(sb,counter);
        sb.append(" where ").append(fieldNameToColumnNameMap.get(pkFieldName)).append(" = ? ");
        sql = sb.toString();
        updateCache.put(clazz.getName(),sql);
        return sql;
    }

    /**
     * get all entity field value except primary key field value.
     * @param clazz
     * @param values
     * @return
     */
    private List<Object> getEntityFieldValues(Class<?> clazz,Map<String,Object> values){
        List<String> fieldList = entityInfo.beanFieldNameList(clazz);
        List<Object> list = new ArrayList<>();
        String primaryKeyName = entityInfo.getPrimaryKeyFieldName(clazz);
        for(String field:fieldList){
            if(!primaryKeyName.equals(field)){
                list.add(values.get(field));
            }
        }
        return list;
    }


    /**
     * remove redundant comma at last char in stringbuilder.
     * @param sb
     * @param counter
     */
    private void deleteLastChar(StringBuilder sb, Integer counter){
        if(counter!=null && counter!=0 ){
            sb.deleteCharAt(sb.length()-1);
        }
    }

    @Override
    public String getSelectSQLInfo(Class<?> clazz){
        String sql = selectCache.get(clazz.getName());
        if(sql!=null){
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getFullTableName(clazz);
        String primaryColumnName = entityInfo.fieldNameToColumnNameMap(clazz).get(entityInfo.getPrimaryKeyFieldName(clazz));
        sb.append("select * from ")
                .append(tableName)
                .append( "  where ")
                .append(primaryColumnName)
                .append(" = ?  ");
        sql = sb.toString();
        selectCache.put(clazz.getName(),sql);
        return sql;
    }

    @Override
    public Pair<String,Object> getDeleteSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        String sql = getDeleteSQL(clazz);
        Object primaryKeyValue = entityInfo.getPrimaryKeyFieldValue(obj);
        Objects.requireNonNull(primaryKeyValue,"Entity of type["+clazz.getName()+"] primaryKey value is null");
        return new Pair<>(sql,primaryKeyValue);
    }

    private String getDeleteSQL(Class<?> clazz){
        String sql = deleteCache.get(clazz.getName());
        if(sql!=null){
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getFullTableName(clazz);
        String primaryColumnName = entityInfo.fieldNameToColumnNameMap(clazz).get(entityInfo.getPrimaryKeyFieldName(clazz));
        sb.append("delete from ")
                .append(tableName)
                .append( "  where ")
                .append(primaryColumnName)
                .append(" = ?  ");
        sql = sb.toString();
        deleteCache.put(clazz.getName(),sql);
        return sql;
    }



}