package cn.deepmax.test.service.service;

import java.sql.SQLException;

public interface ITestService {
    String testTransaction() throws SQLException;
    String testTransactionWithException() throws SQLException;
    String testNoTransaction() throws SQLException;
}
