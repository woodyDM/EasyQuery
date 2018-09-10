package cn.deepmax.adapter.mapper;

import cn.deepmax.adapter.mapper.PropertyMapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateToDateMapper implements PropertyMapper<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return (attribute==null)?null: Date.valueOf(attribute);

    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return (dbData==null)?null: dbData.toLocalDate();
    }
}
