package cn.deepmax.support;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class LocalCache<K, V> {
    private static Object NIL = new Object();
    private Map<K, Object> cache ;

    public LocalCache(Map cache) {
        Objects.requireNonNull(cache);
        this.cache = cache;
    }

    public V get(K key, Supplier<V> supplier){
        Object value = cache.get(key);
        if(value == NIL){
            return null;
        }
        if(value == null){
            V newValue = supplier.get();
            if(newValue==null){
                cache.put(key, NIL);
            }else{
                cache.put(key, newValue);
            }
            return newValue;
        }else{
            return (V)value;
        }
    }
}
