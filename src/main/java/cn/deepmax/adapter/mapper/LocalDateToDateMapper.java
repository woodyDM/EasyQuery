package cn.deepmax.adapter.mapper;

import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.time.LocalDate;

public class LocalDateToDateMapper implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return (attribute==null)?null: Date.valueOf(attribute);

    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return (dbData==null)?null: dbData.toLocalDate();
    }
}
