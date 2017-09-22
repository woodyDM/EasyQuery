package cn.deepmax.querytemplate;

import cn.deepmax.entityUtils.EntityFactory;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.transaction.TransactionFactory;

public interface QueryTemplateFactory {
    QueryTemplate create();
    void setResultSetHandler( ResultSetHandler resultSetHandler);
    void setTransactionFactory ( TransactionFactory transactionFactory);
    void setEntityFactory( EntityFactory entityFactory);

}
