package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;
import cn.deepmax.util.StringUtils;

public class LowerUnderlineToCamelColumnNameMapper implements NameMapper {

    /**
     * create_time -> createTime
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        return StringUtils.lowerCaseUnderlineToCamelCase(name);
    }
}
