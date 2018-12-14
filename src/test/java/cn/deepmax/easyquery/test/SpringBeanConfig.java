package cn.deepmax.easyquery.test;


import cn.deepmax.easyquery.querytemplate.DefaultQueryTemplateFactory;
import cn.deepmax.easyquery.querytemplate.QueryTemplate;
import cn.deepmax.easyquery.querytemplate.QueryTemplateFactory;
import cn.deepmax.easyquery.transaction.DefaultTransactionFactory;
import cn.deepmax.easyquery.transaction.SpringTransactionFactory;
import com.zaxxer.hikari.HikariDataSource;
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


    @Bean("H2Datasource")
    public DataSource h2Datasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource ;
    }

    @Bean("springFactory")
    public QueryTemplateFactory factory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        return builder.setDataSource(h2Datasource())
                .setTransactionFactory(new SpringTransactionFactory())
                .setShowSql(true)
                .build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        return builder.setDataSource(h2Datasource())
                .setTransactionFactory(new DefaultTransactionFactory())
                .setShowSql(true)
                .build();
    }


    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        DataSourceTransactionManager manager = new DataSourceTransactionManager(h2Datasource());
        return manager;
    }


    @Bean("springTemplate")
    public QueryTemplate localSpringFactory(){
        QueryTemplateFactory factory = factory();
        return factory.create();
    }





}
