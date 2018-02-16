package cn.deepmax.generator;

import cn.deepmax.generator.mapper.Mapper;
import cn.deepmax.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * EasyQuery config object.
 */
public class Config {


    private String databaseUrl;
    private String databaseUserName;
    private String databasePassword;
    private String databaseDriver;
    private String packageName;
    private Mapper toFieldMapper;       //using to define columnName to fieldName rules.
    private Mapper toClassMapper;       //using to define databaseTableName to javaClassName rules.
    private String valueObjectPath ;            //generated VO java file root path,(not include package path)
    private String entityPath;                  //generated entity java file root path,(not include package path)
    private TypeTranslator typeTranslator;      //translate ColumnMetaData to javaType in javaClass file.



    public static Logger logger = LoggerFactory.getLogger(Config.class);

    public Mapper getToFieldMapper() {
        return toFieldMapper;
    }

    public Mapper getToClassMapper() {
        return toClassMapper;
    }

    public String getValueObjectPath() {
        return valueObjectPath;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public TypeTranslator getTypeTranslator() {
        return typeTranslator;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public String getPackageName() {
        return packageName;
    }

    private Config() {
    }


    private void normalizePath(){
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

    public static class Builder{
        private Config config = new Config();

        public Builder setDatabaseUrl(String databaseUrl) {
            this.config.databaseUrl = databaseUrl;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.config.packageName = packageName;
            return this;
        }
        public Builder setDatabaseUserName(String databaseUserName) {
            this.config.databaseUserName = databaseUserName;
            return this;
        }

        public Builder setDatabasePassword(String databasePassword) {
            this.config.databasePassword = databasePassword;
            return this;
        }

        public Builder setDatabaseDriver(String databaseDriver) {
            this.config.databaseDriver = databaseDriver;
            return this;
        }

        public Builder setToFieldNameMapper(Mapper toFieldNameMapper) {
            this.config.toFieldMapper = toFieldNameMapper;
            return this;
        }

        public Builder setToClassNameMapper(Mapper toClassNameMapper) {
            this.config.toClassMapper = toClassNameMapper;
            return this;
        }

        public Builder setValueObjectPath(String valueObjectPath) {
            this.config.valueObjectPath = valueObjectPath;
            return this;
        }

        public Builder setEntityPath(String entityPath) {
            this.config.entityPath = entityPath;
            return this;
        }

        public Builder setTypeTranslator(TypeTranslator typeTranslator) {
            this.config.typeTranslator = typeTranslator;
            return this;
        }

        public Config build(){
            if(StringUtils.isEmpty(this.config.databaseDriver)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty databaseDriver.");
            }
            if( this.config.databasePassword==null){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty databasePassword.");
            }
            if(StringUtils.isEmpty(this.config.databaseUrl)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty databaseUrl.");
            }
            if(StringUtils.isEmpty(this.config.databaseUserName)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty databaseUserName.");
            }
            if(StringUtils.isEmpty(this.config.packageName)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty packageName.");
            }
            if(StringUtils.isEmpty(this.config.valueObjectPath)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty valueObjectPath.");
            }
            if(StringUtils.isEmpty(this.config.entityPath)){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a notEmpty entityPath.");
            }
            if(this.config.getToClassMapper() == null){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a non-null toClassMapper.");
            }
            if(this.config.toFieldMapper == null){
                throw new IllegalArgumentException("GeneratorExecutor.config needs a non-null toFieldMapper.");
            }
            if(this.config.typeTranslator==null){
                logger.debug("No typeTranslator found in GeneratorExecutor.config, set default typeTranslator to SimpleJavaTypeTranslator");
                config.typeTranslator= new SimpleJavaTypeTranslator();
            }
            config.normalizePath();
            return config;
        }

    }
}
