只支持 int Integer Long long BigInteger 作为主键,且主键为自增
如果使用DefaultSqlTranslator,数据库结构和实体必须一一对应
目前只测试了mysql
建议实体类都用包装类型
javabean default constructor
//TODO 还需要写一个用注解的实体管理器
test:
http://blog.csdn.net/hfmbook/article/details/54578137
typeAdapter 类型支持

实体操作只支持自增，
@Column只支持在getter & field上
默认的mapper要改

@Transactional
@Bean
@EnableTransactionManagement

