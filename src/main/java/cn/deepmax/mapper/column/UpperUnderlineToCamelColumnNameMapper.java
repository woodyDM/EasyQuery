package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;
import cn.deepmax.util.StringUtils;

public class UpperUnderlineToCamelColumnNameMapper implements NameMapper {

    /**
     * create_time -> createTime
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        if(StringUtils.isEmpty(name)){
            return name;
        }
        name = name.toLowerCase();
        return StringUtils.lowerCaseUnderlineToCamelCase(name);
    }
}
