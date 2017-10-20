package cn.deepmax.entity;


import cn.deepmax.mapper.UpperCaseColumnNameMapper;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.SimpleQueryTemplateFactory;
import cn.deepmax.transaction.DefaultTransactionFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringBeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringBeanConfig.class);


    @Bean("datasource")
    public DataSource getDataSource(){

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource ;
    }

    @Bean("springFactory")
    public QueryTemplateFactory factory(DataSource dataSource){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(dataSource);
        factory.setToColumnNameMapper(new UpperCaseColumnNameMapper());
        factory.isShowSql(true);
        return factory.build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(DataSource dataSource){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(dataSource);
        factory.setToColumnNameMapper(new UpperCaseColumnNameMapper());
        factory.setTransactionFactory(new DefaultTransactionFactory());
        factory.isShowSql(true);
        return factory.build();
    }

}
