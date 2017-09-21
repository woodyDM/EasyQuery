package cn.deepmax.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringTransaction extends DefaultTransaction {

    private static final Logger logger = LoggerFactory.getLogger(SpringTransaction.class);

    private boolean isInSpringTransactionMode;


    public SpringTransaction(DataSource dataSource) {
        super(dataSource);
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
        if(!isInSpringTransactionMode){
            super.beginTransaction();
        }else{
            logger.debug("already in springtransaction.");
        }
    }


    @Override
    public void commit() {
        if(!isInSpringTransactionMode){
            super.commit();

        }else{
            logger.debug("already in springTransaction.");
        }
    }

    @Override
    public void rollback() {
        if(!isInSpringTransactionMode){
            super.rollback();
        }else{
            logger.debug("already in springTransaction.");
        }
    }

    @Override
    public Connection getConnection() {
        if(connection==null){
            doGetConnection();
            logger.debug("DataSourceUtils.getConnection Create connection ["+connection.toString()+"]");
        }

        return connection;
    }

    @Override
    public void close() {
        if(!isInSpringTransactionMode){
            if(connection!=null){
                logger.debug("releaseSpringConnection ["+connection.toString()+"]");
                DataSourceUtils.releaseConnection(connection,dataSource);
                connection = null;
            }
        }else{
            logger.debug("already in springTransaction.");
        }

    }

    private Connection doGetConnection(){

        connection = DataSourceUtils.getConnection(dataSource);
        try {
            isAutoCommit = connection.getAutoCommit();
            oldAutoCommit = isAutoCommit;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isInSpringTransactionMode = DataSourceUtils.isConnectionTransactional(connection,dataSource);
        isTransationMode = isInSpringTransactionMode;
        return connection;
    }
}
