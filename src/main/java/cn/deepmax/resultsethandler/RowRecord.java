package cn.deepmax.resultsethandler;


import cn.deepmax.adapter.AbstractCacheableTypeAdapter;
import cn.deepmax.adapter.SimpleTypeAdapter;
import cn.deepmax.adapter.TypeAdapter;

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

    protected static TypeAdapter typeAdapter = new SimpleTypeAdapter();


    public String getString(String columnName) {
        Object o = get(columnName);
        return (String) typeAdapter.getCompatibleValue(String.class, o);
    }


    public Integer getInt(String columnName) {
        Object o = get(columnName);
        return (Integer) typeAdapter.getCompatibleValue(Integer.class, o);
    }


    public Long getLong(String columnName) {
        Object o = get(columnName);
        return (Long) typeAdapter.getCompatibleValue(Long.class, o);
    }


    public Float getFloat(String columnName) {
        Object o = get(columnName);
        return (Float) typeAdapter.getCompatibleValue(Float.class, o);
    }


    public Double getDouble(String columnName) {
        Object o = get(columnName);
        return (Double) typeAdapter.getCompatibleValue(Double.class, o);
    }


    public BigDecimal getBigDecimal(String columnName ) {
        Object o = get(columnName);
        return (BigDecimal) typeAdapter.getCompatibleValue(BigDecimal.class, o);
    }


    public BigInteger getBigInteger(String columnName) {
        Object o = get(columnName);
        return (BigInteger)  typeAdapter.getCompatibleValue(BigInteger.class, o);
    }


    public Boolean getBoolean(String columnName) {
        Object o = get(columnName);
        return (Boolean) typeAdapter.getCompatibleValue(Boolean.class, o);
    }


    public LocalDateTime getLocalDateTime(String columnName) {
        Object o = get(columnName);
        return (LocalDateTime) typeAdapter.getCompatibleValue(LocalDateTime.class, o);
    }


    public LocalDate getLocalDate(String columnName) {
        Object o = get(columnName);
        return (LocalDate) typeAdapter.getCompatibleValue(LocalDate.class, o);
    }


    public Timestamp getTimestamp(String columnName) {
        Object o = get(columnName);
        return (Timestamp) typeAdapter.getCompatibleValue(Timestamp.class, o);
    }


    public Date getDate(String columnName) {
        Object o = get(columnName);
        return (Date) typeAdapter.getCompatibleValue(Date.class, o);
    }





}
