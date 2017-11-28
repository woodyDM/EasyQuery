package cn.deepmax.entity.transaction;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import java.math.BigDecimal;

public class SpringTransactionTest extends BaseTest{

    @Autowired
    TransactionService service;

    /**
     * !! this test need a local mysql database with engine innoDB .
     */
//    @Test
//    public void testWithException(){
//        SuperUser user = new SuperUser();
//        user.setBigDecimal(BigDecimal.ONE);
//        service.save(user);
//        Assert.notNull(user.getId(),"id null");
//        try{
//            //this service set bigDecimal to BigDecimal.TEN,
//            //rollback after exception.
//            service.causeExceptionSave(user);
//        }catch (Exception e){
//
//        }
//        SuperUser user1 = service.get(user.getId());
//        Assert.isTrue(user1.getBigDecimal().equals(BigDecimal.ONE),"After rollback");
//    }


}
