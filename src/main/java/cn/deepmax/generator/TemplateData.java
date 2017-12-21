package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;
import cn.deepmax.model.Config;
import cn.deepmax.model.DbMetaData;
import cn.deepmax.util.BeanUtils;
import cn.deepmax.util.StringUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * class for Freemarker template.
 */
public class TemplateData {
    private boolean entity = false;
    private String className;
    private String versionAndHashInfo;
    private String tableName;
    private String catalogName;
    private String packageName;
    private List<ClassMetaData> columns = new ArrayList<>();

    private TemplateData(){}
    public static TemplateData instance(DbMetaData dbMetaData, Class<?> clazz, Config config){
        TemplateData data = new TemplateData();
        if(StringUtils.isEmpty(dbMetaData.getTableName()) || StringUtils.isEmpty(dbMetaData.getCatalogName())){
            data.entity = false;
        }else{
            data.entity = true;
            data.tableName = dbMetaData.getTableName();
            data.catalogName = dbMetaData.getCatalogName();
        }
        data.versionAndHashInfo = Generator.FLAG + dbMetaData.getHash();
        data.className = clazz.getSimpleName();
        data.packageName = clazz.getPackage().getName();

        for(ColumnMetaData it:dbMetaData.getColumnMetaDataList()){
            String columnName = it.getColumnName();
            String javaTypeName = config.getTypeTranslator().translate(it);
            String propertyName = config.getToFieldNameMapper().convert(clazz,columnName);
            String writeMethodName = BeanUtils.getWriteMethodName(propertyName, javaTypeName);
            String readMethodName = BeanUtils.getReadMethodName(propertyName, javaTypeName);
            ClassMetaData metaData = new ClassMetaData(propertyName,javaTypeName,writeMethodName,readMethodName,columnName);
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

    public List<ClassMetaData> getColumns() {
        return columns;
    }

    public void setColumns(List<ClassMetaData> columns) {
        this.columns = columns;
    }
}
