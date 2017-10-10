package cn.deepmax.save;


import cn.deepmax.AppTest;
import cn.deepmax.entity.entity.SuperUser;
import cn.deepmax.entity.entity.User;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTest.class)
public class SaveTest {

    @Autowired
    QueryTemplateFactory factory;

    @Test
    public void saveTest(){
        SuperUser u = new SuperUser();
        u.setName("woody4super");
        u.setCreateTime(new Timestamp(System.currentTimeMillis()));
        u.setPoint1(33.3D);
        u.setPoint2(32.2D);
        u.setPoint3(2F);
        u.setPoint4(3.4F);
        u.setUpdateDate(new Date());
        u.setBigDecimal(BigDecimal.TEN);

        QueryTemplate template = factory.create();
        template.save(u);
        System.out.println("after save  "+u);
        u.setPoint1(1111.1D);
        u.setBigDecimal(BigDecimal.ONE);
        template.update(u);
        System.out.println("after update  "+u);
    }
}
