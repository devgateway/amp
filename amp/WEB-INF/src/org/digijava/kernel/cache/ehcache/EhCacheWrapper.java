package org.digijava.kernel.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.digijava.kernel.cache.AbstractCache;

import java.io.Serializable;
import java.util.*;

/**
 * Simple EHCache wrapper.
 * 
 * @author Catalin Andrei
 * @since Dec 18, 2007
 */
public class EhCacheWrapper implements AbstractCache {
    
    private final static String CACHE_TYPE = "EHCache";
    
    private String cacheName;
    
    /**
     * Handles operations to the given cache region.
     */
    public EhCacheWrapper(String cache) {
        this.cacheName = cache;
    }
    
    /**
     * @see org.digijava.kernel.cache.AbstractCache#clear()
     */
    public void clear() {
        getCache().removeAll();
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#evict(java.io.Serializable)
     */
    public void evict(Serializable key) {
        getCache().remove(key);
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#get(java.io.Serializable)
     */
    public Object get(Serializable key) {
        Element element = getCache().get(key);
        if (element == null) {
            return null;
        }
        return element.getObjectValue();
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#getName()
     */
    public String getName() {
        return this.cacheName;
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#getNumberOfGets()
     */
    public long getNumberOfGets() {
        Cache cache = getCache();
        return cache.getStatistics().getCacheHits() + cache.getStatistics().getCacheMisses();
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#getNumberOfPuts()
     */
    public long getNumberOfPuts() {
        return getCache().getStatistics().getObjectCount();
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#getSize()
     */
    public int getSize() {
        return getCache().getSize();
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#getType()
     */
    public String getType() {
        return CACHE_TYPE;
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#put(java.io.Serializable, java.lang.Object)
     */
    public void put(Serializable key, Object value) {
        getCache().put(new Element(key, value));
    }

    /**
     * @see org.digijava.kernel.cache.AbstractCache#toMap()
     */
    public Map toMap() {
        List keys = getCache().getKeys();
        Map elements = new HashMap();
        if (keys != null) {
            for(Iterator iter = keys.iterator(); iter.hasNext(); ) {
                Object key = iter.next();
                elements.put(key, getCache().get(key));
            }
        }
        return Collections.unmodifiableMap( elements );
    }
    
    private static Object ehCacheSyncObject = new Object();
    
    /**
     * @return the current cache handler
     */
    private Cache getCache() {
        synchronized(ehCacheSyncObject) {
            Cache cache = CacheManager.getInstance().getCache(this.cacheName);
            if (cache == null) {
                CacheManager.getInstance().addCache(this.cacheName);
                cache = CacheManager.getInstance().getCache(this.cacheName);
                if (cache == null) {
                    throw new IllegalStateException("Couldn't create cache: " + this.cacheName);
                }
            }       
            return cache;
        }
    }
}
