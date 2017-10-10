package cn.deepmax.entity;

import cn.deepmax.model.Pair;
import java.util.List;


public interface SqlTranslator {
    Pair<String,List<Object>> getInsertSQL(Object obj);
    Pair<String,List<Object>> getUpdateSQL(Object obj);
    Pair<String,List<Object>> getSelectSQL(Object obj);
    Pair<String,List<Object>> getDeleteSQL(Object obj);
    EntityInfo getEntityInfo();
}
