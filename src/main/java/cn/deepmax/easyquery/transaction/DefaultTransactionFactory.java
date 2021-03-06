package cn.deepmax.easyquery.transaction;

import javax.sql.DataSource;

public class DefaultTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(DataSource dataSource) {
        return new DefaultTransaction(dataSource);
    }
}
