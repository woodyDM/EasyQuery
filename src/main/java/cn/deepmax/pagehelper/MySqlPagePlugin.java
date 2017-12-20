package cn.deepmax.pagehelper;

import java.util.Objects;

public class MySqlPagePlugin implements PagePlugin {

    @Override
    public String getSqlForTotalRow(String rawSql) {

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ( ");
        sb.append(rawSql).append(" ) shouldNotBeUsedByUser ");
        return sb.toString();
    }

    @Override
    public String getSqlForPagingData(String rawSql, Long boundLower,Integer pageSize) {
        StringBuilder sb = new StringBuilder();
        sb.append(rawSql).append( " limit ").append(boundLower).append(" , ").append(pageSize);
        return sb.toString();
    }
}
