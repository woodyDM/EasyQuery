package cn.deepmax.entity.querytemplate;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.adapter.EnumType;
import cn.deepmax.entity.adapter.MyColor;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EntityOpsTest extends BaseTest {

    @Test
    public void testSave(){
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.TEN);
        user.setUserName("eq");
        user.setAuth(EnumType.TYPE1);
        user.setBigDecimal(BigDecimal.ONE);
        user.setaBigPoint(123.23D);
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateDate(now.plusDays(1).toLocalDate());
        user.setHide(true);
        user.setShow(false);
        user.setTransientProperty(345.234D);
        user.setColor1(MyColor.BLACK);
        user.setColor4(MyColor.RED);
        user.setColor2(MyColor.WHITE);
        QueryTemplate template = factory.create();
        template.save(user);
        Assert.notNull(user.getId(),"Id null");
        SuperUser u2 = template.get(SuperUser.class, user.getId());
        Assert.notNull(u2,"u2 not null");

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
