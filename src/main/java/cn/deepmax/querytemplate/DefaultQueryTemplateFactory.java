package cn.deepmax.querytemplate;


import cn.deepmax.entity.*;
import cn.deepmax.pagehelper.MySqlPagePlugin;
import cn.deepmax.pagehelper.PagePlugin;
import cn.deepmax.transaction.DefaultTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Objects;


public class DefaultQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private EntityInfo entityInfo;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private TransactionFactory transactionFactory;
    private PagePlugin pagePlugin;
    private boolean isShowSql = false;
    private boolean isCollectMetadata = false;
    public static Logger logger = LoggerFactory.getLogger(DefaultQueryTemplate.class);

    private DefaultQueryTemplateFactory() {
    }

    @Override
    public QueryTemplate create(){
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        return new DefaultQueryTemplate(transaction, entityFactory, sqlTranslator, pagePlugin,this.isShowSql, this.isCollectMetadata);
    }

    public static class DefaultQueryTemplateFactoryBuilder{
        private DefaultQueryTemplateFactory factory = new DefaultQueryTemplateFactory();

        public DefaultQueryTemplateFactoryBuilder setDataSource(DataSource dataSource) {
            Objects.requireNonNull("Datasource should not be null.");
            this.factory.dataSource = dataSource;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setEntityInfo(EntityInfo entityInfo) {
            this.factory.entityInfo = entityInfo;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setEntityFactory(EntityFactory entityFactory) {
            this.factory.entityFactory = entityFactory;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setSqlTranslator(SqlTranslator sqlTranslator) {
            this.factory.sqlTranslator = sqlTranslator;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setTransactionFactory(TransactionFactory transactionFactory) {
            this.factory.transactionFactory = transactionFactory;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setPagePlugin(PagePlugin pagePlugin) {
            this.factory.pagePlugin = pagePlugin;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setShowSql(boolean showSql) {
            this.factory.isShowSql = showSql;
            return this;
        }

        public DefaultQueryTemplateFactoryBuilder setCollectMetadata(boolean collectMetadata) {
            this.factory.isCollectMetadata = collectMetadata;
            return this;
        }

        public DefaultQueryTemplateFactory build(){
            if(this.factory.dataSource==null){
                throw new NullPointerException("DefaultQueryTemplateFactory needs a non-null datasource.");
            }
            if(this.factory.entityInfo==null){
                logger.debug("No EntityInfo set for DefaultQueryTemplateFactory, set default EntityInfo to JpaEntityInfo");
                factory.entityInfo = new JpaEntityInfo();
            }
            factory.entityFactory = new EntityFactory(factory.entityInfo);
            if(this.factory.sqlTranslator ==null){
                logger.debug("No SqlTranslator set for DefaultQueryTemplateFactory, set default SqlTranslator to DefaultSqlTranslator");
                factory.sqlTranslator = new DefaultSqlTranslator(factory.entityInfo);
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
