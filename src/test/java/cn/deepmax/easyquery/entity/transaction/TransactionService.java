package cn.deepmax.easyquery.entity.transaction;

import cn.deepmax.easyquery.entity.adapter.MyColor;
import cn.deepmax.easyquery.entity.model.SuperUser;

public interface TransactionService {


    SuperUser get(Long id);
    SuperUser save(SuperUser user);
    SuperUser causeExceptionSave(Long id, MyColor color1);

}
