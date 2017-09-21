package cn.deepmax.transaction;

import javax.sql.DataSource;

public class SpringTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(DataSource dataSource) {
        return new DefaultTransaction(dataSource);
    }
}
