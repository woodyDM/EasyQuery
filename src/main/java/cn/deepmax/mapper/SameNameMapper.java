package cn.deepmax.mapper;

public class SameNameMapper implements NameMapper {

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
