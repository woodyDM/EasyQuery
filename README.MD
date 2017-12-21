EasyQuery
=
----
[![](https://www.jitpack.io/v/woodyDM/EasyQuery.svg)](https://www.jitpack.io/#woodyDM/EasyQuery)
----
EasyQuery is a simple database result mapping tool for java.
EasyQuery using `QueryTemplate` resolves query result from `ResultSet` to unique 
`Map<String,Object>` / POJO / `RowRecord` or a list result.
`QueryTemplate` also support entity operations,such as save(),delete(),get().The
entity information comes from user defined `EntityInfo` .
<br>
It is recommended to use EasyQuery in a spring project.

1 Quick start
----
Define a `SimpleQueryTemplateFactory` class,which needs a `DataSource` implementation.
```java
@Bean
public QueryTemplateFactory jpaFactory(){
    SimpleQueryTemplateFactory factory = new SimpleQueryTemplateFactory(h2Datasource());
    factory.setEntityInfo(new JpaEntityInfo());
    factory.isShowSql(true);
    return factory.build(); //returns a QueryTemplateFactory
}   
```
Using the factory `create` method to create new `QueryTemplate` which 
supports core SQL operations of EasyQuery.
<br>
```java
QueryTemplate template = factory.create();
SuperUser user = new SuperUser();
user.setName("EasyQuery");
user.setBigDecimal(BigDecimal.ONE);
template.save(user);
Long id = user.getId();
String sql = "update superuser set name ='Kitty' ,ok1 = '2' where id  = ? ";
int i = template.executeUpdate(sql,id);
Assert.isTrue(i==1,"1 row");
SuperUser userFromGet = template.get(SuperUser.class,id);
Assert.isTrue(userFromGet.getName().equals("Kitty"),"name");
```
2 `QueryTemplate`
----
`QueryTemplate` provides SQL query operation and entity operations.
<br>
`select` and `selectList` is common choose to get data from database, `select` for an
unique result or `selectList` for list of result.if any parameters in 
SQL , use '?' as placeholder,and add the parameters in method.
The result type is `Map<String,Object>` or POJO entity or `RowRecord` (or list of those).
The key of map is sql column label name.For example:<br>
```java
String sql="select id as userId, name ,password pwd from t_user where id < ?";
List<Map<String,Object>> resultList = template.selectList(sql,10);

```
the key of map would be "userId, name , pwd".
<br>
`selectEx` and `selectListEx` are provide by EasyQuery.Those method return 
 `RowRecord` ,which is a child class of LinkedHashMap<String,Object>.
 The key rule is same.But `RowRecord` provide another method to get value in the map.
 For example:<br>
If the value of 'userId' is type of `Long`( or even other compatible type like `Integer`),use:
```java
Long userId = rowRecord.getLong("userId");
//but when using Map,must cast:
Long userId =(Long) map.get("userId")
```
The four 'select' methods above that require `class` can also return POJO entity.
see `EntityInfo` next section.<br>
`selectScalar` can be used to get single scalar value.For example:
```java
String sql = "select count(*) from t_user where id < ?";
Long count = template.selectScalar(sql,Long.class,100);
```
`executeBatch` and `executeUpdate` can be used for update,delete,insert SQL statement.

3 `EntityInfo`
----
Tips Before:<br>
1,EasyQuery only support auto-increment primary key in `save` operation.<br>
2,It is highly recommended that the entity class is a standard javabean with default
 constructor ,getter and setter, the field type is not primitive data type.<br>
3,When using `save` method,if the primary key field is null, a new row will insert into table,
else an `update` SQL will be executed to update all fields. If this causes 
performance problem, use `executeUpdate` and provide your own SQL.<br>

All entity operations are related to an `EntityInfo` implementation.
`EntityInfo` defines the relationship between the POJO field name and database
column lable name, and also the relationship between the POJO class and database
tables.
<br>
EasyQuery provide two `EntityInfo` implementation:`MappedEntityInfo` and `JpaEntityInfo`.
<br>

`MappedEntityInfo` uses two `NameMapper` to set those rules.
'toTableNameMapper' in 'MappedEntityInfo' defines the table name according to entity
class name and 'toColumnNameMapper' defines the column name that corresponding 
the field name in POJO. You can define your own `NameMapper` to match your entity class field 
and the database column name.For example:<br>
```java
new NameMapper(){
    @Override
    public String convert(Class<?> clazz, String name) {
            if(name==null){
                return null;
            }
            return name.toUpperCase();
        }
};
```
Defines an upper case column namemapper.If your field name is "userId" ,the database column
 name should be USERID.<br>
Additionally: If any field that is not desired to persist in database,
put `@Ignore` on the field.
<br>

`JpaEntityInfo` supports standard JPA annotation `@Table` and `@Column` in your entity class to define the relationship 
mentioned above. 

4 `Transaction`
---
EasyQuery supports two kind of transaction with class `DefaultTransaction` and 
`SpringTransaction`.<br>
When using `DefaultTransaction` from `DefaultTransactionFactory`,the template gets jdbc connection directly from datasource.you manage transaction yourself like this:
```java
try{
    template.transaction().beginTransaction();
    String sql = "update t_user set name ='newname' where id  = ? ";
    template.executeUpdate(sql,1);
    template.save(user);
    //your code here.
    template.transaction().commit();
}catch (Exception e){
    template.transaction().rollback();
}finally {
    template.transaction().close();
}
```
<br>

When in Spring-managed environment,you can choose `SpringTransaction` from `SpringTransactionFactory` in declarative transaction ,
which is defined with @Transactional and TransactionManager 
(commonly `DataSourceTransactionManager` when using EasyQuery).
In this condition, the template gets jdbc connection via `DataSourceUtils` provided by Spring.
Directly using `QueryTemplate` methods is ok, the transaction now is managed by Spring.
Also in non-transactional condition ,you can also use `beginTransaction`,`commit` and `rollback`.

5 `DefaultQueryTemplateFactory`
---
All `QueryTemplate` comes from `QueryTemplateFactory`, use `DefaultQueryTemplateFactory`
to config `EntityInfo` and `TransactionFactory` of EasyQuery, 
and finally call `build` method to complete.<br>

6 Log
---
EasyQuery uses logback for logging. If SQLs are desired to be logged, set `DefaultQueryTemplateFactory`
isShowSql to true , and logging level of `cn.deepmax.querytemplate.DefaultQueryTemplate` class to DEBUG.


<br>
EasyQuery is now only tested on win10 (x64) with mysql 5.7.15, java 1.8. <br>
Any question or problem: woody@alumni.sjtu.edu.cn. <br>
2017.09~2017.10
