package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;


/**
 * used to rule generator javaType.
 */
public interface TypeTranslator {

    String translate(ColumnMetaData columnMetaData);

}
