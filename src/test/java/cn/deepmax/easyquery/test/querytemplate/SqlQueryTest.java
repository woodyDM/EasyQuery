package cn.deepmax.easyquery.test.querytemplate;

import cn.deepmax.easyquery.querytemplate.DefaultSqlQuery;
import cn.deepmax.easyquery.querytemplate.QueryTemplate;
import cn.deepmax.easyquery.querytemplate.SqlQuery;
import cn.deepmax.easyquery.test.BaseTest;
import cn.deepmax.easyquery.test.adapter.EnumType;
import cn.deepmax.easyquery.test.adapter.MyColor;
import cn.deepmax.easyquery.test.model.SuperUser;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

public class SqlQueryTest extends BaseTest {

    @Test
    public void testSqlQueryNoWhere(){
        QueryTemplate template = defaultFactory.create();

        DefaultSqlQuery<SuperUser> sqlQuery = DefaultSqlQuery.newInstance(SuperUser.class)
                .append("select * from super_user where ID = ?", 1);
        SuperUser user = template.select(sqlQuery);
        Assert.notNull(user,"user not null");

    }

    @Test
    public void testSqlQueryWhere(){
        QueryTemplate template = defaultFactory.create();

        SqlQuery<SuperUser> sqlQuery = DefaultSqlQuery.newInstance(SuperUser.class)
                .append("select * from super_user").where("ID > ?",1);
        List<SuperUser> user = template.selectList(sqlQuery);
        Assert.isTrue(user.size()>0,"user size>0");

    }

    @Test
    public void testSqlQueryWhereAppend(){
        QueryTemplate template = defaultFactory.create();

        SqlQuery<SuperUser> sqlQuery = DefaultSqlQuery.newInstance(SuperUser.class)
                .append("select * from super_user").where().append("id > ?",3);
        List<SuperUser> user = template.selectList(sqlQuery);
        Assert.isTrue(user.size()>0,"user size>0");

    }

    @Test
    public void testSqlQueryWhereAppendAnd(){
        QueryTemplate template = defaultFactory.create();

        SqlQuery<SuperUser> sqlQuery = DefaultSqlQuery.newInstance(SuperUser.class)
                .append("select * from super_user").where().append("id > ?",3).or()
                .append("AUTH != ?", EnumType.TYPE1.toString());
        List<SuperUser> user = template.selectList(sqlQuery);
        Assert.isTrue(user.size()>0,"user size>0");

    }

    @Test
    public void testSqlQueryWhereAppendAndTimes(){
        QueryTemplate template = defaultFactory.create();

        SqlQuery  sqlQuery = DefaultSqlQuery.newInstance( )
                .append("select * from super_user").where().ifNotEmpty(null,"auth = ?", 1 )
                .and().ifNotNull(MyColor.RED.ordinal(),"color2 = ?",1);
        List<Map<String, Object>> user = template.selectListMap(sqlQuery);
        Assert.isTrue(user.size()>0,"user size>0");

    }


    @Test
    public void testSqlQueryWhereCount(){
        QueryTemplate template = defaultFactory.create();
        DefaultSqlQuery.DefaultWhereQuery<Long> sqlQuery = DefaultSqlQuery.newInstance(Long.class)
                .append("select count(*) from super_user ").where("auth = ?", EnumType.TYPE1.toString());
        Long count = template.selectScalar(sqlQuery);
        Assert.isTrue(count>0,"  size>0");
    }
}
