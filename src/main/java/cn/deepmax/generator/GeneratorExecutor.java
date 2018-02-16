
package cn.deepmax.generator;

import cn.deepmax.exception.EasyQueryException;
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
public class GeneratorExecutor {


    private Config config;
    public static final Logger logger = LoggerFactory.getLogger(GeneratorExecutor.class);
    public static final String FLAG = "//#EasyQueryGeneratorV1Hash";
    public GeneratorExecutor(Config config) {
        this.config = config;
    }

    public void generateIfNecessary(DatabaseMetaData dbMetaData, String javaFileName){
        int newHash = dbMetaData.getHash();
        String targetFileName = getTargetFilePath(dbMetaData, javaFileName);
        boolean needUpdate = needUpdate(targetFileName, newHash);
        if(needUpdate){
            logger.debug("[EasyQueryGenerator]Try generating java file -> "+ javaFileName);
            FreemarkerTemplateClassData data = FreemarkerTemplateClassData.instance(dbMetaData,javaFileName,config);
            doGenerate(targetFileName, data);
            logger.debug("[EasyQueryGenerator]End generating java file -> "+ javaFileName);
        }else{
            logger.debug("[EasyQueryGenerator]Skip generating class -> "+ javaFileName);
        }
    }

    /**
     *
     * @param metaData
     * @param javaFileName
     * @return return java path.
     */
    private String getTargetFilePath(DatabaseMetaData metaData, String javaFileName){
        if(StringUtils.isEmpty(metaData.getTableName())){
            return config.getValueObjectPath() + javaFileName+".java";      //value object
        }else{
            return config.getEntityPath() + javaFileName+".java";           //entity
        }
    }

    /**
     * to see whether this file needUpdate.
     * generated file startWith //#EasyQueryV1Hash=XXXX
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

    private static void doGenerate(String outputFileName, FreemarkerTemplateClassData data)  {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        OutputStream out = null;
        try  {
            cfg.setClassForTemplateLoading(GeneratorExecutor.class,"/template");
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
