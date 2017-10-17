package cn.deepmax.entity;

import cn.deepmax.querytemplate.QueryTemplate;
import org.junit.Test;

import org.springframework.util.Assert;

import static org.mockito.Mockito.*;

public class TypeAdapterTest {

    @Test
    public void testAdatper(){
        Object a = null;
        Assert.isNull(TypeAdapter.getCompatibleValue(Integer.class,a),"test  null");
    }
    @Test
    public void testEntityInfo(){
        QueryTemplate template = mock(QueryTemplate.class);
        template.save(null);
    }
}
