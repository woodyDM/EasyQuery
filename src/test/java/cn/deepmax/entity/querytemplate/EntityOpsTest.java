package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class EntityOpsTest extends BaseTest {

    @Test
    public void testSave(){
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.TEN);
        user.setUserName("eq");
        user.setAuth("1234auth");
        user.setBigDecimal(BigDecimal.ONE);
        user.setaBigPoint(123.23D);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setCreateTime(now);
        user.setUpdateDate(new Date(now.getTime()));
        user.setHide(true);
        user.setShow(false);
        user.setTransientProperty(345.234D);
        QueryTemplate template = factory.create();
        template.save(user);
        Assert.notNull(user.getId(),"Id null");

    }

    @Test
    public void testSelect(){
        QueryTemplate queryTemplate = factory.create();
        SuperUser user = queryTemplate.get(SuperUser.class,2);
        Assert.notNull(user,"user null");
        Assert.notNull(user.getBigDecimal(),"bigDecimal null");
        Assert.isTrue(user.getUserName().equals("name2"),"name2 check");
        Assert.isTrue(user.getShow(),"show should be true");
        Assert.isTrue(!user.isHide(),"should hide");
    }

    @Test
    public void testUpdate(){
        QueryTemplate queryTemplate = factory.create();
        SuperUser user = queryTemplate.get(SuperUser.class,1);
        Assert.notNull(user,"user null");
        Assert.isTrue(user.getUserName().equals("name1"),"name1 check.");
        user.setUserName("name1order");
        queryTemplate.save(user);
        SuperUser newUser = queryTemplate.get(SuperUser.class,1);
        Assert.isTrue(newUser.getUserName().equals("name1order"),"update name check");
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
        Assert.isNull(newUser,"selectList new user");
    }

}
