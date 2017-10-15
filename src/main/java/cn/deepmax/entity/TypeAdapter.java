package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.math.BigInteger;

public class TypeAdapter {

    /**
     * change primaryKey value to desired javabean primaryKey type.
     * @param targetType
     * @param value
     * @return
     */
    public static Object getCompatibleValue(Class<?> targetType, Object value){
        if(value==null){
            return null;
        }
        if(targetType.isInstance(value)){
            return value;
        }
        String v = value.toString();
        if(targetType==int.class || targetType==Integer.class){
            return Integer.valueOf(v);
        }else if(targetType==long.class || targetType==Long.class){
            return Long.valueOf(v);
        }else if(targetType==BigInteger.class){
            return BigInteger.valueOf(Long.valueOf(v));
        }else if(targetType==String.class){
            return v;
        }else{
            return value;
            //throw new EasyQueryException("Entity value of type["+targetType.getName()+"] is not compatible with value of type."+value.getClass().getName()+"]");
        }
    }
}
