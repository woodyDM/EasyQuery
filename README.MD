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
It is very easy to use EasyQuery in a spring project.

0 How to use
---

Way 1. Visit _https://www.jitpack.io_ and look up "https://github.com/woodyDM/EasyQuery" 
and follow the instructions, choose the version you like and add to your pom.xml or build.gradle .    
Way 2. Clone this git or download source code to your computer and build jar yourself.  

1 Quick start
---

There is a simple example to show EasyQuery features.  
Step 0: Firstly your need prepare a `DataSource` for EasyQuery.  
Step 1: Use  `DefaultQueryTemplateFactory.Builder` to create `QueryTemplateFactory`. 
```java

    @Bean("springFactory")
    public QueryTemplateFactory factory(){
        DefaultQueryTemplateFactory.Builder builder = new DefaultQueryTemplateFactory.Builder();
        return builder.setDataSource(h2Datasource())
                    .setTransactionFactory(new SpringTransactionFactory())
                    .setShowSql(true)
                    .setPagePlugin(new MySqlPagePlugin())
                    .build();
    }
```
Step 2: Using `create` method of the factory to create new `QueryTemplate` which 
supports core SQL operations of EasyQuery.
<br>
```java
QueryTemplate template = factory.create();
SuperUser user = new SuperUser();
user.setName("EasyQuery");
user.setBigDecimal(BigDecimal.ONE);
template.save(user);                                        //directly save an entity.
Long id = user.getId();         
String sql = "update superuser set name ='Kitty' ,ok1 = '2' where id  = ? ";
int i = template.executeUpdate(sql,id);                     //execute sql .
Assert.isTrue(i==1,"1 row");
SuperUser userFromGet = template.get(SuperUser.class,id);   putIfAbsent
Assert.isTrue(userFromGet.getName().equals("Kitty"),"name");

PageInfo<SuperUser> pageInfo = template.selectPage(sql,(RowRecord oneRecord) ->{
            SuperUser user = new SuperUser();
            user.setUserName(oneRecord.getString("USER_NAME"));
            user.setBigDecimal(oneRecord.getBigDecimal("BIG_DECIMAL"));
            return user;
        }, 3,3,1);                                  //user defined entity mapping using Function<T,R> interface.
```
2 `QueryTemplate`
---

`QueryTemplate` ,the core interface of EasyQuery, provides SQL query and entity operations.`QueryTemplate` is thread-unsafe when using `DefaultTransactionFactory`,
and it is thread-safe when using `SpringTransactionFactory` ,which using ThreadLocal to hold the jdbc connection.
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
`selectEx` and `selectListEx` also work well. Those method return 
 `RowRecord` ,which is a child class of LinkedHashMap<String,Object>.
 The key rule is same.But `RowRecord` provide another method to get value in the map.
 For example:<br>
If the value of 'userId' is type of `Long`( or even other compatible type like `Integer`),use:
```java
Long userId = rowRecord.getLong("userId");
Integer userIdInteger = rowRecord.getInteger("userId");
Boolean isIdNotZero = rowRecord.getBoolean("userId");   //Boolean value
//but when using Map,must cast:
 
```
There are other 'select' methods requiring `Class<T>` as parameter which return POJO entity or entity list.
see `EntityInfo` next section.<br>
`selectScalar` can be used to get single scalar value.For example:
```java
String sql = "select count(*) from t_user where id < ?";
Long count = template.selectScalar(sql,Long.class,100);
```
`executeBatch` and `executeUpdate` can be used for update,delete,insert SQL statement.
<br><br>
**Method in QueryTemplate** <br>
 
 
3 `EntityInfo`
---

Tips Before:<br>
1,EasyQuery only support auto-increment primary key in `save` operation.<br>
2,It is highly recommended that the entity class is a standard javabean with default
 constructor ,getter and setter, the field type is not primitive data type.<br>
3,When using `save` method,if the primary key field is null, a new row will insert into table,
else an `update` SQL will be executed to update all fields. If this causes 
performance problem, use `executeUpdate` and provide your own SQL.<br>

