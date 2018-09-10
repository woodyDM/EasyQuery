package cn.deepmax.resultsethandler;


import cn.deepmax.exception.EasyQueryException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetHandler {



    public static List<Map<String,Object>> handle(ResultSet rs) {
        List<Map<String,Object>> resultsList = new ArrayList<>();
        if(rs==null){
            return resultsList;
        }
        try{
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
        }catch (Exception e){
            throw new EasyQueryException("unable to handle result set", e);
        }
        return resultsList;
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



}
