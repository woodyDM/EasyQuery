package cn.deepmax.entity.adapter;

import cn.deepmax.adapter.mapper.EnumToIntegerPropertyMapper;
import cn.deepmax.adapter.mapper.EnumToStringPropertyMapper;
import cn.deepmax.adapter.mapper.PropertyMapper;
import org.junit.Assert;
import org.junit.Test;

public class EnumMapperTest {

    @Test
    public void test1(){
        EnumToStringPropertyMapper mapper = new EnumToStringPropertyMapper(EnumType.class);
        String st = mapper.convertToDatabaseColumn(EnumType.TYPE1);
        Assert.assertTrue(st.equals(EnumType.TYPE1.toString()));
        EnumType type = (EnumType)mapper.convertToEntityAttribute(st);
        Assert.assertEquals(type, EnumType.TYPE1);

        EnumToIntegerPropertyMapper mapper2 = new EnumToIntegerPropertyMapper(EnumType.class);
        Integer st2 = mapper2.convertToDatabaseColumn(EnumType.TYPE2);
        Assert.assertTrue(st2.equals(EnumType.TYPE2.ordinal()));
        EnumType type2 = (EnumType)mapper2.convertToEntityAttribute(st2);
        Assert.assertEquals(type2, EnumType.TYPE2);
    }
}
