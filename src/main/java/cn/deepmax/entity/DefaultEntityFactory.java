package cn.deepmax.entity;

import java.util.Map;

public class DefaultEntityFactory  implements EntityFactory {


    @Override
    public  <T> T create(Class<T> clazz, Map<String, Object> columnNameWithPropertyValueMap) {
        return null;
    }
}