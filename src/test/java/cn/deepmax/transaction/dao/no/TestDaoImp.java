package cn.deepmax.transaction.dao.no;

import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestDaoImp implements TestDao {
    @Autowired
    QueryTemplateFactory factory;

    @Override
    public List select(String sql, Object... objects) {
        QueryTemplate template = getTemplate();
        return template.select(sql,objects);

    }

    @Override
    public void update(String sql, Object... params) {
        QueryTemplate template = getTemplate();
        template.transaction().beginTransaction();
        template.executeUpdate(sql,params);
    }

    private QueryTemplate getTemplate(){
        return factory.create();
    }
}
