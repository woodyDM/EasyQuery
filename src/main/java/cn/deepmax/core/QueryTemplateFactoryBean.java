package cn.deepmax.core;


import javax.sql.DataSource;

public class QueryTemplateFactoryBean  {

    DataSource dataSource;

    public QueryTemplateFactoryBean(DataSource dataSource) {
        this.dataSource = dataSource;
    }


}
