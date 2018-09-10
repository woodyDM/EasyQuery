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

    @Resource(name = "springTemplate")
    QueryTemplate queryTemplate;

    @Override
    public SuperUser get(Long id) {
        return queryTemplate.get(SuperUser.class, id);
    }

    @Override
    public SuperUser save(SuperUser user) {
        queryTemplate.save(user);
        return user;
    }

    @Override
    public SuperUser causeExceptionSave(SuperUser user) {
        QueryTemplate template = queryTemplate;
        user.setBigDecimal(BigDecimal.TEN);
        template.save(user);
        SuperUser u2 = template.get(SuperUser.class, user.getId());
        System.out.println(u2.getBigDecimal());
        c();
        return user;
    }

    private void c(){

        //throw exception
        queryTemplate.selectList("flewjfklewf");
    }
}
