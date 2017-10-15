package cn.deepmax.select;


import cn.deepmax.AppTest;
import cn.deepmax.entity.entity.SuperUser;
import cn.deepmax.entity.entity.User;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import cn.deepmax.resultsethandler.RowRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTest.class)
public class MapSelectTest {

    @Autowired
    QueryTemplateFactory factory;

    @Test
    public void testSelectList(){

    }

    @Test
    public void testSelectScalar(){


    }

    @Test
    public void testSelectEntity(){


    }

    @Test
    public void testSelectOneEntity(){

    }
    @Test
    public void testSelectList2(){

    }

    @Test
    public void testBatch(){


    }
}
