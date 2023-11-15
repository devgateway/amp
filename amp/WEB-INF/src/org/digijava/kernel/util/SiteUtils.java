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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.services.siteidentity.SiteIdentityService;
import org.digijava.kernel.user.Group;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.*;

/**
 * This class containts site-related utillity functions. Site must be
 * <b>always</b> identified by Site object
 */
public class SiteUtils {

    private static Logger logger = Logger.getLogger(SiteUtils.class);

    public static final long DEFAULT_SITE_ID = 3;

    /**
     * Returns Set of module names, which can be used on the site.
     * <b>Does not</b> include modules with only shared instances (for example,
     * admin)
     * @param site Site object
     * @return Set of module names and empty set, if there are no modules
     */
    public static Set getModuleNamesForSite(Site site) {
        return getModuleNamesForSite(site, false);
    }

    /**
     * Returns Set of module names, which can be used on the site
     * @param site Site object
     * @param includeShared boolean if true, includes modules with shared
     * instances. For example, admin. If false - only modules, which have
     * explicitly defined instances for the given site
     * @return Set of module names and empty set, if there are no modules
     */
    public static Set getModuleNamesForSite(Site site, boolean includeShared) {
        HashSet modules = new HashSet();

        Iterator iter;
        if (includeShared) {
            iter = SiteCache.getInstance().getSharedInstances().iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter.next();
                modules.add(item.getModuleName());
            }
        }

