package cn.deepmax.resultsethandler;

import cn.deepmax.mapper.NameMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowRecord<T> implements ResultRowSet {



    private Map<String,Object> rowResult = new LinkedHashMap<>();
    private Class<T> clazz;
    public T e;

    public RowRecord(Map<String, Object> rowResult, Class<T> clazz,T e) {
        this.rowResult = rowResult;
        this.clazz = clazz;
        this.e = e;
    }

    @Override
    public String getString(String columnName) {
        Object o = rowResult.get(columnName);
        return (o==null)?null:o.toString();
    }


    @Override
    public short getShort(String columnName) {
        String d = getString(columnName);
        try{
            return Short.valueOf(d);
        }catch (NumberFormatException e){
            throw e;
        }
    }

    @Override
    public int getInt(String columnName) {
        String d = getString(columnName);
        try{
            return Integer.valueOf(d);
        }catch (NumberFormatException e){
            throw e;
        }
    }

    @Override
    public long getLong(String columnName) {
        String d = getString(columnName);
        try{
            return Long.valueOf(d);
        }catch (NumberFormatException e){
            throw e;
        }
    }

    @Override
    public float getFloat(String columnName) {
        String d = getString(columnName);
        try{
            return  Float.valueOf(d);
        }catch (NumberFormatException e){
            throw e;
        }
    }

    @Override
    public double getDouble(String columnName) {
        String s = getString(columnName);
        try{
            return Double.valueOf(s);
        }catch (NumberFormatException e){
            throw e;
        }
    }
    @Override
    public Object getObject(String columnName) {
        return rowResult.get(columnName);
    }
    @Override
    public BigDecimal getBigDecimal(String columnName ) {
        return BigDecimal.valueOf(getDouble(columnName));
    }


    @Override
    public boolean getBoolean(String columnName) {
        Object o = rowResult.get(columnName);
        if (o==null){
            return false;
        }
        if(o instanceof Boolean){
            return (Boolean)o;
        }
        String s = getString(columnName);
        if(s==null || s.length()==0) return false;
        try{
            Long i = getLong(columnName);
            return (i!=0);
        }catch (NumberFormatException e){
            try{
                Double d = getDouble(columnName);
                double ERROR = 1e-30;
                return (Math.abs(d)>ERROR);
            }catch (NumberFormatException ee){
                throw new RuntimeException("can't cast column "+columnName+" to boolean.");
            }
        }
    }
    @Override
    public LocalDateTime getLocalDateTime(String columnName) {
        Timestamp t = getTimestamp(columnName);
        if(t==null) return null;
        return t.toLocalDateTime();
    }

    @Override
    public LocalDate getLocalDate(String columnName) {
        Timestamp t = getTimestamp(columnName);
        if(t==null) return null;
        return t.toLocalDateTime().toLocalDate();
    }

    @Override
    public Timestamp getTimestamp(String columnName) {
        Object o = rowResult.get(columnName);
        if(o==null) return null;
        if(o instanceof Timestamp){
            return (Timestamp) o;
        }
        Date d = getDate(columnName);
        if(d==null) return null;
        return new Timestamp(d.getTime());
    }

    @Override
    public Date getDate(String columnName) {
        Object o = rowResult.get(columnName);
        if(o==null) return null;
        if(o instanceof Date){
            return (Date) o;
        }
        throw new IllegalArgumentException("column "+columnName+" is not type of Date");
    }





}
