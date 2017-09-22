package cn.deepmax.transaction.dao.yes;

import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestDaoImpT implements TestDaoT {
    @Autowired
    QueryTemplateFactory factory;

    @Override
    public List select(String sql, Object... objects) {

        QueryTemplate template = getTemplate();

        try{
            template.transaction().beginTransaction();
            List a = template.select(sql,objects);
            template.transaction().commit();
            return a;
        }catch (Exception e){
            template.transaction().rollback();
        }finally {
            template.transaction().close();
        }
        return null;
    }

    @Override
    public void update(String sql, Object... params) {
        QueryTemplate template = getTemplate();
        template.transaction().beginTransaction();

        try{
            template.executeUpdate(sql,params);
            template.transaction().commit();
        }catch (Exception e){
            template.transaction().rollback();
        }finally {
            template.transaction().close();
        }

    }

    private QueryTemplate getTemplate(){
        return factory.create();
    }
}
