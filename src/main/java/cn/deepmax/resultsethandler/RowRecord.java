package cn.deepmax.resultsethandler;


import cn.deepmax.util.TypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowRecord extends LinkedHashMap<String,Object>   {


    public RowRecord(Map<String, Object> rowResult ) {
        super(rowResult);
    }


    public Object getObject(String columnName) {
        return get(columnName);
    }


    public String getString(String columnName) {
        Object o = get(columnName);
        return (String)TypeAdapter.getCompatibleValue(String.class,o);
    }


    public Integer getInt(String columnName) {
        Object o = get(columnName);
        return (Integer) TypeAdapter.getCompatibleValue(Integer.class,o);
    }


    public Long getLong(String columnName) {
        Object o = get(columnName);
        return (Long) TypeAdapter.getCompatibleValue(Long.class,o);
    }


    public Float getFloat(String columnName) {
        Object o = get(columnName);
        return (Float) TypeAdapter.getCompatibleValue(Float.class,o);
    }


    public Double getDouble(String columnName) {
        Object o = get(columnName);
        return (Double) TypeAdapter.getCompatibleValue(Double.class,o);
    }


    public BigDecimal getBigDecimal(String columnName ) {
        Object o = get(columnName);
        return (BigDecimal) TypeAdapter.getCompatibleValue(BigDecimal.class,o);
    }


    public BigInteger getBigInteger(String columnName) {
        Object o = get(columnName);
        return (BigInteger) TypeAdapter.getCompatibleValue(BigInteger.class,o);
    }


    public Boolean getBoolean(String columnName) {
        Object o = get(columnName);
        return (Boolean) TypeAdapter.getCompatibleValue(Boolean.class,o);
    }


    public LocalDateTime getLocalDateTime(String columnName) {
        Object o = get(columnName);
        return (LocalDateTime) TypeAdapter.getCompatibleValue(LocalDateTime.class,o);
    }


    public LocalDate getLocalDate(String columnName) {
        Object o = get(columnName);
        return (LocalDate) TypeAdapter.getCompatibleValue(LocalDate.class,o);
    }


    public Timestamp getTimestamp(String columnName) {
        Object o = get(columnName);
        return (Timestamp) TypeAdapter.getCompatibleValue(Timestamp.class,o);
    }


    public Date getDate(String columnName) {
        Object o = get(columnName);
        return (Date) TypeAdapter.getCompatibleValue(Date.class,o);
    }





}
