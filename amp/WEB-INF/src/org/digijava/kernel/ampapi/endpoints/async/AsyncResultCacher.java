/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.async;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import org.apache.log4j.Logger;

/**
 * @author Viorel Chihai
 */
public class AsyncResultCacher {
    
    private static Logger logger = Logger.getLogger(AsyncResultCacher.class);
    
    protected static final AsyncResultCacher INSTANCE = new AsyncResultCacher();
    
    /** the maximum number of minutes to keep a request result in memory after generation */
    public static final int ENTRY_EXPIRATION_MINUTES = 30;
    
    /** the maximum number of requests to keep in cache */
    public static final int MAXIMUM_CACHE_SIZE = 50;
    
    protected Map<String, AsyncResult> lru =
            (Map) CacheBuilder.newBuilder().softValues()
                    .maximumSize(MAXIMUM_CACHE_SIZE)
                    .expireAfterWrite(ENTRY_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                    .build().asMap();
    
    protected AsyncResultCacher() {
    }
    
    /**
     * adds a new entry to the map (overwriting previous value at the same token value, if one exists).
     * In case resultId is null, the call does nothing.
     *
     * @param resultId
     * @param cachedAsyncResult
     */
    public void addCachedAsyncResult(String resultId, AsyncResult cachedAsyncResult) {
        if (cachedAsyncResult != null && resultId != null) {
            logger.info("caching results with id " + resultId);
            lru.put(resultId, cachedAsyncResult);
        }
    }
    
    /**
     * returns the api import result stored at the given result id. In case of null, always reports null
     *
     * @param resultId
     * @return
     */
    public AsyncResult getCachedAsyncResult(String resultId) {
        if (resultId == null) {
            return null;
        }
        
        AsyncResult res = lru.get(resultId);
        
        if (res == null) {
            logger.info("not found result with id " + resultId);
        }
        
        return res;
    }
    
    public AsyncResult deleteCachedAsyncResults(String resultId) {
        return lru.remove(resultId);
    }
    
    public static AsyncResult getAsyncResult(String resultId) {
        return INSTANCE.getCachedAsyncResult(resultId);
    }
    
    /**
     * please see {@link #addCachedAsyncResult(String, AsyncResult)}
     * @param resultId
     * @param asyncResult
     */
    public static void addAsyncResultsData(String resultId, AsyncResult asyncResult) {
        if (resultId == null) {
            return; // never cache nulls
        }
        
        INSTANCE.addCachedAsyncResult(resultId, asyncResult);
    }
    
    public static void deleteCachedAsyncResult(String resultId) {
        INSTANCE.deleteCachedAsyncResults(resultId);
    }   
}
