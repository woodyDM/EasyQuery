package cn.deepmax.core;

import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;


public class SpringQueryTemplate extends AbstractQueryTemplate{

    public SpringQueryTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public SpringQueryTemplate(DataSource dataSource, ResultSetHandler resultSetHandler) {
        super(dataSource,resultSetHandler);
    }
    @Override
    public Connection getCurrentConnection() {
        try {
            Class clazz = Class.forName("org.springframework.jdbc.datasource.DataSourceUtils");

            Method method = clazz.getMethod("getConnection",DataSource.class);

            Object o = method.invoke(null,super.dataSource);
            return (Connection)o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
