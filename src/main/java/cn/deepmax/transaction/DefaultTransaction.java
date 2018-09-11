package cn.deepmax.transaction;

import cn.deepmax.exception.EasyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultTransaction implements Transaction {

    private static  final  Logger logger = LoggerFactory.getLogger(DefaultTransaction.class);
    protected DataSource dataSource;
    protected boolean isTransactionMode;
    protected Connection connection;
    protected boolean isAutoCommit;
    protected boolean oldAutoCommit;


    public DefaultTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isTransactionMode() {
        return isTransactionMode;
    }

    @Override
    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    @Override
    public void beginTransaction() {
        getConnection();
        if(!isTransactionMode){
            logger.debug(">>>>>>Begin new transaction with connection [{}]",connection.toString());
            isTransactionMode = true;
            oldAutoCommit = isAutoCommit;
            if(isAutoCommit){
                try {
                    connection.setAutoCommit(false);
                    isAutoCommit = false;
                } catch (SQLException e) {
                    throw new EasyQueryException("Failed to set autocommit to false",e);
                }
            }
        }else{
            logger.debug("Already in transaction with connection[{}]",connection.toString());
        }
    }


    @Override
    public void commit() {
        if(isTransactionMode){
            logger.debug(">>>>>>Commit transaction with connection [{}]",connection.toString());
            try {
                connection.commit();
                connection.setAutoCommit(oldAutoCommit);
                isTransactionMode = false;
                isAutoCommit = oldAutoCommit;
            } catch (SQLException e) {
                throw new EasyQueryException("Fail to commit.",e);
            }
        }else{
            throw new EasyQueryException("Unable to commit without transaction.");
        }
    }

    @Override
    public void rollback() {
        if(isTransactionMode){
            logger.debug("<<<<<<Rollback transaction with connection [{}]",connection.toString());
            try {
                connection.rollback();
                connection.setAutoCommit(oldAutoCommit);
                isTransactionMode = false;
                isAutoCommit = oldAutoCommit;
            } catch (SQLException e) {
                throw new EasyQueryException("Fail to rollback.",e);
            }
        }else{
            throw new EasyQueryException("Unable to rollback without transaction.");
        }
    }

    @Override
    public void close() {

        if(connection!=null){
            try {
                connection.close();
                logger.debug("<<<<<<Close connection [{}]",connection.toString());
                connection = null;
            } catch (SQLException e) {
                throw new EasyQueryException("Fail to close connection",e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        if(connection==null){
            doGetConnection();
        }
        return connection;
    }

    private Connection doGetConnection(){
        try {
            connection = dataSource.getConnection();
            isAutoCommit = connection.getAutoCommit();
            oldAutoCommit = isAutoCommit;
            //if isAutoCommit is false , set transactionMode to true.
            isTransactionMode = !isAutoCommit;
            logger.debug(">>>>>>Create new connection [{}], transaction={}",connection.toString(),isTransactionMode);
        } catch (SQLException e) {
            throw new EasyQueryException("Fail to create connection",e);
        }
        return connection;
    }
}
