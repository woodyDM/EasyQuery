package cn.deepmax.entity;

import cn.deepmax.entity.entity.SuperUser;
import cn.deepmax.entity.entity.User;
import org.junit.Test;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class BeanToMapTest {

    @Test
    public void test(){
        SuperUser u = new SuperUser();

        u.setCreateTime(new Timestamp(System.currentTimeMillis()));
        u.setBigDecimal(BigDecimal.ONE);
        u.setUpdateDate(new Date());
        Map m = BeanToMap.convert(u);
        System.out.println(m);
    }
}
