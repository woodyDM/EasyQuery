package cn.deepmax.transaction.dao.yes;

import java.util.List;

public interface TestDaoT {
    List select(String sql,Object... objects);
    void update(String sql,Object... params);
}
