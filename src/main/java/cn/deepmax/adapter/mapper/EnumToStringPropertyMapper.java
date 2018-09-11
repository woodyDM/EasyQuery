package cn.deepmax.adapter.mapper;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.util.StringUtils;


/**
 * when   @Enumerated  EnumType.String
 */
public class EnumToStringPropertyMapper implements PropertyMapper<Enum, String> {

    private Class<? extends Enum> enumClass;

    public EnumToStringPropertyMapper(Class<? extends Enum> enumClass) {
        if(enumClass.isEnum()){
            this.enumClass = enumClass;
        }else{
            throw new IllegalArgumentException("class "+enumClass.getName()+" must be enum class");
        }
    }

    @Override
    public String convertToDatabaseColumn(Enum attribute) {
        if(attribute==null){
            return "";
        }
        return attribute.toString();
    }

    @Override
    public Enum convertToEntityAttribute(String dbData) {
        if(StringUtils.isEmpty(dbData)){
            return null;
        }
        Enum[] enums = enumClass.getEnumConstants();
        for(Enum e:enums){
            if(e.toString().equals(dbData)){
                return e;
            }
        }
        throw new EasyQueryException("Unable to find enum for value "+dbData+" for type "+enumClass.getName());

    }

    @Override
    public String toString() {
        return this.getClass().getName()+"_"+enumClass.getName()+"_string";
    }
}
