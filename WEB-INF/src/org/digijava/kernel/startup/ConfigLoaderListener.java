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

package org.digijava.kernel.startup;

import java.io.File;
import java.security.Policy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.mail.scheduler.MailSpoolManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.service.WebappServiceContext;
import org.digijava.kernel.util.AccessLogger;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.viewmanager.ViewConfigFactory;

/**
 * Parses digi.xml configuration file,
 * this class initialize when wheb context load
 * @author Lasha Dolidze
 * @version 1.0
 */

public class ConfigLoaderListener
    extends HttpServlet implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ConfigLoaderListener.class);

    private static String MODULE_LISTENERS = ConfigLoaderListener.class.
        getName() + ".moduleContextListeners";

    public void contextInitialized(ServletContextEvent sce) {
        //ResourceStreamHandlerFactory.installIfNeeded();

        try {
            String jaasConfPath = sce.getServletContext().getRealPath(
                "/WEB-INF/jaas.config");
            if (jaasConfPath != null) {
                File file = new File(jaasConfPath);
                if (file.exists()) {
                    System.setProperty("java.security.auth.login.config",
                                       jaasConfPath);
                }
            }

            // Custom cache manager must be initialized first
            DigiConfigManager.initialize(sce.getServletContext().
                                         getRealPath(
                                             "/repository"));
            // Initialize services
            ServiceContext serviceContext = new WebappServiceContext(sce.
                getServletContext());
            ServiceManager.getInstance().init(serviceContext, 0);

            DigiCacheManager.getInstance();
            PersistenceManager.initialize(true);

            SiteCache.getInstance();
            DigiPolicy policy = new DigiPolicy();
            policy.install();

            if (DigiConfigManager.getConfig().getSmtp().isEnable()) {
                MailSpoolManager.initialize();
            }

            // Initialize modules
            Map listeners = getModuleContextInitializers();
            Iterator iter = listeners.values().iterator();
            while (iter.hasNext()) {
                ServletContextListener item = (ServletContextListener) iter.
                    next();
                item.contextInitialized(sce);
            }
            sce.getServletContext().setAttribute(MODULE_LISTENERS, listeners);

            // Initialize services
            ServiceManager.getInstance().init(serviceContext, 1);

            ViewConfigFactory.initialize(sce.getServletContext());

        }
        catch (Exception ex) {
            logger.debug("Unable to initialize", ex);
            throw new RuntimeException("Unable to initialize", ex);
        }

    }

    private Map getModuleContextInitializers() throws ClassNotFoundException,
        InstantiationException, IllegalAccessException, InstantiationException,
        ClassCastException {
        // Initialize modules
        Map contextListeners = new HashMap();
        Iterator moduleConfigIter = DigiConfigManager.getModulesConfig().
            entrySet().iterator();
        while (moduleConfigIter.hasNext()) {
            Map.Entry configRecord = (Map.Entry) moduleConfigIter.next();
            ModuleConfig moduleConfig = (ModuleConfig) configRecord.getValue();
            Iterator moduleLsnrIter = moduleConfig.getWebappContextListeners().
                iterator();
            while (moduleLsnrIter.hasNext()) {
                String listenerClassName = (String) moduleLsnrIter.next();
                if (!contextListeners.containsKey(listenerClassName)) {
                    logger.debug("Loading context listener class " +
                                 listenerClassName + " for module " +
                                 ( (String) configRecord.getKey()));
                    Class listenerClass = Class.forName(listenerClassName);
                    ServletContextListener listenerObj = (
                        ServletContextListener) listenerClass.newInstance();
                    contextListeners.put(listenerClassName, listenerObj);
                }
            }
        }
        return contextListeners;
    }

    /**
     * contextDestroyed method call when ServletContextListener object destroy
     * see ServletContextListener form more details.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        ServiceManager.getInstance().shutdown(1);
        try {
            Map listeners = (Map) sce.getServletContext().getAttribute(
                MODULE_LISTENERS);
            if (listeners != null) {
                Iterator iter = listeners.values().iterator();
                while (iter.hasNext()) {
                    ServletContextListener item = (ServletContextListener) iter.
                        next();
                    item.contextDestroyed(sce);
                }
            }
        }
        catch (Exception ex) {
            logger.warn("Module destruction failed", ex);
        }

        try {
            if (Policy.getPolicy() instanceof DigiPolicy) {
                DigiPolicy policy = (DigiPolicy) Policy.getPolicy();
                policy.uninstall();
            }
        }
        finally {
            AccessLogger.shutdown();
            PersistenceManager.cleanup();
            // Custom cache manager must be shut after all other cleanup stuff
            DigiCacheManager.shutdown();
            ServiceManager.getInstance().shutdown(0);

        }
    }
}
