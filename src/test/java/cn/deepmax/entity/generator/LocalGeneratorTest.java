package cn.deepmax.entity.generator;

import cn.deepmax.entity.BaseTest;

public class LocalGeneratorTest extends BaseTest{
//
//
//    @Resource(name = "localSpringFactory")
//    QueryTemplateFactory localFactory;
//    @Test
//    public void generateTest(){
//        QueryTemplate queryTemplate = localFactory.create();
//        Assert.notNull(queryTemplate,"not null");
//        String sql = "select * from common_website where type = ? ";
//        List<RowRecord<CommonWebsite>> list = queryTemplate.selectListEx(sql, CommonWebsite.class,3);
//        List<CommonWebsite> list2 = queryTemplate.selectList(sql,CommonWebsite.class, 1);
//        Assert.notNull(list,"list not null");
//
//    }
//
//    @Test
//    public void afterGenerateSelectTest(){
//        QueryTemplate queryTemplate = localFactory.create();
//        CommonWebsite s = queryTemplate.get(CommonWebsite.class,1);
//        Assert.notNull(s,"s not null");
//        s.setDelFlag(true);
//        s.setWebName("36022");
//        queryTemplate.save(s);
//
//    }
//
//    @Test
//    public void afterGenerateDeleteTest(){
//        QueryTemplate queryTemplate = localFactory.create();
//
//        CommonWebsite s2 = queryTemplate.get(CommonWebsite.class,15);
//        Assert.isNull(s2,"s is null");
//        s2 = new CommonWebsite();
//        s2.setWebName("test");
//        s2.setUrl("url..test");
//        s2.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        s2.setType(1);
//        s2.setDelFlag(true);
//        queryTemplate.save(s2);
//
//    }
//
//    @Test
//    public  void testVOGenerate(){
//        QueryTemplate template = localFactory.create();
//        String sql = "SELECT C.*,U.name user_name from common_website C INNER JOIN " +
//                "superuser U on U.id = C.id ";
//        List<WebSiteVO> webSiteVOS = template.selectList(sql,WebSiteVO.class);
//        Assert.notNull(webSiteVOS,"is not null");
//        WebSiteVO v = webSiteVOS.get(0);
//
//    }
}
