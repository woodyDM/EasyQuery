package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;


/**
 * used to rule generator javatype.
 */
public interface TypeTranslator {

    String translate(ColumnMetaData columnMetaData);

}
