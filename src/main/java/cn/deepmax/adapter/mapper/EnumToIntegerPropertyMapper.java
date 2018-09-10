package cn.deepmax.adapter.mapper;

import cn.deepmax.exception.EasyQueryException;

import java.util.Objects;


/**
 * when   @Enumerated  EnumType.Integer or no annotation
 */
public class EnumToIntegerPropertyMapper implements PropertyMapper<Enum, Number> {

    private Class<? extends Enum> enumClass;

    public EnumToIntegerPropertyMapper(Class<? extends Enum> enumClass) {
        if(enumClass.isEnum()){
            this.enumClass = enumClass;
        }else{
            throw new IllegalArgumentException("class "+enumClass.getName()+" must be enum class");
        }
    }

    @Override
    public Integer convertToDatabaseColumn(Enum attribute) {
        if(attribute==null){
            return -1;
        }
        return attribute.ordinal();
    }

    @Override
    public Enum convertToEntityAttribute(Number dbData) {

        if(dbData==null || dbData.intValue()==-1){
            return null;
        }
        Integer v = dbData.intValue();
        Enum[] enums = enumClass.getEnumConstants();
        for(Enum e:enums){
            if(v.equals(e.ordinal())){
                return e;
            }
        }
        throw new EasyQueryException("Unable to find enum for value "+dbData+" for type "+enumClass.getName());

    }

    @Override
    public String toString() {
        return this.getClass().getName()+"_"+enumClass.getName()+"_integer";
    }
}
