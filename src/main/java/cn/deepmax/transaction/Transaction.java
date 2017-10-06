package cn.deepmax.transaction;

import java.sql.Connection;

public interface Transaction {

    boolean isTransactionMode();
    boolean isAutoCommit();
    void beginTransaction();
    void commit();
    void rollback();
    void close();
    Connection getConnection();

}
