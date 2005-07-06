/*
 *   DigiConfigManager.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: DigiConfigManager.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.ForwardEmails;
import org.digijava.kernel.config.ExceptionEmails;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.config.LogonSite;
import org.digijava.kernel.config.ParamBbTag;
import org.digijava.kernel.config.ParamSafeHTML;
import org.digijava.kernel.config.ParamSeparator;
import org.digijava.kernel.config.ResCache;
import org.digijava.kernel.config.Smtp;
import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.config.moduleconfig.Param;
import org.digijava.kernel.config.moduleconfig.Security;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.config.Module;
import java.io.InputStream;
import org.digijava.kernel.config.SingleServerJobs;
import org.digijava.kernel.config.SingleServerJob;


public class DigiConfigManager {

    private static Logger logger = Logger.getLogger(DigiConfigManager.class);

    private static final String CONFIG_FILE = "digi.xml";
    private static final String MODULE_CONFIG_FILE = "module-config.xml";
    private static DigiConfig digiConfig = null;
    private static HashMap moduleConfigs = null;
    private static volatile boolean initialized = false;

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
            logger.info("File " + configFile.getName() + " was parsed successfully");
        }
        catch (Exception ex) {
            logger.debug("Error while parsing DiGi configuration file: " + configFile.getName(), ex);
            throw new DgException(
                "Error while parsing DiGi configuration file: " + configFile.getName(), ex);
        }

        digester = createModuleConfigDigester();

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
                            " is already defined in " + configDirectory +
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

        initialized = true;
    }

    private static Digester createModuleConfigDigester() {
        Digester digester = new Digester();

        digester.clear();
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

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

        return digester;
    }

    private static Digester createConfigDigester() {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

        digester.addObjectCreate("digi-config", DigiConfig.class);
        digester.addObjectCreate("digi-config/hibernate-classes",HibernateClasses.class);
        digester.addObjectCreate("digi-config/hibernate-classes/hibernate-class",HibernateClass.class);
        digester.addObjectCreate("digi-config/smtp", Smtp.class);
        digester.addObjectCreate("digi-config/logon-site", LogonSite.class);

        digester.addObjectCreate("digi-config/module", Module.class);
        digester.addSetNext("digi-config/module", "addModule");
        digester.addSetProperties("digi-config/module", "name", "name");
        digester.addSetProperties("digi-config/module", "config", "config");

        digester.addSetNext("digi-config/logon-site", "setLogonSite");
        digester.addSetProperties("digi-config/logon-site", "id", "id");
        digester.addSetProperties("digi-config/logon-site", "host", "host");
        digester.addSetProperties("digi-config/logon-site", "path", "path");
        digester.addBeanPropertySetter("digi-config/logon-site", "content");

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
        digester.addSetProperties("digi-config/hibernate-classes/hibernate-class",
                                  "precache", "precache");
        digester.addSetProperties("digi-config/hibernate-classes/hibernate-class",
                                  "filter", "filter");
        digester.addSetProperties("digi-config/hibernate-classes/hibernate-class",
                                  "region", "region");

        digester.addBeanPropertySetter("digi-config/smtp/host", "host");
        digester.addBeanPropertySetter("digi-config/smtp/from", "from");
        digester.addBeanPropertySetter("digi-config/smtp/user-name", "userName");
        digester.addBeanPropertySetter("digi-config/smtp/user-password", "userPassword");
        digester.addSetNext("digi-config/smtp", "setSmtp");

        digester.addBeanPropertySetter("digi-config/enable-logging", "enableLogging");
        digester.addBeanPropertySetter("digi-config/aggregation", "aggregation");
        digester.addSetProperties("digi-config/enable-logging", "poolSize", "accessLogPoolSize");
        digester.addSetProperties("digi-config/enable-logging", "bufferSize", "accessLogBuffSize");

        digester.addBeanPropertySetter("digi-config/site-config", "siteConfigImpl");
        digester.addBeanPropertySetter("digi-config/job-delay-sec", "jobDelaySec");
        digester.addBeanPropertySetter("digi-config/track-sessions", "trackSessions");

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
        digester.addSetProperties("digi-config/server-type", "prefix", "domainPrefix");

        digester.addObjectCreate("digi-config/forward-emails", ForwardEmails.class);
        digester.addSetNext("digi-config/forward-emails", "setForwardEmails");
        digester.addSetProperties("digi-config/forward-emails",
                                  "enabled", "enabled");
        digester.addCallMethod("digi-config/forward-emails/email",
                               "addEmail", 0);

        digester.addObjectCreate("digi-config/exception-emails", ExceptionEmails.class);
        digester.addSetNext("digi-config/exception-emails", "setExceptionEmails");
        digester.addCallMethod("digi-config/exception-emails/email",
                               "addEmail", 0);

        digester.addCallMethod("digi-config/ignored-agents/ignored-agent", "addIgnoredAgent", 0);


	digester.addObjectCreate("digi-config/single-server-jobs",SingleServerJobs.class);
	digester.addSetNext("digi-config/single-server-jobs", "setSingleServerJobs");

	digester.addObjectCreate("digi-config/single-server-jobs/job",SingleServerJob.class);
	digester.addSetNext("digi-config/single-server-jobs/job", "addSingleServerJob");
	digester.addSetProperties("digi-config/single-server-jobs/job", "class", "className");
	digester.addCallMethod("digi-config/single-server-jobs/job/job-host",
			       "addJobHost", 0);

        return digester;
    }

    public static DigiConfig getConfig()  {
        checkAvailable();
        return digiConfig;
    }

    public static ModuleConfig getModuleConfig(String moduleName) {
        checkAvailable();
        return (ModuleConfig)moduleConfigs.get(moduleName);
    }

    public static HashMap getModulesConfig() {
        checkAvailable();
        return moduleConfigs;
    }

    private static void checkAvailable() throws IllegalStateException {
        if (!initialized) {
            throw new IllegalStateException("DiGi Configuration is not yet initialized");
        }
    }

}
