package cn.deepmax.querytemplate;


import cn.deepmax.resultsethandler.DefaultResultSetHandler;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.entityUtils.DefaultEntityFactory;
import cn.deepmax.entityUtils.EntityFactory;
import cn.deepmax.transaction.DefaultTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import javax.sql.DataSource;


public class DefaultQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private ResultSetHandler resultSetHandler;
    private TransactionFactory transactionFactory;
    private EntityFactory entityFactory;

    public DefaultQueryTemplateFactory(DataSource dataSource) {
        this.dataSource = dataSource;
        if(resultSetHandler==null){
            resultSetHandler = new DefaultResultSetHandler();
        }
        if(transactionFactory==null){
            transactionFactory = new DefaultTransactionFactory();
        }
        if(entityFactory==null){
            entityFactory = new DefaultEntityFactory();
        }
    }

    public void setResultSetHandler(ResultSetHandler resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }


    @Override
    public QueryTemplate create(){
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        return new DefaultQueryTemplate(resultSetHandler,transaction,entityFactory);
    }


}
