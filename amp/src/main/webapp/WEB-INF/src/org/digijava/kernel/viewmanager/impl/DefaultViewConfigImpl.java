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

package org.digijava.kernel.viewmanager.impl;

import org.digijava.kernel.viewmanager.AbstractViewConfig;
import javax.servlet.ServletContext;
import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;

import org.apache.commons.digester.Digester;
import org.digijava.kernel.siteconfig.Instance;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.Module;
import org.digijava.kernel.siteconfig.ModuleLayout;
import org.digijava.kernel.siteconfig.Page;
import org.digijava.kernel.siteconfig.Put;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.apache.log4j.*;
import org.digijava.kernel.siteconfig.Teaser;
import org.digijava.kernel.viewmanager.ViewConfigException;
import org.digijava.kernel.request.Site;
import java.util.List;
import org.digijava.kernel.viewmanager.ViewConfig;
import java.util.Set;

public class DefaultViewConfigImpl
    extends DefaultViewConfigUtil {
    protected static Logger logger =
        Logger.getLogger(DefaultViewConfigImpl.class);

    private Site site;

    public DefaultViewConfigImpl(Site site, ServletContext servletContext) throws
        ViewConfigException {
        super(servletContext);
        this.site = site;

        reload();
    }

    public void reload() throws ViewConfigException {
        this.dependencyWalker.clear();
        this.siteConfig = createConfiguration(site.getFolder(), false);
    }

    public String getMainLayoutPath(String layoutName) throws
        ViewConfigException {
        String layoutPath = null;

        if ( (siteConfig.getSiteLayout() == null) ||
            (!siteConfig.getSiteLayout().getLayout().containsKey(layoutName)) ||
            ( ( (Layout) siteConfig.getSiteLayout().getLayout().get(
                layoutName)).getFile() == null)) {

            layoutPath = findExistingFile("mainLayout.jsp",
                                          site.getFolder(),
                                          false, siteConfig.getTemplate(),
                                          LAYOUT_DIR, null);
        }
        else {
            layoutPath = ( (Layout) siteConfig.getSiteLayout().getLayout().get(
                layoutName)).getFile();
            layoutPath = findExistingFile(layoutPath,
                                          site.getFolder(),
                                          false, siteConfig.getTemplate(),
                                          LAYOUT_DIR, null);
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
    public Map getMasterContextAttributes(String layoutName) throws
        ViewConfigException {

        SiteLayout siteLayout = siteConfig.getSiteLayout();

        if (siteLayout == null) {
            throw new ViewConfigException(
                "At least one layout must exist in site configuration file");
        }

        // Get context attributes for default layout
        HashMap contextAttributes = new HashMap(getContextAttributes(siteLayout,
            layoutName));

        return contextAttributes;
    }

    public String getFilePath(String fileName) throws ViewConfigException {

        String tmpFileName = null;
        try {

            tmpFileName = findExistingFile(fileName,
                                           site.getFolder(),
                                           false, siteConfig.getTemplate(),
                                           "", null);
        }
        catch (Exception ex) {
            logger.debug("file " + fileName + " for site " + site.getSiteId() +
                         " not found");
            return null;
        }
        return tmpFileName;
    }

    /**
     * Returns path of the JSP page from site's 'layouts' directory.
     * @param pageName name of the page (header, footer, etc)
     * @param appendExtension true, if .jsp extension should be appended to
     * pageName and false, if not
     * @return Returns path of the JSP page
     * @throws ViewConfigException
     */
    public String getPagePath(String pageName, boolean appendExtension) throws
        ViewConfigException {
        logger.debug("SiteConfigUtils.getPagePath.layout " + pageName);

        return findExistingFile(
            (appendExtension ? pageName + ".jsp" : pageName),
            site.getFolder(),
            false, siteConfig.getTemplate(),
            LAYOUT_DIR, null);

    }

    /**
     * Find file's path for given site, module and instance. File can be layout
     * file (header, footer, etc. moduleName==null, moduleInstance==null),
     * default teaser file (pageName == null && teaserName == null), secondary
     * page (pageName != null, teaserName == null), non-default teaser file
     * (pageName == null, teaserName != null)
     * @param moduleName name of the module. If null - assume layout
     * @param moduleInstance name of the module's instance
     * @param pageName name of the page
     * @param teaserName name of the non-default teaser
     * @return file's path
     * @throws ViewConfigException
     */
    public String getFilePath(String moduleName, String moduleInstance,
                              String pageName, String teaserName) throws
        ViewConfigException {

        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("getFilePath() called for module {0}, instance {1}"
                            + ", page {2}, teaser {3}",
                    moduleName, moduleInstance, pageName, teaserName));
        }

        String fileName = null;

        if (moduleName == null) {
            // Assume as layout
            fileName = findExistingFile(pageName + ".jsp",
                                        site.getFolder(),
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
                                if (teaser.isRenderJSP()) {
                                    fileName = teaser.getValue();
                                }
                                else {
                                    return null;
                                }
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
                                if (teaser.isRenderJSP()) {
                                    fileName = teaser.getValue();
                                }
                                else {
                                    return null;
                                }
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

            if ( (fileName == null) || (fileName.trim().length() == 0)) {
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

                fileName = findExistingFile(fileName,
                                            site.getFolder(),
                                            false, siteConfig.getTemplate(),
                                            MODULE_DIR, moduleName);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getFilePath() returned " + fileName);
        }

        return fileName;
    }

    public String getTeaserAction(String moduleName,
                                  String moduleInstance,
                                  String teaserName) throws
        ViewConfigException {

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

    public String getTemplateName() {
        String templateName = siteConfig.getTemplate();
        if (templateName == null) {
            templateName = BLANK_TEMPLATE_NAME;
        }
        return templateName;
    }

    protected String findExistingFile(String path, String folderName,
                                      boolean isTemplate,
                                      String parentTemplateName,
                                      String groupType,
                                      String groupName) throws
        ViewConfigException {
        if (logger.isDebugEnabled()) {
            logger.debug("CALL: findExistingFile(" + path + "," + folderName +
                         "," + isTemplate + "," + parentTemplateName + "," +
                         groupType + "," + groupName + ")");
        }

        if (path.startsWith("/")) {
            File file = new File(servletContext.getRealPath(path));
            if (file.exists()) {
                return path;
            }
            else {
                throw new ViewConfigException("Unable to open file " + path +
                                              " with absolute path " +
                                              file.getAbsolutePath());
            }
        }
        // Assemble path
        String fileName = expandFilePath(path, folderName, isTemplate,
                                         groupType,
                                         groupName);
        File file = new File(servletContext.getRealPath(fileName));

        if (!file.exists()) {
            if (isTemplate &&
                folderName.equals(BLANK_TEMPLATE_NAME)) {
                /* Root (blank) template will be processed in the end.
                    If it does not contain this file, throw exception.
                 */
                throw new ViewConfigException("Unable to open file " + fileName +
                                              " with absolute path " +
                                              file.getAbsolutePath());
            }
            else {
                String parentTemplate = parentTemplateName == null ?
                    BLANK_TEMPLATE_NAME :
                    parentTemplateName;

                fileName = findExistingFile(path,
                                            parentTemplate, true, null,
                                            groupType, groupName);

            }
        }

        return fileName;
    }

    private Map getContextAttributes(SiteLayout siteLayout,
                                     String layoutName) throws
        ViewConfigException {
        HashMap contextAttributes = new HashMap();

        Layout layout = (Layout) siteLayout.getLayout().get(layoutName);
        if (layout == null) {
            throw new ViewConfigException("Layout " + layoutName +
                                          " does not exist for site " +
                                          site.getSiteId());
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

}
