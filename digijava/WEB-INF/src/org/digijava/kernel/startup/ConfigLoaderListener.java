/*
 *   ConfigLoaderListener.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ConfigLoaderListener.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
package org.digijava.kernel.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.security.DBPolicy;
import java.security.Policy;
import org.digijava.kernel.util.SiteCache;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.AccessLogger;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.syndication.aggregator.AggregatorManager;

/**
 * Parses digi.xml configuration file,
 * this class initialize when wheb context load
 * @author Lasha Dolidze
 * @version 1.0
 */

public class ConfigLoaderListener
    extends HttpServlet
    implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ConfigLoaderListener.class);

    public void contextInitialized(ServletContextEvent sce) {

        try {
            ViewConfigFactory.initialize(sce.getServletContext());
            DigiConfigManager.initialize(sce.getServletContext().getRealPath("/repository"));

            // Custom cache manager must be initialized first
            DigiCacheManager.getInstance();
            PersistenceManager.initialize(true);
            DigiPolicy policy = new DigiPolicy();
            policy.install();
            SiteCache.getInstance();

            if( DigiConfigManager.getConfig().isAggregation() ) {
                AggregatorManager.initialize();
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to initialize", ex);
            throw new RuntimeException("Unable to initialize", ex);
        }

    }

    /**
     * contextDestroyed method call when ServletContextListener object destroy
     * see ServletContextListener form more details.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (Policy.getPolicy()instanceof DigiPolicy) {
                DigiPolicy policy = (DigiPolicy) Policy.getPolicy();
                policy.uninstall();
            }
        }
        finally {
            AccessLogger.shutdown();
            PersistenceManager.cleanup();
            // Custom cache manager must be shut after all other cleanup stuff
            DigiCacheManager.shutdown();
        }
    }
}
