package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.resultsethandler.RowRecord;
import org.junit.Test;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExecuteTest extends BaseTest {

    @Test
    public void testExecuteUpdate(){
        QueryTemplate template = factory.create();
        SuperUser user = new SuperUser();
        user.setName("kitty");
        user.setBigDecimal(BigDecimal.ONE);
        template.save(user);
        Long id = user.getId();
        String sql = "update superuser set name ='ko' ,ok1 = '2' where id  = ? ";
        int i = template.executeUpdate(sql,id);
        Assert.isTrue(i==1,"execute 1 row");
        SuperUser sUser = template.get(SuperUser.class,id);
        Assert.isTrue(sUser.getName().equals("ko"),"udpated name check");
        Assert.isTrue(sUser.getOk1(),"decimal  check");
    }

    @Test
    public void testSelectList(){
        QueryTemplate template = factory.create();
        String sql = "select * from superuser";
        List<SuperUser> list = template.selectList(sql,SuperUser.class);
        Assert.notNull(list,"list not null");
        Assert.isTrue(list.size()>0,"size check");
        List<Map<String,Object>> list2 = template.selectList(sql);
        Assert.isTrue(list2.size()>0,"size check2");
        List<RowRecord<SuperUser>> list3 = template.selectListEx(sql,SuperUser.class);
        Assert.isTrue(list3.get(0).e.getId()!=null,"id test");
        List<RowRecord> list4 = template.selectListEx(sql);
        Assert.isTrue(list4.size()>0,"size check4");
    }

}