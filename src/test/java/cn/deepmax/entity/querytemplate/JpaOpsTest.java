package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class JpaOpsTest extends BaseTest{

    @Test
    public void testDefault(){
        jpaTest(defaultFactory.create());
    }

    @Test
    public void testSpring01(){
        jpaTest(springFactory.create());
    }
    @Test
    public void testSpring02(){
        jpaTest(queryTemplate);
    }



    private void jpaTest(QueryTemplate template){

        Assert.notNull(template,"tem null");
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.TEN);
        template.save(user);
        Long id = user.getId();
        Assert.notNull(id,"id null");
        SuperUser u = template.get(SuperUser.class,id);
        Assert.isTrue(u.getBigDecimal().equals(user.getBigDecimal()),"eq");
        user.setBigDecimal(BigDecimal.ONE);
        template.save(user);
        u = template.get(SuperUser.class,id);
        Assert.isTrue(u.getBigDecimal().equals(user.getBigDecimal()),"eq");


    }
}
