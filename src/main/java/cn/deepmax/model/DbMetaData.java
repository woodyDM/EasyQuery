package cn.deepmax.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DbMetaData {

    public static final String INIT_VALUE= "";

    private String tableName = INIT_VALUE;
    private String catalogName = INIT_VALUE;
    private Map<String,String> columnClassTypeName = new LinkedHashMap<>(); //columnName, TypeName



    public int getHash(){
        if(columnClassTypeName.size()==0){
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry:columnClassTypeName.entrySet()){
            sb.append(entry.getKey()).append("_").append(entry.getValue());
        }
        return sb.toString().hashCode();
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Map<String, String> getColumnClassTypeName() {
        return columnClassTypeName;
    }

    public void setColumnClassTypeName(Map<String, String> columnClassTypeName) {
        this.columnClassTypeName = columnClassTypeName;
    }
}
