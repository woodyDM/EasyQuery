package cn.deepmax.mapper.table;

import cn.deepmax.mapper.NameMapper;
import cn.deepmax.util.StringUtils;


public class CamelToLowerUnderLineTableNameMapper implements NameMapper {
    /**
     * convert name
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        char[] chars = clazz.getSimpleName().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        String low = new String(chars);
        return StringUtils.camelCaseToLowerCaseUnderLine(low);
    }
}
