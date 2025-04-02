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

package org.digijava.kernel.taglib.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RefLinkManager {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(RefLinkManager.class);

    private static RefLinkManager instance;
    private static int checkedOut = 0;
    private static AbstractCache refLinkCache;

    public RefLinkManager() {
        try {
            refLinkCache = DigiCacheManager.getInstance().getCache(this.
                    getClass().getName());
            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug("Initialize RefLinkManager JCS Cache");
            }

        }
        catch (Exception e) {
            throw new RuntimeException("Could not initialize RefLinkManager", e);
        }
    }

    /**
     * Singleton access point to the manager.
     */
    public static RefLinkManager getInstance() {
        if (instance == null) {
            synchronized (RefLinkManager.class) {
                if (instance == null) {
                    instance = new RefLinkManager();
                }
            }
        }

        synchronized (instance) {
            RefLinkManager.checkedOut++;
        }

        return instance;
    }

    /**
     *
     * @param servletContext
     * @param fromCache
     * @param siteName
     * @param moduleName
     * @param moduleInstance
     * @param pageName
     * @param teaserName
     * @return
     * @throws java.lang.Exception
     */
/*
        public String getFilePath(HttpServletRequest request,
                                     boolean fromCache,
                                     String siteName,
                                     String moduleName,
                                     String moduleInstance, String pageName,
                                     String teaserName) throws
        Exception {
 */
public String getFilePath(Site site,
                             boolean fromCache,
                             //String siteName,
                             String moduleName,
                             String moduleInstance, String pageName,
                             String teaserName) throws
Exception {

        //Site site = DgUtil.getCurrentSite(request);
        RefLink refLink = null;
        String path = null;
        String id = generateId(site.getFolder(),moduleName, moduleInstance, pageName, teaserName);

        // First, if requested, attempt to load from cache
        if (fromCache) {
            // try to get page path from cache
            refLink = (RefLink) refLinkCache.get(site.getSiteId() + id);
        }
        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);

        // Either fromCache was false or the object was not found, so
        // call loadRefLink to create it
        if (refLink == null) {
            path = viewConfig.getFilePath(moduleName, moduleInstance,
                                               pageName, teaserName);

            if( path != null ) {
                refLink = new RefLink();
                refLink.setPath(path);
                if( fromCache ) {
                    refLinkCache.put(site.getSiteId() + id, refLink);
                }
            }
        }
        else {
            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Get file path from cache {0} {1}", refLink.getPath(), id));
            }
        }

        return (refLink != null) ? refLink.getPath() : null;
    }

    /**
     *
     * @param servletContext
     * @param fromCache
     * @param siteFolder
     * @param pageName
     * @param appendExtension
     * @return
     * @throws java.lang.Exception
     */
    /*
    public String getPagePath(HttpServletRequest request,
                                     boolean fromCache,
                                     String siteFolder,
                                     String pageName, boolean appendExtension) throws
        Exception { */
        public String getPagePath(Site site,
                                         boolean fromCache,
                                         String pageName, boolean appendExtension) throws
            Exception {

        RefLink refLink = null;
        String path = null;
        String id = generateId(site.getFolder(),pageName, null, null, null);

        // First, if requested, attempt to load from cache
        if (fromCache) {
            // try to get page path from cache
            refLink = (RefLink) refLinkCache.get(site.getSiteId() + id );
        }

        // Either fromCache was false or the object was not found, so
        // call loadRefLink to create it
        if (refLink == null) {
            ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);

            path = viewConfig.getPagePath(pageName, appendExtension);

            if( path != null ) {
                refLink = new RefLink();
                refLink.setPath(path);
                if( fromCache ) {
                    refLinkCache.put(site.getSiteId() + id, refLink);
                }
            }
        }
        else {
            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Get page path from cache {0} {1}", refLink.getPath(), id));
            }
        }

        return (refLink != null) ? refLink.getPath() : null;
    }


    /**
     *
     * @return
     */
    public RefLink getRefLink(String id, boolean fromCache,
                              HttpServletRequest request) {
        RefLink refLink = null;
        Site site = RequestUtils.getSite(request);

        // First, if requested, attempt to load from cache
        if (fromCache) {
            refLink = (RefLink) refLinkCache.get(site.getSiteId() + id);
        }

        // Either fromCache was false or the object was not found, so
        // call loadRefLink to create it
        if (refLink == null) {
            refLink = loadRefLink(id, fromCache, site);
        }
        else {
            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.debug("Get file from cache " + refLink.getPath());
            }
        }

        return refLink;
    }

    /**
     * generate unique identifier
     *
     * @param param1
     * @param param2
     * @param param3
     * @param param4
     * @param param5
     * @return
     */
    private String generateId(String param1, String param2, String param3,
                         String param4, String param5) {

       return ((param1 == null) ? "" : "_" + param1) +
             ((param2 == null) ? "" : "_" + param2) +
             ((param3 == null) ? "" : "_" + param3) +
             ((param4 == null) ? "" : "_" + param4) +
             ((param5 == null) ? "" : "_" + param5);
    }


    public RefLink loadRefLink(String id, boolean cached,
                               Site site) {
        RefLink refLink = null;
        String path = null;
        ViewConfig viewConfig = null;
        try {
            viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);
        }
        catch (Exception ex1) {
            throw new RuntimeException(ex1);
        }

        try {
            //Site site = DgUtil.getCurrentSite(request);
            //String servletPath = (String) request.getAttribute(Constants.INCLUDE_SERVLET_PATH);

            try {

                // Get file full path with name
                path = viewConfig.getFilePath(id);
                /*
                if (path == null) {
                    // get site id
                    if (servletPath != null) {
                        String arr[] = null;
                        arr = servletPath.split("/");
                        if (arr != null && arr.length > 0) {
                            if (arr[1].equals(SiteConfigUtils.TEMPLATE_DIR) ||
                                arr[1].equals(SiteConfigUtils.SITE_DIR)) {
                                path = viewConfig.getFilePath(request.
                                    getSession().
                                    getServletContext(), arr[2],
                                    id);

                            }
                        }
                    }
                    // -----------
                }
                    */
                if (path != null) {

                    refLink = new RefLink();
                    // set file path with name
                    refLink.setPath(path);

                    // cache the value object if found
                    if (path != null && path.length() > 0 && cached) {
                        refLinkCache.put(site.getSiteId() + id, refLink);

                        // if set debug mode then print out
                        if (logger.isDebugEnabled()) {
                            logger.debug(MessageFormat.format("Put file in cache {0}, cache size {1}",
                                    refLink.getPath(), 0));
                        }
                    }
                }
            }
            catch (Exception ex) {

                // if set debug mode then print out
                if (logger.isDebugEnabled()) {
                    logger.error("Exception " + ex.getMessage());
                }
            }
        }
        catch (Exception ex) {
            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                logger.error("Exception " + ex.getMessage());
            }
        }

        return refLink;
    }

}
