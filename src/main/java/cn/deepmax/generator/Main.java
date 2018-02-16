package cn.deepmax.generator;

import cn.deepmax.generator.mapper.LowerUnderlineToCamelMapper;
import cn.deepmax.generator.mapper.Mapper;

public class Main {

    public static void main(String[] args) {
        fromSqlGenerator();
    }

    public static void mysqlTableGenerator(){
        Config.Builder builder = new Config.Builder();
        Mapper mapper = new LowerUnderlineToCamelMapper();
        Config config = builder.setDatabaseDriver("com.mysql.jdbc.Driver")
                .setDatabasePassword("")
                .setDatabaseUserName("root")
                .setDatabaseUrl("jdbc:mysql://localhost:3306/test")
                .setPackageName("cn.deepmax.entity")
                .setEntityPath("/E:/test/po")
                .setValueObjectPath("/E:/test/vo")
                .setToClassNameMapper(mapper)
                .setToFieldNameMapper(mapper)
                .build();
        MysqlTableGenerator generator = new MysqlTableGenerator(config,"shoppingh");
        generator.addTables("db_types");
        generator.generate();
    }


    public static void fromSqlGenerator(){
        Config.Builder builder = new Config.Builder();
        Mapper mapper = new LowerUnderlineToCamelMapper();
        Config config = builder.setDatabaseDriver("com.mysql.jdbc.Driver")
                .setDatabasePassword("")
                .setDatabaseUserName("root")
                .setDatabaseUrl("jdbc:mysql://localhost:3306/shoppingh")
                .setPackageName("cn.deepmax.entity")
                .setEntityPath("/E:/test/po")
                .setValueObjectPath("/E:/test/vo")
                .setToClassNameMapper(mapper)
                .setToFieldNameMapper(mapper)
                .build();
        FromSqlGenerator generator = new FromSqlGenerator(config);
        String sql = "SELECT p.user_id,p.myTime,p.adress,u.email FROM  t_page p  INNER JOIN t_user u on p.user_id = u.id\n" +
                "where p.id < ? ";
        generator.generate("JoinTest",sql,-1);
        String sql2 = "select * from db_types limit 1";
        generator.generate("TestJav",sql2);

    }


}
