package cn.deepmax.transaction;


import cn.deepmax.App;
import cn.deepmax.transaction.service.no.TestService;
import cn.deepmax.transaction.service.yes.TestServiceT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TransactionTest {
    @Autowired
    TestServiceT testServiceT;

    @Autowired
    TestService testService;

    private final static Logger logger = LoggerFactory.getLogger(TransactionTest.class);
    @Test
    public void test(){
        logger.info("--------------------start no transactiontest");
        testService.testDefalt(1);
        logger.info("---------------------start transactiontest");
        testServiceT.testDefalt(1);
    }
}
