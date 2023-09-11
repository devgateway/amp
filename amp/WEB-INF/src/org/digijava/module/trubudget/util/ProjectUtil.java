package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

public class ProjectUtil {
    public static void createProject(AmpActivityVersion ampActivityVersion)
    {
        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token =(String) myCache.get("loginToken");
        System.out.println("Token:"+token);
    }
}
