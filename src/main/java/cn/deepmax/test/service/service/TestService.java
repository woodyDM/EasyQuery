package cn.deepmax.test.service.service;


import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.*;
import java.util.List;


@Service
public class TestService implements ITestService{

    @Autowired
    QueryTemplateFactory factory;

    public String testTransaction() throws SQLException {



        QueryTemplate template = factory.create();

        template.transaction().beginTransaction();
        String sql = " update t_sys_role set name = 'testTransaction' where  id =  ? ";
        int ok = template.executeUpdate(sql,1);
        System.out.println("result="+ok);
        System.out.println(template.transaction().isAutoCommit());
        System.out.println(template.transaction().isTransactionMode());
        String sql2 = " select * from t_sys_role where id = ?";
        List l = template.select(sql2,1);
        template.transaction().commit();
        String sql3 = " update t_sys_role set name = 'testNoTransaction' where  id =  ? ";
        int ok3 = template.executeUpdate(sql3,1);
        System.out.println("result="+ok3);
        System.out.println(template.transaction().isAutoCommit());
        System.out.println(template.transaction().isTransactionMode());

        return "OK";
    }

    @Override
    public String testNoTransaction() throws SQLException {
        QueryTemplate template = factory.create();
        String sql3 = " update t_sys_role set name = 'testNoTransaction' where  id =  ? ";
        int ok3 = template.executeUpdate(sql3,1);
        System.out.println("result="+ok3);
        System.out.println(template.transaction().isAutoCommit());
        System.out.println(template.transaction().isTransactionMode());

        return "OK";
    }

    @Override
    public String testTransactionWithException() throws SQLException {
        QueryTemplate template = factory.create();


            template.transaction().beginTransaction();
            String sql = " update t_sys_role set name = 'testTransactionWithException' where  id =  ? ";
            int ok = template.executeUpdate(sql,1);
            System.out.println("result="+ok);
            System.out.println(template.transaction().isAutoCommit());
            System.out.println(template.transaction().isTransactionMode());
            throw new IllegalStateException("llll");


    }
}
