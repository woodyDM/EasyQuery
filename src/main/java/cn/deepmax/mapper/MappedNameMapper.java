package cn.deepmax.mapper;

import java.util.Map;

public class MappedNameMapper implements NameMapper {

    private Map<String,String> nameRelationMap;

    public MappedNameMapper(Map<String, String> nameRelationMap) {
        this.nameRelationMap = nameRelationMap;
    }

    /**
     *
     * @param clazz
     * @param name
     * @return
     */
    @Override
    public String convert(Class<?> clazz, String name) {
        return nameRelationMap.get(name);
    }
}
