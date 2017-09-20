package cn.deepmax.test.service.service;


import cn.deepmax.core.QueryTemplate;
import cn.deepmax.entity.EQMock;
import cn.deepmax.rowrecord.RowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Service
public class TestService implements ITestService{

    @Autowired
    DataSource dataSource;

    public String get(Integer id) throws SQLException {
        QueryTemplate template = new QueryTemplate(dataSource);


        String sql = "select A.* ,B.* from t_user A  " +
                "left join t_sys_role B on A.id = B.user_id " +
                "where A.id =  ? ";
        List<RowRecord<EQMock>> list = template.select(sql,EQMock.class,1);
        System.out.println(list.get(0).getTimestamp("create_time"));

        return "OK";

    }
}
