package cn.deepmax.entity;


import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.querytemplate.SimpleQueryTemplateFactory;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static org.mockito.Mockito.*;

@Configuration
public class BeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    List<Map<String,Object>> datas = new ArrayList<>();
    {
        Map<String,Object> row1 = new HashMap<String, Object>();
        row1.put("id", 1);
        row1.put("name", "gary1");

        datas.add(row1) ;

        Map<String,Object> row2 = new HashMap<String, Object>();
        row2.put("id", 1);
        row2.put("name", "gary");

        datas.add(row2) ;
    }


    boolean started = false ;
    Iterator<Map<String,Object>> iterator ;
    Map<String,Object> currentMap ;


    @Bean("datasource")
    public DataSource getDataSource(){
        DataSource dataSource = mock(DataSource.class);
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class) ;
        try {
            dataSource.getConnection() ;
            when(dataSource.getConnection()).then(t -> {
                logger.debug( "get conn" ); ;
                return conn ;
            });

            doAnswer(t -> {
                Object[] arguments = t.getArguments() ;
                logger.debug( "set autocommit " + arguments[0]);
                return new DoesNothing();
            }).when(conn).setAutoCommit(anyBoolean());

            doAnswer(t -> {
                logger.debug( "commit");
                return new DoesNothing();
            }).when(conn).commit();
            doAnswer(t -> {
                logger.debug( "rollback");
                return new DoesNothing();
            }).when(conn).rollback();
            doAnswer(t -> {
                logger.debug( "close");
                return new DoesNothing();
            }).when(conn).close();

            when(conn.createStatement()).thenReturn(stmt) ;

            when(stmt.executeQuery(anyString())).thenReturn(rs) ;

            doAnswer( t -> {
                if(!started){
                    started = true;
                    iterator = datas.iterator();
                }
                boolean flag = iterator.hasNext();
                if(!flag){
                    started = false ;
                    iterator = null ;
                }
                if(started){
                    currentMap = iterator.next();
                }
                return flag ;
            }).when(rs).next() ;


            doAnswer(t->{
                Object[] params = t.getArguments();
                return currentMap.get( params[0] ) ;
            }).when(rs).getObject(anyString()) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource ;
    }

    @Bean
    public QueryTemplateFactory factory(DataSource dataSource){
        SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(dataSource);
        factory.isShowSql(true);
        return factory.build();
    }

}
