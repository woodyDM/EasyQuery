package cn.deepmax.transaction;

import cn.deepmax.exception.EasyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringTransaction implements Transaction{


    private DataSource dataSource;
    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(SpringTransaction.class);

    public SpringTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean needClose() {
        return true;
    }

    @Override
    public void beginTransaction() {
        logger.debug("Already in springTransaction.Do not [beginTransaction]");
    }


    @Override
    public void commit() {
        logger.debug("Already in springTransaction.Do not [commit]");
    }

    @Override
    public void rollback() {
        logger.debug("Already in springTransaction.Do not [rollback]");
    }

    @Override
    public Connection getConnection() {
        if(connectionThreadLocal.get()==null){
            createConnection();
        }
        return connectionThreadLocal.get();
    }

    @Override
    public void close() {
        Connection connection = connectionThreadLocal.get();
        Assert.notNull(connection, "Connection is null.");
        logger.debug("<<<<<<ReleaseSpringConnection [{}]",connection.toString());
        DataSourceUtils.releaseConnection(connection, dataSource);
        connectionThreadLocal.remove();
    }

    private Connection createConnection(){
        Connection connection = DataSourceUtils.getConnection(dataSource);
        boolean isInSpringTransactionMode = DataSourceUtils.isConnectionTransactional(connection, dataSource);
        logger.debug(">>>>>>Create connection[{}] via DataSourceUtils.Spring transactional:[{}]",connection.toString(),isInSpringTransactionMode);
        connectionThreadLocal.set(connection);
        return connection;
    }
}
