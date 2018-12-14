package cn.deepmax.easyquery.adapter.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Support java8 time to database type;
 */
public class LocalDateTimeToTimestampMapper implements PropertyMapper<LocalDateTime, Timestamp> {

    private static LocalDateTimeToTimestampMapper INSTANCE =null;

    public static LocalDateTimeToTimestampMapper getInstance(){
        if(INSTANCE==null){
            synchronized (LocalDateTimeToTimestampMapper.class){
                if(INSTANCE==null){
                    INSTANCE = new LocalDateTimeToTimestampMapper();

                }
            }

        }
        return INSTANCE;
    }

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute==null) ? null : Timestamp.valueOf(attribute);

    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return (dbData==null) ? null : dbData.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "LocalDateTimeToTimestampMapper_EQ";
    }
}
