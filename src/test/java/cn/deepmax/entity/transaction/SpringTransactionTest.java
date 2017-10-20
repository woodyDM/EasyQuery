package cn.deepmax.entity.transaction;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.exception.EasyQueryException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class SpringTransactionTest extends BaseTest{

    @Autowired
    TransactionService service;

    @Test(expected = EasyQueryException.class)
    public void testWithException(){

    }

}
