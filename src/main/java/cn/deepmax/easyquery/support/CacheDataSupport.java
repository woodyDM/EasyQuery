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


    protected Map<ID , D> cacheData = new ConcurrentHashMap<>(128);
    public static final Logger logger = LoggerFactory.getLogger(CacheDataSupport.class);


    abstract public D load(ID uniqueKey) throws Exception;

    protected <V> V loadThen(ID uniqueKey, Function<D,V> action){
        cacheData.computeIfAbsent(uniqueKey, (v)->{
            logger.debug("Try to load data  {}  in class {}", uniqueKey, this.getClass().getName());
            try{
                D theData = load(uniqueKey);
                if(theData==null){
                    throw new IllegalStateException("Unable to cache null data, the load MUST NOT return null value.");
                }
                return theData;
            }catch (Exception e){
                if(e instanceof EasyQueryException){
                    throw (EasyQueryException)e;
                }else{
                    throw new EasyQueryException("Fail to load data of type:"+uniqueKey.toString(), e);
                }
            }
        });
        D data = cacheData.get(uniqueKey);
        if(data==null){
            throw new IllegalStateException("Get null from cache , probably bug. Please contact author.");
        }
        return action.apply(data);

    }


}
