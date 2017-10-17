package cn.deepmax.querytemplate;


public interface QueryTemplateFactory {

    QueryTemplate create();
    void isShowSql(Boolean isShowSql);

}
