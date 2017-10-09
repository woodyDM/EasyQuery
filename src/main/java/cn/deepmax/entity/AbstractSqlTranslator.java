package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.Pair;
import java.util.*;

public class AbstractSqlTranslator {

    private EntityInfo entityInfo;

    private Map<String,String> insertCache = new HashMap<>();
    private Map<String,String> updateCache = new HashMap<>();
    private Map<String,String> selectCache = new HashMap<>();
    private Map<String,String> deleteCache = new HashMap<>();


    public AbstractSqlTranslator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public Pair<String,List<Object>> getInsertSQL(Object obj){
        Map<String,Object> values = BeanToMap.convert(obj);
        int size = values.size();
        List<Object> objects = new ArrayList<>();


        String tableName = getEntityFullTable(obj);
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append(" (");

        return null;

    }

    protected String getInsertSql(Class<?> clazz, Set<String> fieldNameSet){
        String sql = selectCache.get(clazz.getName());
        if(sql!=null){
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        String tableName = getEntityFullTable(clazz);
        String pk = entityInfo.getPrimaryKeyFieldName(clazz);
        Map<String,String> convertMap = entityInfo.fieldNameToColumnNameMap(clazz);
        sb.append("insert into ").append(tableName).append(" (");
        int counter = 0;
        for(String field:fieldNameSet){
            if(!field.equals(pk)){
                String columnName = convertMap.get(field);
                if(columnName!=null && columnName.length()!=0){
                    sb.append(columnName).append(",");
                    counter++;
                }
            }
        }
        deleteLast(sb,counter);
        sb.append(") values ( ");
        for (int i = 0; i < counter; i++) {
            sb.append("?,");
        }
        deleteLast(sb,counter);
        sb.append(") ");
        sql = sb.toString();
        selectCache.put(clazz.getName(),sql);
        return sql;
    }

    private void deleteLast(StringBuilder sb, int counter){
        if(counter!=0){
            sb.deleteCharAt(sb.length()-1);
        }
    }

    public Pair<String,List<Object>> getUpdateSQL(Object obj){
        return null;
    }

    public Pair<String,List<Object>> getSelectSQL(Object obj){
        return null;
    }

    public Pair<String,List<Object>> getDeleteSQL(Object obj){
        return null;
    }



    private String getEntityFullTable(Class<?> clazz){
        String catalogName = entityInfo.getCatalogName(clazz);
        if(catalogName==null || catalogName.length()==0){
            return getEntityTable(clazz);
        }else{
            return catalogName+"."+getEntityTable(clazz);
        }
    }

    private String getEntityTable(Class<?> clazz){
        String tableName = entityInfo.getTableName(clazz);
        if(tableName==null || tableName.length()==0){
            throw new EasyQueryException("Can't get table name of entity with type["+clazz.getName()+"] .");
        }
        return tableName;
    }

}
