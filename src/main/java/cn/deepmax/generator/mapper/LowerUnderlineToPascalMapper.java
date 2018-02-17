package cn.deepmax.generator.mapper;

import cn.deepmax.util.StringUtils;

public class LowerUnderlineToPascalMapper implements Mapper {

    @Override
    public String map(String from) {
        String temp = StringUtils.lowerCaseUnderlineToCamelCase(from);
        char[] chars = temp.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
