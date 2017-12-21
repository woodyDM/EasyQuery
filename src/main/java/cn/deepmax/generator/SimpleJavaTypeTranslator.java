package cn.deepmax.generator;


import cn.deepmax.model.ColumnMetaData;


/**
 * using javaType from database.
 */
public class SimpleJavaTypeTranslator implements TypeTranslator {

    @Override
    public String translate(ColumnMetaData columnMetaData) {
        String fullJavaType = columnMetaData.getClassTypeName();
        return fullJavaType.replaceAll("java\\.lang\\.","");
    }
}
