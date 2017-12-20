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
        String sql = "select * from super_user";
        PageInfo<Map<String,Object>> info = template.selectPage(sql,3,1);
        Assert.notNull(info,"info notnull");

        PageInfo<RowRecord> info2 = template.selectPageEx(sql,3,3);
        Assert.notNull(info2,"info2 notnull");

        PageInfo<RowRecord<SuperUser>> info3 = template.selectPageEx(sql,SuperUser.class,3,3);
        Assert.notNull(info3,"info3 notnull");
        Assert.isTrue(info3.isNotEmpty(),"not empty");

    }
}
