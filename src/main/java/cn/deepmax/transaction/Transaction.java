package cn.deepmax.transaction;

import java.sql.Connection;

public interface Transaction {

    /**
     * need auto close?
     * @return
     */
    boolean needClose();

    /**
     * begin transaction.
     */
    void beginTransaction();

    /**
     * commit changes
     */
    void commit();

    /**
     * rollback if exception
     */
    void rollback();

    /**
     * close connection
     */
    void close();

    /**
     * get connection for using.
     * @return
     */
    Connection getConnection();

}
