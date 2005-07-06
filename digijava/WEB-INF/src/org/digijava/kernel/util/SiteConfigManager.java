/*
 *   SiteConfigManager.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: SiteConfigManager.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

/**
 * Manages SiteConfigManager class objects
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class SiteConfigManager {

    private static Logger logger = I18NHelper.getKernelLogger(SiteConfigManager.class);

    private static SiteConfigManager currentInstance = null;
    private Map configurations;
    private boolean triggerConfigChanges = true;
    private Digester digester;

    /**
     * Data class, which stores site configuration
     */
    class SiteConfiguration {
        SiteConfig configuration;
        HashMap configurationFiles;

        public SiteConfiguration() {
            configuration = null;
            configurationFiles = new HashMap();
        }
    }

    static {
        synchronized (SiteConfigManager.class) {
            currentInstance = new SiteConfigManager();
        }
    }

    /**
     * Returns instance of ConfigurationManagerFactory. This instance will be
     * created once an stored in static variable. Method will return the same
     * instance on other calls.
     * @return instance of ConfigurationManagerFactory
     */
    public static SiteConfigManager depr_getInstance() {
        return currentInstance;
    }

    /**
     * Initialize internal variables
     */
    public SiteConfigManager() {
        this.configurations = Collections.synchronizedMap(new HashMap());
        this.digester = createDigester();
    }

    /**
     * Returns site configuration. Monitors site-config.xml file changes and
     * re-parses it after change
     * @param siteName name of the directory, under which this site is deployed
         * @param context context of the caller servlet (Method needs it to determine
     * real pathes)
     * @return Returns site configuration
     * @throws Exception if error occures
     */
    public SiteConfig depr_getSiteConfig(String folderName, ServletContext context) throws
        Exception {
        SiteConfig config = null;

        // Get cached configuration
        SiteConfiguration siteConfiguration = (SiteConfiguration)
            configurations.
            get(folderName);

        if (siteConfiguration == null) {
            // Actually, site does not have cached configuration.
            siteConfiguration = new SiteConfiguration();

            siteConfiguration.configuration = depr_createConfiguration(folderName, false,
                context, siteConfiguration.configurationFiles);

            // Put configuration into cache
            configurations.put(folderName, siteConfiguration);

            if (logger.isDebugEnabled()) {
                Object[] params = {
                    siteConfiguration.configuration};
                logger.l7dlog(Level.DEBUG,
                              "SiteConfigManager.currentSiteConfiguration",
                              params, null);
            }
        }
        else {
            synchronized (siteConfiguration) {
                boolean fireReload = false;

                // For the normal operation siteConfiguration.configurationFiles
                // will have at least one entry. If it was cleared, configuration
                // must be re-parsed
                if (siteConfiguration.configurationFiles.size() == 0) {
                    fireReload = true;
                }
                else {
                    // If Multisite is running configuration change triggering mode, then
                    // it will check all files, current site configuration depends on
                    // and if at least one of them was changed, reloads configuration
                    if (triggerConfigChanges) {
                        Iterator iter = siteConfiguration.configurationFiles.
                            entrySet().
                            iterator();
                        while (iter.hasNext()) {
                            Map.Entry item = (Map.Entry) iter.next();
                            String fileName = (String) item.getKey();
                            long lastModificationDate = ( (Long) item.getValue()).
                                longValue();

                            File file = new File(fileName);
                            if (file.lastModified() > lastModificationDate) {
                                fireReload = true;
                                break;
                            }
                        }
                    }
                }

                if (fireReload) {
                    siteConfiguration.configurationFiles.clear();
                    siteConfiguration.configuration = depr_createConfiguration(
                        folderName, false,
                        context, siteConfiguration.configurationFiles);

                    if (logger.isDebugEnabled()) {
                        Object[] params = {
                            siteConfiguration.configuration};
                        logger.l7dlog(Level.DEBUG,
                            "SiteConfigManager.currentSiteConfiguration",
                                      params, null);
                    }

                }
            }
        }

        return siteConfiguration.configuration;

    }

    /**
     * Create new Digester object, which will parse site configuration file
     * @return Digester object
     */
    private Digester createDigester() {
        Digester digester = new Digester();
        // Turn of validation against DTD
        digester.setValidating(false);
        // Workaround Tomcat's issue with ClassLoader
        digester.setUseContextClassLoader(true);

        // Configure digester
        digester.addObjectCreate("site-config", SiteConfig.class);
        digester.addSetProperties("site-config", "template", "template");

        digester.addObjectCreate("site-config/site-layout", SiteLayout.class);
        digester.addSetNext("site-config/site-layout", "setSiteLayout");

        digester.addObjectCreate("site-config/site-layout/layout", Layout.class);
        digester.addSetNext("site-config/site-layout/layout", "addLayout");
        digester.addSetProperties("site-config/site-layout/layout", "name",
                                  "name");
        digester.addSetProperties("site-config/site-layout/layout", "file",
                                  "file");
        digester.addSetProperties("site-config/site-layout/layout", "extends",
                                  "extendsLayout");

        digester.addObjectCreate("site-config/site-layout/layout/put", Put.class);
        digester.addSetNext("site-config/site-layout/layout/put", "addPut");
        digester.addBeanPropertySetter("site-config/site-layout/layout/put",
                                       "value");
        digester.addSetProperties("site-config/site-layout/layout/put", "name",
                                  "name");

        digester.addObjectCreate("site-config/site-layout/layout/put-item",
                                 PutItem.class);
        digester.addSetNext("site-config/site-layout/layout/put-item",
                            "addPutItem");
        digester.addBeanPropertySetter(
            "site-config/site-layout/layout/put-item", "value");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "name", "name");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "module", "module");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "page", "page");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "instance", "instance");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "file", "file");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "layout", "layout");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "teaser", "teaser");

        digester.addObjectCreate("site-config/module-layout", ModuleLayout.class);
        digester.addSetNext("site-config/module-layout", "setModuleLayout");

        digester.addObjectCreate("site-config/module-layout/module", Module.class);
        digester.addSetNext("site-config/module-layout/module", "addModule");
        digester.addSetProperties("site-config/module-layout/module", "name",
                                  "name");
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/teaser-file", "teaserFile");

        digester.addObjectCreate("site-config/module-layout/module/page", Page.class);
        digester.addSetNext("site-config/module-layout/module/page", "addPage");
        digester.addBeanPropertySetter("site-config/module-layout/module/page",
                                       "value");
        digester.addSetProperties("site-config/module-layout/module/page",
                                  "name", "name");
        digester.addObjectCreate(
            "site-config/module-layout/module/teaser", Teaser.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/teaser", "value");
        digester.addSetNext("site-config/module-layout/module/teaser",
                            "addTeaser");
        digester.addSetProperties(
            "site-config/module-layout/module/teaser", "name", "name");
        digester.addSetProperties(
            "site-config/module-layout/module/teaser", "action", "action");

        digester.addObjectCreate("site-config/module-layout/module/instance",
                                 Instance.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/teaser-file",
            "teaserFile");
        digester.addSetNext("site-config/module-layout/module/instance",
                            "addInstance");
        digester.addSetProperties("site-config/module-layout/module/instance",
                                  "name", "name");

        digester.addObjectCreate(
            "site-config/module-layout/module/instance/page", Page.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/page", "value");
        digester.addSetNext("site-config/module-layout/module/instance/page",
                            "addPage");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/page", "name", "name");

        digester.addObjectCreate(
            "site-config/module-layout/module/instance/teaser", Teaser.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/teaser", "value");
        digester.addSetNext("site-config/module-layout/module/instance/teaser",
                            "addTeaser");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/teaser", "name", "name");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/teaser", "action",
            "action");

        return digester;
    }

    /**
     * Parse configuration file and store configuration to easy access via
     * getSiteConfig() method
     * @param file configuration file
     * @return parsed configuration
     * @throws Exception if error occures during parse
     */
    public SiteConfig depr_parseConfigurationFile(File file) throws Exception {
        return (SiteConfig) digester.parse(file);
    }

    /**
     * Method Reloads Site configuration file
     * @param siteName Name of the site
     * @param context Context of the calling servlet
     * @throws Exception if error occurs
     */
    public void depr_reloadSiteConfiguration(String folderName, ServletContext context) throws
        Exception {
        // Make sure that site configuration is loaded in the cache
        this.depr_getSiteConfig(folderName, context);

        SiteConfiguration siteConfiguration = (SiteConfiguration)
            configurations.get(folderName);
        siteConfiguration.configurationFiles.clear();

        // Reload configuration
        this.depr_getSiteConfig(folderName, context);

    }

    /**
     * Load Site's or Template's configuration file (site-config.xml)
     * @param siteName name of the site/template
     * @param isTemplate are we processing template or not (in this case we're
     * processing site)
     * @param context caller servlet's context
     * @param configurationFiles alreary processed configuration files. Maps
     * file path to SiteConfig object
     * @return site's configuration
     * @throws Exception if some error occurs
     */
    private SiteConfig loadConfiguration(String folderName, boolean isTemplate,
                                         ServletContext context,
                                         HashMap configurationFiles) throws
        Exception {
        File configFile;
        if (isTemplate) {
            configFile = new File(context.getRealPath("/" +
                SiteConfigUtils.TEMPLATE_DIR + "/" +
                folderName + "/site-config.xml"));
        }
        else {
            configFile = new File(context.getRealPath("/" +
                SiteConfigUtils.SITE_DIR + "/" +
                folderName + "/site-config.xml"));
        }

        SiteConfig siteConfig = null;
        if (configFile.exists()) {
            synchronized (configurationFiles) {
                siteConfig = depr_parseConfigurationFile(configFile);
                configurationFiles.put(configFile.getAbsolutePath(),
                                       new Long(configFile.lastModified()));
            }

            siteConfig.validate();

            SiteLayout siteLayout = siteConfig.getSiteLayout();
            if (siteLayout != null) {
                Iterator iter = siteLayout.getLayout().values().iterator();
                while (iter.hasNext()) {
                    Layout layout = (Layout) iter.next();
                    layout.setFileBlank(layout.getFile() == null);
                    /*
                    layout.setFile(SiteConfigUtils.expandFilePath(layout.
                        getFile(),
                        siteName,
                        isTemplate, "layout", null));
                     */
                    if (layout.getPutItem() != null) {
                        Iterator putItemIter = layout.getPutItem().values().
                            iterator();
                        while (putItemIter.hasNext()) {
                            PutItem putItem = (PutItem) putItemIter.next();
                            /*
                            putItem.setFile(SiteConfigUtils.expandFilePath(
                                putItem.
                                getFile(),
                                siteName, isTemplate, "layout", null));
                             */
                        }
                    }
                }
            }
            /*
            // Modify file path information for jsp files
            ModuleLayout moduleLayout = siteConfig.getModuleLayout();
            if (moduleLayout != null) {
                Iterator iter = moduleLayout.getModule().values().iterator();
                while (iter.hasNext()) {
                    Module module = (Module) iter.next();

                    // Modify default page path
                    module.setTeaserFile(SiteConfigUtils.expandFilePath(module.
                        getTeaserFile(),
                        siteName, isTemplate, "module", module.getName()));

                    // Modify pathes for module instances
                    if (module.getInstance() != null) {
                        Iterator instIter = module.getInstance().values().
                            iterator();
                        while (instIter.hasNext()) {
                            Instance moduleInstance = (Instance) instIter.next();
                            moduleInstance.setTeaserFile(SiteConfigUtils.
                                expandFilePath(
                                moduleInstance.getTeaserFile(), siteName,
                                isTemplate, "module", module.getName()));

                            // Modify path for module's secondary pages
                            if (moduleInstance.getPage() != null) {
                                Iterator pageIter = moduleInstance.getPage().
                                    values().iterator();
                                while (pageIter.hasNext()) {
                                    Page page = (Page) pageIter.next();
                                    page.setValue(SiteConfigUtils.
                                                  expandFilePath(
                                        page.getValue(),
                                        siteName, isTemplate, "module",
                                        module.getName()));
                                }
                            }
                            // Modify instance's teasers
                            if (moduleInstance.getTeaser() != null) {
                                Iterator teaserIter = moduleInstance.getTeaser().
                                    values().iterator();
                                while (teaserIter.hasNext()) {
                                    Teaser teaser = (Teaser) teaserIter.next();
                                    teaser.setValue(SiteConfigUtils.
                                        expandFilePath(
                                        teaser.getValue(),
                                        siteName, isTemplate, "module",
                                        module.getName()));
                                }

                            }

                        }
                    }

                    // Modify path for module's secondary pages
                    if (module.getPage() != null) {
                        Iterator pageIter = module.getPage().values().iterator();
                        while (pageIter.hasNext()) {
                            Page page = (Page) pageIter.next();
                            page.setValue(SiteConfigUtils.expandFilePath(page.
                                getValue(),
                                siteName, isTemplate, "module", module.getName()));
                        }
                    }
                    // Modify module's teasers
                    if (module.getTeaser() != null) {
                        Iterator teaserIter = module.getTeaser().values().
                            iterator();
                        while (teaserIter.hasNext()) {
                            Teaser teaser = (Teaser) teaserIter.next();
                            teaser.setValue(SiteConfigUtils.
                                            expandFilePath(
                                teaser.getValue(),
                                siteName, isTemplate, "module",
                                module.getName()));
                        }

                    }

                }
            }
            */
        }

        return siteConfig;
    }

    /**
     * Create site's or template's configuration. Method loads site
     * configuration and expands it using inheritanse mechanism
     * @param siteName name of the site/template
     * @param isTemplate are we processing template or not (in this case we're
     * processing site)
     * @param context caller servlet's context
     * @param configurationFiles alreary processed configuration files. Maps
     * file path to SiteConfig object
     * @return site's configuration
     * @throws Exception if some error occurs
     */
    public SiteConfig depr_createConfiguration(String folderName, boolean isTemplate,
                                           ServletContext context,
                                           HashMap configurationFiles) throws
        Exception {
        SiteConfig siteConfig = null;
        SiteConfig currentConfig = loadConfiguration(folderName, isTemplate,
            context,
            configurationFiles);

        if (isTemplate && folderName.equals(SiteConfigUtils.BLANK_TEMPLATE_NAME)) {
            // Blank site must always have site-config.xml
            if (currentConfig == null) {
                throw new Exception(
                    "Blank site configuration file does not exist");
            }
            siteConfig = currentConfig;
            expandLayoutInheritance(siteConfig);
        }
        else {
            if (currentConfig == null) {
                // If site does not have site-config.xml, use blank site's one
                siteConfig = depr_createConfiguration(SiteConfigUtils.
                                                 BLANK_TEMPLATE_NAME, true,
                                                 context, configurationFiles);
                expandLayoutInheritance(siteConfig);
            }
            else {
                // Get parent's configuration
                String templateName;
                if (isTemplate) {
                    // Template is always inherited from blank template
                    templateName = SiteConfigUtils.BLANK_TEMPLATE_NAME;
                }
                else {
                    if (currentConfig.getTemplate() == null ||
                        currentConfig.getTemplate().trim().length() == 0) {
                        templateName = SiteConfigUtils.BLANK_TEMPLATE_NAME;
                    }
                    else {
                        templateName = currentConfig.getTemplate().trim();
                    }
                }

                SiteConfig parentConfig = depr_createConfiguration(templateName, true,
                    context, configurationFiles);

                // Assemble site configuration from parent's configuration +
                // this site configuration

                siteConfig = parentConfig;
                siteConfig.merge(currentConfig);
                expandLayoutInheritance(siteConfig);
            }
        }

        return siteConfig;
    }

    /**
     * Process layouts which extend other layouts. As the result, site
     * configuration will contain expanded layouts (i.e. layouts which have all
     * own and parent's properties)
     * @param siteConfig site configuration
     */
    private void expandLayoutInheritance(SiteConfig siteConfig) {
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
     * Expands given layout
     * @param layout layout to expand
     * @param sourceLayout source storage of layouts
     * @param destinationLayout destination storage of layouts
     */
    private void expandLayoutAndStore(Layout layout, HashMap sourceLayout,
                                      HashMap destinationLayout) {
        // If layout does has parent, expand it
        if (layout.getExtendsLayout() != null) {
            // Was parent layout processed?
            if (!destinationLayout.containsKey(layout.getExtendsLayout())) {
                // If not, then process it first
                expandLayoutAndStore( (Layout) sourceLayout.get(layout.
                    getExtendsLayout()),
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

    /**
     * Returns the current status of site-config.xml change triggering
     * @return the current status of site-config.xml change triggering
     */
    public boolean depr_isTriggerConfigChanges() {
        return triggerConfigChanges;
    }

    /**
     * Sets triggerConfigChanges property. If this property set to true,
     * getSiteConfig() performs last modification file check and reloads
     * configuration, if it changed. if false, then user must reload
     * configuration manually
     * @param triggerConfigChanges new value
     */
    public void depr_setTriggerConfigChanges(boolean triggerConfigChanges) {
        this.triggerConfigChanges = triggerConfigChanges;
    }

}