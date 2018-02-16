package cn.deepmax.generator;

import cn.deepmax.model.ColumnMetaData;
import cn.deepmax.model.DatabaseMetaData;
import cn.deepmax.querytemplate.DefaultQueryTemplateFactory;
import cn.deepmax.querytemplate.QueryTemplate;
import cn.deepmax.resultsethandler.RowRecord;
import cn.deepmax.transaction.DefaultTransactionFactory;
import java.util.*;

public class MysqlTableGenerator {

    private Set<String> tables = new HashSet<>();
    private Set<String> prefixList = new HashSet<>();
    private String tableSchema ;
    private Config config;
    private TypeTranslator typeTranslator = new MysqlDbTypeTranslator();
    private static final String sql = "SELECT TABLE_NAME from information_schema.`TABLES`  where TABLE_SCHEMA = ? ";
    private static final String cSql = "SELECT DISTINCT column_name,data_type,column_comment ,column_key,numeric_precision FROM Information_schema.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ? ";

    public MysqlTableGenerator(Config config,String tableSchema) {
        this.config = config;
        this.tableSchema = tableSchema;
    }

    public MysqlTableGenerator setTypeTranslator(TypeTranslator typeTranslator) {
        this.typeTranslator = typeTranslator;
        return this;
    }

    public MysqlTableGenerator addTables(String oneTable, String... others){
        tables.add(oneTable);
        Collections.addAll(tables, others);
        return this;
    }

    public MysqlTableGenerator addTables(Collection<String> collection){
        tables.addAll(collection);
        return this;
    }

    public MysqlTableGenerator addPrefix(String onePrefix,String... otherPrefix){
        prefixList.add(onePrefix);
        Collections.addAll(prefixList, otherPrefix);
        return this;
    }

    public MysqlTableGenerator addPrefix(Collection<String> collection){
        prefixList.addAll(collection);
        return this;
    }

    /**
     *  generate
     */
    public void generate(){
        SimpleDataSource dataSource = new SimpleDataSource(config);
        DefaultQueryTemplateFactory.DefaultQueryTemplateFactoryBuilder builder = new DefaultQueryTemplateFactory.DefaultQueryTemplateFactoryBuilder();
        DefaultQueryTemplateFactory factory = builder.setDataSource(dataSource)
                .setTransactionFactory(new DefaultTransactionFactory())
                .setShowSql(true)
                .build();
        QueryTemplate template = factory.create();
        List<RowRecord> tables = template.selectListEx(sql, this.tableSchema);
        GeneratorExecutor executor = new GeneratorExecutor(config);
        for(RowRecord it : tables){
            String tableName = it.getString("TABLE_NAME");
            if(!isGenerate(tableName)){
                continue;   //do not generate
            }
            List<RowRecord> columns = template.selectListEx(cSql, tableName, this.tableSchema);
            DatabaseMetaData dbmeta = new DatabaseMetaData();
            dbmeta.setCatalogName(this.tableSchema);
            dbmeta.setTableName(tableName);
            for(RowRecord column:columns){
                ColumnMetaData columnMetaData = new ColumnMetaData(column.getString("column_name"),
                        null ,
                        column.getString("data_type"),
                        column.getInt("numeric_precision"),
                        column.getString("column_comment"));
                dbmeta.getColumnMetaDataList().add(columnMetaData);
                String javaTypeName = typeTranslator.translate(columnMetaData);
                columnMetaData.setClassTypeName(javaTypeName);
            }
            executor.generateIfNecessary(dbmeta, config.getToClassMapper().map(tableName));
        }

    }

    /**
     * generate if in tableSet or prefix matches.
     * @param tableName
     * @return
     */
    private Boolean isGenerate(String tableName){
        return tables.contains(tableName)
                || prefixList.stream().anyMatch(it->tableName.startsWith(it));
    }
}
