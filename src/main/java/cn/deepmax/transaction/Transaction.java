package cn.deepmax.transaction;

import java.sql.Connection;

public interface Transaction {
    boolean isTransactionMode();
    void commit();
    void rollback();
    void flush();
    Connection getConnection();
    boolean isAutoCommit();
    void setAutoCommit();
}
