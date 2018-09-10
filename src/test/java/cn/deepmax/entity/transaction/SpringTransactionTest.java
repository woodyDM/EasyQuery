package cn.deepmax.entity.transaction;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class SpringTransactionTest extends BaseTest{

    @Autowired
    TransactionService service;

    /**
     * !! this test need a local mysql database with engine innoDB .
     */
    @Test
    public void testWithException(){
        SuperUser user = new SuperUser();
        user.setBigDecimal(BigDecimal.ONE);
        user.setHide(true);
        user.setShow(false);
        user.setaBigPoint(23D);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateDate(null);
        service.save(user);
        SuperUser u2 = service.get(9L);
        Assert.notNull(user.getId(),"id null");

        //service.causeExceptionSave(user);

        SuperUser user1 = service.get(user.getId());
        //Assert.isTrue(user1.getBigDecimal().equals(BigDecimal.ONE),"After rollback");
    }


}
