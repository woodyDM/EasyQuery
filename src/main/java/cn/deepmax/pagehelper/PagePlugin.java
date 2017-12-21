package cn.deepmax.pagehelper;

public interface PagePlugin {


    /**
     *
     * @param rawSql original sql.
     * @return the sql which returns a scalar of total row count.
     */
    String getSqlForTotalRow(String rawSql);

    /**
     *
     * @param rawSql  original sql.
     * @param boundLower  start of row .   0 is the first row.
     * @param pageSize      page size of one query.
     * @return
     */
    String getSqlForPagingData(String rawSql,Long boundLower,Integer pageSize);

}
