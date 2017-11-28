package cn.deepmax.entity;


import cn.deepmax.mapper.column.*;
import cn.deepmax.mapper.table.PascalToLowerUnderLineTableNameMapper;
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
        DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory(h2Datasource());
        factory.setTransactionFactory(new SpringTransactionFactory());
        factory.isShowSql(true);
        MappedEntityInfo entityInfo = new MappedEntityInfo();
        entityInfo.setToColumnNameMapper(new CamelToUpperUnderLineColumnNameMapper());
        entityInfo.setToTableNameMapper(new PascalToLowerUnderLineTableNameMapper());
        factory.setEntityInfo(entityInfo);
        return factory.build();
    }

    @Bean("defaultFactory")
    public QueryTemplateFactory defaultFactory(){
        DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory(h2Datasource());
        factory.isShowSql(true);
        MappedEntityInfo entityInfo = new MappedEntityInfo();
        entityInfo.setToColumnNameMapper(new CamelToUpperUnderLineColumnNameMapper());
        entityInfo.setToTableNameMapper(new PascalToLowerUnderLineTableNameMapper());
        factory.setEntityInfo(entityInfo);
        factory.getConfig().setGenerateClass(true);
        factory.getConfig().setToFieldNameMapper(new UpperUnderlineToCamelColumnNameMapper());
        factory.getConfig().setValueObjectPath("D:/test/vo");
        factory.getConfig().setEntityPath("D:/test/entity");
        return factory.build();
    }

    @Bean("jpaFactory")
    public QueryTemplateFactory jpaFactory(){
        DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory(h2Datasource());
        factory.setEntityInfo(new JpaEntityInfo());
        factory.isShowSql(true);
        factory.getConfig().setGenerateClass(true);
        factory.getConfig().setToFieldNameMapper(new UpperUnderlineToCamelColumnNameMapper());
        factory.getConfig().setValueObjectPath("D:/test/vo");
        factory.getConfig().setEntityPath("D:/test/entity");
        return factory.build();
    }


//    @Bean
//    public PlatformTransactionManager platformTransactionManager(){
//        DataSourceTransactionManager manager = new DataSourceTransactionManager(localDatasource());
//        return manager;
//    }
//
//    @Bean("localDatasource")
//    public DataSource localDatasource(){
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false");
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");
//        return dataSource ;
//    }
//
//
//
//    @Bean("localSpringFactory")
//    public QueryTemplateFactory localSpringFactory(){
//        DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory(localDatasource());
//        factory.setTransactionFactory(new SpringTransactionFactory());
//
//        factory.setEntityInfo(new JpaEntityInfo());
//        factory.isShowSql(true);
//        factory.getConfig().setGenerateClass(true);
//        factory.getConfig().setToFieldNameMapper(new LowerUnderlineToCamelColumnNameMapper());
//        factory.getConfig().setValueObjectPath("D:\\Projects\\EasyQuery\\src\\test\\java\\");
//        factory.getConfig().setEntityPath("D:\\Projects\\EasyQuery\\src\\test\\java\\");
//        return factory.build();
//    }




}
