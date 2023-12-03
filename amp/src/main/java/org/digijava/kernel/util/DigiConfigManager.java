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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.*;
import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.config.moduleconfig.Param;
import org.digijava.kernel.config.moduleconfig.Security;
import org.digijava.kernel.exception.DgException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import org.digijava.kernel.config.ServiceDependencyConfig;


public class DigiConfigManager {

    private static final String ENV_SMTP_HOST = "SMTP_HOST";
    private static final String ENV_SMTP_FROM = "SMTP_FROM";
    private static final String PROP_SMTP_HOST = "smtpHost";
    private static final String PROP_SMTP_FROM = "smtpFrom";

    private static Logger logger = Logger.getLogger(DigiConfigManager.class);

    public static final String CONFIG_FILE = "digi.xml";
    private static final String MODULE_CONFIG_FILE = "module-config.xml";
    private static DigiConfig digiConfig = null;
    private static HashMap moduleConfigs = null;
    private static volatile boolean initialized = false;
    private static Map configBeans = null;

    /**
     * Initializes configuration
     * @param configDirectory String path to directory where main configuration
     * file is stored
     * @throws DgException if configuration error occurs
     */
    public static synchronized void initialize(String configDirectory) throws DgException {
        File configDirFile = new File(configDirectory);
        if (!configDirFile.exists() || !configDirFile.isDirectory()) {
            throw new DgException("Configuration directory " + configDirectory + " does not exist or is not directory");
        } else {
            logger.debug("Parsing configuration from " + configDirectory);
        }

        // Create Digester object
        Digester digester = createConfigDigester();
        File configFile = new File(configDirFile.getAbsolutePath() + File.separator + CONFIG_FILE);
        try {
            logger.debug("Parsing " + configFile.getName());
            digiConfig = (DigiConfig) digester.parse(configFile);
            afterDigiConfigParse();
            logger.info("File " + configFile.getName() + " was parsed successfully");
        }
        catch (Exception ex) {
            logger.debug("Error while parsing DiGi configuration file: " + configFile.getName(), ex);
            throw new DgException(
                "Error while parsing DiGi configuration file: " + configFile.getName(), ex);
        }

        initializeModuleConfigurations(configDirFile);
        initializeConfigBeans();

        initialized = true;
    }

    private static void afterDigiConfigParse() {
        attemptSmtpOverride(System.getenv(ENV_SMTP_HOST), System.getenv(ENV_SMTP_FROM));
        attemptSmtpOverride(System.getProperty(PROP_SMTP_HOST), System.getProperty(PROP_SMTP_FROM));
    }

    private static void attemptSmtpOverride(String host, String from) {
        if (host != null && from != null) {
            Smtp smtp = new Smtp();
            smtp.setHost(host);
            smtp.setFrom(from);
            digiConfig.setSmtp(smtp);
        }
    }

