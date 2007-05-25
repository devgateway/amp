package net.sf.hibernate.cache;

import net.sf.swarmcache.ObjectCache;

import java.io.Serializable;

/**
 * SwarmCache
 * @author Jason Carreira
 * Created Sep 19, 2003 9:03:26 PM
 */
public class SwarmCache implements Cache {
    private ObjectCache cache;

    public SwarmCache(ObjectCache cache) {
        this.cache = cache;
    }

    /**
     * Get an item from the cache
     * @param key
     * @return the cached object or <tt>null</tt>
     * @throws CacheException
     */
    public Object get(Object key) throws CacheException {
        if (key instanceof Serializable) {
            return cache.get((Serializable)key);
        } else {
            throw new CacheException("Keys must implement Serializable");
        }
    }

    /**
     * Add an item to the cache
     * @param key
     * @param value
     * @throws CacheException
     */
    public void put(Object key, Object value) throws CacheException {
        if (key instanceof Serializable) {
            cache.put((Serializable)key, value);
        } else {
            throw new CacheException("Keys must implement Serializable");
        }
    }

    /**
     * Remove an item from the cache
     */
    public void remove(Object key) throws CacheException {
        if (key instanceof Serializable) {
            cache.clear((Serializable)key);
        } else {
            throw new CacheException("Keys must implement Serializable");
        }
    }

    /**
     * Clear the cache
     */
    public void clear() throws CacheException {
        cache.clearAll();
    }

    /**
     * Clean up
     */
    public void destroy() throws CacheException {
        // destroy() instead of clearAll(). Mikheil Kapanadze
        cache.destroy();
    }

    /**
     * If this is a clustered cache, lock the item
     */
    public void lock(Object key) throws CacheException {
        //there is no locking
    }

    /**
     * If this is a clustered cache, unlock the item
     */
    public void unlock(Object key) throws CacheException {
        //there is no locking
    }

    /**
     * Generate a timestamp
     */
    public long nextTimestamp() {
        return Timestamper.next();
    }

    /**
     * Get a reasonable "lock timeout"
     */
    public int getTimeout() {
        return Timestamper.ONE_MS * 60000;
    }
}
