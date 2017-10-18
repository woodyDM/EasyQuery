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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BeanConfig.class)
public class BaseTest {

    @Autowired
    protected QueryTemplateFactory factory;
    @Autowired
    DataSource dataSource;

    @Before
    public void init() throws Exception{
        String sql = getClass().getResource("/superuser.sql").toURI().toString().substring(6);
        Connection cn = dataSource.getConnection();
        Statement st = cn.createStatement();
        String delete = "drop table if exists SUPERUSER;";
        String createTalbe="CREATE TABLE  superuser  (" +
                "  id int  PRIMARY KEY auto_increment, " +
                "  name varchar(255)  ,createTime datetime, bigDecimal decimal(10,3),point1 DOUBLE  ,point2 double ," +
                " point3 FLOAT ,point4 FLOAT , ok1 int ,  ok2 int,updateDate date, )   ;";
        st.execute(delete);
        st.execute(createTalbe);
        st.execute("INSERT INTO `superuser` VALUES ('1', 'name1', '2017-10-18 13:57:05', '1.300', '1.500', '1.500', '1.400', '1.200', '1', '0', '2017-10-18');");
        st.execute("INSERT INTO `superuser` VALUES ('2', 'name2', '2017-10-18 13:57:05', '1.400', '1.500', '1.500', '1.400', '1.200', '1', '0', '2017-10-18');");
        st.close();
        cn.close();
    }

    @Test
    public void test(){
        Assert.notNull(factory,"facotyr is null");
        QueryTemplate template = factory.create();
        Assert.notNull(template,"template is null");
    }
}
