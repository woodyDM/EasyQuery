package cn.deepmax.model;

import cn.deepmax.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * database info ,including tableName and catalogName.
 */
public class DbMetaData {

    public static final String INIT_VALUE= "";

    private String tableName = INIT_VALUE;
    private String catalogName = INIT_VALUE;
    private List<ColumnMetaData> columnMetaDataList = new ArrayList<>();

    public int getHash(){
        if(columnMetaDataList.size()==0){
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotEmpty(tableName)){
            sb.append(tableName).append("_");
        }
        if(StringUtils.isNotEmpty(catalogName)){
            sb.append(catalogName).append("_");
        }
        for(ColumnMetaData it:columnMetaDataList){
            sb.append(it.hashCode()).append("_");
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

    public List<ColumnMetaData> getColumnMetaDataList() {
        return columnMetaDataList;
    }

    public void setColumnMetaDataList(List<ColumnMetaData> columnMetaDataList) {
        this.columnMetaDataList = columnMetaDataList;
    }
}
