package cn.deepmax.core;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface ResultSetHandler {

    List<Map<String,Object>> handle(ResultSet resultSet);
}
