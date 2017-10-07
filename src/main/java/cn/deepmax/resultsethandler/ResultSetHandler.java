package cn.deepmax.resultsethandler;

import cn.deepmax.model.Pair;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetHandler {

    public static Pair<List<Map<String,Object>>,Map<String,String>> handle(ResultSet rs) {
        List<Map<String,Object>> resultsList = new ArrayList<>();
        Map<String,String> typeMap = new LinkedHashMap<>();
        Pair<List<Map<String,Object>>,Map<String,String>> re = new Pair<>(resultsList,typeMap);
        if(rs==null){
            return re;
        }
        try {
            while (rs.next()){
                Map<String,Object> row = new LinkedHashMap<>();
                int totalColumnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= totalColumnCount; i++) {
                    addToMap(row,typeMap,rs,i);
                }
                resultsList.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return re;
    }

    private static void addToMap(Map<String,Object> oneResult,Map<String,String> typeMap,ResultSet rs,int pos){

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            String labelName = metaData.getColumnLabel(pos);
            if( null == labelName || 0 == labelName.length()){
                labelName = metaData.getColumnName(pos);
            }
            String oldLableName = labelName;
            Object result =rs.getObject(pos);
            int start = 0;
            while (oneResult.containsKey(labelName)){
                start++;
                labelName = oldLableName+start;
            }
            oneResult.put(labelName,result);
            if(typeMap.size()!=metaData.getColumnCount()){
                typeMap.put(labelName,metaData.getColumnTypeName(pos));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
