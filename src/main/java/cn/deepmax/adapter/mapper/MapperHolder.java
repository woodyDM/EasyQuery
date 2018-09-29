package cn.deepmax.adapter.mapper;

import cn.deepmax.support.LocalCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperHolder {

    /**
     * key: MapperClassFullName
     * value: the mapper Instance.
     *
     */
    protected static LocalCache<String, PropertyMapper> cache = new LocalCache<>(new ConcurrentHashMap());

    /**
     * key : field
     * value MapperClassFullName
     */
    protected Map<String,String> toMapperMap = new ConcurrentHashMap<>();

    public PropertyMapper getMapper(String fieldName){
        if(toMapperMap.containsKey(fieldName)){
            String fullName = toMapperMap.get(fieldName);
            if(cache.contains(fullName)){
                return cache.get(fullName);
            }else{
                throw new IllegalStateException("toMapperMap contains "+fieldName+" but cache not found.");
            }
        }else{
            return null;
        }
    }

    public void setMapper(String fieldName, PropertyMapper mapper){
        String mapperName = mapper.getUniqueMapperName();
        toMapperMap.put(fieldName, mapperName);
        cache.putIfAbsent(mapperName,()->mapper);
    }

}
