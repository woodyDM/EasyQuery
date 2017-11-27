package cn.deepmax.querytemplate;


import cn.deepmax.entity.*;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.mapper.NameMapper;
import cn.deepmax.model.Config;
import cn.deepmax.transaction.DefaultTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import cn.deepmax.util.StringUtils;

import javax.sql.DataSource;
import java.util.Objects;


public class DefaultQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private EntityInfo entityInfo;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private TransactionFactory transactionFactory;
    private Config config = new Config();

    private boolean init = false;

    public DefaultQueryTemplateFactory(DataSource dataSource) {
        Objects.requireNonNull(dataSource,"DataSource is null.");
        this.dataSource = dataSource;
    }

    public DefaultQueryTemplateFactory build(){

        if(entityInfo==null){
            entityInfo = new MappedEntityInfo();
        }
        entityFactory = new EntityFactory(entityInfo);
        if(this.sqlTranslator ==null){
            sqlTranslator = new DefaultSqlTranslator(entityInfo);
        }
        if(transactionFactory==null){
            transactionFactory = new DefaultTransactionFactory();
        }
        if(config.isGenerateClass()){
            if(config.getToFieldNameMapper()==null){
                throw new IllegalArgumentException("When isGenerateClass is true, a NameMapper should be set.");
            }
            if(StringUtils.isEmpty(config.getValueObjectPath())){
                throw new IllegalArgumentException("When isGenerateClass is true, valueObjectPath should be set.");
            }
            if(StringUtils.isEmpty(config.getEntityPath())){
                throw new IllegalArgumentException("When isGenerateClass is true, entityPath should be set.");
            }
            config.normalizePath();
        }

        init = true;
        return this;
    }


    public void isShowSql(Boolean isShowSql) {
        this.config.setShowSql(isShowSql);
    }

    public void isGenerateClass(Boolean isGenerateClass) {
        this.config.setGenerateClass(isGenerateClass);
    }

    public void setToFieldNameMapper(NameMapper toFieldNameMapper) {
        this.config.setToFieldNameMapper(toFieldNameMapper);
    }

    public void setValueObjectPath(String valueObjectPath) {
        this.config.setValueObjectPath(valueObjectPath);
    }

    public void setEntityPath(String entityPath) {
        this.config.setEntityPath(entityPath);
    }


    @Override
    public QueryTemplate create(){
        if(!init){
            throw new EasyQueryException("QueryTemplateFactory init failed, build() should be called.");
        }
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        return new DefaultQueryTemplate(transaction,entityFactory,this.config,sqlTranslator);
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setEntityInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }


    public void setSqlTranslator(SqlTranslator sqlTranslator) {
        this.sqlTranslator = sqlTranslator;
    }




}
