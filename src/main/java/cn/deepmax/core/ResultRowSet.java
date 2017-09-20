package cn.deepmax.core;

public interface ResultRowSet {
    String getString(String columnName);
    boolean getBoolean(String columnName);
    short getShort(String columnName);
    int getInt(String columnName);
    long getLong(String columnName);
    float getFloat(String columnName);
    double getDouble(String columnName);
    java.math.BigDecimal getBigDecimal(String columnName);
    java.sql.Date getDate(String columnName);
    java.sql.Timestamp getTimestamp(String columnName);
    Object getObject(String columnName);
    java.time.LocalDateTime getLocalDateTime (String columnName);
    java.time.LocalDate getLocalDate(String columnName) ;
}
