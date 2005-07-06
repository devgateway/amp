/*
 *   SiteConfigUtils.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: SiteConfigUtils.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletContext;

import org.digijava.kernel.siteconfig.Instance;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.Module;
import org.digijava.kernel.config.*;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.digijava.kernel.siteconfig.*;
import javax.servlet.http.HttpServletRequest;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import java.util.*;
import java.io.BufferedReader;
import java.io.*;

/**
 * Helper class, which provides some useful methods to simplify access to site
 * configuration
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class SiteConfigUtils {

    private static Logger logger = I18NHelper.getKernelLogger(SiteConfigUtils.class);

    public static final String MODULE_DIR = "module";
    public static final String LAYOUT_DIR = "layout";
    public static final String TEMPLATE_DIR = "TEMPLATE";
    public static final String SITE_DIR = "SITE";
    public static final String BLANK_TEMPLATE_NAME = "blank";

    /**
     * Returns path to site's main layout. If site does not have main layout file,
     * it returns main layout for the default site
     * @param servletContext context of the caller servlet
     * @param siteName name of directory, under which this site is deployed
     * @return layoutName layout to load
     * @throws Exception if error occurs
     */
    public static String depr_getMainLayoutPath(ServletContext servletContext,
                                           String folderName,
                                           String layoutName) throws Exception {
        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().
            depr_getSiteConfig(folderName, servletContext);

        String layoutPath = null;

        if ( (siteConfig.getSiteLayout() == null) ||
            (!siteConfig.getSiteLayout().getLayout().containsKey(layoutName)) ||
            ( ( (Layout) siteConfig.getSiteLayout().getLayout().get(
            layoutName)).getFile() == null)) {

            layoutPath = findExistingFile(servletContext, "mainLayout.jsp",
                                          folderName,
                                          false, siteConfig.getTemplate(),
                                          LAYOUT_DIR, null);
        }
        else {
            layoutPath = ( (Layout) siteConfig.getSiteLayout().getLayout().get(
                layoutName)).getFile();
            layoutPath = findExistingFile(servletContext, layoutPath,
                                          folderName,
                                          false, siteConfig.getTemplate(),
                                          LAYOUT_DIR, null);
            /*
            layoutPath = ( (Layout) siteConfig.getSiteLayout().getLayout().get(
                layoutName)).getFile();
            File layoutFileFile = new File(servletContext.getRealPath(
                layoutPath));
            if (!layoutFileFile.exists()) {
                throw new Exception("unable to open main layout file " +
                                    layoutPath);
            }
*/
        }

        return layoutPath;
    }

    /**
     * Assemble context attributes for master layout. This method puts correct
     * filenames int tiles context to pass them to master layout
     * @param servletContext context of the caller servlet
     * @param siteName name of directory, under which this site is deployed
     * @param layoutName name of the layout in site-config.xml file
     * @return context attributes for master layout
     * @throws Exception if error occurs
     */
    public static HashMap depr_getMasterContextAttributes(ServletContext
        servletContext, String folderName, String layoutName) throws Exception {

        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().
            depr_getSiteConfig(folderName, servletContext);

        SiteLayout siteLayout = siteConfig.getSiteLayout();

        if (siteLayout == null) {
            throw new Exception(
                "At least one layout must exist in site configuration file");
        }

        // Get context attributes for default layout
        HashMap contextAttributes = new HashMap(getContextAttributes(
            servletContext,
            folderName, siteLayout, layoutName));

        return contextAttributes;
    }

    private static HashMap getContextAttributes(ServletContext
                                                servletContext, String folderName,
                                                SiteLayout siteLayout,
                                                String layoutName) throws
        Exception {
        HashMap contextAttributes = new HashMap();

        Layout layout = (Layout) siteLayout.getLayout().get(layoutName);
        if (layout == null) {
            throw new Exception("Layout " + layoutName +
                                " does not exist for site " +
                                folderName);
        }

        // Process put tags. They need no special processing
        Iterator iter = layout.getPut().values().iterator();
        while (iter.hasNext()) {
            Put item = (Put) iter.next();
            contextAttributes.put(item.getName(), item.getValue());
        }

        /*
         * @todo Maybe we want to implement &lt;putList&gt; tags too in
         * site-config.xml file?
         */

        /*
             * process put-item tags. They define JSPs, which will be inserted on the
         * master layout
         */
        iter = layout.getPutItem().values().iterator();
        while (iter.hasNext()) {
            PutItem item = (PutItem) iter.next();
            // <put-item> tag may look like <put-item name="header" /> or
            // <put-item name="body">newsBody</body>. For the first case JSP must be
            // determined for "header" and for the second case - for "newsBody",
            // not for "news"

            // Determine correct JSP
            contextAttributes.put(item.getName(), item);

        }

        return contextAttributes;
    }

    /**
     * Assemble file's absolute path
     * @param path file's relative path against site and group
     * @param siteName name of the site/template
     * @param isTemplate true, if we are processing template and false, if we're
     * processing site
     * @param groupType type of the file groups. Usually: "layout" or "module"
     * @param groupName Name of the group. For layouts - usually null, for
     * modules - "news", "poll", etc
     * @return file's absolute path
     */
    public static String depr_expandFilePath(String path, String folderName,
                                        boolean isTemplate, String groupType,
                                        String groupName) {
        String expandedPath = null;
        if ( (path != null) && (path.trim().length() != 0) &&
            !path.startsWith("/")) {

            String groupDir = groupName == null ? "" : "/" + groupName;
            if (isTemplate) {
                if( !groupType.equalsIgnoreCase("") ) {
                    expandedPath = "/" + TEMPLATE_DIR + "/" + folderName + "/" +
                        groupType +
                        groupDir + "/" + path;
                } else {
                    expandedPath = "/" + TEMPLATE_DIR + "/" + folderName + groupDir + "/" + path;
                }
            }
            else {
                if( !groupType.equalsIgnoreCase("") ) {
                    expandedPath = "/" + SITE_DIR + "/" + folderName + "/" + groupType + groupDir +
                        "/" +
                        path;
                } else {
                    expandedPath = "/" + SITE_DIR + "/" + folderName + groupDir +
                        "/" +
                        path;
                }
            }
        }
        return expandedPath;
    }

    public static String depr_getFilePath(ServletContext servletContext,
                                     String folderName,
                                     String fileName) throws
        Exception {

        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().depr_getSiteConfig(folderName, servletContext);
        String tmpFileName = null;

        try {

            tmpFileName = findExistingFile(servletContext, fileName,
                                           folderName,
                                           false, siteConfig.getTemplate(),
                                           "", null);
        }
        catch (Exception ex) {
            return null;
        }

        return tmpFileName;
    }

    /**
     * Returns path of the JSP page from site's 'layouts' directory.
     * @param servletContext
     * @param siteFolder name of the site's folder
     * @param pageName name of the page (header, footer, etc)
     * @param appendExtension true, if .jsp extension should be appended to
     * pageName and false, if not
     * @return Returns path of the JSP page
     * @throws Exception
     */
    public static String depr_getPagePath(ServletContext servletContext,
                                     String siteFolder,
                                     String pageName, boolean appendExtension) throws
        Exception {
        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG,
                          "SiteConfigUtils.getPagePath.layout",
                          new Object[] {
                          pageName}
                          , null);
        }

        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().
            depr_getSiteConfig(siteFolder, servletContext);
        return findExistingFile(servletContext,
                                (appendExtension ? pageName + ".jsp" : pageName),
                                siteFolder,
                                false, siteConfig.getTemplate(),
                                LAYOUT_DIR, null);

    }

    /**
     * Find file's path for given site, module and instance. File can be layout
     * file (header, footer, etc. moduleName==null, moduleInstance==null),
     * default teaser file (pageName == null && teaserName == null), secondary
     * page (pageName != null, teaserName == null), non-default teaser file
     * (pageName == null, teaserName != null)
     * @param servletContext Caller servlet's context
     * @param siteName name of the site
     * @param moduleName name of the module. If null - assume layout
     * @param moduleInstance name of the module's instance
     * @param pageName name of the page
     * @param teaserName name of the non-default teaser
     * @return file's path
     * @throws Exception
     */
    public static String depr_getFilePath(ServletContext servletContext,
                                     String folderName,
                                     String moduleName,
                                     String moduleInstance, String pageName,
                                     String teaserName) throws
        Exception {

        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG,
                          "SiteConfigUtils.getFilePathCalled",
                          new Object[] {
                          moduleName, moduleInstance, pageName, teaserName}
                          , null);
        }

        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().
            depr_getSiteConfig(folderName, servletContext);
        String fileName = null;

        if (moduleName == null) {
            // Assume as layout
            fileName = findExistingFile(servletContext, pageName + ".jsp",
                                        folderName,
                                        false, siteConfig.getTemplate(),
                                        LAYOUT_DIR, null);
        }
        else {
            ModuleLayout moduleLayout = siteConfig.getModuleLayout();
            if ( (moduleLayout != null) &&
                moduleLayout.getModule().containsKey(moduleName)) {
                Module currentModule = (Module) moduleLayout.getModule().get(
                    moduleName);

                if (moduleInstance != null) {
                    // Check, is there any configuration for particular instance
                    if (currentModule.getInstance().containsKey(moduleInstance)) {
                        logger.debug(
                            "This instance is configured let's get file name");
                        // This instance is configured let's get file name
                        Instance instanceConf = (Instance) currentModule.
                            getInstance().
                            get(moduleInstance);
                        if (teaserName == null) {
                            if (pageName == null) {
                                // We need default teaser.
                                fileName = instanceConf.getTeaserFile();
                            }
                            else {
                                // we need secondary page
                                Page page = (Page) instanceConf.getPage().get(
                                    pageName);
                                if (page != null) {
                                    fileName = page.getValue();
                                }
                            }
                        }
                        else {
                            // We need non-default teaser
                            Teaser teaser = (Teaser) instanceConf.getTeaser().
                                get(
                                teaserName);
                            if (teaser != null) {
                                fileName = teaser.getValue();
                            }
                        }
                    }
                    if (fileName == null) {
                        if (teaserName == null) {
                            if (pageName == null) {
                                // Instance configuration does not exist,
                                // instanceName is set ->  We need default teaser
                                // file.
                                fileName = currentModule.getTeaserFile();
                            }
                            else {
                                logger.debug(
                                    "Instance is not configured. we need secondary page");
                                // Instance is not configured. we need secondary page
                                Page page = (Page) currentModule.getPage().get(
                                    pageName);
                                if (page != null) {
                                    fileName = page.getValue();
                                }
                                logger.debug("Secondary page is " +
                                    fileName);
                            }
                        }
                        else {
                            // We need non-default teaser
                            Teaser teaser = (Teaser) currentModule.getTeaser().
                                get(
                                teaserName);
                            if (teaser != null) {
                                fileName = teaser.getValue();
                            }
                        }

                    }
                }
                else {
                    logger.debug(
                        "Instance is not set. we need secondary page");
                    // Instance is not set. we need secondary page
                    Page page = (Page) currentModule.getPage().get(pageName);
                    if (page != null) {
                        fileName = page.getValue();
                    }
                    logger.debug("Secondary page is " + fileName);
                }
            }

            if ((fileName == null) || (fileName.trim().length() == 0)) {
                // Search default file in all directories: site, parent
                // template, blank template
                //String tempFileName = null;
                if (teaserName == null) {
                    if (pageName == null) {
                        fileName = "default.jsp";
                    }
                    else {
                        fileName = pageName + ".jsp";
                    }
                }
                else {
                    fileName = teaserName + ".jsp";
                }
            }

            if (!fileName.startsWith("/")) {

                fileName = findExistingFile(servletContext, fileName,
                                            folderName,
                                            false, siteConfig.getTemplate(),
                                            MODULE_DIR, moduleName);
            }
                /*
            }
            else {
                // Check file existence
                File file = new File(servletContext.getRealPath(fileName));
                if (!file.exists()) {
                    throw new Exception("Unable to open file " + fileName +
                                        " with absolute path " +
                                        file.getAbsolutePath());
                }
            }
            */
        }

        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG,
                          "SiteConfigUtils.getFilePathReturns",
                          new Object[] {
                          fileName}
                          , null);
        }

        return fileName;
    }

    public static String depr_getTeaserAction(ServletContext servletContext,
                                         String folderName,
                                         String moduleName,
                                         String moduleInstance,
                                         String teaserName) throws
        Exception {

        SiteConfig siteConfig = SiteConfigManager.depr_getInstance().
            depr_getSiteConfig(folderName, servletContext);

        String actionName = null;
        ModuleLayout moduleLayout = siteConfig.getModuleLayout();
        if ( (moduleLayout != null) &&
            moduleLayout.getModule().containsKey(moduleName)) {
            Module currentModule = (Module) moduleLayout.getModule().get(
                moduleName);

            if (currentModule.getInstance().containsKey(moduleInstance)) {
                Instance instanceConf = (Instance) currentModule.
                    getInstance().
                    get(moduleInstance);

                Teaser teaser = (Teaser) instanceConf.getTeaser().
                    get(
                    teaserName);
                if (teaser != null) {
                    actionName = teaser.getAction();
                }
            }
            else {
                Teaser teaser = (Teaser) currentModule.getTeaser().
                    get(
                    teaserName);
                if (teaser != null) {
                    actionName = teaser.getAction();
                }
            }
        }

        if (actionName == null) {
            actionName = teaserName + ".do";
        }

        return "/" + moduleName + "/" + moduleInstance + "/" + actionName;

    }

    /**
     * Searches files in the directory structure. At first it searches in the
     * site's directory. If file exists, returns its path. If not - searches
     * in the parent site template
     * @param servletContext Context of the calling servlet
     * @param path path of the file
     * @param siteName name of the site
     * @param isTemplate is this site template or not
     * @param parentTemplateName name of the parent template. If null - "blank"
     * is assumed
     * @param groupType Type of the page group. Currently - "layout" or "module"
     * @param groupName Name of the page group. For layouts group is always null,
     * for modules - "news", "poll", "registration", in the other words - name
     * of the module.
     * @return path to the file
     * @throws Exception If file was not found
     */
    private static String findExistingFile(ServletContext servletContext,
                                           String path, String folderName,
                                           boolean isTemplate,
                                           String parentTemplateName,
                                           String groupType,
                                           String groupName) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG,
                          "SiteConfigUtils.findExistingFileCalled",
                          new Object[] {
                          path, new Boolean(isTemplate), parentTemplateName, groupType, groupName}
                          , null);
        }

	// Is it an absolute path? Then skip processing (Irakli)
	//--- logger.debug ( " ######PATH##### " + path );
	if ( path.charAt(0) == File.separator.charAt(0) ) return path;

        // Assemble path
        String fileName = depr_expandFilePath(path, folderName, isTemplate, groupType,
                                         groupName);
        File file = new File(servletContext.getRealPath(fileName));

        if (!file.exists()) {
            if (isTemplate &&
                folderName.equals(BLANK_TEMPLATE_NAME)) {
                /* Root (blank) template will be processed in the end.
                    If it does not contain this file, throw exception.
                 */
                throw new Exception("Unable to open file " + fileName +
                                    " with absolute path " +
                                    file.getAbsolutePath());
            }
            else {
                String parentTemplate = parentTemplateName == null ?
                    BLANK_TEMPLATE_NAME :
                    parentTemplateName;

                fileName = findExistingFile(servletContext, path,
                                            parentTemplate, true, null,
                                            groupType, groupName);

            }
        }

        return fileName;
    }

    /**
     * Generate Url relative or non relative url
     *
     * @param request
     * @param relative
     * @return
     */
    public static String buildDgURL(HttpServletRequest request,
                                    boolean relative) {

        // get site domain object from request
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);

        return buildDgURL(request, siteDomain, relative);
    }

    /**
     * Generate Url, relative or non relative
     *
     * @param request
     * @param relative
     * @return
     */
    public static String buildDgURL(HttpServletRequest request,
                                    SiteDomain siteDomain, boolean relative) {

        String loginUrl = null;

        if (!relative) {
            loginUrl = (request.getContextPath() == null ? "" :
                        request.getContextPath()) +
                (siteDomain.getSitePath() == null ? "" :
                 siteDomain.getSitePath());
        }
        else {
            loginUrl = (siteDomain.getSitePath() == null ? "" :
                        siteDomain.getSitePath());
        }

        return loginUrl;
    }

    /**
     * Get current module name with instance
     * for example: /news/sport
     *
     * @param request
     * @param relative
     * @return
     */
    public static String getCurrentModuleURL(HttpServletRequest request) {

        String url = null;
        String moduleName;
        String moduleInstanceName;

        ComponentContext context = ComponentContext.getContext(request);

        if (context == null) {
            url = "/" + (String) request.getAttribute(Constants.MODULE_NAME) +
                "/" + (String) request.getAttribute(Constants.MODULE_INSTANCE);
        }
        else {
            url = "/" + (String) context.getAttribute(Constants.MODULE_NAME) +
                "/" + (String) context.getAttribute(Constants.MODULE_INSTANCE);
        }

        return url;
    }

    /**
     * Get logon site url
     *
     * @return
     */

    public static String getLogonSite() {

        DigiConfig congig = null;

        try {
            congig = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon Site",ex);
        }

        return congig == null ? null : congig.getLogonSite().getContent();

    }

    /**
     * Get logon site id
     *
     * @return
     */

    public static String getLogonSiteId() {

        DigiConfig congig = null;

        try {
            congig = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon SiteId",ex);
        }

        return congig == null ? null : congig.getLogonSite().getId();

    }

    /**
     * check is logon site
     *
     * @return
     */
    public static boolean isLogonSite(HttpServletRequest request) {

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);
        String logonSiteId = getLogonSiteId();

        if (siteDomain != null) {
            return siteDomain.getSite().getSiteId().equals(logonSiteId);
        }

        return false;

    }


    /**
     * Check param in request URL
     *
     * @param name
     * @param request
     * @return
     * @todo this "strange" method needs to be placed somewere, not here
     */
    public static boolean isParam( String name, HttpServletRequest request ) {

        String paramValue = request.getParameter(name);

        return (paramValue != null && paramValue.length() > 0);
    }

 }

