package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;
import cn.deepmax.model.DatabaseMetaData;
import cn.deepmax.model.Pair;
import cn.deepmax.querytemplate.DefaultQueryTemplateFactory;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.DefaultTransactionFactory;

import java.util.List;
import java.util.Map;

public class FromSqlGenerator {


    private Config config;

    public FromSqlGenerator(Config config) {
        this.config = config;
    }

    public void generate(String javaClassName, String sql , Object... params){
        SimpleDataSource dataSource = new SimpleDataSource(config);
        DefaultQueryTemplateFactory.DefaultQueryTemplateFactoryBuilder builder = new DefaultQueryTemplateFactory.DefaultQueryTemplateFactoryBuilder();
        DefaultQueryTemplateFactory factory = builder.setDataSource(dataSource)
                .setTransactionFactory(new DefaultTransactionFactory())
                .setShowSql(true)
                .setCollectMetadata(true)
                .build();
        QueryTemplate template = factory.create();
        Pair<DatabaseMetaData,List<Map<String,Object>>> pair = template.doSelect(sql, params);
        DatabaseMetaData metaData = pair.first;
        GeneratorExecutor executor = new GeneratorExecutor(config);
        executor.generateIfNecessary(metaData, javaClassName);
    }
}
