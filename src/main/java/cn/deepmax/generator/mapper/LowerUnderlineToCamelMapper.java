package cn.deepmax.generator.mapper;

import cn.deepmax.util.StringUtils;

public class LowerUnderlineToCamelMapper implements Mapper {


    @Override
    public String map(String from) {
        return StringUtils.lowerCaseUnderlineToCamelCase(from);
    }
}
