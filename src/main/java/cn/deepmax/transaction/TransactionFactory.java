package cn.deepmax.transaction;

import javax.sql.DataSource;

public interface TransactionFactory {
    Transaction newTransaction(DataSource dataSource);
}
