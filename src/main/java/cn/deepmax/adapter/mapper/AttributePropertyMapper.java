package cn.deepmax.adapter.mapper;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.util.BeanUtils;

import javax.persistence.AttributeConverter;

public class AttributePropertyMapper<X,Y> implements PropertyMapper<X,Y> {

    private Object attributeConverter ;
    private boolean isJpaMapper;

    public AttributePropertyMapper(Class<?> mapper){
        if(AttributeConverter.class.isAssignableFrom(mapper)){
            isJpaMapper = true;
        }else if(PropertyMapper.class.isAssignableFrom(mapper)){
            isJpaMapper = false;
        }else{
            throw new EasyQueryException(("mapper of type "+mapper.getName()+" not support."));
        }
        this.attributeConverter = BeanUtils.newInstance(mapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Y convertToDatabaseColumn(X attribute) {
        if(isJpaMapper){
            return (Y)((AttributeConverter)attributeConverter).convertToDatabaseColumn(attribute);
        }else{
            return (Y)((PropertyMapper)attributeConverter).convertToDatabaseColumn(attribute);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public X convertToEntityAttribute(Y dbData) {
        if(isJpaMapper){
            return (X)((AttributeConverter)attributeConverter).convertToEntityAttribute(dbData);
        }else{
            return (X)((PropertyMapper)attributeConverter).convertToEntityAttribute(dbData);
        }
    }



    @Override
    public String toString() {
        if(isJpaMapper){
            return this.getClass().getName()+"_wrapping_"+attributeConverter.getClass().getName();
        }else{
            return ((PropertyMapper)attributeConverter).getUniqueMapperName();
        }
    }
}
