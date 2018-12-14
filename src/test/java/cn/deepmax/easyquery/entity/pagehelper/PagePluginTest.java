package cn.deepmax.easyquery.entity.pagehelper;

import cn.deepmax.easyquery.pagehelper.PageInfo;
import cn.deepmax.easyquery.querytemplate.QueryTemplate;
import cn.deepmax.easyquery.resultsethandler.RowRecord;
import cn.deepmax.easyquery.entity.BaseTest;
import cn.deepmax.easyquery.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Map;

public class PagePluginTest extends BaseTest {


    @Test
    public void testPageHelperDefault(){
        testPageHelper_(defaultFactory.create());
    }

    @Test
    public void testPageHelperSpring(){
        testPageHelper_(springFactory.create());
    }

    @Test
    public void testPageHelperSpring2(){
        testPageHelper_(queryTemplate);
    }


    private void testPageHelper_(QueryTemplate template  ){


        String sql = "select * from super_user where big_decimal > ? ";
        PageInfo<Map<String,Object>> info = template.selectPage(sql,1,3,1);
        Assert.isTrue(info.getData().size() > 0 ,"info notnull");

        PageInfo<RowRecord> info2 = template.selectPageEx(sql,2,3,3.6);
        Assert.isTrue(info2.getData().size()> 0 ,"info2 notnull");

        PageInfo<RowRecord> info3 = template.selectPageEx(sql, 3,3,0.3333);
        Assert.notNull(info3,"info3 notnull");
        Assert.isTrue(info3.isNotEmpty(),"info3 not empty");

        String sql2 = "select * from super_user where big_decimal > ? and id > ? ";
        PageInfo<SuperUser> info4 = template.selectPage(sql2,SuperUser.class,1,3,1.2,2);
        Assert.isTrue(info4.isNotEmpty(),"info4 not empty.");

        PageInfo<SuperUser> pageInfo = template.selectPage(sql2,(RowRecord oneRecord) ->{
            SuperUser user = new SuperUser();
            user.setUserName(oneRecord.getString("USER_NAME"));
            user.setBigDecimal(oneRecord.getBigDecimal("BIG_DECIMAL"));
            return user;
        }, 1,4,0.2,4);
        Assert.isTrue(pageInfo.isNotEmpty(),"pageInfo not empty");
        
    }
}
