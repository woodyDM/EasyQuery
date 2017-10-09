package cn.deepmax.entity;

public class SqlTranslator {
    private EntityInfo entityInfo;

    public SqlTranslator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public String getInsertSQL(Object obj){
        return null;
    }

    public String getUpdateSQL(Object obj){
        return null;
    }

    public String getSelectSQL(Object obj){
        return null;
    }

    public String getDeleteSQL(Object obj){
        return null;
    }
}
