package cn.deepmax.querytemplate;

import cn.deepmax.mapper.ColumnNameMapper;
import cn.deepmax.transaction.TransactionFactory;

public interface QueryTemplateFactory {

    QueryTemplate create();
    void setColumnNameMapper( ColumnNameMapper columnNameMapper);
    void setTransactionFactory ( TransactionFactory transactionFactory);
    void setShowSql(boolean isShowSql);
}
