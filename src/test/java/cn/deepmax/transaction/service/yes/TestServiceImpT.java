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
        String sql = " select * from t_user where id = ? ";
        String sql2 = " update t_user set name = ? where id = ?";
        dao.update(sql2,"wang",id);
        return dao.select(sql,id);
    }

    @Override
    public List testSpring(Integer id) {
        return null;
    }
}
