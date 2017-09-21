package cn.deepmax.test.controller;

import cn.deepmax.test.service.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class TestController {

    @Autowired
    ITestService testService;


    @GetMapping("/testTransaction.json")
    public String testTransaction() throws SQLException {
        return testService.testTransaction();
    }
    @GetMapping("/testNoTransaction.json")
    public String testNoTransaction() throws SQLException {
        return testService.testNoTransaction();
    }
    @GetMapping("/testTransactionWithException.json")
    public String testTransactionWithException() throws SQLException {
        return testService.testTransactionWithException();
    }
}
