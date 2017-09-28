package org.digijava.kernel.cache.ehcache;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.CacheManager;

/**
 * This implementation will work with EHCache. 
 * 
 * @author Catalin Andrei
 * @since Dec 13, 2007
 */
public class CacheManagerImpl implements CacheManager {

    /**
     * @see org.digijava.kernel.cache.CacheManager#getCache(java.lang.String)
     */
    public AbstractCache getCache(String name) {
        return new EhCacheWrapper(name);
    }

    /**
     * @see org.digijava.kernel.cache.CacheManager#getAvailableCaches()
     */
    public List<AbstractCache> getAvailableCaches() {
        List<AbstractCache> caches = new ArrayList<AbstractCache>();
        String []cacheNames = net.sf.ehcache.CacheManager.getInstance().getCacheNames();
        
        if (cacheNames != null) {
            for (String name : cacheNames) {
                caches.add( getCache(name) );
            }
        }
        
        return caches;
    }

}
