package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.resultsethandler.RowRecord;
import org.junit.Test;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ExecuteTest extends BaseTest {

    @Test
    public void testExecuteUpdate(){
        QueryTemplate template = factory.create();
        SuperUser user = new SuperUser();
        user.setUserName("kitty");
        user.setBigDecimal(BigDecimal.ONE);
        template.save(user);
        Long id = user.getId();
        String sql = "update super_user set user_name ='ko' ,hide = ? where id  = ? ";
        int i = template.executeUpdate(sql,1,id);
        Assert.isTrue(i==1,"execute 1 row");
        SuperUser sUser = template.get(SuperUser.class,id);
        Assert.isTrue(sUser.getUserName().equals("ko"),"udpated name check");
        Assert.isTrue(sUser.isHide(),"boolean  check");
    }

    @Test
    public void testSelectList(){
        QueryTemplate template = factory.create();
        String sql = "select * from super_user";
        List<SuperUser> list = template.selectList(sql,SuperUser.class);
        Assert.notNull(list,"list not null");
        Assert.isTrue(list.size()>0,"size check");
        List<Map<String,Object>> list2 = template.selectList(sql);
        Assert.isTrue(list2.size()>0,"size check2");
        List<SuperUser> list3 = template.selectList (sql,it->{
            SuperUser temp = new SuperUser();
            temp.setUserName(it.getString("USER_NAME"));
            return temp;
        });

        List<RowRecord> list4 = template.selectListEx(sql);
        Assert.isTrue(list4.size()>0,"size check4");
    }

    @Test
    public void testSelectScalar(){
        QueryTemplate template = factory.create();
        String sql = "select count(*) awe from super_user";
        Integer l = template.selectScalar(sql,Integer.class);
        Assert.isTrue(l>0,"L>0");

    }

}
