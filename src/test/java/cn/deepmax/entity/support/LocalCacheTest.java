package cn.deepmax.entity.support;

import cn.deepmax.support.LocalCache;
import org.junit.Test;
import java.util.HashMap;

public class LocalCacheTest {

    @Test
    public void test(){
        LocalCache<Integer,Integer> cache = new LocalCache<>(new HashMap());

        cache.get(123,()->1);
        cache.get(123,()->1);
        cache.get(1234,()->null);
        cache.get(1234,()->null);

    }
}
