package cn.deepmax.entity.transaction;

import cn.deepmax.entity.adapter.MyColor;
import cn.deepmax.entity.model.SuperUser;

public interface TransactionService {


    SuperUser get(Long id);
    SuperUser save(SuperUser user);
    SuperUser causeExceptionSave(Long id, MyColor color1);

}
