/**
 * 
 */
package org.dgfoundation.amp.reports;

import com.google.common.cache.CacheBuilder;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Report output cacher that uses a Guava-powered LRU softmap.
 * The key of the report is a String <strong>token</strong> which is used as a old-style HashMap key with <i>special treatment of null keys</i>: null-keyed values are not saved to the cache. Naturally, null-keyed gets get a "miss" back.
 * @author Constantin Dolghier
 */
public class ReportCacher {
    private static Logger logger = Logger.getLogger(ReportCacher.class);
    
    protected final static ReportCacher instance = new ReportCacher();
    
    /** the maximum number of minutes to keep a report in memory after generation */
    public final static int ENTRY_EXPIRATION_MINUTES = 10;
    
    /** the maximum number of reports to keep in cache */
    public final static int MAXIMUM_CACHE_SIZE = 50;
    
    /** the frequency (once in how many cache get calls) with which to report cache hit statistics in the logs. <= 0: do not */
    public final static int CACHE_REPORTING_FREQUENCY = 40;
    
    protected Map<String, CachedReportData> lru = (Map) CacheBuilder.newBuilder().softValues().maximumSize(MAXIMUM_CACHE_SIZE).expireAfterWrite(ENTRY_EXPIRATION_MINUTES, TimeUnit.MINUTES).build().asMap();
    protected long calls;
    protected long hits;
    
    protected ReportCacher() {
    }
    
    /**
     * adds a new entry to the map (overwriting previous value at the same token value, if one exists). In case reportToken is null, the call does nothing
     * @param reportToken
     * @param cachedReportData
     */
    public void addCachedReportData(String reportToken, CachedReportData cachedReportData) {
        if (cachedReportData != null && reportToken != null) {
            logger.info("caching report with token " + reportToken);
            lru.put(reportToken, cachedReportData);
        }
    }
    
    /**
     * returns the report stored at the given reportToken. In case of null, always reports null
     * @param reportToken
     * @return
     */
    public CachedReportData getCachedReportData(String reportToken) {
        if (reportToken == null)
            return null;
        calls ++;
        CachedReportData res = lru.get(reportToken);
        if (res == null) {
            logger.info("not found report with token " + reportToken);
        }
        else {
            hits ++;
        }
        if (CACHE_REPORTING_FREQUENCY > 0 && calls % CACHE_REPORTING_FREQUENCY == 0 && calls > 0)
            logger.info(String.format("had %d calls, of which %d hits (%.2f percent hit rate)", calls, hits, hits * 100.0 / calls));
        return res;
    }
    
    public CachedReportData deleteCachedReportData(String reportToken) {
        return lru.remove(reportToken);
    }
    
    /**
     * please see {@link #getCachedReportData(String)}
     * @param reportToken
     * @return
     */
    public static CachedReportData getReportData(String reportToken) {
        if (reportToken == null)
            return null;
        return instance.getCachedReportData(reportToken);
    }
    
    /**
     * please see {@link #addCachedReportData(String, CachedReportData)}
     * @param reportToken
     * @param cachedReportData
     */
    public static void addReportData(String reportToken, CachedReportData cachedReportData) {
        if (reportToken == null)
            return; // never cache nulls
        instance.addCachedReportData(reportToken, cachedReportData);
    }
    
    public static void deleteReportData(String reportToken) {
        instance.deleteCachedReportData(reportToken);
    }   
}
