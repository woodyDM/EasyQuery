package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class JpaOpsTest extends BaseTest{

    @Test
    public void test1(){
        QueryTemplate template = factory.create();
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
