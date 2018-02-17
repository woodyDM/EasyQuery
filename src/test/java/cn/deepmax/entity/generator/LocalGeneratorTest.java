package cn.deepmax.entity.generator;

import cn.deepmax.entity.BaseTest;
import cn.deepmax.generator.Config;
import cn.deepmax.generator.FromSqlGenerator;
import cn.deepmax.generator.MysqlTableGenerator;
import cn.deepmax.generator.mapper.LowerUnderlineToCamelMapper;
import cn.deepmax.generator.mapper.LowerUnderlineToPascalMapper;
import cn.deepmax.generator.mapper.Mapper;
import org.junit.Test;

public class LocalGeneratorTest {

//    @Test
//    public void mysqlTableGenerator(){
//        Config.Builder builder = new Config.Builder();
//        Mapper fieldMapper = new LowerUnderlineToCamelMapper();
//        Mapper classNameMapper = new LowerUnderlineToPascalMapper();
//        Config config = builder.setDatabaseDriver("com.mysql.jdbc.Driver")
//                .setDatabasePassword("")
//                .setDatabaseUserName("root")
//                .setDatabaseUrl("jdbc:mysql://localhost:3306/test")
//                .setPackageName("cn.deepmax.entity")
//                .setEntityPath("/E:/test/po")
//                .setValueObjectPath("/E:/test/vo")
//                .setToClassNameMapper(classNameMapper)
//                .setToFieldNameMapper(fieldMapper)
//                .build();
//        MysqlTableGenerator generator = new MysqlTableGenerator(config,"shoppingh");
//        generator.addTables("db_types");
//        generator.generate();
//    }
//
//    @Test
//    public void fromSqlGenerator(){
//        Config.Builder builder = new Config.Builder();
//        Mapper fieldMapper = new LowerUnderlineToCamelMapper();
//        Mapper classNameMapper = new LowerUnderlineToPascalMapper();
//        Config config = builder.setDatabaseDriver("com.mysql.jdbc.Driver")
//                .setDatabasePassword("")
//                .setDatabaseUserName("root")
//                .setDatabaseUrl("jdbc:mysql://localhost:3306/shoppingh")
//                .setPackageName("cn.deepmax.entity")
//                .setEntityPath("/E:/test/po")
//                .setValueObjectPath("/E:/test/vo")
//                .setToClassNameMapper(classNameMapper)
//                .setToFieldNameMapper(fieldMapper)
//                .build();
//        FromSqlGenerator generator = new FromSqlGenerator(config);
//        String sql = "SELECT p.user_id,p.myTime,p.adress,u.email FROM  t_page p  INNER JOIN t_user u on p.user_id = u.id\n" +
//                "where p.id < ? ";
//        generator.generate("JoinTest",sql,-1);
//        String sql2 = "select * from db_types limit 1";
//        generator.generate("TestJav",sql2);
//
//    }
}
