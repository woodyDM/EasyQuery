package cn.deepmax.easyquery.transaction;

import cn.deepmax.easyquery.exception.EasyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultTransaction implements Transaction {

    private static  final  Logger logger = LoggerFactory.getLogger(DefaultTransaction.class);
    protected DataSource dataSource;
    protected Connection connection;
    protected boolean isTransactionMode = false;
    protected Boolean oldAutoCommit ;


    public DefaultTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * need auto close ?
     * when managed by user , return false;
     * @return
     */
    @Override
    public boolean needClose() {
        return !isTransactionMode;
    }

    @Override
    public void beginTransaction() {
        if(!isTransactionMode){
            getConnection();
            logger.debug(">>>>>>Begin new transaction with connection [{}]",connection.toString());
            isTransactionMode = true;
            try {
                oldAutoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

            } catch (SQLException e) {
                throw new EasyQueryException("Failed to set autocommit to false",e);
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
                oldAutoCommit = null;
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
                oldAutoCommit = null;
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
                if(oldAutoCommit!=null){
                    connection.setAutoCommit(oldAutoCommit);
                    oldAutoCommit = null;
                }
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
            Connection connection = dataSource.getConnection();
            logger.debug(">>>>>>Create new connection [{}], transaction={}",connection.toString(),isTransactionMode);
            return this.connection = connection;
        } catch (SQLException e) {
            throw new EasyQueryException("Fail to create connection",e);
        }

    }
}
