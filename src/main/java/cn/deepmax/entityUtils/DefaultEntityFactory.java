package cn.deepmax.entityUtils;

import javax.persistence.EntityManager;
import java.util.Map;

public class DefaultEntityFactory  implements EntityFactory {


    @Override
    public  <T> T create(Class<T> clazz, Map<String, Object> propertyValueMap) {
        return null;
    }
}
