只支持 int Integer Long long BigInteger 作为主键,且主键为自增
如果使用DefaultSqlTranslator,数据库结构和实体必须一一对应
目前只测试了mysql

//TODO 还需要写一个用注解的实体管理器
