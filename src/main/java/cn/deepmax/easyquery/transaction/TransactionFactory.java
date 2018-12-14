package cn.deepmax.easyquery.transaction;

import javax.sql.DataSource;

public interface TransactionFactory {
    Transaction newTransaction(DataSource dataSource);
}
