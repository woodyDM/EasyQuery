package cn.deepmax.easyquery.adapter;

import cn.deepmax.easyquery.adapter.mapper.*;
import cn.deepmax.easyquery.adapter.mapper.*;
import cn.deepmax.easyquery.exception.EasyQueryException;
import cn.deepmax.easyquery.model.Pair;
import cn.deepmax.easyquery.support.CacheDataSupport;
import cn.deepmax.easyquery.util.BeanToMap;
import cn.deepmax.easyquery.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * to support entity field of type Enum
 * @Convert and @Enumerated
 */
public class JpaAnnotatedTypeAdapter extends CacheDataSupport<String,ClassFieldTypeData<MapperHolder>> implements TypeAdapter {


    public static final Logger logger = LoggerFactory.getLogger(JpaAnnotatedTypeAdapter.class);

    @Override
    public ClassFieldTypeData<MapperHolder> load(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        ClassFieldTypeData<MapperHolder> data = new ClassFieldTypeData<>(new MapperHolder());
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            parseField(clazz, data, it);
        }
        return data;
    }

    private void parseField(Class<?> clazz,ClassFieldTypeData<MapperHolder> classFieldTypeData, PropertyDescriptor propertyDescriptor){
        String fieldName = propertyDescriptor.getName();
        Field field = BeanUtils.getField(clazz, fieldName);
        if(field==null){
            throw new IllegalStateException("unable to get field ["+fieldName+"]in class "+clazz.getName()+"");
        }
        Method getter = propertyDescriptor.getReadMethod();
        Class<?> converter = getConverterClass(field, getter);
        if(converter!=null){
            classFieldTypeData.getValue().setMapper(fieldName, new AttributePropertyMapper<>(converter));
            logger.debug("converter {} found on field [{}] of class[{}].",converter.getName(),fieldName,clazz.getName());
            return;
        }
        Pair<Class<? extends Enum>,Boolean> pair = getEnumeratedClassWithType(field, getter);
        if(pair!=null){
            if(pair.last){
                classFieldTypeData.getValue().setMapper(fieldName, new EnumToStringPropertyMapper(pair.first));
                logger.debug("enumType[{}] found on field [{}] of class[{}].","String",fieldName,clazz.getName());
            }else{
                classFieldTypeData.getValue().setMapper(fieldName, new EnumToIntegerPropertyMapper(pair.first));
                logger.debug("enumType[{}] found on field [{}] of class[{}].","ordinal",fieldName,clazz.getName());
            }
            return;
        }
        PropertyMapper mapper = DefaultMapperRegistry.lookFor(field.getType());
        if(mapper!=null){

            classFieldTypeData.getValue().setMapper(fieldName, mapper);
            logger.debug("defaultMapper [{}] found on field [{}] of class[{}].",mapper.getUniqueMapperName(),fieldName,clazz.getName());
        }else{
            classFieldTypeData.registerTypical(fieldName, field.getType());
            logger.debug("set typical type to field [{}] of class{}", fieldName, clazz.getName());
        }
    }


    private Pair<Class<? extends Enum>,Boolean> getEnumeratedClassWithType(Field field, Method method){
        Class<?> clazz = field.getType();
        if(!clazz.isEnum()){
            return null;
        }
        Enumerated enumeratedF = field.getAnnotation(Enumerated.class);
        Enumerated enumeratedG = method.getAnnotation(Enumerated.class);
        if(enumeratedF!=null && enumeratedG!=null){
            throw new EasyQueryException("Both field and getter are @Enumerated annotated on field ==> "+field);
        }
        if(enumeratedF==null && enumeratedG!=null){
            return doGetEnumeratedClassWithType(enumeratedG, field);
        }
        if(enumeratedF!=null){
            return doGetEnumeratedClassWithType(enumeratedF, field);
        }
        return doGetEnumeratedClassWithType(null, field);
    }

    @SuppressWarnings("unchecked")
    private Pair<Class<? extends Enum>,Boolean> doGetEnumeratedClassWithType(Enumerated enumerated, Field field){
        boolean byString = false;
        if(enumerated!=null && enumerated.value()== EnumType.STRING){
            byString = true;
        }
        return new Pair<>((Class<? extends Enum>)field.getType(), byString);
    }

    private Class<?> getConverterClass(Field field, Method getter){
        Convert convertF = field.getAnnotation(Convert.class);
        Convert convertG = getter.getAnnotation(Convert.class);
        if(convertF==null && convertG==null){
            return null;
        }
        if(convertF!=null && convertG!=null){
            throw new EasyQueryException("both field and getter are @Convert annotated on field ==> "+field);

        }
        if(convertG!=null){
            return convertG.converter();
        }
        return convertF.converter();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value) {
        return loadThen(entityClass.getName(),(data)->{
            PropertyMapper mapper = data.getValue().getMapper(fieldName);
            if(mapper!=null){
                return mapper.convertToEntityAttribute(value);
            }else{
                return getCompatibleValue(data.getFieldType(fieldName), value);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName, Object value) {
        return loadThen(entityClass.getName(),(data)->{
            PropertyMapper mapper = data.getValue().getMapper(fieldName);
            if(mapper!=null){
                return mapper.convertToDatabaseColumn(value);
            }else{
                return getCompatibleValue(data.getDatabaseType(fieldName), value);
            }
        });
    }
}
