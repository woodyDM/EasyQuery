package cn.deepmax.select;


import cn.deepmax.AppTest;
import cn.deepmax.entity.entity.SuperUser;
import cn.deepmax.entity.entity.User;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.resultsethandler.RowRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTest.class)
public class MapSelectTest {

    @Autowired
    QueryTemplateFactory factory;

    @Test
    public void testSelectList(){
        String sql = "select * from t_user where 0 = ? ";
        QueryTemplate template = factory.create();
        List<RowRecord<User>> re = template.select(sql,User.class,1);
        Assert.assertTrue(re.size()==0);

    }

    @Test
    public void testSelectScalar(){
        String sql = "select count(*) from t_user where name like '%pp%' and 0=1 ";
        QueryTemplate template = factory.create();
        Long re = template.selectScalar(sql,Long.class);
        Assert.assertTrue(re==0);

    }
}