All entity operations are related to an `EntityInfo` implementation. The key point of entity mapping ( from Map<String,Object>
to POJO ) is to rule relationship between POJO fields and database columns.
`EntityInfo` defines the relationship between the POJO field name and database
column label name, and also the relationship between the POJO class and database
tables.
<br><br>
Default `EntityInfo` implementation is  `JpaEntityInfo` .<br>
(`MappedEntityInfo` is not supported anymore.)
<br>
 
**JpaEntityInfo**

`JpaEntityInfo` supports standard JPA annotations :`@Table`, `@Column`, `@Id`<br>
 Just like using Hibernate, put `@Table` on your class, and `@Column` in your class field (or field getter) ,
 and `@Id` on the primaryKey field. Every entity class must have a primary key field.
 <br>Also, the typical JPA annotation :<br>@Enumerated<br>
 @Transient      <br>@Convert(converter = MyConverter.class) are supported.
 In @Convert(convert = XX.class) the XX.class should implements `javax.persistence.AttributeConverter` or
 `cn.deepmax.adapter.mapper.PropertyMapper<X,Y>`<br>
 for example:<br>
 ```java

@Entity
@Table(name = "super_user" )
public class SuperUser extends User{

    @Id
    @Column(name = "ID")    //also no need to define this @Column, the default will be same as field name.
    private Long id;
    
    @Column(name = "BIG_DECIMAL")
    private BigDecimal bigDecimal;
    
    // MyColor is an Enum Type
    @Column(name = "COLOR1")
    private MyColor color1; //no @Enumerated annotated is same as @Enumerated(javax.persistence.EnumType.ORDINAL)
    

    @Column(name = "AUTH")
    @Enumerated(javax.persistence.EnumType.STRING)
    private EnumType auth;
    

    @Column(name = "COLOR2")
    @Enumerated(javax.persistence.EnumType.ORDINAL)
    private MyColor color2;
    
    @Column(name = "COLOR3")
    @Convert(converter = JpaConverter.class)
    private MyColor color3;
    
    @Transient      // transient
    @Convert(converter = MyConverter.class) //using convert is also ok.
    @Column(name = "COLOR4")
    private MyColor color4;
    
    @Column(name = "USER_NAME")
    private String userName;
    
    @Column(name = "CREATE_TIME")
    @Convert(converter = LocalDateTimeToTimestampMapper.class)
    private LocalDateTime createTime;
    
    @Column(name = "UPDATE_DATE")
    @Convert(converter = LocalDateToDateMapper.class)
    private LocalDate updateDate;
    
    @Column(name = "SHOW")
    private Boolean show;
    //getter and setters    
}
```
 
 

4 Transaction
---
EasyQuery supports two kind of transaction with class `DefaultTransaction` and 
`SpringTransaction`.<br><br>
**DefaultTransaction**

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

**SpringTransaction**

When in Spring-managed environment,you can choose `SpringTransaction` from `SpringTransactionFactory` in declarative transaction ,
which is defined with @Transactional and TransactionManager 
(commonly `DataSourceTransactionManager` when using EasyQuery).
In this condition, the template gets jdbc connection via `DataSourceUtils` provided by Spring.
Directly using `QueryTemplate` methods is ok, the transaction now is managed by Spring.
Also in non-transactional condition ,you can also use `beginTransaction`,`commit` and `rollback`.

5 DefaultQueryTemplateFactory
---

All `QueryTemplate` comes from `QueryTemplateFactory`, use `DefaultQueryTemplateFactory`
to config `EntityInfo` and `TransactionFactory` , 
and finally call `build` method to complete.<br>

6 Log
--

EasyQuery uses logback for logging. If SQLs are desired to be logged, set `DefaultQueryTemplateFactory`
defaultQueryTemplateFactory.config().setShowSql(true) to true , and logging level of `cn.deepmax.querytemplate.DefaultQueryTemplate` class to DEBUG.

7 Generator
--
This feature is no longer supported.

<br>
EasyQuery is now only tested on win10 (x64) with mysql 5.7.15, java 1.8. <br>
Any question or problem: woody@alumni.sjtu.edu.cn. <br>
2017.09~2017.10
