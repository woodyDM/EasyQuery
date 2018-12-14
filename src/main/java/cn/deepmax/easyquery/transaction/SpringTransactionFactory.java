package cn.deepmax.easyquery.transaction;

import javax.sql.DataSource;

public class SpringTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(DataSource dataSource) {
        return new SpringTransaction(dataSource);
    }
}
