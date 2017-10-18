package cn.deepmax.resultsethandler;

import java.math.BigInteger;

public interface ResultRowSet {
    Object getObject(String columnName);
    String getString(String columnName);

    /**
     * only support integer to Boolean
     * @param columnName
     * @return
     */
    Boolean getBoolean(String columnName);

    Integer getInt(String columnName);
    Long getLong(String columnName);
    Float getFloat(String columnName);
    Double getDouble(String columnName);
    java.math.BigDecimal getBigDecimal(String columnName);
    BigInteger getBigInteger(String columnName);

    java.sql.Date getDate(String columnName);
    java.sql.Timestamp getTimestamp(String columnName);
    java.time.LocalDateTime getLocalDateTime (String columnName);
    java.time.LocalDate getLocalDate(String columnName) ;
}
