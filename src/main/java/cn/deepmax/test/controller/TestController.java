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


    @GetMapping("/index.json")
    public String index() throws SQLException {
        return testService.get(1);
    }
}
