package cn.deepmax.pagehelper;

public interface PagePlugin {

    String getSqlForTotalRow(String rawSql);

    String getSqlForPagingData(String rawSql,Long boundLower,Integer pageSize);

}
