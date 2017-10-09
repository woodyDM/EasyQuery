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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Test
    public void testSelectEntity(){
        String sql = "select * from t_user ";
        QueryTemplate template = factory.create();
        List<User> re = template.selectEntity(sql,User.class);
        Assert.assertTrue(re!=null);

    }

    @Test
    public void testSelectOneEntity(){
        String sql = "select * from t_user where id1=? ";
        QueryTemplate template = factory.create();
        User re = template.selectOneEntity(sql,User.class,-1);
        Assert.assertTrue(re==null);
    }
    @Test
    public void testSelectList2(){
        String sql = "select id1 id2, createTime create_time,bigDecimal big_decimal, point1 from t_user where id1=? ";
        QueryTemplate template = factory.create();
        Map<String,String> map = new HashMap<>();
        map.put("id2","id2");
        map.put("create_time","createTime");
        map.put("big_decimal","bigDecimal");
        SuperUser re = template.selectOneEntity(sql,SuperUser.class,map,-1);
        Assert.assertTrue(re==null);
    }

    @Test
    public void testBatch(){
        String sql = "update t_user set name = ? where id1= ? ";
        QueryTemplate template = factory.create();
        List<Object> list = new ArrayList<>();
        list.add("smalllllllllllll");
        list.add(1);
        List<Object> list2 = new ArrayList<>();
        List<List<Object>> listAll = new ArrayList<>();
        listAll.add(list);
        listAll.add(list2);
        list2.add("big");
        list2.add(3);
        int[] re = template.executeBatch(sql,listAll);
        Assert.assertTrue(re.length==2);

        List<Object> list1 = new ArrayList<>();
        list1.add("okkkkkk");
        list1.add(1);
        template.executeUpdate(sql,list1.toArray());

    }
}
