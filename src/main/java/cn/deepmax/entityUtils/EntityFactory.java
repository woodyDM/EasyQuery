package cn.deepmax.entityUtils;

import java.util.Map;

public interface EntityFactory<T> {
    T create(Class<T> clazz, Map<String,Object> propertyValueMap);
}
