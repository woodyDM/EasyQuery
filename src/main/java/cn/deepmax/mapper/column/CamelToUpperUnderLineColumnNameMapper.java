package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;
import cn.deepmax.util.StringUtils;

public class CamelToUpperUnderLineColumnNameMapper implements NameMapper {

    /**
     * convert name
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        name = StringUtils.camelCaseToLowerCaseUnderLine(name);
        if(StringUtils.isNotEmpty(name)){
            return name.toUpperCase();
        }else{
            return name;
        }
    }
}
