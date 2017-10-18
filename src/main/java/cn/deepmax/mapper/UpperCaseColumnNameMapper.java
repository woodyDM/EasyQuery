package cn.deepmax.mapper;

public class UpperCaseColumnNameMapper implements NameMapper {

    /**
     * convert name
     *
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
