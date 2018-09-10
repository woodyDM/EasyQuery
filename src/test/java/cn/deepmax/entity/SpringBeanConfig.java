package cn.deepmax.entity;


import cn.deepmax.adapter.JpaAnnotatedTypeAdapter;
import cn.deepmax.adapter.TypeAdapter;
import cn.deepmax.pagehelper.MySqlPagePlugin;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.DefaultQueryTemplateFactory;
import cn.deepmax.transaction.SpringTransactionFactory;
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
                .setPagePlugin(new MySqlPagePlugin())
                .build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();


        return builder.setDataSource(h2Datasource())
                .setShowSql(true)

                .build();
    }

    @Bean("jpaFactory")
    public QueryTemplateFactory jpaFactory(){

        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();

        return builder.setDataSource(h2Datasource())
                .setShowSql(true)

                .build();
    }


    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        DataSourceTransactionManager manager = new DataSourceTransactionManager(localDatasource());
        return manager;
    }

    @Bean("localDatasource")
    public DataSource localDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/test");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        return dataSource ;
    }



    @Bean("springTemplate")
    public QueryTemplate localSpringFactory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        TypeAdapter typeAdapter = new JpaAnnotatedTypeAdapter();
        EntityInfo info = new JpaEntityInfo(typeAdapter);
        builder.setDataSource(localDatasource())
                .setTransactionFactory(new SpringTransactionFactory())
                .setEntityInfo(info)
                .setSqlTranslator(new DefaultSqlTranslator(info,(it)->"\""+it+"\"" , typeAdapter))
                .setShowSql(true);
        return builder.build().create();
    }





}
