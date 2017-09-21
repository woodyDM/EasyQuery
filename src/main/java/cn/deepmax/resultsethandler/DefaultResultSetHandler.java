package cn.deepmax.resultsethandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DefaultResultSetHandler implements ResultSetHandler{

    @Override
    public List<Map<String,Object>> handle(ResultSet rs) {
        List<Map<String,Object>> list = new ArrayList<>();
        if(rs==null){
            return list;
        }
        ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
            while (rs.next()){
                Map<String,Object> row = new LinkedHashMap<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    addToMap(row,rs,metaData,i+1);
                }
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    private void addToMap(Map<String,Object> target,ResultSet rs,ResultSetMetaData metaData,int pos){
        try {
            String labelName = metaData.getColumnLabel(pos);
            String oldLableName = labelName;
            Object result =rs.getObject(pos);
            int start = 0;
            while (target.containsKey(labelName)){
                start++;
                labelName = oldLableName+start;
            }
            target.put(labelName,result);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
