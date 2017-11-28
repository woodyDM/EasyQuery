package cn.deepmax.transaction;

import cn.deepmax.exception.EasyQueryException;
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
        return isTransactionMode;
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
            logger.debug("Already in springTransaction.");
        }
    }


    @Override
    public void commit() {
        if(!isInSpringTransactionMode){
            super.commit();
        }else{
            logger.debug("Already in springTransaction.");
        }
    }

    @Override
    public void rollback() {
        if(!isInSpringTransactionMode){
            super.rollback();
        }else{
            logger.debug("Already in springTransaction.");
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
            logger.debug("<<<<<<ReleaseSpringConnection [{}]",connection.toString());
            DataSourceUtils.releaseConnection(connection,dataSource);
            connection = null;
        }
    }

    private Connection doGetConnection(){
        connection = DataSourceUtils.getConnection(dataSource);
        try {
            isAutoCommit = connection.getAutoCommit();
            oldAutoCommit = isAutoCommit;
        } catch (SQLException e) {
            throw new EasyQueryException(e);
        }
        isInSpringTransactionMode = DataSourceUtils.isConnectionTransactional(connection,dataSource);
        isTransactionMode = false;
        logger.debug(">>>>>>Create connection[{}] via DataSourceUtils.Spring transactional:[{}]",connection.toString(),isInSpringTransactionMode);
        return connection;
    }
}
