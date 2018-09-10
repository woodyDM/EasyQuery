package cn.deepmax.entity.adapter;

import cn.deepmax.util.StringUtils;

import javax.persistence.AttributeConverter;

public class JpaConverter implements AttributeConverter<MyColor,String> {

    @Override
    public String convertToDatabaseColumn(MyColor attribute) {
        if(attribute==null){
            return "NULL";
        }else{
            return attribute.getDesc();
        }
    }

    @Override
    public MyColor convertToEntityAttribute(String dbData) {
        return MyColor.parse(dbData);
    }
}
