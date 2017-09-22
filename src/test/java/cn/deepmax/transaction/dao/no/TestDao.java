package cn.deepmax.transaction.dao.no;

import java.util.List;

public interface TestDao {
    List select(String sql, Object... objects);
    void update(String sql, Object... params);
}
