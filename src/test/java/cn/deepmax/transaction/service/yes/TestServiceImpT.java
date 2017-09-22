package cn.deepmax.transaction.service.yes;


import cn.deepmax.transaction.dao.yes.TestDaoT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class TestServiceImpT implements TestServiceT {

    @Autowired
    TestDaoT dao;

    @Override
    public List testDefalt(Integer id) {
        String sql = " select * from t_sys_role where id = ? ";
        String sql2 = " update t_sys_role set user_id = ? where id = 1";
        dao.update(sql2,id);
        return dao.select(sql,id);
    }

    @Override
    public List testSpring(Integer id) {
        return null;
    }
}
