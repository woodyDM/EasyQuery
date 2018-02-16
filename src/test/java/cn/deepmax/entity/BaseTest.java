package cn.deepmax.entity;

import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringBeanConfig.class)
public class BaseTest {

    @Resource(name = "springFactory")
    protected QueryTemplateFactory factory;
    @Resource(name = "jpaFactory")
    protected QueryTemplateFactory jpaFactory;
    @Resource(name ="defaultFactory")
    protected QueryTemplateFactory defaultFactory;
    @Resource(name = "H2Datasource")
    DataSource dataSource;
//    @Resource(name = "localDatasource")
//    DataSource localDatasource;

    @Before
    public void init() throws Exception{
        Connection cn = dataSource.getConnection();
        Statement st = cn.createStatement();
        String delete = "drop table if exists super_user;";
        String createTalbe="CREATE TABLE  super_user  (" +
                "  id int  PRIMARY KEY auto_increment, " +
                "  user_name varchar(255)  ,create_time datetime, big_decimal decimal(10,0), a_big_point DOUBLE   ," +
                "  update_date date,show bit(1),hide bit(1) ) ENGINE=InnoDB   ;";
        st.execute(delete);
        st.execute(createTalbe);
        st.execute("INSERT INTO `super_user` VALUES ('1', 'name1', '2017-10-18 13:57:05', '1.52', '1.500',   '2017-10-18', '0', '1');");
        st.execute("INSERT INTO `super_user` VALUES ('2', 'name2', '2017-10-18 13:57:05', '2.25', '2.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('3', 'name3', '2017-10-18 13:57:05', '3.25', '3.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('4', 'name4', '2017-10-18 13:57:05', '4.25', '4.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('5', 'name5', '2017-10-18 13:57:05', '5.25', '5.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('6', 'name6', '2017-10-18 13:57:05', '6.25', '6.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('7', 'name7', '2017-10-18 13:57:05', '7.25', '7.500',   '2017-10-18', '1', '0');");
        st.execute("INSERT INTO `super_user` VALUES ('8', 'name8', '2017-10-18 13:57:05', '8.25', '8.500',   '2017-10-18', '1', '0');");


        st.close();
        cn.close();
    }

    @Test
    public void test(){
        Assert.notNull(factory,"factory is null");
        Assert.notNull(defaultFactory,"defaultFactory is null");
        Assert.notNull(dataSource,"dataSource is null");
    }
}
