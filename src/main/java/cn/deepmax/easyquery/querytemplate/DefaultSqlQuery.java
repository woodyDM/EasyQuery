package cn.deepmax.easyquery.querytemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DefaultSqlQuery<T> implements SqlQuery<T> {

    private Class<T> clazz;
    private StringBuilder sb = new StringBuilder();
    private List<Object> paramList = new ArrayList<>();

    public static <T> DefaultSqlQuery<T> newInstance(){
        return new DefaultSqlQuery<>();
    }



    public DefaultSqlQuery<T> addTargetClass(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        this.clazz = clazz;
        return this;
    }



    public DefaultSqlQuery<T> append(String sqlFraction) {
        sb.append(" ").append(sqlFraction).append(" ");
        return this;
    }


    public DefaultSqlQuery<T> append(String sqlFraction, Object... args) {
        this.append(sqlFraction);
        addParam(args);
        return this;
    }


    public DefaultWhereQuery<T> where() {
        return new DefaultWhereQuery<>(this, false);
    }



    public DefaultWhereQuery<T> where(String condition) {
        this.append("where").append(condition);
        return new DefaultWhereQuery<>(this, true);
    }


    public DefaultWhereQuery<T> where(String condition, Object... args) {
        this.append("where").append(condition, args);
        return new DefaultWhereQuery<>(this, true);
    }


    public DefaultWhereQuery<T> and() {
        return new DefaultWhereQuery<>(this,"and");
    }


    public DefaultWhereQuery<T> or() {
        return new DefaultWhereQuery<>(this,"or");
    }


    public String toSql() {
        return sb.toString();
    }


    public Object[] toParameters() {
        return paramList.toArray();
    }


    public Class<T> getTargetClass() {
        return  this.clazz;
    }


    public static class DefaultWhereQuery<T> implements SqlQuery<T>  {

        private DefaultSqlQuery<T> query;
        private boolean alreadyInWhere;
        private String lastOpt = null;

        DefaultWhereQuery(DefaultSqlQuery<T> query, boolean alreadyInWhere) {
            this.query = query;
            this.alreadyInWhere = alreadyInWhere;

        }

        DefaultWhereQuery(DefaultSqlQuery<T> query, String lastOpt) {
            this.query = query;
            this.lastOpt = lastOpt;
            alreadyInWhere = true;
        }

        public DefaultSqlQuery<T> then() {
            return query;
        }

        public DefaultWhereQuery<T> and() {
            lastOpt = "and";
            return this;
        }

        public DefaultWhereQuery<T> or() {
            lastOpt = "or";
            return this;
        }

        public DefaultWhereQuery<T> append(String sqlFraction, Object... args) {
            return ifTrue(true, sqlFraction, args);
        }

        public DefaultWhereQuery<T> append(String sqlFraction) {
            return ifTrue(true,sqlFraction);
        }

        public DefaultWhereQuery<T> ifTrue(boolean predicate, String sqlFraction, Object... args) {
            if(predicate){
                boolean a = appendWhereIfNeed();
                appendOptIfNeed(a);

                this.query.append("(").append(sqlFraction).append(")");
                this.query.addParam(args);
            }
            return this;
        }

        public DefaultWhereQuery<T> ifNotEmpty(String queryWords, String sqlFraction, int repeatTime) {
            return doAppendIf(queryWords,word->word!=null && word.toString().length()!=0, sqlFraction, repeatTime);

        }

        public DefaultWhereQuery<T> ifNotNull(Object obj, String sqlFraction, int repeatTime) {
            return doAppendIf(obj,Objects::nonNull, sqlFraction, repeatTime);
        }

        @Override
        public String toSql() {
            return this.query.toSql();
        }

        @Override
        public Object[] toParameters() {
            return this.query.toParameters();
        }

        @Override
        public Class<T> getTargetClass() {
            return this.query.getTargetClass();
        }

        private void appendOptIfNeed(boolean isJustAppendWhere){
            if(lastOpt!=null && alreadyInWhere){
                if(!isJustAppendWhere){
                    this.query.append(lastOpt);
                }
                lastOpt = null;
            }
        }

        private boolean appendWhereIfNeed(){
            if(!alreadyInWhere){
                this.query.append("where");
                alreadyInWhere = true;
                return true;
            }else{
                return false;
            }
        }

        private DefaultWhereQuery<T> doAppendIf(Object obj, Function<Object,Boolean> mapper, String sql, int repeatTime){
            if(mapper.apply(obj)){
                boolean a = appendWhereIfNeed();
                appendOptIfNeed(a);
                this.query.append("(").append(sql).append(")");
                if(repeatTime<=0){
                    throw new IllegalArgumentException("repeatTime should >0");
                }else if(repeatTime==1){
                    this.query.paramList.add(obj);
                }else{
                    for (int i = 0; i < repeatTime; i++) {
                        this.query.paramList.add(obj);
                    }
                }
            }
            return this;
        }
    }




    private static boolean isNotEmpty(Object... objects){
        return objects!=null && objects.length!=0;
    }

    private void addParam(Object... args){
        if(isNotEmpty(args)){
            for (int i = 0; i < args.length; i++) {
                paramList.add(args[i]);
            }
        }
    }

}
