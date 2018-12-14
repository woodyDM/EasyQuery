package cn.deepmax.easyquery.test.adapter;

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
