package cn.deepmax.mapper.table;

import cn.deepmax.mapper.NameMapper;

public class LowerCaseTableNameMapper implements NameMapper {

    /**
     * SuperUser - > superuser
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        return clazz.getSimpleName().toLowerCase();
    }
}
