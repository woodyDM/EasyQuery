package cn.deepmax.entity;

import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BeanConfig.class)
public class ApplicationContext {
    @Autowired
    QueryTemplateFactory factory;

    @Test
    public void test(){
        Assert.notNull(factory,"factory is null");
        QueryTemplate template = factory.create();
        Assert.notNull(template,"factory is null");
        Object o = template.select("select * from user");
        Assert.notNull(o,"ff");


    }
}
