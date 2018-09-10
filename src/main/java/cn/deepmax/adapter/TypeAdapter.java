package cn.deepmax.adapter;

public interface TypeAdapter{

    Object getCompatibleValue(Class<?> targetType,  Object value);

}