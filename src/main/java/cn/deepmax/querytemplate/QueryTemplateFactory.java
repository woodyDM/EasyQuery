package cn.deepmax.querytemplate;

import cn.deepmax.entity.SqlTranslator;
import cn.deepmax.mapper.NameMapper;
import cn.deepmax.transaction.TransactionFactory;

public interface QueryTemplateFactory {

    QueryTemplate create();
    void isShowSql(Boolean isShowSql);

}
