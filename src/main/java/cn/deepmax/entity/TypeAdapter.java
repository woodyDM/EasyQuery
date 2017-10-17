package cn.deepmax.entity;

import cn.deepmax.exception.EasyQueryException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeAdapter {

    /**
     * change value to desired java type.
     * @param targetType
     * @param value
     * @return
     */
    public static Object getCompatibleValue(Class<?> targetType, Object value){
        if(value==null){
            return null;
        }
        if(isCompatibleType(value,targetType)){
            return value;
        }
        String v = value.toString();
        if(targetType==int.class || targetType==Integer.class){         //Int
            return Integer.valueOf(v);
        }else if(targetType==float.class || targetType==Float.class){  //Float
            return Float.valueOf(v);
        }else if(targetType==long.class || targetType==Long.class){     //Long
            return Long.valueOf(v);
        }else if(targetType==double.class || targetType==Double.class){  //Double
            return Double.valueOf(v);
        }else if(targetType==BigInteger.class){                         //BigInteger
            return BigInteger.valueOf(Long.valueOf(v));
        }else if(targetType==BigDecimal.class){                         //BigDecimal
            return BigDecimal.valueOf(Long.valueOf(v));
        }else if(targetType==String.class){                             //String
            return v;
        }else if(targetType==Boolean.class || targetType==boolean.class) {  //Boolean
            Integer intValue = Integer.valueOf(v);
            if (intValue.equals(0)) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }else{
            throw new EasyQueryException("Value of type["+value.getClass().getName()+"] is not compatible with target type."+targetType.getName()+"]");
        }
    }


    /** Copy from apache DbUtils
     * @param value The value to be passed into the setter method.
     * @param type The setter's parameter type (non-null)
     * @return boolean true if the value is compatible
     */
    private static boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value) || matchesPrimitive(type, value.getClass())) {
            return true;
        }
        return false;
    }

    /**check whether valueType can be coerced (e.g. autoboxed) into targetType.
     * @param targetType The primitive type to target.
     * @param valueType The value to match to the primitive type.
     * @return
     */
    private static boolean matchesPrimitive(Class<?> targetType, Class<?> valueType) {
        //if targetType is not primitive, valueType is not compatible.
        if (!targetType.isPrimitive()) {
            return false;
        }
        try {
            // see if there is a "TYPE" field.  This is present for primitive wrappers.
            Field typeField = valueType.getField("TYPE");
            Object primitiveValueType = typeField.get(valueType);
            if (targetType == primitiveValueType) {
                return true;
            }
        } catch (NoSuchFieldException e) {
            // lacking the TYPE field is a good sign that we're not working with a primitive wrapper.
            //  can't match for compatibility
        } catch (IllegalAccessException e) {
            // an inaccessible TYPE field is a good sign that we're not working with a primitive wrapper.
            // nothing to do.  we can't match for compatibility
        }
        return false;
    }
}
