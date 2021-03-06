package cn.deepmax.easyquery.entity.querytemplate;

import cn.deepmax.easyquery.querytemplate.QueryTemplate;
import cn.deepmax.easyquery.resultsethandler.RowRecord;
import cn.deepmax.easyquery.entity.BaseTest;
import cn.deepmax.easyquery.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExecuteTest extends BaseTest {


    @Test
    public void testDefault(){
        testExecuteUpdate(defaultFactory.create());
        testSelectList(defaultFactory.create());
        testSelectList(defaultFactory.create());
    }

    @Test
    public void testSpring01(){
        testExecuteUpdate(springFactory.create());
        testSelectList(springFactory.create());
        testSelectList(springFactory.create());
    }
    @Test
    public void testSpring02(){
        testExecuteUpdate(queryTemplate);
        testSelectList(queryTemplate);
        testSelectList(queryTemplate);
    }


    private void testExecuteUpdate(QueryTemplate template){

        SuperUser user = new SuperUser();
        user.setUserName("kitty");
        user.setBigDecimal(BigDecimal.ONE);
        user.setHide(false);
        template.save(user);
        Long id = user.getId();
        String sql = "update super_user set user_name ='ko' ,hide = ? where id  = ? ";
        int i = template.executeUpdate(sql,1,id);
        Assert.isTrue(i==1,"execute 1 row");
        SuperUser sUser = template.get(SuperUser.class,id);
        Assert.isTrue(sUser.getUserName().equals("ko"),"udpated name check");
        Assert.isTrue(sUser.isHide(),"boolean  check");
    }


    private void testSelectList(QueryTemplate template){

        String sql = "select * from super_user where id > ? ";
        List<SuperUser> list = template.selectList(sql,SuperUser.class,1);
        Assert.notNull(list,"list not null");
        Assert.isTrue(list.size()>0,"size check");
        List<Map<String,Object>> list2 = template.selectList(sql,2);
        Assert.isTrue(list2.size()>0,"size check2");
        List<SuperUser> list3 = template.selectList (sql,it->{
            SuperUser temp = new SuperUser();
            temp.setUserName(it.getString("USER_NAME"));
            return temp;
        },2);

        List<RowRecord> list4 = template.selectListEx(sql,3);
        Assert.isTrue(list4.size()>0,"size check4");
    }




    private void testSelectScalar(QueryTemplate template){

        String sql = "select count(*) awe from super_user where id > ? ";
        Integer l = template.selectScalar(sql,Integer.class, 2);
        Assert.isTrue(l>0,"L>0");
        String sql2 = " select sum(a_big_point) from super_user where id > ? ";
        BigDecimal decimal = template.selectScalar(sql2, BigDecimal.class, 3);
        Assert.isTrue(decimal.intValue()>0,"sum > 0 ");

    }

}
