package cn.deepmax.easyquery.test.transaction;

import cn.deepmax.easyquery.test.adapter.MyColor;
import cn.deepmax.easyquery.test.model.SuperUser;

public interface TransactionService {


    SuperUser get(Long id);
    SuperUser save(SuperUser user);
    SuperUser causeExceptionSave(Long id, MyColor color1);

}
