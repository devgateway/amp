package org.digijava.kernel.cache;

import java.util.List;

/**
 * The implementing classes should provide region cache access.
 *  
 * @author Catalin Andrei
 * @since Dec 13, 2007
 */
public interface CacheManager {
    
    /**
     * @param name
     * @return a cache handler; if that cache doesn't exist, a new one will be created.
     */
    public AbstractCache getCache(String name);
    
    /**
     * @return a list of available caches
     */
    public List<AbstractCache> getAvailableCaches();
}
