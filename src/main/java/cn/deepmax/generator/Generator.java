
package cn.deepmax.generator;

import cn.deepmax.annotation.SelfManage;
import cn.deepmax.exception.EasyQueryException;
import cn.deepmax.model.Config;
import cn.deepmax.model.DatabaseMetaData;
import cn.deepmax.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * main class for code generator
 */
public class Generator {


    private Config config;
    public static final String FLAG = "//#EasyQueryGeneratorV1Hash";
    public Generator(Config config) {
        this.config = config;
    }

    private static final Logger logger = LoggerFactory.getLogger(Generator.class);


    public void generateIfNecessary(DatabaseMetaData dbMetaData, Class<?> clazz){
        int newHash = dbMetaData.getHash();
        String targetFileName = getTargetFilePath(dbMetaData,clazz);
        boolean isSelfManage = isSelfManaged(clazz);
        boolean needUpdate = needUpdate(targetFileName, newHash);
        if(!isSelfManage && needUpdate){
            logger.debug("[EasyQueryGenerator]Try generating java file for class "+clazz.getName());
            FreemarkerTemplateClassData data = FreemarkerTemplateClassData.instance(dbMetaData,clazz,config);
            doGenerate(targetFileName, data);
            logger.debug("[EasyQueryGenerator]End generating java file for class "+clazz.getName());
        }else{
            logger.debug("[EasyQueryGenerator]Skip generating class "+clazz.getName());
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
    private String getTargetFilePath(DatabaseMetaData metaData, Class<?> clazz){
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



    public static void doGenerate(String outputFileName, FreemarkerTemplateClassData data)  {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        OutputStream out = null;
        try  {
            cfg.setClassForTemplateLoading(Generator.class,"/template");
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



}
