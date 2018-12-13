package cn.deepmax.adapter.mapper;

import cn.deepmax.support.LocalCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * default field to database type mapper .
 * valid always if no @Convert found.
 */
public class DefaultMapperRegistry {

    protected static LocalCache<String, PropertyMapper> registry = new LocalCache<>(new ConcurrentHashMap());

    static {
        register(LocalDateTime.class, LocalDateTimeToTimestampMapper.getInstance());
        register(LocalDate.class, LocalDateToDateMapper.getInstance());
    }

    public static PropertyMapper lookFor(Class<?> fieldClass){
        Objects.requireNonNull(fieldClass);
        return registry.get(fieldClass.getName());
    }

    public static void register(Class<?> fieldClass, PropertyMapper mapper){
        Objects.requireNonNull(fieldClass);
        Objects.requireNonNull(mapper);
        registry.putIfAbsent(fieldClass.getName(),()->mapper);
    }

}
