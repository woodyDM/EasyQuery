package cn.deepmax.transaction;


import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.DefaultQueryTemplateFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"cn.deepmax"})
public class AppTransactionTest {
    public static void main(String[] args) {
        SpringApplication.run(AppTransactionTest.class,args);
    }

    @Bean
    public QueryTemplateFactory queryTemplateFactory(DataSource dataSource){
        DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory(dataSource);
        factory.setTransactionFactory(new SpringTransactionFactory());
        return factory;
    }
}
