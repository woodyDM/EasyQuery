package cn.deepmax.easyquery.resultsethandler;


import cn.deepmax.easyquery.exception.EasyQueryException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultSetHandler {


    /**
     * handle ResultSet
     * @param rs
     * @return
     */
    public static List<? extends Map<String,Object>> handle(ResultSet rs) {
        List<RowRecord> resultsList = new ArrayList<>();
        try{
            while (rs.next()){
                int totalColumnCount = rs.getMetaData().getColumnCount();
                RowRecord row = new RowRecord();
                for (int i = 1; i <= totalColumnCount; i++) {
                    String mapKey = getLabelName(rs.getMetaData(),row.keySet(), i);
                    Object value = rs.getObject(i);
                    row.put(mapKey, value);
                }
                resultsList.add(row);
            }
        }catch (Exception e){
            throw new EasyQueryException("Unable to handle ResultSet", e);
        }
        return resultsList;
    }


    /**
     *
     * @param metaData
     * @param valueSet
     * @param position
     * @return
     * @throws SQLException
     */
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
