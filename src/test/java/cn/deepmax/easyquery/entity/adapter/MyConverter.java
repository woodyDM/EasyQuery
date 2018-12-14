package cn.deepmax.easyquery.entity.adapter;

import cn.deepmax.easyquery.adapter.mapper.PropertyMapper;

public class MyConverter implements PropertyMapper<MyColor,String> {

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
