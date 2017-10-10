package cn.deepmax.querytemplate;


import cn.deepmax.entity.DefaultSqlTranslator;
import cn.deepmax.entity.EntityFactory;
import cn.deepmax.entity.SimpleEntityInfo;
import cn.deepmax.entity.SqlTranslator;
import cn.deepmax.mapper.ColumnNameMapper;
import cn.deepmax.mapper.SameColumnNameMapper;
import cn.deepmax.resultsethandler.ResultSetHandler;
import cn.deepmax.transaction.SpringTransactionFactory;
import cn.deepmax.transaction.Transaction;
import cn.deepmax.transaction.TransactionFactory;
import javax.sql.DataSource;
import java.util.Objects;


public class DefaultQueryTemplateFactory implements QueryTemplateFactory {

    private DataSource dataSource;
    private TransactionFactory transactionFactory;
    private EntityFactory entityFactory;
    private SqlTranslator sqlTranslator;
    private boolean isShowSql;

    public DefaultQueryTemplateFactory(DataSource dataSource) {
        Objects.requireNonNull(dataSource,"dataSource is null.");
        this.dataSource = dataSource;
        transactionFactory = new SpringTransactionFactory();
        entityFactory = new EntityFactory(new SameColumnNameMapper());
        sqlTranslator = new DefaultSqlTranslator(new SimpleEntityInfo());
        isShowSql = false;
    }

    @Override
    public void setColumnNameMapper(ColumnNameMapper columnNameMapper) {
        entityFactory = new EntityFactory(columnNameMapper);
    }

    @Override
    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void setShowSql(boolean isShowSql) {
        this.isShowSql = isShowSql;
    }

    @Override
    public void setSqlTranslator(SqlTranslator sqlTranslator) {
        this.sqlTranslator = sqlTranslator;
    }

    @Override
    public QueryTemplate create(){
        Transaction transaction = transactionFactory.newTransaction(dataSource);
        return new DefaultQueryTemplate(transaction,entityFactory,isShowSql,sqlTranslator);
    }


}
