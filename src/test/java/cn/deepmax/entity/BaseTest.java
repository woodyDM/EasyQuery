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
        String delete = "drop table if exists superuser;";
        String createTalbe="CREATE TABLE  superuser  (" +
                "  id int  PRIMARY KEY auto_increment, " +
                "  user_name varchar(255)  ,create_time datetime, big_decimal decimal(10,0), a_big_point DOUBLE   ," +
                "  ,update_date date,is_show bit(1),is_hide bit(1) ) ENGINE=InnoDB   ;";
        st.execute(delete);
        st.execute(createTalbe);
        st.execute("INSERT INTO `superuser` VALUES ('1', 'name1', '2017-10-18 13:57:05', '1', '1.500', '1.500', '2017-10-18', '0', '1');");
        st.execute("INSERT INTO `superuser` VALUES ('2', 'name2', '2017-10-18 13:57:05', '2', '2.500', '1.500', '2017-10-18',, '1', '0');");
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
