package cn.deepmax.querytemplate;


import cn.deepmax.adapter.JpaAnnotatedTypeAdapter;
import cn.deepmax.adapter.TypeAdapter;
import cn.deepmax.entity.*;
import cn.deepmax.pagehelper.MySqlPagePlugin;
import cn.deepmax.pagehelper.PagePlugin;
import cn.deepmax.transaction.DefaultTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DefaultQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private EntityInfo entityInfo;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private TransactionFactory transactionFactory;
    private PagePlugin pagePlugin;
    private boolean isShowSql = false;
    private TypeAdapter typeAdapter;

    public static Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);

    public DefaultQueryTemplateFactory() {
    }

    public DefaultQueryTemplateFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public QueryTemplate create(){
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        DefaultQueryTemplate template =  new DefaultQueryTemplate(transaction, entityFactory, sqlTranslator, pagePlugin,this.isShowSql );
        template.setTypeAdapter(this.typeAdapter);
        return template;
    }



    public static class Builder{
        private DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory();


        public Builder setDataSource(DataSource dataSource) {
            Objects.requireNonNull("Datasource should not be null.");
            this.factory.dataSource = dataSource;
            return this;
        }

        public Builder setTypeAdapter(TypeAdapter typeAdapter) {
            Objects.requireNonNull("typeAdapter should not be null.");
            this.factory.typeAdapter = typeAdapter;
            return this;
        }

        public Builder setEntityInfo(EntityInfo entityInfo) {
            this.factory.entityInfo = entityInfo;
            return this;
        }

        public Builder setEntityFactory(EntityFactory entityFactory) {
            this.factory.entityFactory = entityFactory;
            return this;
        }

        public Builder setSqlTranslator(SqlTranslator sqlTranslator) {
            this.factory.sqlTranslator = sqlTranslator;
            return this;
        }

        public Builder setTransactionFactory(TransactionFactory transactionFactory) {
            this.factory.transactionFactory = transactionFactory;
            return this;
        }

        public Builder setPagePlugin(PagePlugin pagePlugin) {
            this.factory.pagePlugin = pagePlugin;
            return this;
        }

        public Builder setShowSql(boolean showSql) {
            this.factory.isShowSql = showSql;
            return this;
        }



        public DefaultQueryTemplateFactory build(){
            if(this.factory.dataSource==null){
                throw new NullPointerException("DefaultQueryTemplateFactory needs a non-null datasource.");
            }
            if(factory.typeAdapter ==null){
                logger.debug("No typeAdapter set for DefaultQueryTemplateFactory, set default typeAdapter to JpaAnnotatedTypeAdapter");
                factory.typeAdapter = new JpaAnnotatedTypeAdapter();
            }
            if(this.factory.entityInfo==null){
                logger.debug("No EntityInfo set for DefaultQueryTemplateFactory, set default EntityInfo to JpaEntityInfo");
                factory.entityInfo = new JpaEntityInfo(factory.typeAdapter);
            }
            factory.entityFactory = new EntityFactory(factory.entityInfo, factory.typeAdapter);
            if(this.factory.sqlTranslator ==null){
                logger.debug("No SqlTranslator set for DefaultQueryTemplateFactory, set default SqlTranslator to DefaultSqlTranslator");
                factory.sqlTranslator = new DefaultSqlTranslator(factory.entityInfo, null, factory.typeAdapter);
            }
            if(factory.transactionFactory == null){
                logger.debug("No TransactionFactory set for DefaultQueryTemplateFactory, set default TransactionFactory to DefaultTransactionFactory");
                factory.transactionFactory = new DefaultTransactionFactory();
            }
            if(factory.pagePlugin == null){
                logger.debug("No PagePlugin set for DefaultQueryTemplateFactory, set default PagePlugin to MySqlPagePlugin");
                factory.pagePlugin = new MySqlPagePlugin();
            }

            return factory;
        }
    }

}
