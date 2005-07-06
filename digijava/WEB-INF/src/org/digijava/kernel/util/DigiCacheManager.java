/*
 *   DigiCacheManager.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Dec 22, 2003
 *	 CVS-ID: $Id: DigiCacheManager.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.util;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import net.sf.swarmcache.CacheFactory;
import net.sf.swarmcache.CacheConfiguration;
import net.sf.swarmcache.CacheConfigurationManager;
import java.util.Iterator;
import net.sf.swarmcache.ObjectCache;
import org.apache.log4j.Logger;

public class DigiCacheManager {

    private static Logger logger = Logger.getLogger(DigiCacheManager.class);

    private Map caches;
    private CacheFactory factory;
    private static DigiCacheManager instance;

    /**
     * Returns DigiCacheManager class instance
     * @return DigiCacheManager class instance
     */
    public static synchronized DigiCacheManager getInstance() {
        if (instance == null) {
            instance = new DigiCacheManager();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private DigiCacheManager() {
        caches = new HashMap();
        CacheConfiguration conf = CacheConfigurationManager.getConfig();
        factory = new CacheFactory(conf);
    }

    /**
     * Returns cache for particular region. Creates new one if cache does
     * not exist
     * @param region name of the cache region
     * @return cache for particular region
     */
    public synchronized ObjectCache getCache(String region) {
        ObjectCache result = (ObjectCache) caches.get(region);
        if (result == null) {
            result = factory.createCache(region);
            caches.put(region, result);
        }

        return result;
    }

    /**
     * Perform clean shutdown of all caches
     */
    public static synchronized void shutdown() {
        logger.debug("shutdown() called");
        if (instance == null) {
            throw new IllegalStateException(
                "CacheManager was not initialized or is already shut down");
        }
        try {
            Iterator iter = instance.caches.values().iterator();
            while (iter.hasNext()) {
                ObjectCache cache = (ObjectCache) iter.next();
                try {
                    cache.destroy();
                }
                catch (Exception ex) {
                    logger.warn("Cache region destruction failed", ex);
                }
            }
        }
        finally {
            instance.factory.shutdown();
            instance = null;
        }
    }
}