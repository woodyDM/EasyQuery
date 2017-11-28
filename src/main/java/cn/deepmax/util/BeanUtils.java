package cn.deepmax.util;

public class BeanUtils {

    private static final String IS_PREFIX = "is";
    private static final String SET_PREFIX= "set";
    private static final String GET_PREFIX = "get";


    public static String getReadMethodName(String propertyName,String javaType){
        checkParam(propertyName, javaType);
        String deCap = normalize(propertyName);
        if(propertyName.startsWith(IS_PREFIX)&&(javaType.contains("boolean")||javaType.contains("Boolean"))){
            return propertyName;
        }else{
            return GET_PREFIX+deCap;
        }
    }

    public static String getWriteMethodName(String propertyName,String javaType){
        checkParam(propertyName, javaType);
        String deCap = normalize(propertyName);
        if((javaType.contains("boolean")||javaType.contains("Boolean")) && propertyName.startsWith(IS_PREFIX)){
            return SET_PREFIX + propertyName.substring(2);
        }else{
            return SET_PREFIX + deCap;
        }
    }


    private static void checkParam(String propertyName,String javaType){
        if(StringUtils.isEmpty(propertyName)){
            throw new IllegalArgumentException("propertyName should not be empty.");
        }
        if(StringUtils.isEmpty(javaType)){
            throw new IllegalArgumentException("JavaType should not be empty.");
        }
    }


    /**
     * userName -> UserName
     * URL -> URL
     * aBig -> aBig
     * @param name
     * @return
     */
    private static String normalize(String name){
        if(StringUtils.isEmpty(name)){
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))){
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }


}
