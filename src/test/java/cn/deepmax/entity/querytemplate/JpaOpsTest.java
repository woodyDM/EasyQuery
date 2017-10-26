package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class JpaOpsTest extends BaseTest{

    @Test
    public void test1(){
        QueryTemplate template = jpaFactory.create();
        Assert.notNull(template,"tem null");
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.TEN);
        template.save(user);
    }
}
