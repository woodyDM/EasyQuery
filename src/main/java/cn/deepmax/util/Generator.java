package cn.deepmax.util;

import cn.deepmax.exception.EasyQueryException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Generator {


    public static void generateAsyn(String outputFileName){
        new Thread(()->doGenerate(outputFileName)).start();
    }


    public static void doGenerate(String outputFileName)  {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        String path=Generator.class.getResource("/template").getPath();
        OutputStream out = null;
        try  {

            cfg.setDirectoryForTemplateLoading(new File(path)) ;
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template template = cfg.getTemplate("Class.ftl");
            File f = new File(outputFileName);
            out = new FileOutputStream(f);
            Writer outW = new OutputStreamWriter(out);
            template.process(getData(),outW);
            out.flush();
        } catch (IOException e) {
            throw new EasyQueryException("File not found in code generator",e);
        } catch (TemplateException e) {
            throw new EasyQueryException("Exception in code generator.",e);
        } finally {
            if(out!=null) try {
                out.close();
            } catch (IOException e) {

            }
        }

    }


    private static Map<String,Object> getData(){
        Map<String,Object> obj = new HashMap<>();
        obj.put("versionAndHashInfo","Hash2345");
        obj.put("packageName","cn.deepmax.entity");
        obj.put("isEntity",true);
        obj.put("className","User");
        obj.put("tableName","t_user");
        obj.put("catalogName","siteMail");
        List<ClassMetaData> list = new ArrayList<>();
        ClassMetaData m1 =new ClassMetaData("id","Long","setId","getId","id");
        ClassMetaData m2 =new ClassMetaData("userName","String","setUserName","getUserName","user_name");
        ClassMetaData m3 =new ClassMetaData("createTime","java.sql.Timestamp","setCreateTime","getCreateTime","create_time");
        list.add(m1);
        list.add(m2);
        list.add(m3);
        obj.put("columns",list);
        return obj;
    }


}
