package cn.deepmax.generator;

import cn.deepmax.exception.EasyQueryException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SimpleDataSource implements DataSource {
    private Config config;

    public SimpleDataSource(Config config) {
        this.config = config;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(config);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection(config);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    private static Connection getConnection(Config config){
        try{
            Class.forName(config.getDatabaseDriver());
            Connection cn = DriverManager.getConnection(config.getDatabaseUrl(), config.getDatabaseUserName(), config.getDatabasePassword());
            cn.setAutoCommit(true);
            return cn;
        }catch (Exception e){
            throw new EasyQueryException("exception in SimpleDataSource" , e);
        }
    }
}
