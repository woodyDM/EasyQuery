package cn.deepmax.entity.generator;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

public class GeneratorTest extends BaseTest {

    @Test
    public void testGenerator(){
        QueryTemplate template = defaultFactory.create();
        String sql = "select * from super_user where id =  ? ";
        SuperUser user = template.select(sql,SuperUser.class,1);
        Assert.notNull(user,"usernot null");
    }
}
