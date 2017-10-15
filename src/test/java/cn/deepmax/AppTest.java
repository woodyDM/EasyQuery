package cn.deepmax;


import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.SimpleQueryTemplateFactory;
import cn.deepmax.transaction.DefaultTransactionFactory;
import cn.deepmax.transaction.TransactionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"cn.deepmax"})
public class AppTest {
    public static void main(String[] args) {
        SpringApplication.run(AppTest.class,args);
    }

    @Bean
    public QueryTemplateFactory queryTemplateFactory(DataSource dataSource){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(dataSource);
        TransactionFactory factory1 = new DefaultTransactionFactory();
        factory.setTransactionFactory(factory1);
        factory.isShowSql(true);
        return factory.build();
    }

}
