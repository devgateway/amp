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

package org.digijava.kernel.viewmanager;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.digijava.kernel.util.SiteCache;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Ecnapsulates basic methods for file-based view configuration. Implementation
 * is <b>not</b> a thread-safe!
 */
public abstract class AbstractViewConfig implements ViewConfig{
    protected static Logger logger =
        Logger.getLogger(AbstractViewConfig.class);

    public static final String MODULE_DIR = "module";
    public static final String LAYOUT_DIR = "layout";
    public static final String TEMPLATE_DIR = "TEMPLATE";
    public static final String SITE_DIR = "SITE";
    public static final String BLANK_TEMPLATE_NAME = "blank";


    protected DependencyWalker dependencyWalker = new DependencyWalker();
    protected SiteConfig siteConfig;
    protected ServletContext servletContext;

    protected AbstractViewConfig(ServletContext servletContext) throws ViewConfigException {
        this.servletContext = servletContext;
    }

    public boolean isUpToDate() {
        return dependencyWalker.isUpToDate();
    }



    protected static String expandFilePath(String path, String folderName,
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

    protected SiteConfig addConfigurationFile(String folderName,
                                            boolean isTemplate) throws
        ViewConfigException {
        File configFile;
        if (isTemplate) {
            configFile = new File(servletContext.getRealPath("/" +
                TEMPLATE_DIR + "/" +
                folderName + "/site-config.xml"));
        }
        else {
            configFile = new File(servletContext.getRealPath("/" +
                SITE_DIR + "/" + folderName + "/site-config.xml"));
        }

        SiteConfig siteConfig = null;
        if (configFile.exists()) {
            dependencyWalker.addFile(configFile);
            siteConfig = SiteConfigParser.parseConfigurationFile(configFile);

            try {
                siteConfig.validate();
            }
            catch (Exception ex) {
                throw new ViewConfigException("Validation has failed for site configuration", ex);
            }

            SiteLayout siteLayout = siteConfig.getSiteLayout();
            if (siteLayout != null) {
                Iterator iter = siteLayout.getLayout().values().iterator();
                while (iter.hasNext()) {
                    Layout layout = (Layout) iter.next();
                    layout.setFileBlank(layout.getFile() == null);
                }
            }
        } else {
            logger.warn("Configuration file " + configFile.getAbsolutePath() + " was not found");
        }

        logger.debug("Adding configuration file for folder: " + folderName + " template: " + isTemplate);
        logger.debug(siteConfig);

        return siteConfig;
    }

    /**
     * Process layouts which extend other layouts. As the result, site
     * configuration will contain expanded layouts (i.e. layouts which have all
     * own and parent's properties)
     * @param siteConfig site configuration
     * @throws ViewConfigException if configuration error occurs
     */
    protected void expandLayoutInheritance(SiteConfig siteConfig) throws ViewConfigException {
        if (siteConfig.getSiteLayout() != null) {
            // Create empty storage for layouts
            HashMap newLayout = new HashMap();
            // Iterate through layouts
            Iterator iter = siteConfig.getSiteLayout().getLayout().values().
                iterator();
            while (iter.hasNext()) {
                Layout layout = (Layout) iter.next();
                // If this layout is not processed yet, process it
                if (!newLayout.containsKey(layout.getName())) {
                    expandLayoutAndStore(layout,
                                         siteConfig.getSiteLayout().getLayout(),
                                         newLayout);
                }
            }
            // Assign new layout list to site configuration
            siteConfig.getSiteLayout().setLayout(newLayout);
        }
    }

    /**
     * Expands given layout recursively
     * @param layout layout to expand
     * @param sourceLayout source storage of layouts
     * @param destinationLayout destination storage of layouts
     * @throws ViewConfigException if configuration error occurs
     */
    protected void expandLayoutAndStore(Layout layout, HashMap sourceLayout,
                                      HashMap destinationLayout) throws ViewConfigException {
        // If layout has parent, expand it
        if (layout.getExtendsLayout() != null) {
            // Was parent layout processed?
            if (!destinationLayout.containsKey(layout.getExtendsLayout())) {
                // If not, then process it first
                Layout parentLayout = (Layout) sourceLayout.get(layout.
                    getExtendsLayout());
                if (parentLayout == null) {
                    throw new ViewConfigException("Layout " + layout.getName() +
                        " references to non-existent layout " + layout.
                                                  getExtendsLayout());
                }
                expandLayoutAndStore(parentLayout ,
                                     sourceLayout, destinationLayout);
            }
            // Get parent layout, which is already expanded
            Layout parentLayout = (Layout) destinationLayout.get(layout.
                getExtendsLayout());

            // overwrite all put and put-item items
            HashMap newPut = (HashMap) parentLayout.getPut().clone();
            newPut.putAll(layout.getPut());

            HashMap newPutItem = (HashMap) parentLayout.getPutItem().clone();
            newPutItem.putAll(layout.getPutItem());

            layout.setPut(newPut);
            layout.setPutItem(newPutItem);

                /** @todo: layout.getFile() == null is not needed I think. Mikheil */
            if ( (layout.getFile() == null) || (layout.isFileBlank())) {
                layout.setFile(parentLayout.getFile());
            }
        }
        // Add processed layout to destination storage
        destinationLayout.put(layout.getName(), layout);
    }

    /* =========================== interface methods =========================== */

    public String getLayoutFilePath(String layoutName) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getLayoutFilePath() not yet implemented.");
    }
    public String getTeaserActionPath(String moduleName, String instanceName, String teaserName) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getTeaserActionPath() not yet implemented.");
    }
    public String getTeaserFilePath(String moduleName, String instanceName, String teaserName) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getTeaserFilePath() not yet implemented.");
    }
    public String getPageFilePath(String moduleName, String instanceName, String pageName) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getPageFilePath() not yet implemented.");
    }
    public String getGenericFilePath(String moduleName, String instanceName, String fileName) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getGenericFilePath() not yet implemented.");
    }
    public boolean isAbsoluteFilePath(String filePath) {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method isAbsoluteFilePath() not yet implemented.");
    }
    public List getModuleInstances() {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig method*/
        throw new java.lang.UnsupportedOperationException("Method getModuleInstances() not yet implemented.");
    }

    public Set getReferencedModules(boolean includeCommons) {
        HashSet modules = new HashSet();
        Iterator iter = getReferencedInstances(includeCommons).iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance)iter.next();
            modules.add(item.getModuleName());
        }

        return modules;
    }

    public Set getReferencedInstances(boolean includeCommons) {
        // Get required module instances from template

        SiteLayout siteLayout = siteConfig.getSiteLayout();
        //HashSet moduleInstances = new HashSet();
        TreeSet moduleInstances = new TreeSet(SiteCache.moduleInstanceComparator);

        if (siteLayout != null) {
            Iterator iter = siteLayout.getLayout().values().iterator();
            while (iter.hasNext()) {
                Layout layout = (Layout) iter.next();
                Iterator iter1 = layout.getPutItem().values().iterator();
                while (iter1.hasNext()) {
                    PutItem putItem = (PutItem) iter1.next();
                    if ( (putItem.getModule() != null) &&
                        (putItem.getModule().trim().length() != 0) &&
                        (putItem.getInstance() != null) &&
                        (putItem.getInstance().trim().length() != 0)
                        ) {
                       ModuleInstance moduleInstance = new ModuleInstance();
                       moduleInstance.setSite(null);
                       moduleInstance.setModuleName(putItem.getModule());
                       moduleInstance.setInstanceName(putItem.getInstance());
                       moduleInstance.setPermitted(true);
                       moduleInstance.setRealInstance(null);

                       if (includeCommons) {
                           moduleInstances.add(moduleInstance);
                       } else {
                           List sharedInstances = SiteCache.getInstance().
                               getSharedInstances();
                           if (Collections.binarySearch(sharedInstances,
                               moduleInstance,
                               SiteCache.moduleInstanceComparator) < 0) {
                               moduleInstances.add(moduleInstance);
                           }
                       }
                   }
                }
            }
        }

        return moduleInstances;
    }

    public boolean isLayoutDefined(String layoutName) {
        if (siteConfig.getSiteLayout() != null) {
            if (siteConfig.getSiteLayout().getLayout().containsKey(layoutName)) {
                return true;
            }
        }

        return false;
    }

    public String getXML() throws ViewConfigException {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        try {
            Document document = getDocument();
            if (document == null) {
                logger.debug("Document is null");
                return null;
            }
            Transformer transformer = TransformerFactory.newInstance().
                newTransformer();
            transformer.transform(new DOMSource(document), streamResult);
            writer.flush();
        }
        catch (ViewConfigException ex) {
            logger.debug("ViewConfigException was caught. re-raising");
            throw ex;
        }
        catch (Exception ex) {
            throw new ViewConfigException(ex);
        }
        return writer.toString();
    }

    public Document getDocument() throws ViewConfigException {
        if (siteConfig == null) {
            logger.debug("Site configuration is null");
            return null;
        }
        String toString = siteConfig.toString();
        StringReader reader = new StringReader(toString);
        //StringBufferInputStream is = new StringBufferInputStream(toString);
        Document document = null;
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(reader));
        }
        catch (SAXException ex) {
            logger.error("Parse error while parsing document\n" + toString);
            throw new ViewConfigException(ex);
        }
        catch (Exception ex) {
            throw new ViewConfigException(ex);
        }

        return document;
    }

}
