package cn.deepmax.test.service.service;

import java.sql.SQLException;

public interface ITestService {
    String get(Integer id) throws SQLException;
}