        iter = SiteCache.getInstance().getInstances(site).iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance) iter.next();
            modules.add(item.getModuleName());
        }

        return modules;
    }

    /**
     * Returns Set of ModuleInstance objects, which are available for the
     * given site and belong to specified module, <b>including</b> shared
     * instances. The returned Set can be used to search ModuleInstance objects
     * in it
     * @param site Site object
     * @param module String name of the module
     * @return Set of ModuleInstance objects and empty set, if there are no
     * instances
     */
    public static Set getModuleInstances(Site site, String module) {
        TreeSet instances = new TreeSet(SiteCache.moduleInstanceComparator);

        Iterator iter = SiteCache.getInstance().getSharedInstances().iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance) iter.next();
            if (module.equals(item.getModuleName())) {
                instances.add(item);
            }
        }

        iter = SiteCache.getInstance().getInstances(site).iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance) iter.next();
            if (module.equals(item.getModuleName())) {
                instances.add(item);
            }
        }

        return instances;
    }

    public static Site getGlobalSite()
    {
        return getDefaultSite();
    }
    
    public static Site getDefaultSite() {
        return getSiteByName("amp");
    }

    /**
     * Get <code>Site</code> object for the given site id
     * @param siteName site identity (string type)
     * @return Site if found. null - if not
     */
    private static Site getSiteByName(String siteName) {
        Session session = PersistenceManager.getSession();

        session.clear();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Site> criteriaQuery = criteriaBuilder.createQuery(Site.class);
        Root<Site> siteRoot = criteriaQuery.from(Site.class);

        criteriaQuery.select(siteRoot)
                .where(criteriaBuilder.equal(siteRoot.get("siteId"), siteName));

        return session.createQuery(criteriaQuery)
                .setCacheable(true)
                .setCacheRegion(Constants.KERNEL_QUERY_CACHE_REGION)
                .uniqueResult();
    }

    /**
     * Get site by its primary key
     * @param id site's primary key
     * @return Site
     * @throws DgException if database error occurs
     */
    public static Site getSite(Long id) throws DgException {
        Site site = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            site = (Site)session.get(Site.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get site from database ", ex);
            throw new DgException("Unable to get site from database ", ex);
        }
        return site;
    }

    /**
     * Returns translation languages for the given site. This list is formed
     * using using language inheritance business logic (see DiGi Multilangual
     * document for more details)
     * @param site Site
     * @return translation languages for the given site
     */
    public static Set getTransLanguages(Site site) {
        if (site == null)
            return null;
        Collection langs = SiteCache.getInstance().
            getTranslationLanguages(site);
        return langs == null ? null : new HashSet(langs);
    }

    /**
     * Returns user languages for the given site. This list is formed
     * using using language inheritance business logic (see DiGi Multilangual
     * document for more details)
     * @param site Site
     * @return user languages for the given site
     */
    public static Set<Locale> getUserLanguages(Site site) {
        if (site == null)
            return null;
        Collection<Locale> langs = SiteCache.getInstance().getUserLanguages(site);
        return langs == null ? null : new HashSet<Locale>(langs);
    }
    
    public static Collection<String> getUserLanguagesCodes(Site site) {
        Collection<String> codes = null;
        if (site != null) 
            codes = SiteCache.getInstance().getUserLanguagesCodes(site);
        if (codes == null)
            codes = new ArrayList<String>();
        return codes;
    }
        

    /**
     * Returns default language for specified site
     * @param site Site object, default language of which should be retrieved
     * @return site's default language
     */
    public static Locale getDefaultLanguages(Site site) {
        if (site == null)
            return null;
        Locale defLanguage = SiteCache.getInstance().getDefaultLanguage(site);
        return defLanguage;
    }

    /**
     * Assembles site's root URL for given SiteDomain object and other
     * parameters
     * @param siteDomain SiteDomain object
     * @param scheme request scheme. Can be determined from HttpServletRequest
     * object
     * @param port Server port. Can be determined from HttpServletRequest object
     * @param contextPath WEB Application context path. Can be determined from
     * HttpServletRequest object
     * @return site's root URL
     */
    public static String getSiteURL(SiteDomain siteDomain, String scheme,
                                    int port, String contextPath) {
        return getSiteURL(siteDomain.getSiteDomain(), siteDomain.getSitePath(),
                          scheme, port, contextPath);
    }

    /**
     * Assembles site's root URL for given site's domain, path and other
     * parameters
     * @param domain Site's domain
     * @param path Site's path (if any)
     * @param scheme request scheme. Can be determined from HttpServletRequest
     * object
     * @param port Server port. Can be determined from HttpServletRequest object
     * @param contextPath WEB Application context path. Can be determined from
     * HttpServletRequest object
     * @return site's root URL
     */
    public static String getSiteURL(String domain, String path, String scheme,
                                    int port, String contextPath) {
        StringBuffer sb = new StringBuffer();

        if (scheme != null) {
            sb.append(scheme).append("://");
        }
        sb.append(domain);

        if (! ( (scheme.equals("http") && port == 80) ||
               (scheme.equals("https") && port == 443))) {
            sb.append(":").append(port);
        }

        if (contextPath != null && !contextPath.equals("/")) {
            sb.append(contextPath);
        }

        if (path != null) {
            sb.append(path);
        }

        return sb.toString();
    }


    /**
     * Returns default domain for the given site. If site does not have default
     * domain (in case if someone changed it in the database), returns one of
     * the domains. It will return null, if site does not have any domains
     * @param site Site
     * @return default domain for the given site
     */
    public static SiteDomain getDefaultSiteDomain(Site site) {
        SiteDomain siteDomain = null;
        Iterator iter = site.getSiteDomains().iterator();
        while (iter.hasNext()) {
            siteDomain = (SiteDomain) iter.next();
            if (siteDomain.isDefaultDomain()) {
                break;
            }
        }

        return siteDomain;
    }

    /**
     * Converts the original (as defined in the database) domain name to a "prefixed"
     * domain name. Prefixed domain is calculated as:<br/>
     * <code>siteDomain = prefix + siteDbDomain;</code><br/>
     * where "prefix" is defined in digi.xml's &lt;server-type&gt; tag.
     *
     * <p>Prefixes help independant installations on different physical
     * servers (different IPs) to use the same database hence the
     * same domain records, for sites. By putting prefix in digi.xml the
     * domain names that system uses runtime will change, so you can assign
     * prefixed domain names to a different server (say, development or staging),
     * but use the same database records.
     *
     * @todo We may need to introduce pattern instead of prefix
     * on a later day. Using pattern, it will be possible to put
     * additional string anywhere in the original domain and from a
     * domain name like www.dgmarket.com get www-staging.dgmarket.com.
     * In the current implementation we can only get staging.www.dgmarket.com
     *
     * However, this requires usage of substr or something and considering
     * how often this method is called, will slow down system. We do not
     * really need patterns, yet.
     * @param dbDomainName name of the domain in database (non-prefixed domain)
     * @return prefixed domain if prefix is set in digi configuration.
     * dbDomainName parameter value - if it is not set
     */
    public static String prefixDomainName(String dbDomainName) {
        String prefix = DigiConfigManager.getConfig().getDomainPrefix();
        logger.debug("##### Domain prefix: " + prefix);
        return prefix != null ? prefix + dbDomainName : dbDomainName;
    }

    /**
     * Get numeric site id for site, identified by string id
     * @param siteId string identifier of the site. "demosite", "dglogin", etc
     * @return Long site identity
     * @throws DgException if error occurs
     */
    public static Long getNumericSiteId(String siteName) throws DgException {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("select s.id from " +
                                          Site.class.getName() +
                                          " s, where (s.siteId=:siteId)");
            q.setCacheable(true);
            q.setCacheRegion(Constants.KERNEL_QUERY_CACHE_REGION);
            q.setParameter("siteId", siteName, StringType.INSTANCE);

            List result = q.list();
            if (result.size() != 0) {
                return (Long) result.get(0);
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get site from database ", ex);
            throw new DgException("Unable to get site from database ", ex);
        }
    }
    /**
     * Returns List of the child sites for the current site, ordered by site
     * name
     * @param parentId parent site's numeric identity
     * @return List of the child sites for the current site
     * @throws DgException if error occurs
     */
    public static List getChildSites(long parentId) throws DgException {
        List sites;
        Session session;
        try {
            session = PersistenceManager.getSession();
            Query query = session.createQuery("from " +
                                              Site.class.getName() +
                " s where s.parentId = :parentId order by s.name");
            query.setCacheable(true);
            query.setCacheRegion(Constants.KERNEL_QUERY_CACHE_REGION);
            query.setParameter("parentId", parentId, LongType.INSTANCE);
            sites = query.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get child sites list from database ", ex);
            throw new DgException(
                "Unable to get child sites list from database ", ex);
        }
        return sites;
    }

    /**
     * Returns true, if module-specific alerts must be sent to site admins too.
     * false - if only module admins receive such alerts. If site does not have
     * this parameter defined, it is determined by hierarchy tree
     * @param site Target site
     * @return boolean value
     */
    public static boolean isSendAlertsToAdmin(Site site) {
        if (site == null)
            return false;
        Boolean send = SiteCache.getInstance().getSendAlertsToAdmin(site);
        return send == null ? false : send;
    }

    /**
     * Adds default groups to site and synchronizes their parent groups
     * @param site Site
     * @throws DgException if database error occurs
     */
    public static void addDefaultGroups(Site site) throws DgException {
        Site parentSite = null;
        if (site.getParentId() != null) {
            parentSite = getSite(site.getParentId());
        }
        HashMap parentDefGroups = new HashMap<>();
        if (parentSite != null) {
            for (Object o : parentSite.getGroups()) {
                Group group = (Group) o;
                if (group.isDefaultGroup()) {
                    parentDefGroups.put(group.getKey(), group);
                }
            }
        }
        HashSet existingDefGroups = new HashSet<>();
        if (site.getGroups() == null) {
            site.setGroups(new HashSet<>());
        }
        else {
            // Fix parents for existing default groups
            for (Object o : site.getGroups()) {
                Group group = (Group) o;
                if (group.isDefaultGroup()) {
                    Group parentGroup = (Group) parentDefGroups.get(group.
                            getKey());
                    if (parentGroup != null &&
                            (parentGroup.getId() == null ||
                                    !parentGroup.getId().equals(group.getId()))) {
                        group.setParentId(parentGroup.getId());
                    }
                    existingDefGroups.add(group.getKey());
                }
            }
        }
        for (Object o : Group.defaultGroups.entrySet()) {
            Map.Entry item = (Map.Entry) o;
            if (!existingDefGroups.contains(item.getValue())) {
                Group group = new Group(site, (String) item.getValue(),
                        (String) item.getKey());
                Group parentGroup = (Group) parentDefGroups.get(group.getKey());
                if (parentGroup != null) {
                    group.setParentId(parentGroup.getId());
                }
                site.getGroups().add(group);
                PersistenceManager.getRequestDBSession().saveOrUpdate(site);
                existingDefGroups.add(item.getValue());
            }
        }
        PersistenceManager.getRequestDBSession().flush();
    }

    /**
     * Inspects site's default group permissions and adds required ones if
     * needed
     * @param site Site
     * @throws DgException if any error occurs
     */
    public static void fixDefaultGroupPermissions(Site site) throws DgException {
        for (Object o : site.getGroups()) {
            Group group = (Group) o;
            if (!group.isDefaultGroup()) {
                continue;
            }
            GroupPrincipal gp = new GroupPrincipal(group.getId(),
                    site.getName(),
                    group.getName());

            PermissionCollection permissions = DigiSecurityManager.getPermissions(gp);
            SitePermission requiredPerm = new SitePermission(site,
                    group.getRequiredActions());

            boolean found = false;
            if (permissions != null) {
                Enumeration enumElements = permissions.elements();
                while (enumElements.hasMoreElements()) {
                    Permission perm = (Permission) enumElements.nextElement();
                    if (perm.equals(requiredPerm)) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                DigiSecurityManager.grantPermission(gp, requiredPerm);
            }
        }
    }

    /**
     * Returns site identity based on configuration setting. If
     * siteIdentityService service is defined in the system, it is asked to
     * calculate identity. If it is not - site's string identity is returned
     * @param site Site
     * @param service String name of the service for which identity should be
     * determined. For example - "google", "omniture",
     * @return site identity
     */
    public static String getSiteIdentity(Site site, String service) {
        SiteIdentityService siteIdentityService = (SiteIdentityService)
            ServiceManager.getInstance().getService("siteIdentityService");

        if (siteIdentityService == null) {
            return site.getSiteId();
        }

        return siteIdentityService.getSiteId(service, site);
    }

    /**
     * Returns true, if effective language LeftToRight is false.
     *
     * @return boolean value
     */
    public static boolean isEffectiveLangRTL() {
        Locale locale = getDefaultSite().getLocale(TLSUtils.getEffectiveLangCode());
        return !locale.getLeftToRight();
    }

    public static String getBaseUrl() {
        String url = "";
        Set<SiteDomain> siteDomains = SiteUtils.getDefaultSite().getSiteDomains();
        SiteDomain principalSiteDomain = siteDomains.stream()
                .filter(SiteDomain::isDefaultDomain)
                .findFirst()
                .orElse(null);
        
        if (principalSiteDomain != null) {
            url = principalSiteDomain.getSiteDomain();
        }
        
        return url;
    }    
}
