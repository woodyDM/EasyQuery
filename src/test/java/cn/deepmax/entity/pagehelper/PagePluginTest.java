package cn.deepmax.entity.pagehelper;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.pagehelper.PageInfo;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.resultsethandler.RowRecord;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Map;

public class PagePluginTest extends BaseTest {


    @Test
    public void testPageHelper(){

        QueryTemplate template = factory.create();
        String sql = "select * from super_user where id > ? ";
//        PageInfo<Map<String,Object>> info = template.selectPage(sql,3,1,1);
//        Assert.notNull(info,"info notnull");

        PageInfo<RowRecord> info2 = template.selectPageEx(sql,3,3,1);
        Assert.notNull(info2,"info2 notnull");

        PageInfo<RowRecord> info3 = template.selectPageEx(sql, 3,3,1);
        Assert.notNull(info3,"info3 notnull");
        Assert.isTrue(info3.isNotEmpty(),"info3 not empty");

        PageInfo< SuperUser> info4 = template.selectPage(sql,SuperUser.class,3,3,1);
        Assert.notNull(info4,"info4 notnull");

        PageInfo<SuperUser> info5 = template.selectPage(sql, 3,3,it->{
           SuperUser user = new SuperUser();
           user.setUserName(it.getString("USER_NAME"));
           user.setBigDecimal(it.getBigDecimal("BIG_DECIMAL"));
           return user;
        },1);
        Assert.isTrue(info5.isNotEmpty(),"info5 not empty");
        
    }
}
