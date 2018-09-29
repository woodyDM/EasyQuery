package cn.deepmax.adapter.mapper;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
