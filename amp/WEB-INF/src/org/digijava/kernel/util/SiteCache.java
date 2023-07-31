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

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.hibernate.Session;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SiteCache implements Runnable {

    private static Logger logger = I18NHelper.getKernelLogger(SiteCache.class);
    private static final String appScopeKey = SiteCache.class.getName();

    public class CachedSite {

        public CachedSite(Site site) {
            this.site = site;
            this.instances = new ArrayList();
            this.userLanguages = new ArrayList(site.getUserLanguages());
            this.translationLanguages = new ArrayList(site.getTranslationLanguages());
            this.setDefaultLanguage(site.getDefaultLanguage());
            this.sendAlertsToAdmin = site.getSendAlertsToAdmin();
            initLanguageCodes();

            instances.addAll(site.getModuleInstances());
            Collections.sort(instances, moduleInstanceComparator);
        }
        
        private void initLanguageCodes() {
            this.userLanguagesCodes = new ArrayList<String>(this.userLanguages.size());
            for (Locale locale : userLanguages) {
                userLanguagesCodes.add(locale.getCode());
            }
        }

        private Site site;
        private Site rootSite;
        private List instances;
        private Collection<Locale> userLanguages;
        private Collection<String> userLanguagesCodes;
        private Collection translationLanguages;
        private Locale defaultLanguage;
        private Boolean sendAlertsToAdmin;

        public Site getSite() {
            return site;
        }

        public Site getRootSite() {
            return rootSite;
        }

        public void setRootSite(Site rootSite) {
            this.rootSite = rootSite;
        }

        public List getInstances() {
            return instances;
        }

        public Collection<Locale> getUserLanguages() {
            return userLanguages;
        }

        public void setUserLanguages(Collection<Locale> userLanguages) {
            this.userLanguages = new ArrayList<Locale>(userLanguages);
        }
        
        public Collection<String> getUserLanguagesCodes() {
            return userLanguagesCodes;
        }

        public void setUserLanguagesCodes(Collection<String> userLanguagesCodes) {
            this.userLanguagesCodes = new ArrayList<String>(userLanguagesCodes);
        }

        public Collection getTranslationLanguages() {
            return translationLanguages;
        }

        public void setTranslationLanguages(Collection translationLanguages) {
            this.translationLanguages = new ArrayList(translationLanguages);
        }

        public Locale getDefaultLanguage() {
            return defaultLanguage;
        }

        public void setDefaultLanguage(Locale defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
        }

        public Boolean getSendAlertsToAdmin() {
            return sendAlertsToAdmin;
        }

        public void setSendAlertsToAdmin(Boolean sendAlertsToAdmin) {
            this.sendAlertsToAdmin = sendAlertsToAdmin;
        }

    }

    private static final Comparator<String> reverseStringComparator = new Comparator<String>() {
        public int compare(String s1,
                           String s2) {
           return -s1.compareTo(s2);
        }
    };

    public static final Comparator<ModuleInstance> moduleInstanceComparator = (i1, i2) -> {
        int result;
        result = i1.getModuleName().compareTo(i2.getModuleName());
        if (result == 0) {
            result = i1.getInstanceName().compareTo(i2.getInstanceName());
        }
        return result;
    };

    private java.util.List sharedInstances;
    private HashMap<Long, CachedSite> sites;
    private HashMap<String, CachedSite> sitesByStringId;
    private HashMap<String, SortedMap<String, SiteDomain>> siteDomains;
    private static SiteCache currentInstance;
    private volatile Long cacheVersion;
    private AbstractCache appScopeCache;

    public static SiteCache getInstance() {
        return currentInstance;
    }

    private SiteCache() {
        this.appScopeCache = DigiCacheManager.getInstance().getCache(Constants.APP_SCOPE_REGION);
        cacheVersion = new Long(1);
        try {
            load(false);
        }
        catch (DgException ex) {
            throw new RuntimeException("Unable to initialize site cache");
        }
    }

    private void handleVersioning() {
        Long versionFromCache = (Long) appScopeCache.get(appScopeKey);
        if (versionFromCache == null) {
            versionFromCache = new Long(0);
        }

        if (!cacheVersion.equals(versionFromCache)) {
            //Fire reload
            Thread loader = new Thread(this, "Site cache loader thread");
            loader.setDaemon(true);
            loader.start();
        }
    }



    public void load() throws DgException {
        load(false);
    }

    public void load(boolean silent) throws DgException {
        logger.debug("Reloading SiteCache. silent=" + silent);
        HashMap<Long, CachedSite> siteCache = new HashMap<Long, CachedSite>();
        HashMap<String, CachedSite> sitesByStringIdCache = new HashMap<String, CachedSite>();
        HashMap<String, SortedMap<String, SiteDomain>> siteDomainCache = new HashMap<String, SortedMap<String, SiteDomain>>();
        List<ModuleInstance> newSharedInstances = null;

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = " from " + ModuleInstance.class.getName() +
                                 " mi where mi.site is null";
            
            newSharedInstances = new ArrayList<ModuleInstance>(session.createQuery(queryString).list());

            Collections.sort(newSharedInstances, moduleInstanceComparator);

            queryString = "from " + SiteDomain.class.getName() + " sd left join fetch sd.site site " +
                          " left join fetch site.translationLanguages left join fetch site.userLanguages " +
                          " left join fetch site.countries";
            
            Iterator<SiteDomain> iter = session.createQuery(queryString).list().iterator();
            while (iter.hasNext()) {
                SiteDomain siteDomain = iter.next();

                String path = siteDomain.getSitePath() == null ? "" :
                    siteDomain.getSitePath().trim();
                SortedMap<String, SiteDomain> siteDomainPathes = (SortedMap<String, SiteDomain>) siteDomainCache.get(
                    siteDomain.getSiteDomain().trim());
                if (siteDomainPathes == null) {
                    siteDomainPathes = new TreeMap<String, SiteDomain>(reverseStringComparator);
                    siteDomainCache.put(siteDomain.getSiteDomain().trim(),
                                        siteDomainPathes);
                }
                siteDomainPathes.put(path, siteDomain);

                Site site = siteDomain.getSite();
                if( site != null ) {
                    if (!siteCache.containsKey(site.getId())) {
                        siteCache.put(site.getId(), new CachedSite(site));
                        sitesByStringIdCache.put(site.getSiteId(), new CachedSite(site));
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.error("No site for domain " + siteDomain.getSiteDomain());
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.error("load() failed ",ex);
            throw new DgException("load() failed ",ex);
        }

        Iterator iter = siteCache.values().iterator();
        while (iter.hasNext()) {
            CachedSite cachedSite = (CachedSite) iter.next();
            synchronizePreferences(siteCache, cachedSite);
        }

        synchronized (this) {
            sites = siteCache;
            sitesByStringId = sitesByStringIdCache;
            siteDomains = siteDomainCache;
            sharedInstances = newSharedInstances;

            boolean putBack = false;
            Long versionFromCache = (Long) appScopeCache.get(appScopeKey);
            if (versionFromCache == null) {
                versionFromCache = new Long(0);
                putBack = true;
            }

            if (cacheVersion.compareTo(versionFromCache) < 0) {
                cacheVersion = versionFromCache;
            } else {
                if (cacheVersion.compareTo(versionFromCache) > 0) {
                    putBack = true;
                }
            }

            if (!silent) {
                cacheVersion = new Long(cacheVersion.longValue() + 1);
                putBack = true;
            }

            if (putBack) {
                logger.debug("Putting new version " + cacheVersion +
                             " of SiteCache in the application scope");
                appScopeCache.put(appScopeKey, cacheVersion);
            }


            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Current site cache"
                        + "\\n=================================================="
                        + "\\n{0}"
                        + "\\n==================================================",
                        toXml()));
            }
        }
    }

    private void synchronizePreferences(HashMap sites, CachedSite cachedSite) {
        Site site = cachedSite.getSite();

        if (site.getParentId() == null) {
            // If root site does not have languages assigned, assume English
            // as default, user and translation languages
            if (site.getDefaultLanguage() == null) {
                Locale language = new Locale();
                language.setCode(java.util.Locale.ENGLISH.getLanguage());
                language.setName(java.util.Locale.ENGLISH.getDisplayName());

                cachedSite.setDefaultLanguage(language);
                //site.setDefaultLanguage(language);
                HashSet languages = new HashSet();
                languages.add(language);
                //site.setUserLanguages(languages);
                //site.setTranslationLanguages(languages);
                cachedSite.setUserLanguages(languages);
                cachedSite.setTranslationLanguages(languages);
            }
            cachedSite.setRootSite(site);
            if (site.getSendAlertsToAdmin() == null) {
                cachedSite.setSendAlertsToAdmin(Boolean.FALSE);
            }
        }
        else {
            CachedSite cachedParentSite = (CachedSite) sites.get(site.
                getParentId());
            Site parentSite = cachedParentSite.getSite();
            synchronizePreferences(sites, cachedParentSite);
            // If site does not have language preferences assigned, then
            // import them from root site
            if (site.getDefaultLanguage() == null) {
                cachedSite.setUserLanguages(cachedParentSite.getUserLanguages());
                cachedSite.setTranslationLanguages(cachedParentSite.getTranslationLanguages());
                cachedSite.setDefaultLanguage(cachedParentSite.getDefaultLanguage());
            } else {
                cachedSite.setUserLanguages(site.getUserLanguages());
                cachedSite.setTranslationLanguages(site.getTranslationLanguages());
                cachedSite.setDefaultLanguage(site.getDefaultLanguage());
            }
            cachedSite.setRootSite(cachedParentSite.getRootSite());

            if (site.getSendAlertsToAdmin() == null) {
                cachedSite.setSendAlertsToAdmin(cachedParentSite.getSendAlertsToAdmin());
            }
        }
    }

    public Site getSite(Long siteId) {
        CachedSite cachedSite = getSites().get(siteId);
        if (cachedSite == null) {
            return null;
        }
        return cachedSite.getSite();
    }

    public Site getSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite == null) {
            return null;
        }
        return cachedSite.getSite();
    }

    public Site getSiteByName(String siteName) { // siteName is actually siteId in disguise - but PLEASE don't use site.siteId as key anymore
        CachedSite cachedSite = (CachedSite) getSitesByStringId().get(siteName);
        if (cachedSite == null) {
            return null;
        }
        return cachedSite.getSite();
    }


    public Site getRootSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite == null) {
            return null;
        }
        return cachedSite.getRootSite();
    }

    public Boolean getSendAlertsToAdmin(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite == null) {
            return null;
        }
        return cachedSite.getSendAlertsToAdmin();
    }

    public Site getParentSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        Long parentId = cachedSite.getSite().getParentId();
        if (parentId != null) {
            Site parent = ((CachedSite)getSites().get(parentId)).getSite();
            if (parent == null) {
                logger.warn("Site #" + parentId + " was not found");
            }
            return parent;
        } else {
            return null;
        }
    }

    public Collection<Locale> getUserLanguages(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getUserLanguages();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }
    
    public Collection<String> getUserLanguagesCodes(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getUserLanguagesCodes();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }

    public Collection getTranslationLanguages(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getTranslationLanguages();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }

    public Locale getDefaultLanguage(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getDefaultLanguage();
        }
        else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }


    public List getInstances(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getInstances();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }

    public SiteDomain getSiteDomain(String domain, String path) {
        SortedMap siteDomainPathes = (SortedMap) getSiteDomains().get(domain.
            trim());
        if (siteDomainPathes == null) {
            return null;
        }

        if ( (path == null) || (path.trim().length() == 0)) {
            return (SiteDomain) siteDomainPathes.get("");
        }

        SiteDomain siteDomain = null;
        Iterator iter = siteDomainPathes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            if (path.startsWith(item.getKey() + "/")) {
                siteDomain = (SiteDomain) item.getValue();
                break;
            }
        }

        return siteDomain;
    }
    
    /**
     * looks up the site by name. Handles some special cases: for siteId = 0 or siteId = null returns null
     * for invalid siteId values (not found in the DB) also returns null, but also prints an error message
     * @param siteId
     * @return
     */
    public static Site lookupById(Long siteId)
    {
        if (siteId == null)
            return SiteUtils.getDefaultSite();
        if (siteId == 0)
            return SiteUtils.getGlobalSite(); // global site
        Site result = getInstance().getSite(siteId);
        if (result == null)
        {
            logger.error("could not find site by id: " + siteId, new RuntimeException());
            return null;
        }
        return result;
    }

    public static Site lookupByName(String siteName)
    {
        if (siteName == null)
            return null;
        Site result = getInstance().getSiteByName(siteName);
        if (result == null)
        {
            logger.error("could not find site by name: " + siteName, new RuntimeException());
            return null;
        }
        return result;
    }
    
    private synchronized HashMap<String, SortedMap<String, SiteDomain>> getSiteDomains() {
        handleVersioning();
        return siteDomains;
    }

    private synchronized HashMap<Long, CachedSite> getSites() {
        handleVersioning();
        return sites;
    }

    private synchronized HashMap<String, CachedSite> getSitesByStringId() {
        handleVersioning();
        return sitesByStringId;
    }

    public synchronized List getSharedInstances() {
        handleVersioning();
        return sharedInstances;
    }

    static {
        synchronized (SiteCache.class) {
            currentInstance = new SiteCache();
        }
    }

    public String toXml() {
        HashMap<Long, CachedSite> siteCache = null;

        synchronized (this) {
            siteCache = sites;
        }
        StringBuffer buff = new StringBuffer();
        final String newLn = "\n";
        buff.append("<site-cache>").append(newLn);
        for (CachedSite cachedSite:siteCache.values()) {
            buff.append("    ").append("<site id=\"").append(cachedSite.getSite().
                getId()).append("\" ");
            buff.append("root=\"");
            if (cachedSite.getRootSite() == null) {
                buff.append("NULL");
            }
            else {
                buff.append(cachedSite.getRootSite().getId());
            }
            buff.append("\">");
            buff.append(cachedSite.getSite().getSiteId());
            buff.append("</site>").append(newLn);
        }
        buff.append("</site-cache>");
        return buff.toString();
    }

    public void run() {
        try {
            load(true);
        }
        catch (DgException ex) {
            logger.error("Unabme to refresh SiteCache", ex);
            throw new RuntimeException("Unabme to refresh SiteCache", ex);
        }
    }
     
}
