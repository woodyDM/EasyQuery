package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;
import cn.deepmax.model.DatabaseMetaData;
import cn.deepmax.util.BeanUtils;
import cn.deepmax.util.StringUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * class for Freemarker template.
 */
public class FreemarkerTemplateClassData {
    private boolean entity = false;
    private String className;
    private String versionAndHashInfo;
    private String tableName;
    private String catalogName;
    private String packageName;
    private List<FreemarkerClassFieldData> columns = new ArrayList<>();

    private FreemarkerTemplateClassData(){}
    public static FreemarkerTemplateClassData instance(DatabaseMetaData dbMetaData, String javaClassName, Config config){
        FreemarkerTemplateClassData data = new FreemarkerTemplateClassData();
        if(StringUtils.isEmpty(dbMetaData.getTableName()) || StringUtils.isEmpty(dbMetaData.getCatalogName())){
            data.entity = false;
        }else{
            data.entity = true;
            data.tableName = dbMetaData.getTableName();
            data.catalogName = dbMetaData.getCatalogName();
        }
        data.versionAndHashInfo = GeneratorExecutor.FLAG + dbMetaData.getHash();
        data.className = javaClassName;
        data.packageName = config.getPackageName();

        for(ColumnMetaData it:dbMetaData.getColumnMetaDataList()){
            String columnName = it.getColumnName();
            String javaTypeName = config.getTypeTranslator().translate(it);
            String propertyName = config.getToFieldMapper().map(columnName);
            String writeMethodName = BeanUtils.getWriteMethodName(propertyName, javaTypeName);
            String readMethodName = BeanUtils.getReadMethodName(propertyName, javaTypeName);
            String comment = it.getComment();
            FreemarkerClassFieldData metaData = new FreemarkerClassFieldData(propertyName,javaTypeName,writeMethodName,readMethodName,columnName,comment);
            data.columns.add(metaData);
        }
        return data;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVersionAndHashInfo() {
        return versionAndHashInfo;
    }

    public void setVersionAndHashInfo(String versionAndHashInfo) {
        this.versionAndHashInfo = versionAndHashInfo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public List<FreemarkerClassFieldData> getColumns() {
        return columns;
    }

    public void setColumns(List<FreemarkerClassFieldData> columns) {
        this.columns = columns;
    }
}
