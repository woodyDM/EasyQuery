package cn.deepmax.support;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class LocalCache<K, V> {
    private static final Object NIL = new Object();
    private Map<K, Object> cache ;

    public LocalCache(Map cache) {
        Objects.requireNonNull(cache);
        this.cache = cache;
    }

    public boolean contains(K key){
        return cache.containsKey(key);
    }

    /**
     * should use with contains.
     * @param key
     * @return
     */
    public V get(K key){
        V value = (V)cache.get(key);
        if(value==NIL){
            return null;
        }else{
            return value;
        }
    }

    public V put(K key,V value){
        if(value==null){
            cache.put(key, NIL);
        }else{
            cache.put(key, value);
        }
        return value;
    }

    public V putIfAbsent(K key, Supplier<V> supplier){
        Object value = cache.get(key);
        if(value == NIL){
            return null;
        }
        if(value == null){
            V newValue = supplier.get();
            return put(key, newValue);
        }else{
            return (V)value;
        }
    }
}
