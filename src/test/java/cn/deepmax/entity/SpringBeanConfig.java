package cn.deepmax.entity;


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

    @Bean("springFactory")
    public QueryTemplateFactory factory(){
        JpaEntityInfo entityInfo = new JpaEntityInfo();

        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        return builder.setDataSource(h2Datasource())
                .setTransactionFactory(new SpringTransactionFactory())
                .setShowSql(true)

                .setEntityInfo(entityInfo)
                .setPagePlugin(new MySqlPagePlugin())
                .build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        EntityInfo entityInfo = new JpaEntityInfo();

        return builder.setDataSource(h2Datasource())
                .setShowSql(true)
                .setEntityInfo(entityInfo)

                .build();
    }

    @Bean("jpaFactory")
    public QueryTemplateFactory jpaFactory(){

        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();

        return builder.setDataSource(h2Datasource())
                .setShowSql(true)

                .build();
    }

//
//    @Bean
//    public PlatformTransactionManager platformTransactionManager(){
//        DataSourceTransactionManager manager = new DataSourceTransactionManager(localDatasource());
//        return manager;
//    }
//
//    @Bean("localDatasource")
//    public DataSource localDatasource(){
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/test");
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("123456");
//        return dataSource ;
//    }
//
//
//
//    @Bean("localSpringFactory")
//    public QueryTemplateFactory localSpringFactory(){
//        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
//        EntityInfo info = new JpaEntityInfo();
//        builder.setDataSource(localDatasource())
//                .setTransactionFactory(new SpringTransactionFactory())
//                .setEntityInfo(info)
//                .setSqlTranslator(new DefaultSqlTranslator(info,(it)->"\""+it+"\""))
//                .setShowSql(true);
//        return builder.build();
//    }





}
