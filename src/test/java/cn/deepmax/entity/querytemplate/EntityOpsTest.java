package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class EntityOpsTest extends BaseTest {

    @Test
    public void testSave(){
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.TEN);
        user.setName("woody");
        QueryTemplate template = factory.create();
        template.save(user);
        Assert.notNull(user.getId(),"id null");
        Assert.isTrue(user.getId().equals(3L),"id equals");
    }

    @Test
    public void testSelect(){
        QueryTemplate queryTemplate = factory.create();
        SuperUser user = queryTemplate.get(SuperUser.class,2);
        Assert.notNull(user,"user null");
        Assert.notNull(user.getBigDecimal(),"bigdecimal null");
        Assert.isTrue(user.getName().equals("name2"),"namecheck");
    }

    @Test
    public void testUpdate(){
        QueryTemplate queryTemplate = factory.create();
        SuperUser user = queryTemplate.get(SuperUser.class,1);
        Assert.notNull(user,"user null");
        user.setName("name1order");
        queryTemplate.save(user);
        SuperUser newUser = queryTemplate.get(SuperUser.class,1);
        Assert.isTrue(newUser.getName().equals("name1order"),"update name check");
    }

    @Test
    public void testDelete(){
        QueryTemplate queryTemplate = factory.create();
        SuperUser user = new SuperUser();
        queryTemplate.save(user);
        Long userId = user.getId();
        Assert.notNull(userId,"save incre id");
        queryTemplate.delete(user);
        SuperUser newUser = queryTemplate.get(SuperUser.class,userId);
        Assert.isNull(newUser,"select new user");
    }

}
