package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;
import cn.deepmax.util.StringUtils;

public class CamelToLowerUnderLineColumnNameMapper implements NameMapper {

    /**
     * userName -> user_name
     * userEmail -> user_email
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        return StringUtils.camelCaseToLowerCaseUnderLine(name);
    }
}