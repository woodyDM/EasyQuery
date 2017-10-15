package cn.deepmax.querytemplate;


import cn.deepmax.entity.*;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.mapper.LowerCaseTableNameMapper;
import cn.deepmax.mapper.NameMapper;
import cn.deepmax.mapper.SameNameMapper;
import cn.deepmax.transaction.SpringTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import javax.sql.DataSource;
import java.util.Objects;


public class SimpleQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private TransactionFactory transactionFactory;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private Boolean isShowSql;
    private NameMapper toTableNameMapper;
    private NameMapper toColumnNameMapper;
    private boolean init = false;

    public SimpleQueryTemplateFactory(DataSource dataSource) {
        Objects.requireNonNull(dataSource,"DataSource is null.");
        this.dataSource = dataSource;
        isShowSql = false;
    }

    public SimpleQueryTemplateFactory build(){

        if(toTableNameMapper==null){
            toTableNameMapper = new LowerCaseTableNameMapper();
        }
        if(toColumnNameMapper==null){
            toColumnNameMapper = new SameNameMapper();
        }
        EntityInfo entityInfo = new SimpleEntityInfo(toTableNameMapper,toColumnNameMapper);
        if(this.entityFactory==null){
            entityFactory = new EntityFactory(entityInfo);
        }
        if(this.sqlTranslator ==null){
            sqlTranslator = new DefaultSqlTranslator(entityInfo);
        }
        if(transactionFactory==null){
            transactionFactory = new SpringTransactionFactory();
        }
        if(isShowSql==null){
            isShowSql = false;
        }
        init = true;
        return this;
    }


    @Override
    public void isShowSql(Boolean isShowSql) {
        this.isShowSql = isShowSql;
    }

    @Override
    public QueryTemplate create(){
        if(!init){
            throw new EasyQueryException("QueryTemplateFactory init failed, build() should be called.");
        }
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        return new DefaultQueryTemplate(transaction,entityFactory,isShowSql,sqlTranslator);
    }

    public void setToTableNameMapper(NameMapper toTableNameMapper) {
        this.toTableNameMapper = toTableNameMapper;
    }

    public void setToColumnNameMapper(NameMapper toColumnNameMapper) {
        this.toColumnNameMapper = toColumnNameMapper;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void setSqlTranslator(SqlTranslator sqlTranslator) {
        this.sqlTranslator = sqlTranslator;
    }




}
