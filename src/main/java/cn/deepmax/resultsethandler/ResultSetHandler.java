package cn.deepmax.resultsethandler;


import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.ColumnMetaData;
import cn.deepmax.model.DatabaseMetaData;
import cn.deepmax.model.Pair;
import cn.deepmax.util.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetHandler {



    public static Pair<DatabaseMetaData,List<Map<String,Object>>> handle(ResultSet rs, boolean needMetaData) {
        List<Map<String,Object>> resultsList = new ArrayList<>();
        DatabaseMetaData metaData = (needMetaData) ? new DatabaseMetaData() : null;
        Pair<DatabaseMetaData,List<Map<String,Object>>> pair = new Pair<>(metaData,resultsList);
        if(rs==null){
            return pair;
        }
        try{
            if(needMetaData){
                Map<String,Integer> labelIndex = new LinkedHashMap<>();
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                int totalColumnCount = resultSetMetaData.getColumnCount();
                for (int i = 1; i <= totalColumnCount ; i++) {
                    String labelName = getLabelName(rs.getMetaData(), labelIndex.keySet(), i);
                    labelIndex.put(labelName,i);
                    handleTableName(metaData, resultSetMetaData.getTableName(i));
                    handleCatalogName(metaData, resultSetMetaData.getCatalogName(i));
                    String className = resultSetMetaData.getColumnClassName(i);
                    String dbtypeName = resultSetMetaData.getColumnTypeName(i);
                    int precision = resultSetMetaData.getPrecision(i);
                    ColumnMetaData columnMetaData = new ColumnMetaData(labelName,className,dbtypeName,precision,null);
                    metaData.getColumnMetaDataList().add(columnMetaData);
                }
                while (rs.next()){
                    Map<String,Object> row = new LinkedHashMap<>();
                    for (Map.Entry<String,Integer> entry:labelIndex.entrySet()) {
                        String mapKey = entry.getKey();
                        Object value = rs.getObject(entry.getValue());
                        row.put(mapKey, value);
                    }
                    resultsList.add(row);
                }
            }else{
                while (rs.next()){
                    int totalColumnCount = rs.getMetaData().getColumnCount();
                    Map<String,Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= totalColumnCount; i++) {
                        String mapKey = getLabelName(rs.getMetaData(),row.keySet(), i);
                        Object value = rs.getObject(i);
                        row.put(mapKey, value);
                    }
                    resultsList.add(row);
                }
            }
        }catch (Exception e){
            throw new EasyQueryException(e);
        }
        return pair;
    }

    private static String getLabelName(ResultSetMetaData metaData, Set<String> valueSet,int position ) throws SQLException {
        String labelName = metaData.getColumnLabel(position);
        if( null == labelName || 0 == labelName.length()){
            labelName = metaData.getColumnName(position);
        }
        return genLabelName(valueSet, labelName);
    }

    private static String genLabelName(Set<String> valueSet, String oldLabelName){
        if(!valueSet.contains(oldLabelName)){
            return oldLabelName;
        }else{
            int start = 0;
            String labelName = oldLabelName;
            while (valueSet.contains(labelName)){
                start++;
                labelName = oldLabelName+start;
            }
            return labelName;
        }

    }

    private static void handleTableName(DatabaseMetaData metaData, String comingTableName){
        if(metaData.getTableName()==null){   //if more than one table.
            return;
        }
        if(metaData.getTableName().length()==0 && StringUtils.isNotEmpty(comingTableName)){ //no table found before.
            metaData.setTableName(comingTableName);
        }else{
            if(!metaData.getTableName().equals(comingTableName)){
                metaData.setTableName(null);      //other tables found.
            }
        }
    }
    private static void handleCatalogName(DatabaseMetaData metaData, String comingCatalogName){
        if(metaData.getCatalogName()==null){   //if more than one catalog.
            return;
        }
        if(metaData.getCatalogName().length()==0 && StringUtils.isNotEmpty(comingCatalogName)){ //no catalog found before.
            metaData.setCatalogName(comingCatalogName);
        }else{
            if(!metaData.getCatalogName().equals(comingCatalogName)){
                metaData.setCatalogName(null);      //other catalog found.
            }
        }
    }

}
