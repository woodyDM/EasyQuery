package cn.deepmax.easyquery.entity.transaction;

import cn.deepmax.easyquery.entity.BaseTest;
import cn.deepmax.easyquery.entity.adapter.MyColor;
import cn.deepmax.easyquery.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SpringTransactionTest extends BaseTest{

    @Autowired
    TransactionService service;

    /**
     *
     */
    @Test
    public void testWithNoException(){
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.ONE);
        user.setHide(true);
        user.setShow(false);
        user.setaBigPoint(23D);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateDate(null);
        service.save(user);
        SuperUser u2 = service.get(user.getId());

        Assert.isTrue(u2.getId().equals(user.getId()),"is true");
    }

    @Test
    public void testWithException(){
        Long testId  = 1L;
        SuperUser user0 = service.get(testId);
        Assert.isTrue(user0.getColor1().equals(MyColor.BLACK),"color check.");
        try{
            SuperUser user = service.causeExceptionSave(testId, MyColor.RED);
        }catch (Exception e){
            //
        }
        SuperUser userF = service.get(testId);
        Assert.isTrue(user0.getColor1().equals(MyColor.BLACK),"color check.");


    }


}
