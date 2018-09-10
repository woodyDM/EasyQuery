package cn.deepmax.adapter.mapper;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeToTimestampMapper implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute==null)?null: Timestamp.valueOf(attribute);

    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return (dbData==null)?null: dbData.toLocalDateTime();
    }
}
