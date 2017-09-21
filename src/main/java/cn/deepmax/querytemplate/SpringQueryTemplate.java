package cn.deepmax.querytemplate;

import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.entityUtils.EntityFactory;
import cn.deepmax.transaction.Transaction;

import java.sql.Connection;


public class SpringQueryTemplate extends AbstractQueryTemplate{


    public SpringQueryTemplate( ResultSetHandler resultSetHandler, Transaction transaction, EntityFactory entityFactory) {
        super( resultSetHandler, transaction, entityFactory);
    }



    @Override
    public Transaction transaction() {
        return transaction;
    }

    @Override
    public int execute(String sql, Object... params) {
        return 0;
    }

    @Override
    public boolean save(Object obj) {
        return false;
    }

    @Override
    public boolean delete(Object obj) {
        return false;
    }
}
