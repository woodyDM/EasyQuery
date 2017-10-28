package cn.deepmax.resultsethandler;


import cn.deepmax.entity.TypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowRecord<T> extends LinkedHashMap<String,Object> implements ResultRowSet {



    private Class<T> clazz;
    public T e;

    public RowRecord(Map<String, Object> rowResult, Class<T> clazz,T e) {
        super(rowResult);
        this.clazz = clazz;
        this.e = e;
    }

    @Override
    public Object getObject(String columnName) {
        return get(columnName);
    }

    @Override
    public String getString(String columnName) {
        Object o = get(columnName);
        return (String)TypeAdapter.getCompatibleValue(String.class,o);
    }

    @Override
    public Integer getInt(String columnName) {
        Object o = get(columnName);
        return (Integer) TypeAdapter.getCompatibleValue(Integer.class,o);
    }

    @Override
    public Long getLong(String columnName) {
        Object o = get(columnName);
        return (Long) TypeAdapter.getCompatibleValue(Long.class,o);
    }

    @Override
    public Float getFloat(String columnName) {
        Object o = get(columnName);
        return (Float) TypeAdapter.getCompatibleValue(Float.class,o);
    }

    @Override
    public Double getDouble(String columnName) {
        Object o = get(columnName);
        return (Double) TypeAdapter.getCompatibleValue(Double.class,o);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName ) {
        Object o = get(columnName);
        return (BigDecimal) TypeAdapter.getCompatibleValue(BigDecimal.class,o);
    }

    @Override
    public BigInteger getBigInteger(String columnName) {
        Object o = get(columnName);
        return (BigInteger) TypeAdapter.getCompatibleValue(BigInteger.class,o);
    }

    @Override
    public Boolean getBoolean(String columnName) {
        Object o = get(columnName);
        return (Boolean) TypeAdapter.getCompatibleValue(Boolean.class,o);
    }

    @Override
    public LocalDateTime getLocalDateTime(String columnName) {
        Object o = get(columnName);
        return (LocalDateTime) TypeAdapter.getCompatibleValue(LocalDateTime.class,o);
    }

    @Override
    public LocalDate getLocalDate(String columnName) {
        Object o = get(columnName);
        return (LocalDate) TypeAdapter.getCompatibleValue(LocalDate.class,o);
    }

    @Override
    public Timestamp getTimestamp(String columnName) {
        Object o = get(columnName);
        return (Timestamp) TypeAdapter.getCompatibleValue(Timestamp.class,o);
    }

    @Override
    public Date getDate(String columnName) {
        Object o = get(columnName);
        return (Date) TypeAdapter.getCompatibleValue(Date.class,o);
    }





}
