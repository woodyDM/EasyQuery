package cn.deepmax.adapter;

import java.util.HashMap;
import java.util.Map;

public class ClassFieldTypeData<V> {
    public Map<String,Class<?>> fieldEntityTypeMap = new HashMap<>();
    public Map<String,Class<?>> fieldDatabaseTypeMap = new HashMap<>();

    public void registerTypical(String fieldName,Class<?> type){
        fieldEntityTypeMap.put(fieldName,type);
        fieldDatabaseTypeMap.put(fieldName, type);
    }

    public V value;

}
