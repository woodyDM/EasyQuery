package cn.deepmax.entity.generator;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.List;

public class GeneratorTest extends BaseTest {

    @Test
    public void testGenerator(){
        QueryTemplate template = defaultFactory.create();
        String sql = "select * from super_user where id >  ? ";
        List<SuperUser> user = template.selectList(sql,SuperUser.class,0);
        Assert.notNull(user,"usernot null");
    }
}
