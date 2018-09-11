package cn.deepmax.entity;


import cn.deepmax.adapter.TypeAdapter;
import cn.deepmax.model.Pair;
import cn.deepmax.support.CacheDataSupport;
import cn.deepmax.util.BeanToMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * default SqlTranslator implementation supporting cache.
 */
public class DefaultSqlTranslator extends CacheDataSupport<String,SqlCacheData> implements SqlTranslator{

    private EntityInfo entityInfo;
    private SqlColumnWrapper columnWrapper;
    private TypeAdapter typeAdapter;

    public DefaultSqlTranslator(EntityInfo entityInfo, SqlColumnWrapper columnWrapper, TypeAdapter typeAdapter) {
        Objects.requireNonNull(entityInfo);
        Objects.requireNonNull(typeAdapter);
        this.entityInfo = entityInfo;
        if(columnWrapper==null){
            this.columnWrapper = (data)->data;
        }else{
            this.columnWrapper = columnWrapper;
        }
        this.typeAdapter = typeAdapter;
    }

    @Override
    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    @Override
    public SqlCacheData load(String target) throws Exception {
        Class<?> clazz = Class.forName(target);
        String selectSql = getSelectSQLInternal(clazz);
        String updateSql = getUpdateSQLInternal(clazz);
        String insertSql = getInsertSqlInternal(clazz);
        String deleteSql = getDeleteSQLInternal(clazz);
        SqlCacheData data = new SqlCacheData();
        data.selectSQL = selectSql;
        data.updateSQL = updateSql;
        data.insertSQL = insertSql;
        data.deleteSQL = deleteSql;
        return data;
    }


    @Override
    public Pair<String,List<Object>> getInsertSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        String sql = loadThen(clazz.getName(),(data)->data.insertSQL);
        Map<String,Object> values = BeanToMap.convert(obj);
        List<Object> fieldValueList = getEntityFieldValues(clazz,values);
        return new Pair<>(sql,fieldValueList);
    }

    /**
     * putIfAbsent insert sql from cache.
     * if cache does not exist, create an insert sql and put it into cache.
     * @param clazz
     * @return
     */
    private String getInsertSqlInternal(Class<?> clazz){
        List<String> fieldNameList = entityInfo.beanFieldNameList(clazz);
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getTableName(clazz);
        String pk = entityInfo.getPrimaryKeyFieldName(clazz);
        Map<String,String> fieldNameToColumnNameMap = entityInfo.fieldNameToColumnNameMap(clazz);
        sb.append("INSERT into ").append(tableName).append(" (");
        int counter = 0;
        for(String field:fieldNameList){
            if(!field.equals(pk)){
                String columnName = fieldNameToColumnNameMap.get(field);
                if(columnName!=null && columnName.length()!=0){
                    columnName = columnWrapper.wrap(columnName);
                    sb.append(columnName).append(",");
                    counter++;
                }
            }
        }
        deleteLastChar(sb,counter);
        sb.append(" ) values ( ");
        for (int i = 0; i < counter; i++) {
            sb.append("?,");
        }
        deleteLastChar(sb,counter);
        sb.append(") ");
        return sb.toString();
    }

    @Override
    public Pair<String,List<Object>> getUpdateSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        String sql = loadThen(clazz.getName(),(data)->data.updateSQL);
        Map<String,Object> values = BeanToMap.convert(obj);
        List<Object> fieldValueList = getEntityFieldValues(clazz,values);
        fieldValueList.add(entityInfo.getPrimaryKeyFieldValue(obj));    //add primary key value.
        return new Pair<>(sql,fieldValueList);
    }

    /**
     * putIfAbsent update sql
     * @param clazz
     * @return
     */
    private String getUpdateSQLInternal(Class<?> clazz){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ")
                .append(entityInfo.getTableName(clazz))
                .append(" set ");
        Map<String,String> fieldNameToColumnNameMap = entityInfo.fieldNameToColumnNameMap(clazz);
        String pkFieldName = entityInfo.getPrimaryKeyFieldName(clazz);
        int counter=0;
        for(String fieldName:entityInfo.beanFieldNameList(clazz)){
            String columnName =fieldNameToColumnNameMap.get(fieldName);
            if(columnName!=null && columnName.length()!=0 && !fieldName.equals(pkFieldName)){//exclude primarykey
                columnName = columnWrapper.wrap(columnName);
                sb.append(columnName).append(" = ? ,");
                counter++;
            }
        }
        deleteLastChar(sb,counter);
        String pkColumnName = fieldNameToColumnNameMap.get(pkFieldName);
        sb.append(" where ").append(columnWrapper.wrap(pkColumnName)).append(" = ? ");
        return sb.toString();
    }

    @Override
    public String getSelectSQLInfo(Class<?> clazz){
        String className = clazz.getName();
        return loadThen(className,(data)->data.selectSQL);
    }

    protected String getSelectSQLInternal(Class<?> clazz){
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getTableName(clazz);
        String primaryFieldName = entityInfo.getPrimaryKeyFieldName(clazz);
        String primaryColumnName = entityInfo.fieldNameToColumnNameMap(clazz).get(primaryFieldName);
        primaryColumnName = columnWrapper.wrap(primaryColumnName);
        sb.append("select  * from ")
                .append(tableName)
                .append( "  where ")
                .append(primaryColumnName)
                .append(" = ?  ");
        return sb.toString();
    }

    @Override
    public Pair<String,Object> getDeleteSQLInfo(Object obj){
        Class<?> clazz = obj.getClass();
        String sql = loadThen(clazz.getName(),(data)->data.deleteSQL);
        Object primaryKeyValue = entityInfo.getPrimaryKeyFieldValue(obj);
        Objects.requireNonNull(primaryKeyValue,"Entity of type["+clazz.getName()+"] primaryKey value is null");
        return new Pair<>(sql, primaryKeyValue);
    }

    private String getDeleteSQLInternal(Class<?> clazz){
        StringBuilder sb = new StringBuilder();
        String tableName = entityInfo.getTableName(clazz);
        String primaryColumnName = entityInfo.fieldNameToColumnNameMap(clazz).get(entityInfo.getPrimaryKeyFieldName(clazz));
        primaryColumnName = columnWrapper.wrap(primaryColumnName);
        sb.append("DELETE from ")
                .append(tableName)
                .append( "  where ")
                .append(primaryColumnName)
                .append(" = ?  ");
        return sb.toString();
    }



    /**
     * putIfAbsent all entity field value except primary key field value.
     * and convert to desired type values.
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
                Object v = values.get(field);
                v = typeAdapter.getCompatibleDatabaseValue(clazz, field, v);
                list.add(v);
            }
        }
        return list;
    }

    /**
     * remove redundant comma at last char in stringBuilder.
     * @param sb
     * @param counter
     */
    private void deleteLastChar(StringBuilder sb, Integer counter){
        if(counter!=null && counter!=0 ){
            sb.deleteCharAt(sb.length()-1);
        }
    }


    public interface SqlColumnWrapper {
        String wrap(String definedColumnName);
    }





}
