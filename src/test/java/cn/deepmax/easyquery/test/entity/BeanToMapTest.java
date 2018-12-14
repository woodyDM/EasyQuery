package cn.deepmax.easyquery.test.entity;

import cn.deepmax.easyquery.test.model.SuperUser;
import cn.deepmax.easyquery.util.BeanToMap;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

public class BeanToMapTest {

    @Test
    public void testBeanToMap(){
        SuperUser user = new SuperUser();
        user.setId(10L);
        user.setBigDecimal(BigDecimal.ONE);
        user.setUserName("userName");

        Map<String,Object> beanMap = BeanToMap.convert(user);
        Assert.notNull(beanMap.get("id"),"id null test");
        Assert.isNull(beanMap.get("isShow"),"1 null test");
        // primitive fields have default value
        //so ok2 is not null
        Assert.notNull(beanMap.get("hide"),"hide null test");
        //userUser has 11 fields
        Assert.isTrue(beanMap.size()!=0,"size test");
    }
}