    /**
     * Initializes module configuration according configuration file
     * @param configDirFile File module configuration file
     * @throws DgException if initialization error occurs
     */
    private static void initializeModuleConfigurations(
        File configDirFile) throws DgException {

        Digester digester = createModuleConfigDigester();

        moduleConfigs = new HashMap();

        // parse new format modules
        FileFilter fileFilter = new FileFilter(){
             public boolean accept(File file) {
                 return file.isDirectory();
             }
        };
        File[] subDirectories = configDirFile.listFiles(fileFilter);

        for (int i = 0; i < subDirectories.length; i++) {
            File subDir = subDirectories[i];
            String moduleName = subDir.getName();

            logger.debug("Searching configuration file for module " + moduleName + ":");
            File modConfigFile = new File(subDir.getAbsolutePath() +
                                          File.separator + MODULE_CONFIG_FILE);
            if (modConfigFile.exists()) {
                ModuleConfig moduleConfig = null;
                try {
                    logger.debug("Parsing " + subDir.getName() + File.separator + modConfigFile.getName());
                    moduleConfig = (ModuleConfig) digester.parse(modConfigFile);
                    logger.info("File " + subDir.getName() + File.separator + modConfigFile.getName() + " was parsed successfully");
                }
                catch (Exception ex1) {
                    logger.debug(
                        "Error while parsing configuration file for module " +
                        moduleName, ex1);
                    throw new DgException(
                        "Error while parsing configuration file for module " +
                        moduleName, ex1);
                }

                moduleConfigs.put(moduleName, moduleConfig);
            }

        }

        // Parse old-format modules
        Iterator iter = digiConfig.getModules().values().iterator();
        while (iter.hasNext()) {
            Module module = (Module) iter.next();
            if (moduleConfigs.containsKey(module.getName())) {
                logger.warn("Configuration for module " + module.getName() +
                            " is already defined in " + configDirFile.getAbsolutePath() +
                            " skipping <module> definition");
                continue;
            }
            logger.debug("Parsing configuration file from <module> definition: " + module.getConfig());

            InputStream is =
                DigiConfigManager.class.getClassLoader().
                getResourceAsStream(
                module.getConfig());
            if (is == null) {
                logger.debug("File not found");
            }
            ModuleConfig moduleConfig = null;
            try {
                moduleConfig = (ModuleConfig) digester.parse(is);
            }
            catch (Exception ex1) {
                logger.debug(
                    "Error while parsing configuration file for module " +
                    module.getName(), ex1);
                throw new DgException(
                    "Error while parsing configuration file for module " +
                    module.getName(), ex1);
            }
            moduleConfigs.put(module.getName(), moduleConfig);
        }
    }

