package cn.deepmax.easyquery.querytemplate;




public interface SqlQuery<T> {

    String toSql();

    Object[] toParameters();

    Class<T> getTargetClass();

}
