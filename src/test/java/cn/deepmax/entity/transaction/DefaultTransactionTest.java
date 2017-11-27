package cn.deepmax.entity.transaction;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class DefaultTransactionTest extends BaseTest {

    @Test
    public void testDefaultTransactionNoExceptionUsingDefaultFactory(){
        testDefaultTransactionNoException(defaultFactory);
    }
    @Test
    public void testDefaultTransactionNoExceptionUsingSpringFactory(){
        testDefaultTransactionNoException(factory);
    }

    private void testDefaultTransactionNoException(QueryTemplateFactory factory){
        QueryTemplate template = factory.create();
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.ONE);
        user.setUserName("wwww");

        Long newId=null;
        template.selectList("select  1 as one");
        try{
            template.transaction().beginTransaction();
            template.save(user);
            newId = user.getId();
            user.setUserName("ww");
            template.save(user);
            template.transaction().commit();
        }catch (Exception e){
            template.transaction().rollback();
        }finally {
            template.transaction().close();
        }
        Assert.notNull(newId,"new id null");
        SuperUser user2 = template.get(SuperUser.class,newId);
        Assert.notNull(user2,"user2 null");
        Assert.isTrue(user2.getUserName().equals("ww"),"name not match");

    }

    @Test
    public void testDefaultTransactionWithExceptionUsingDefaultFactory(){
        testDefaultTransactionException(defaultFactory);
    }
    @Test
    public void testDefaultTransactionWithExceptionUsingSpringFactory(){
        testDefaultTransactionException(factory);
    }

    private void testDefaultTransactionException(QueryTemplateFactory factory){
        QueryTemplate template = factory.create();
        template.selectList("select 1 as one");
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.ONE);
        user.setUserName("wwww");

        template.save(user);
        Long newId=user.getId();
        try{
            template.transaction().beginTransaction();
            user.setUserName("www2");
            template.save(user);
            SuperUser inUser = template.get(SuperUser.class,newId);
            Assert.isTrue(inUser.getUserName().equals("www2"),"name not match in transaction");
            Integer temp = Integer.valueOf("cause exception");    //cause exception.
        }catch (Exception e){
            template.transaction().rollback();
        }finally {
            template.transaction().close();
        }
        Assert.notNull(newId,"new id null");
        SuperUser user2 = template.get(SuperUser.class,newId);
        Assert.notNull(user2,"user2 null");
        Assert.isTrue(user2.getUserName().equals("wwww"),"name not match");

    }
}
