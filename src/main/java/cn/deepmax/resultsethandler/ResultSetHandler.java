package cn.deepmax.resultsethandler;


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
        DatabaseMetaData metaData = (needMetaData)?new DatabaseMetaData():null;
        Pair<DatabaseMetaData,List<Map<String,Object>>> pair = new Pair<>(metaData,resultsList);
        if(rs==null){
            return pair;
        }
        try {
            boolean isFirstLoop = true;
            while (rs.next()){
                int totalColumnCount = rs.getMetaData().getColumnCount();
                Map<String,Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= totalColumnCount; i++) {
                    addToMap(metaData, row, rs, i, isFirstLoop);
                }
                resultsList.add(row);
                isFirstLoop = false;        //no need to repeat getting dbMetaData
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pair;
    }

    /**
     * add result value to map and analysis metaData in firstLoop.
     * @param row
     * @param rs
     * @param pos
     * @param isFirstLoop
     */
    private static void addToMap(DatabaseMetaData dbMetaData, Map<String,Object> row, ResultSet rs, int pos, boolean isFirstLoop){
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            String labelName = metaData.getColumnLabel(pos);
            if( null == labelName || 0 == labelName.length()){
                labelName = metaData.getColumnName(pos);
            }
            String oldLableName = labelName;
            Object value =rs.getObject(pos);
            int start = 0;
            while (row.containsKey(labelName)){
                start++;
                labelName = oldLableName+start;
            }
            row.put(labelName,value);   //add values
            if(!isFirstLoop || dbMetaData==null){
                return;
            }
            //handle tableName and catalogName .only unique allowed.
            handleTableName(dbMetaData, metaData.getTableName(pos));
            handleCatalogName(dbMetaData, metaData.getCatalogName(pos));
            String className = metaData.getColumnClassName(pos);
            String dbtypeName = metaData.getColumnTypeName(pos);
            int precision = metaData.getPrecision(pos);
            ColumnMetaData columnMetaData = new ColumnMetaData(labelName,className,dbtypeName,precision);
            dbMetaData.getColumnMetaDataList().add(columnMetaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
