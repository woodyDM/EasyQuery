package cn.deepmax.mapper.column;

import cn.deepmax.mapper.NameMapper;

public class UpperCaseColumnNameMapper implements NameMapper {

    /**
     * create_time -> CREATE_TIME
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        if(name==null){
            return null;
        }
        return name.toUpperCase();
    }
}
