package cn.deepmax.adapter.mapper;

import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.util.BeanUtils;

import javax.persistence.AttributeConverter;

public class AttributePropertyMapper<X,Y> implements PropertyMapper<X,Y> {

    private AttributeConverter attributeConverter ;

    public AttributePropertyMapper(Class<?> mapper){
        Object mapperObj = BeanUtils.newInstance(mapper);
        if(mapperObj instanceof AttributeConverter){
            attributeConverter = (AttributeConverter) mapperObj;
        }else{
            throw new EasyQueryException(("mapper of type "+mapper.getName()+" not support."));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Y convertToDatabaseColumn(X attribute) {

        return (Y)(attributeConverter.convertToDatabaseColumn(attribute));

    }

    @SuppressWarnings("unchecked")
    @Override
    public X convertToEntityAttribute(Y dbData) {
        return (X)(attributeConverter).convertToEntityAttribute(dbData);
    }

    @Override
    public String toString() {
        if(attributeConverter instanceof PropertyMapper){
            return ((PropertyMapper)attributeConverter).getUniqueMapperName();
        }else{
            return this.getClass().getName()+"_wrapping_"+attributeConverter.getClass().getName();
        }
    }
}
