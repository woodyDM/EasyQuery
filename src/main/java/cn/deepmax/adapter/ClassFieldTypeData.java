package cn.deepmax.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * to hold class type information.
 * @param <V> for extra holder info.
 */
public class ClassFieldTypeData<V> {

    private Map<String,Class<?>> fieldEntityTypeMap = new HashMap<>();
    private Map<String,Class<?>> fieldDatabaseTypeMap = new HashMap<>();
    private V value;


    public ClassFieldTypeData() {
    }

    public ClassFieldTypeData(V value) {
        this.value = value;
    }

    public Class<?> getFieldType(String fieldName){
        return fieldEntityTypeMap.get(fieldName);
    }

    public Class<?> getDatabaseType(String fieldName){
        return fieldDatabaseTypeMap.get(fieldName);
    }

    /**
     * @param fieldName
     * @param type
     */
    public void registerTypical(String fieldName, Class<?> type){
        fieldEntityTypeMap.put(fieldName,type);
        fieldDatabaseTypeMap.put(fieldName, type);
    }

    public V getValue(){
        return value;
    }

}
