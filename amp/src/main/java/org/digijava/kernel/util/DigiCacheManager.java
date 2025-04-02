/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.CacheManager;
import org.digijava.kernel.cache.ehcache.CacheManagerImpl;
import org.digijava.module.aim.action.GetAvailableUsersForWorkspaceJSON;

public class DigiCacheManager {

    private static Logger logger = Logger.getLogger(DigiCacheManager.class);
    
    private static CacheManager cacheManager;
    private static DigiCacheManager instance;

    /**
     * Returns DigiCacheManager class instance
     * @return DigiCacheManager class instance
     */
    public static synchronized DigiCacheManager getInstance() {
        if (instance == null) {
            cacheManager = new CacheManagerImpl();
            instance = new DigiCacheManager();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private DigiCacheManager() {
    }

    /**
     * Returns cache for particular region. Creates new one if cache does
     * not exist
     * @param region name of the cache region
     * @return cache for particular region
     */
    public synchronized AbstractCache getCache(String region) {
        
        if (cacheManager != null) {
            AbstractCache cache = cacheManager.getCache(region); 
            return cache;
        } else {
            throw new IllegalStateException("cacheManager isn't configured");
        }
    }

    /**
     * @return a list of available caches
     */
    public List<AbstractCache> getCaches() {
        if (cacheManager != null) {
            return cacheManager.getAvailableCaches();
        } else {
            throw new IllegalStateException("cacheManager isn't configured");
        }
    }
    
    /**
     * Perform clean shutdown of all caches
     */
    public static synchronized void shutdown() {
         net.sf.ehcache.CacheManager.getInstance().shutdown();      
    }
}
