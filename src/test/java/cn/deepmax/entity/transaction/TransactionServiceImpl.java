package cn.deepmax.entity.transaction;

import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    //@Resource(name = "localSpringFactory")
    QueryTemplateFactory factory;

    @Override
    public SuperUser get(Long id) {
        return factory.create().get(SuperUser.class, id);
    }

    @Override
    public SuperUser save(SuperUser user) {
        factory.create().save(user);
        return user;
    }

    @Override
    public SuperUser causeExceptionSave(SuperUser user) {
        QueryTemplate template = factory.create();
        user.setBigDecimal(BigDecimal.TEN);
        template.save(user);
        SuperUser u2 = template.get(SuperUser.class, user.getId());
        //SuperUser u2 = get(user.getId());
        //
        if(!u2.getBigDecimal().equals(BigDecimal.TEN)){
            throw new IllegalStateException("not equal.");
        }
        c();
        return user;
    }

    private void c(){
        QueryTemplate template = factory.create();
        //throw exception
        template.selectList("flewjfklewf");
    }
}
