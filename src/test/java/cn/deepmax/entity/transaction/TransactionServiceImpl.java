package cn.deepmax.entity.transaction;

import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Resource(name = "localSpringFactory")
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
    public void causeException() {
        factory.create().select("flewjfklewf");
    }
}
