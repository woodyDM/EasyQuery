package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;

public class SameColumnNameMapper implements NameMapper {

    /**
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        return name;
    }


}
