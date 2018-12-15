package cn.deepmax.easyquery.support;

import cn.deepmax.easyquery.exception.EasyQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * provide cache support for type <D> using unique key <ID>
 * @param <ID>
 * @param <D>
 */
public abstract class CacheDataSupport<ID,D> {


    protected Map<ID, D> cacheData = new ConcurrentHashMap<>(128);
    public static final Logger logger = LoggerFactory.getLogger(CacheDataSupport.class);


    abstract public D load(ID uniqueKey) throws Exception;

    protected <V> V loadThen(ID uniqueKey, Function<D,V> action){
        if(!cacheData.containsKey(uniqueKey)){
            synchronized (uniqueKey.toString().intern()){   //? really effect
                if(!cacheData.containsKey(uniqueKey)){
                    try{
                        logger.debug("Try to load data "+uniqueKey+" in class ["+this.getClass()+"]");
                        D theData= load(uniqueKey);
                        if(theData!=null){
                            cacheData.putIfAbsent(uniqueKey, theData);
                            logger.debug("Load data complete , uniqueKey is  ["+uniqueKey+"] in class ["+this.getClass()+"]");
                        }else{
                            throw new EasyQueryException("load(ID target) should always return a non-null instance. ");
                        }
                    }catch (Exception e){
                        if(e instanceof EasyQueryException){
                            throw (EasyQueryException)e;
                        }else{
                            throw new EasyQueryException("Fail to load data of type:"+uniqueKey.toString(), e);
                        }
                    }
                }
            }
        }
        D data = cacheData.get(uniqueKey);
        if(data==null){
            throw new IllegalStateException("Unable to putIfAbsent data, probably bug. Please contact author.");
        }
        return action.apply(data);
    }


}
