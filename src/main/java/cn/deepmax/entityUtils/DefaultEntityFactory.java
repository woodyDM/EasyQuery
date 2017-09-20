package cn.deepmax.entityUtils;

import javax.persistence.EntityManager;
import java.util.Map;

public class DefaultEntityFactory<T> implements EntityFactory<T> {


    @Override
    public T create(Class<T> clazz, Map<String, Object> propertyValueMap) {
        return null;
    }
}
