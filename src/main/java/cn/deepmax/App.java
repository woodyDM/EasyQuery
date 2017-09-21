package cn.deepmax;


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
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }

    @Bean
    public QueryTemplateFactory queryTemplateFactory(DataSource dataSource){
        return new DefaultQueryTemplateFactory(dataSource);
    }
}
