package cn.deepmax.entity;

import cn.deepmax.entity.EntityFactory;
import cn.deepmax.entity.entity.SuperUser;
import cn.deepmax.mapper.SameColumnNameMapper;
import org.junit.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试entityFactory 生成实体是否有效
 */
public class Test {

    @org.junit.Test
    public void test01(){
        Map<String,Object> result = new HashMap<>();
        result.put("id1",1);
        result.put("id2",2);
        result.put("id3",33L);
        result.put("id4",44L);
        result.put("name","woody");
        result.put("createTime",new Timestamp(System.currentTimeMillis()));
        result.put("ok1",true);
        result.put("ok2",false);
        result.put("point1",11.3D);
        result.put("point2",11.4D);
        result.put("point3",11.3F);
        result.put("point4",1.3F);
        result.put("bigDecimal", BigDecimal.valueOf(20000L));
        result.put("updateDate",new Date());
        EntityFactory factory = new EntityFactory(new SameColumnNameMapper());
        SuperUser user = factory.create(SuperUser.class, result);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getBigDecimal());
        Assert.assertNotNull(user.getOk1());
        Assert.assertNotNull(user.getUpdateDate());
    }
}
