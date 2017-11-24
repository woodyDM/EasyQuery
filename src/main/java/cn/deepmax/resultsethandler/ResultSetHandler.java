package cn.deepmax.resultsethandler;


import cn.deepmax.model.Pair;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetHandler {

    public static Pair<String,List<Map<String,Object>>> handle(ResultSet rs) {

        List<Map<String,Object>> resultsList = new ArrayList<>();
        Pair<String,List<Map<String,Object>>> pair = new Pair<>("",resultsList);
        if(rs==null){
            return pair;
        }
        try {
            while (rs.next()){
                int totalColumnCount = rs.getMetaData().getColumnCount();
                Map<String,Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= totalColumnCount; i++) {
                    addToMap(pair,row,rs,i);
                }
                pair.last.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pair;
    }

    private static void addToMap(Pair<String,List<Map<String,Object>>> pair,Map<String,Object> row,ResultSet rs,int pos){
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
            //handle tableName only unique tableName is allowed.
            if(pair.first==null){   //if more than one table.
                return;
            }
            String nowTableName = metaData.getTableName(pos);
            if(pair.first.length()==0||(nowTableName!=null && nowTableName.length()!=0)){ //no table found before.
                pair.first = nowTableName;
            }else{
                if(!pair.first.equals(nowTableName)){
                    pair.first = null;      //other tables found.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
