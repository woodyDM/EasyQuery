package cn.deepmax.entity;


import cn.deepmax.mapper.SameNameMapper;
import cn.deepmax.mapper.UpperCaseColumnNameMapper;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.SimpleQueryTemplateFactory;
import cn.deepmax.transaction.DefaultTransactionFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "cn.deepmax")
@EnableTransactionManagement
public class SpringBeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringBeanConfig.class);


    @Bean("H2Datasource")
    public DataSource h2Datasource(){

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource ;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        DataSourceTransactionManager manager = new DataSourceTransactionManager(localDatasource());
        return manager;
    }

    @Bean("localDatasource")
    public DataSource localDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource ;
    }

    @Bean("springFactory")
    public QueryTemplateFactory factory(){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(h2Datasource());
        factory.setToColumnNameMapper(new UpperCaseColumnNameMapper());
        factory.isShowSql(true);
        return factory.build();
    }

    @Bean("localSpringFactory")
    public QueryTemplateFactory localSpringFactory(){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(localDatasource());
        factory.setToColumnNameMapper(new SameNameMapper());
        factory.isShowSql(true);
        return factory.build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(h2Datasource());
        factory.setToColumnNameMapper(new UpperCaseColumnNameMapper());
        factory.setTransactionFactory(new DefaultTransactionFactory());
        factory.isShowSql(true);
        return factory.build();
    }

}
