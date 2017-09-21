package cn.deepmax.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultTransaction implements Transaction {

    private static  final  Logger logger = LoggerFactory.getLogger(DefaultTransaction.class);
    protected DataSource dataSource;
    protected boolean isTransationMode;
    protected Connection connection;
    protected boolean isAutoCommit;
    protected boolean oldAutoCommit;


    public DefaultTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
        getConnection();
    }

    @Override
    public boolean isTransactionMode() {
        return isTransationMode;
    }

    @Override
    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    @Override
    public void beginTransaction() {

        if(!isTransationMode){
            logger.debug("open new Transaction with connection ["+connection.toString()+"]");
            isTransationMode = true;
            oldAutoCommit = isAutoCommit;
            if(isAutoCommit){
                try {
                    connection.setAutoCommit(false);
                    isAutoCommit = false;
                } catch (SQLException e) {
                    throw new RuntimeException("failed to set autocommit to false",e);
                }
            }
        }else{
            logger.debug("already in transationMode with connection"+connection.toString()+"]");
        }
    }


    @Override
    public void commit() {
        if(isTransationMode){
            logger.debug("commit Transaction with connection"+connection.toString()+"]");
            try {
                connection.commit();
                connection.setAutoCommit(oldAutoCommit);
                isTransationMode = false;
                isAutoCommit = oldAutoCommit;
                close();
            } catch (SQLException e) {
                throw new RuntimeException("fail to commit.",e);
            }
        }else{
            throw new IllegalStateException("should not commit without a transaction.");
        }
    }

    @Override
    public void rollback() {
        if(isTransationMode){
            logger.debug("rollback Transaction with connection "+connection.toString()+"]");
            try {
                connection.rollback();
                connection.setAutoCommit(oldAutoCommit);
                isTransationMode = false;
                isAutoCommit = oldAutoCommit;
            } catch (SQLException e) {
                throw new RuntimeException("fail to rollback.",e);
            }
        }else{
            throw new IllegalStateException("should not rollback without a transaction.");
        }
    }

    @Override
    public Connection getConnection() {
        if(connection==null){
            doGetConnection();
        }
        return connection;
    }

    @Override
    public void close() {
        if(connection!=null){
            logger.debug("Close connection "+connection.toString()+"]");
            try {
                if(isTransationMode){
                    rollback();
                }
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection doGetConnection(){
        try {
            connection = dataSource.getConnection();
            isAutoCommit = connection.getAutoCommit();
            isTransationMode = false;
            logger.debug(" Create new connection "+connection.toString()+"]");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
