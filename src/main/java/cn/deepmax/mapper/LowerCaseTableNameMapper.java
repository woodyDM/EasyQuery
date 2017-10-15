package cn.deepmax.mapper;

public class LowerCaseTableNameMapper implements NameMapper {
    @Override
    public String convert(Class<?> clazz, String name) {
        return clazz.getSimpleName().toLowerCase();
    }
}
