package cn.deepmax.test.service.service;


import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.*;


@Service
@Transactional
public class TestService implements ITestService{

    @Autowired
    QueryTemplateFactory factory;

    public String get(Integer id) throws SQLException {
        QueryTemplate template = factory.create();

        template.transaction().beginTransaction();

        String sql = " update t_sys_role set name = 'ko' where  id =  ? ";
        int ok = template.executeUpdate(sql,1);
        System.out.println("result="+ok);
        System.out.println(template.transaction().isAutoCommit());
        System.out.println(template.transaction().isTransactionMode());

        template.transaction().commit();
        System.out.println(template.transaction().isAutoCommit());
        System.out.println(template.transaction().isTransactionMode());
        //throw new IllegalArgumentException("w");

        return "OK";


    }
}
