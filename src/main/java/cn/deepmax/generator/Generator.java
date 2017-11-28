//#EasyQueryGeneratorV1Hash-12356
package cn.deepmax.generator;
import cn.deepmax.annotation.SelfManage;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.ClassMetaData;
import cn.deepmax.model.Config;
import cn.deepmax.model.DbMetaData;
import cn.deepmax.model.TemplateData;
import cn.deepmax.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * main class for code generator
 */
public class Generator {


    private Config config;
    public static final String FLAG = "//#EasyQueryGeneratorV1Hash";
    public Generator(Config config) {
        this.config = config;
    }

    public void generateIfNecessaryAsyn(DbMetaData dbMetaData,Class<?> clazz){
        new Thread(()->generateIfNecessary(dbMetaData, clazz));
    }

    public void generateIfNecessary(DbMetaData dbMetaData, Class<?> clazz){
        int newHash = dbMetaData.getHash();
        String targetFileName = getTargetFilePath(dbMetaData,clazz);
        boolean isSelfManage = isSelfManaged(clazz);
        boolean needUpdate = needUpdate(targetFileName, newHash);
        if(!isSelfManage && needUpdate){
            TemplateData data = TemplateData.instance(dbMetaData,clazz,config);
            doGenerate(targetFileName, data);
        }
    }

    /**
     * whether the target is selfManaged
     * @param clazz
     * @return
     */
    private boolean isSelfManaged(Class<?> clazz){
        SelfManage selfManage = clazz.getAnnotation(SelfManage.class);
        return (selfManage!=null);
    }

    /**
     *
     * @param metaData
     * @param clazz
     * @return return java path.
     */
    private String getTargetFilePath(DbMetaData metaData, Class<?> clazz){
        String pa = getPackageAndFilePath(clazz);
        if(StringUtils.isEmpty(metaData.getTableName())){   //value object
            return config.getValueObjectPath()+pa;
        }else{
            //entity
            return config.getEntityPath()+pa;
        }
    }

    private String getPackageAndFilePath(Class<?> clazz){
        String className = clazz.getName();
        String path;
        if(File.separator.equals("\\")){        //windows
            path = className.replaceAll("\\.", "\\\\");
        }else{              //linux
            path = className.replaceAll("\\.", "/");
        }

        return path+".java";
    }


    /**
     * generated file startwith //#EasyQueryV1Hash=XXXX
     * @param filePath
     * @return
     */
    private boolean needUpdate(String filePath,int newHash){
        File file = new File(filePath);
        if(!file.exists()){
            return true;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String firstLine = reader.readLine();
            String rightLine = FLAG + newHash;
            return !(StringUtils.isNotEmpty(firstLine) && firstLine.equals(rightLine));
        } catch (Exception e) {
            return false;       //exception happen ,do nothing.
        } finally {
            if(reader!=null) try {
                reader.close();
            } catch (IOException e) {

            }
        }
    }


    public static void main(String[] args) throws Exception {
        String name = "D:\\test\\vo\\model\\cn\\deepmax\\a.txt";
        File f = new File(name);
        if(!f.exists()) f.createNewFile();
    }

    public static void doGenerate(String outputFileName, TemplateData data)  {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        String path= Generator.class.getResource("/template").getPath();
        OutputStream out = null;
        try  {
            cfg.setDirectoryForTemplateLoading(new File(path)) ;
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template template = cfg.getTemplate("Class.ftl");
            File f = new File(outputFileName);
            File parent = f.getParentFile();
            if(!parent.exists()) parent.mkdirs();
            if(!f.exists()) f.createNewFile();
            out = new FileOutputStream(f);
            Writer outW = new OutputStreamWriter(out);
            template.process(data,outW);
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


    private static void createNewFile(File file,List<File> folders){

    }

}
