package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;

/**
 * when using MysqlTableGenerator,the metadata of database tables are selected from
 * Information_schema.COLUMNS. So there are no javaTypes of columns,
 * ColumnMetaData.classTypeName is null.
 * This file using ColumnMetaData to convert database type (int ,varchar...etc) to
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
        return "java.lang.String";
    }
}
