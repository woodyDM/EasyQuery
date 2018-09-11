package cn.deepmax.entity.transaction;

import cn.deepmax.entity.adapter.MyColor;
import cn.deepmax.entity.model.SuperUser;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.querytemplate.QueryTemplateFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
    public SuperUser causeExceptionSave(Long id, MyColor color1) {
        SuperUser u2 = queryTemplate.get(SuperUser.class, id);
        u2.setColor1(color1);
        queryTemplate.save(u2);
        SuperUser u3 = queryTemplate.get(SuperUser.class, id);
        Assert.isTrue(u3.getColor1().equals(color1),"color check");
        c();
        return u3;
    }



    private void c(){
        //throw exception
        queryTemplate.selectList("flewjfklewf");

    }
}
