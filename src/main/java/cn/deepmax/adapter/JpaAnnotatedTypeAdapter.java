package cn.deepmax.adapter;

import cn.deepmax.adapter.mapper.*;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.Pair;
import cn.deepmax.support.CacheDataSupport;
import cn.deepmax.support.LocalCache;
import cn.deepmax.util.BeanToMap;
import cn.deepmax.util.BeanUtils;
import cn.deepmax.util.ForceTypeAdapter;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class JpaAnnotatedTypeAdapter extends CacheDataSupport<String,ClassFieldTypeData<MapperHolder>> implements TypeAdapter {


    @Override
    public ClassFieldTypeData<MapperHolder> load(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        ClassFieldTypeData<MapperHolder> data = new ClassFieldTypeData<>();
        data.value = new MapperHolder();
        List<PropertyDescriptor> propertyDescriptors = BeanToMap.getPropertyDescriptor(clazz);
        for(PropertyDescriptor it:propertyDescriptors){
            parseField(clazz, data, it);
        }
        return data;
    }

    private void parseField(Class<?> clazz,ClassFieldTypeData<MapperHolder> classFieldTypeData, PropertyDescriptor propertyDescriptor){
        String fieldName = propertyDescriptor.getName();
        Field field = BeanUtils.getField(clazz, fieldName);
        Method getter = propertyDescriptor.getReadMethod();
        Class<?> converter = getConverterClass(field, getter);
        if(converter!=null){
            classFieldTypeData.value.setMapper(fieldName, new AttributePropertyMapper(converter));
            return;
        }
        Pair<Class<? extends Enum>,Boolean> pair = getEnumeratedClassWithType(field, getter);
        if(pair!=null){
            if(pair.last)
                classFieldTypeData.value.setMapper(fieldName, new EnumToStringPropertyMapper(pair.first));
            else
                classFieldTypeData.value.setMapper(fieldName, new EnumToIntegerPropertyMapper(pair.first));
            return;
        }
        classFieldTypeData.registerTypical(fieldName, field.getType());
    }


    private Pair<Class<? extends Enum>,Boolean> getEnumeratedClassWithType(Field field, Method method){
        Class<?> clazz = field.getType();
        if(!clazz.isEnum()){
            return null;
        }
        Enumerated enumeratedF = field.getAnnotation(Enumerated.class);
        Enumerated enumeratedG = method.getAnnotation(Enumerated.class);
        if(enumeratedF!=null && enumeratedG!=null){
            throw new EasyQueryException("Both field and getter are @Enumerated annotated.");
        }
        if(enumeratedF==null && enumeratedG!=null){
            return doGetEnumeratedClassWithType(enumeratedG, field);
        }
        if(enumeratedF!=null){
            return doGetEnumeratedClassWithType(enumeratedF, field);
        }
        return doGetEnumeratedClassWithType(null, field);
    }

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
            throw new EasyQueryException("both field and getter are @Convert annotated.");

        }
        if(convertG!=null){
            return convertG.converter();
        }
        return convertF.converter();
    }

    @Override
    public Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value) {
        return loadThen(entityClass.getName(),(data)->{
            PropertyMapper mapper = data.value.getMapper(fieldName);
            if(mapper!=null){
                return mapper.convertToEntityAttribute(value);
            }else{
                return ForceTypeAdapter.getCompatibleValue(data.fieldEntityTypeMap.get(fieldName), value);
            }
        });
    }

    @Override
    public Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName, Object value) {
        return loadThen(entityClass.getName(),(data)->{
            PropertyMapper mapper = data.value.getMapper(fieldName);
            if(mapper!=null){
                return mapper.convertToDatabaseColumn(value);
            }else{
                return ForceTypeAdapter.getCompatibleValue(data.fieldDatabaseTypeMap.get(fieldName), value);
            }
        });
    }
}
