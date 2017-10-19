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
        getConnection();
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

        if(!isTransactionMode){
            logger.debug("Open new Transaction with connection [{}]",connection.toString());
            isTransactionMode = true;
            oldAutoCommit = isAutoCommit;
            if(isAutoCommit){
                try {
                    connection.setAutoCommit(false);
                    isAutoCommit = false;
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to set autocommit to false",e);
                }
            }
        }else{
            logger.debug("Already in transaction mode with connection [{}]",connection.toString());
        }
    }


    @Override
    public void commit() {
        if(isTransactionMode){
            logger.debug("Commit transaction with connection [{}]",connection.toString());
            try {
                connection.commit();
                connection.setAutoCommit(oldAutoCommit);
                isTransactionMode = false;
                isAutoCommit = oldAutoCommit;
            } catch (SQLException e) {
                throw new RuntimeException("Fail to commit.",e);
            }
        }else{
            throw new IllegalStateException("Should not commit without a transaction.");
        }
    }

    @Override
    public void rollback() {
        if(isTransactionMode){
            logger.debug("Rollback transaction with connection [{}]",connection.toString());
            try {
                connection.rollback();
                connection.setAutoCommit(oldAutoCommit);
                isTransactionMode = false;
                isAutoCommit = oldAutoCommit;
            } catch (SQLException e) {
                throw new RuntimeException("Fail to rollback.",e);
            }
        }else{
            throw new IllegalStateException("Should not rollback without a transaction.");
        }
    }

    @Override
    public void close() {
        if(connection!=null){

            try {
                connection.close();
                logger.debug("Close connection [{}]",connection.toString());
                connection = null;
            } catch (SQLException e) {
                throw new EasyQueryException(e);
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
            isTransactionMode = false;
            logger.debug("Create new connection [{}]",connection.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
