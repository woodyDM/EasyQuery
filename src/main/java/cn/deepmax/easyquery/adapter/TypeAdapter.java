package cn.deepmax.easyquery.adapter;

import cn.deepmax.easyquery.util.ForceTypeAdapter;

public interface TypeAdapter{

    /**
     * convert the object from database to field in class.
     * @param entityClass
     * @param fieldName
     * @param value the value from database .
     * @return
     */
    Object getCompatibleFieldValue(Class<?> entityClass, String fieldName, Object value);

    /**
     * convert the object of field  to database type..
     * @param entityClass
     * @param fieldName
     * @param value the field object.
     * @return
     */
    Object getCompatibleDatabaseValue(Class<?> entityClass, String fieldName,  Object value);


    /**
     * change value to desired java type.
     * @param targetType
     * @param value
     * @return
     */
    default Object getCompatibleValue(Class<?> targetType,Object value){
        return ForceTypeAdapter.doGetCompatibleValue(targetType, value);
    }

}