    /**
     * Initialize configuration beans
     * @throws DgException if configuration error occurs
     */
    private static void initializeConfigBeans() throws DgException {
        configBeans = new HashMap();
        Iterator configBeanIter = digiConfig.getConfigBeans().values().iterator();
        while (configBeanIter.hasNext()) {
            ConfigBean configBeanDef = (ConfigBean) configBeanIter.next();
            Object configBean = null;
            try {
                configBean = Class.forName(configBeanDef.
                                           getBeanClass()).newInstance();
                BeanUtils.populate(configBean, configBeanDef.getProperties());
                configBeans.put(configBeanDef.getBeanId(), configBean);

            }
            catch (Exception ex) {
                throw new DgException(
                    "Can not initialize configuration bean with id" +
                    configBeanDef.getBeanId(), ex);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Initialized configuration bean " +
                             configBeanDef.getBeanId() +
                             "[" + configBean.toString() + "]"
                             );
            }
        }
    }

    /**
     * Creates Digester instance to parse module configuration file
     * @return Digester instance
     */
    private static Digester createModuleConfigDigester() {
        Digester digester = null;
        try {
            digester = DigesterFactory.newDigester(true, true, null);
        }
        catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex);
        }

        digester.addObjectCreate("module-config", ModuleConfig.class);
        digester.addObjectCreate("module-config/hibernate-classes",HibernateClasses.class);
        digester.addObjectCreate("module-config/hibernate-classes/hibernate-class",HibernateClass.class);

        digester.addSetNext("module-config/hibernate-classes",
                            "setHibernateClasses");
        digester.addSetNext(
            "module-config/hibernate-classes/hibernate-class",
            "addHibernateClass");
        digester.addSetProperties("module-config/hibernate-classes",
                                  "required", "required");
        digester.addBeanPropertySetter(
            "module-config/hibernate-classes/hibernate-class", "content");
        digester.addSetProperties(
            "module-config/hibernate-classes/hibernate-class", "required",
            "required");
        digester.addSetProperties("module-config/hibernate-classes/hibernate-class",
                                  "precache", "precache");
        digester.addSetProperties("module-config/hibernate-classes/hibernate-class",
                                  "filter", "filter");
        digester.addSetProperties("module-config/hibernate-classes/hibernate-class",
                                  "region", "region");

        digester.addBeanPropertySetter("module-config/name");
        digester.addBeanPropertySetter("module-config/default-instance", "defaultInstance");

        digester.addObjectCreate("module-config/security", Security.class);
        digester.addSetNext("module-config/security", "setSecurity");
        digester.addSetProperties("module-config/security", "default",
                                  "defaultAction");
        digester.addSetProperties("module-config/security",
                                  "login-required", "loginRequired");

        digester.addObjectCreate("module-config/security/action", Action.class);
        digester.addSetNext("module-config/security/action", "addAction");
        digester.addSetProperties("module-config/security/action",
                                  "pattern", "pattern");
        digester.addSetProperties("module-config/security/action",
                                  "login-required", "loginRequired");

        digester.addBeanPropertySetter("module-config/security/action",
                                       "value");

        digester.addObjectCreate("module-config/action", Action.class);
        digester.addSetNext("module-config/action", "addAction");
        digester.addSetProperties("module-config/action",
                                  "pattern", "pattern");
        digester.addSetProperties("module-config/action",
                                  "identity-type", "identityType");
        digester.addSetProperties("module-config/action",
                                  "identity-pattern", "identityPattern");
        digester.addSetProperties("module-config/action",
                                  "questionMarkPosition", "questionMarkPosition");
        digester.addSetProperties("module-config/action",
                                  "hideAfterQMark", "hideAfterQMark");


        digester.addObjectCreate("module-config/action/param", Param.class);
        digester.addSetNext("module-config/action/param", "addParam");
        digester.addSetProperties("module-config/action/param",
                                  "name", "name");

        digester.addCallMethod("module-config/webapp-context-listener",
                               "addWebappContextListener", 0);

        return digester;
    }

    /**
     * Creates and returns Digester instance to parse module configuration
     * @return Digester instance
     */
    public static Digester createConfigDigester() {
        //Digester digester = new Digester();
        //digester.setValidating(false);
        //digester.setUseContextClassLoader(true);
        Digester digester = null;
        try {
            digester = DigesterFactory.newDigester(true, true, null);
        }
        catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex);
        }

        digester.addObjectCreate("digi-config", DigiConfig.class);
        digester.addObjectCreate("digi-config/hibernate-classes",
                                 HibernateClasses.class);
        digester.addObjectCreate(
            "digi-config/hibernate-classes/hibernate-class", HibernateClass.class);
        digester.addObjectCreate("digi-config/smtp", Smtp.class);
        digester.addObjectCreate("digi-config/logon-site", LogonSite.class);
        digester.addObjectCreate("digi-config/site-domain", LogonSite.class);
        digester.addObjectCreate("digi-config/module", Module.class);
        digester.addSetNext("digi-config/module", "addModule");
        digester.addSetProperties("digi-config/module", "name", "name");
        digester.addSetProperties("digi-config/module", "config", "config");

        digester.addSetNext("digi-config/logon-site", "setLogonSite");
        digester.addSetProperties("digi-config/logon-site", "id", "id");
        digester.addSetProperties("digi-config/logon-site", "host", "host");
        digester.addSetProperties("digi-config/logon-site", "path", "path");
        digester.addBeanPropertySetter("digi-config/logon-site", "content");
        
        digester.addSetNext("digi-config/site-domain", "setSiteDomain");
        digester.addSetProperties("digi-config/site-domain", "id", "id");
        digester.addSetProperties("digi-config/site-domain", "host", "host");
        digester.addSetProperties("digi-config/site-domain", "path", "path");
        digester.addBeanPropertySetter("digi-config/site-domain", "content");

        digester.addBeanPropertySetter("digi-config/http-port", "httpPort");
        digester.addBeanPropertySetter("digi-config/https-port", "httpsPort");
        
        digester.addBeanPropertySetter("digi-config/ecsDisable", "ecsDisable");
        digester.addBeanPropertySetter("digi-config/ecsServerName", "ecsServerName");
        digester.addBeanPropertySetter("digi-config/propertiesFile", "propertiesFile");
        
        digester.addSetNext("digi-config/hibernate-classes",
                            "setHibernateClasses");
        digester.addSetNext(
            "digi-config/hibernate-classes/hibernate-class",
            "addHibernateClass");
        digester.addSetProperties("digi-config/hibernate-classes",
                                  "required", "required");
        digester.addBeanPropertySetter(
            "digi-config/hibernate-classes/hibernate-class", "content");
        digester.addSetProperties(
            "digi-config/hibernate-classes/hibernate-class", "required",
            "required");
        digester.addSetProperties(
            "digi-config/hibernate-classes/hibernate-class",
            "precache", "precache");
        digester.addSetProperties(
            "digi-config/hibernate-classes/hibernate-class",
            "filter", "filter");
        digester.addSetProperties(
            "digi-config/hibernate-classes/hibernate-class",
            "region", "region");
        digester.addSetProperties("digi-config/hibernate-classes/hibernate-class", "forcePrecache", "forcePrecache");

        digester.addBeanPropertySetter("digi-config/smtp/host", "host");
        digester.addBeanPropertySetter("digi-config/smtp/from", "from");
        digester.addBeanPropertySetter("digi-config/smtp/user-name", "userName");
        digester.addBeanPropertySetter("digi-config/smtp/user-password",
                                       "userPassword");

        digester.addBeanPropertySetter("digi-config/smtp/error-cache",
                                       "cacheMinutes");
        digester.addBeanPropertySetter("digi-config/smtp/clear-log",
                                       "claerLogDays");
        digester.addBeanPropertySetter("digi-config/smtp/clear-scheduler",
                                       "enable");
        digester.addBeanPropertySetter("digi-config/smtp/log-email", "logEmail");

        digester.addSetNext("digi-config/smtp", "setSmtp");

        digester.addBeanPropertySetter("digi-config/enable-logging",
                                       "enableLogging");
        digester.addBeanPropertySetter("digi-config/aggregation", "aggregation");
        digester.addBeanPropertySetter("digi-config/search", "search");
        digester.addSetProperties("digi-config/enable-logging", "poolSize",
                                  "accessLogPoolSize");
        digester.addSetProperties("digi-config/enable-logging", "bufferSize",
                                  "accessLogBuffSize");

        digester.addBeanPropertySetter("digi-config/enable-autologin", "enableAutoLogin");
        
        digester.addBeanPropertySetter("digi-config/site-config",
                                       "siteConfigImpl");
        digester.addBeanPropertySetter("digi-config/job-delay-sec",
                                       "jobDelaySec");
        digester.addBeanPropertySetter("digi-config/track-sessions",
                                       "trackSessions");

        digester.addObjectCreate("digi-config/param-separator", ParamSeparator.class);
        digester.addSetNext("digi-config/param-separator", "setParamSeparator");
        digester.addBeanPropertySetter("digi-config/param-separator", "content");

        digester.addObjectCreate("digi-config/res-cache", ResCache.class);
        digester.addSetNext("digi-config/res-cache", "setResCache");
        digester.addBeanPropertySetter("digi-config/res-cache", "content");

        digester.addObjectCreate("digi-config/param-safeHTML", ParamSafeHTML.class);
        digester.addSetNext("digi-config/param-safeHTML", "setParamSafehtml");
        digester.addBeanPropertySetter("digi-config/param-safeHTML", "content");

        digester.addObjectCreate("digi-config/param-bbTag", ParamBbTag.class);
        digester.addSetNext("digi-config/param-bbTag", "setParamBbTag");
        digester.addBeanPropertySetter("digi-config/param-bbTag", "content");

        digester.addBeanPropertySetter("digi-config/server-type",
                                       "serverType");
        digester.addSetProperties("digi-config/server-type", "prefix",
                                  "domainPrefix");

        digester.addSetProperties("digi-config/server-type", "enableOmniture","enableOmniture");
        digester.addSetProperties("digi-config/server-type","caseSensitiveTranslatioKeys","caseSensitiveTranslatioKeys");

        digester.addObjectCreate("digi-config/forward-emails", ForwardEmails.class);
        digester.addSetNext("digi-config/forward-emails", "setForwardEmails");
        digester.addSetProperties("digi-config/forward-emails",
                                  "enabled", "enabled");
        digester.addCallMethod("digi-config/forward-emails/email",
                               "addEmail", 0);

        digester.addObjectCreate("digi-config/exception-emails",
                                 ExceptionEmails.class);
        digester.addSetNext("digi-config/exception-emails",
                            "setExceptionEmails");
        digester.addCallMethod("digi-config/exception-emails/email",
                               "addEmail", 0);

        digester.addCallMethod("digi-config/ignored-agents/ignored-agent",
                               "addIgnoreAgent", 0);

        digester.addCallMethod("digi-config/agents/agent",
                                "addAgent", 0);

        digester.addCallMethod("digi-config/nonSSOPath/path",
                                "addNoneSSOPath", 0);

        digester.addObjectCreate("digi-config/single-server-jobs",
                                 SingleServerJobs.class);
        digester.addSetNext("digi-config/single-server-jobs",
                            "setSingleServerJobs");

        digester.addObjectCreate("digi-config/single-server-jobs/job",
                                 SingleServerJob.class);
        digester.addSetNext("digi-config/single-server-jobs/job",
                            "addSingleServerJob");
        digester.addSetProperties("digi-config/single-server-jobs/job", "class",
                                  "className");
        digester.addCallMethod("digi-config/single-server-jobs/job/job-host",
                               "addJobHost", 0);

        digester.addObjectCreate("digi-config/config-bean", ConfigBean.class);
        digester.addSetNext("digi-config/config-bean", "addConfigBean");
        digester.addSetProperties("digi-config/config-bean", "id",
                                  "beanId");
        digester.addSetProperties("digi-config/config-bean", "class",
                                  "beanClass");

        digester.addObjectCreate("digi-config/config-bean/property",
                                 KeyValuePair.class);
        digester.addSetNext("digi-config/config-bean/property",
                            "addProperty");
        digester.addSetProperties("digi-config/config-bean/property", "name",
                                  "key");
        digester.addBeanPropertySetter("digi-config/config-bean/property", "value");

        digester.addObjectCreate("digi-config/service", ServiceConfig.class);
        digester.addSetNext("digi-config/service", "addService");
        digester.addSetProperties("digi-config/service", "id",
                                  "serviceId");
        digester.addSetProperties("digi-config/service", "class",
                                  "serviceClass");
        digester.addSetProperties("digi-config/service", "level",
                                  "level");
        digester.addBeanPropertySetter("digi-config/service/description", "description");

        digester.addObjectCreate("digi-config/service/property",
                                 KeyValuePair.class);
        digester.addSetNext("digi-config/service/property",
                            "addProperty");
        digester.addSetProperties("digi-config/service/property", "name",
                                  "key");
        digester.addBeanPropertySetter("digi-config/service/property", "value");

