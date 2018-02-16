package cn.deepmax.generator;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.ColumnMetaData;

/**
 * when using MysqlTableGenerator,the metadata of database tables are selected from
 * Information_schema.COLUMNS. So there are no javaTypes of columns from jdbc,
 * so the ColumnMetaData.classTypeName is null.
 * This class using ColumnMetaData to convert mysql database type (int ,varchar...etc) to
 * proper java type (Integer ,String ...).
 */
public class MysqlDbTypeTranslator implements TypeTranslator{

    /**
     *
     * @param columnMetaData
     * @return
     */
    @Override
    public String translate(ColumnMetaData columnMetaData) {
        String t = columnMetaData.getDbTypeName().toLowerCase();
        Integer precise = columnMetaData.getPrecise();
        if(t.equals("integer")){
            return "java.lang.Long";
        }else if(t.equals("int")||t.equals("tinyint")||t.equals("smallint")||t.equals("mediumint")){
            return "java.lang.Integer";
        }else if(t.equals("char")||t.equals("varchar")||t.contains("text")){
            return "java.lang.String";
        }else if(t.equals("bit")&&precise==1){
            return "java.lang.Boolean";
        }else if(t.equals("bigint")){
            return "java.lang.Long";
        }else if(t.equals("float")){
            return "java.lang.Float";
        }else if(t.equals("double")){
            return "java.lang.Double";
        }else if(t.equals("decimal")){
            return "java.math.BigDecimal";
        }else if(t.equals("date")||t.equals("year")){
            return "java.sql.Date";
        }else if(t.equals("time")){
            return "java.sql.Time";
        }else if(t.equals("datetime")||t.equals("timestamp")){
            return "java.sql.Timestamp";
        }else{
            throw new EasyQueryException("unsupported database type ["+columnMetaData+"]");
        }
    }
}
