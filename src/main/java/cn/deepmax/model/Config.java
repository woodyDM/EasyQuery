package cn.deepmax.model;

import cn.deepmax.mapper.NameMapper;

import java.io.File;

public class Config {

    private boolean isShowSql = false;      //whether show sql in log
    private boolean isGenerateClass = false;    //whether enable class generator for entity and VO
    private NameMapper toFieldNameMapper;       //using to define columnName to fieldName rules.
    private String valueObjectPath ;            //generated VO java file root path,(not include package path)
    private String entityPath;                  //generated entity java file root path,(not include package path)

    public String getValueObjectPath() {
        return valueObjectPath;
    }

    public void setValueObjectPath(String valueObjectPath) {
        this.valueObjectPath = valueObjectPath;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public boolean isShowSql() {
        return isShowSql;
    }

    public void setShowSql(boolean showSql) {
        isShowSql = showSql;
    }

    public boolean isGenerateClass() {
        return isGenerateClass;
    }

    public void setGenerateClass(boolean generateClass) {
        isGenerateClass = generateClass;
    }

    public NameMapper getToFieldNameMapper() {
        return toFieldNameMapper;
    }

    public void setToFieldNameMapper(NameMapper toFieldNameMapper) {
        this.toFieldNameMapper = toFieldNameMapper;
    }


    public void normalizePath(){
        this.valueObjectPath = normalizedPath(valueObjectPath);
        this.entityPath = normalizedPath(entityPath);
    }

    private String normalizedPath(String path){
        if(path.endsWith(File.separator)){
            return path;
        }else{
            return path+File.separator;
        }
    }
}
