package cn.deepmax.easyquery.entity.adapter;

import cn.deepmax.easyquery.adapter.mapper.MapperHolder;
import cn.deepmax.easyquery.querytemplate.QueryTemplate;
import cn.deepmax.easyquery.support.LocalCache;
import cn.deepmax.easyquery.entity.BaseTest;
import cn.deepmax.easyquery.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public class AdapterCacheTest extends BaseTest {

    @Test
    public void testCache(){
        QueryTemplate template = defaultFactory.create();
        for (int i = 0; i < 100; i++) {
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
            template.save(user);
            Assert.notNull(user.getId(),"Id null");
            SuperUser u2 = template.get(SuperUser.class, user.getId());
            Assert.notNull(u2,"u2 not null");
            u2.setColor1(MyColor.RED);
            template.save(u2);
            SuperUser u3 = template.get(SuperUser.class, u2.getId());
            Assert.isTrue(u3.getColor1().equals(MyColor.RED),"color check");
            template.delete(u3);
        }
        LocalCache cache = new LocalCache(new HashMap());
        MapperHolder holder = new MapperHolder();
        Assert.notNull(template,"pause");

    }
}
