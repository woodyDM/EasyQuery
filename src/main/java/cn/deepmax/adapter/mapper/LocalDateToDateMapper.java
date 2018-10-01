package cn.deepmax.adapter.mapper;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Support java8 time to database type;
 */
public class LocalDateToDateMapper implements PropertyMapper<LocalDate, Date> {

    private static LocalDateToDateMapper INSTANCE = null;


    public static LocalDateToDateMapper getInstance(){
        if(INSTANCE==null){
            synchronized (LocalDateToDateMapper.class){
                if(INSTANCE==null){
                    INSTANCE = new LocalDateToDateMapper();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return (attribute==null)?null: Date.valueOf(attribute);

    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return (dbData==null)?null: dbData.toLocalDate();
    }

    @Override
    public String toString() {
        return "LocalDateToDateMapper_EQ";
    }
}