/*
        digester.addObjectCreate("digi-config/service/depends",
                                 ServiceDependencyConfig.class);
        digester.addSetNext("digi-config/service/depends", "addDependency");
        digester.addSetProperties("digi-config/service/depends", "property", "property");
        digester.addSetProperties("digi-config/service/depends", "required", "required");
        digester.addBeanPropertySetter("digi-config/service/depends", "serviceName");
*/
        return digester;
    }

    /**
     * Returns DiGi configuration
     * @return DigiConfig configuration instance
     */
    public static DigiConfig getConfig()  {
        checkAvailable();
        return digiConfig;
    }

    /**
     * Returns module configuration
     * @param moduleName String name of the module
     * @return ModuleConfig module configuration
     */
    public static ModuleConfig getModuleConfig(String moduleName) {
        checkAvailable();
        return (ModuleConfig)moduleConfigs.get(moduleName);
    }

    /**
     * Get module configurations
     * @return HashMap module configurations
     */
    public static HashMap getModulesConfig() {
        checkAvailable();
        return moduleConfigs;
    }

    /**
     * Returns configuration bean
     * @param beanId String configuration bean identifier
     * @return Object configuration bean instance
     */
    public static Object getConfigBean(String beanId) {
        checkAvailable();
        return configBeans.get(beanId);
    }

    /**
     * Returns all configuration beans in id-bean form
     * @return Map all configuration beans
     */
    public static Map getConfigBeans() {
        checkAvailable();
        return configBeans;
    }

    private static void checkAvailable() throws IllegalStateException {
        if (!initialized) {
            throw new IllegalStateException("DiGi Configuration is not yet initialized");
        }
    }

}
