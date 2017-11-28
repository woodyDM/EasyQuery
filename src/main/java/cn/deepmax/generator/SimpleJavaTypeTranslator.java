package cn.deepmax.generator;


import cn.deepmax.model.ColumnMetaData;


/**
 *
 */
public class SimpleJavaTypeTranslator implements TypeTranslator {

    @Override
    public String translate(ColumnMetaData columnMetaData) {
        return columnMetaData.getClassTypeName();
    }
}
