package cn.deepmax.querytemplate;




public interface SqlQuery<T> {

    String toSql();

    Object[] toParameters();

    Class<T> getTargetClass();

}
