package org.digijava.kernel.cache;

import java.io.Serializable;
import java.util.Map;

/**
 * The implementing classes should provide general cache access/operations.
 * 
 * @author Catalin Andrei
 * @since Dec 12, 2007
 */
public interface AbstractCache {

    /**
     * @return the cache's name
     */
    public String getName();

    /**
     * @return the type of the cache (usually, the name of the underlying cache provider)
     */
    public String getType();
    
    /**
     * @return the size in number of entries of the cache
     */
    public int getSize();

    /**
     * Clears all the cached entries.
     */
    public void clear();

    /**
     * @return an unmodifiable Map equivalent 
     */
    public Map toMap();

    /**
     * @return the number of "get" operations performed since creation or reset.
     */
    public long getNumberOfGets();

    /**
     * @return the number of "put" operations performed since creation or reset.
     */
    public long getNumberOfPuts();
    
    /**
     * @param key
     * @param value
     */
    public void put(Serializable key, Object value);
    
    /**
     * @param key
     * @return
     */
    public Object get(Serializable key);
        
    /**
     * @param key
     */
    public void evict(Serializable key);
}
