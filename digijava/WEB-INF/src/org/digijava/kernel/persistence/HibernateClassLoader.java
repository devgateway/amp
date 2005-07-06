/*
 *   HibernateClassLoader.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: HibernateClassLoader.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.kernel.persistence;

import java.util.Iterator;

import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.startup.ConfigListener;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import org.digijava.kernel.util.DigiConfigManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.I18NHelper;
import net.sf.hibernate.Session;
import java.util.HashMap;
import java.util.*;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;

/**
 * Hibernate Class loader, see digi.xml file for more details.
 *
 * @author Lasha Dolidze
 * @version 1.0
 */
public class HibernateClassLoader {

    private static Logger logger = I18NHelper.getKernelLogger(HibernateClassLoader.class);

    private static SessionFactory sessionFactory;
    private static Configuration cfg = null;

    /**
     * get Hibernate SessionFactory object, you will call openSession();
     * to obtain a JDBC connection and instantiate a new Session.
     * form example:
         * Session session = HibernateClassLoader.getSessionFactory().openSession();
     *
     * @return hibernate SessionFactory object
     * @throws HibernateException
     */
    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static synchronized Configuration getConfiguration() {
        return cfg;
    }

    /**
     * initialize hibernate classes for kernel
     * see module configuration file for more details
     *
     */
    public static void initialize(DigiConfig config) {

        loadHibernateClasses(config.getHibernateClasses());
    }


    /**
     * initialize hibernate classes for module
     * see module configuration file for more details
     *
     * @param config
     */
    public static void initialize(HashMap config) {

        Iterator iterModules = config.keySet().iterator();
        while (iterModules.hasNext()) {
            String moduleName = (String) iterModules.next();
            ModuleConfig moduleConfig = (ModuleConfig) config.get(moduleName);

            HibernateClasses classes = moduleConfig.getHibernateClasses();
            if( classes != null ) {
                loadHibernateClasses(classes);
            } else {
                logger.warn("No hibernate classes for module " + moduleName);
            }
        }
    }

    /**
     *
     * @param classes
     */
    public static void loadHibernateClasses(HibernateClasses classes) {

        boolean required = false;

        if (cfg == null)
            cfg = new Configuration();

        if( classes == null ) {
            throw new IllegalArgumentException("classes parameter must be not-null");
        }

        Iterator iter = classes.iterator();
        while (iter.hasNext()) {
            HibernateClass hibernateClass = (HibernateClass) iter.next();
            try {

                // check if class is critical resource to load
                // see catch block
                required = ( ( (classes.getRequired() == null ||
                                classes.getRequired().equalsIgnoreCase(
                    "true")) &&
                              (hibernateClass.getRequired() == null ||
                               !hibernateClass.getRequired().
                               equalsIgnoreCase(
                    "false"))) ||
                            ( (classes.getRequired() != null &&
                               classes.getRequired().equalsIgnoreCase(
                    "false")) &&
                             (hibernateClass.getRequired() != null &&
                              hibernateClass.getRequired().equalsIgnoreCase(
                    "true")))
                            );

                if (logger.isDebugEnabled()) {
                    Object[] params = {
                        hibernateClass.getContent()};
                    logger.l7dlog(Level.DEBUG,
                        "HibernateClassLoader.loadingHibernateClass",
                        params, null);
                }

                // adding class to load
                cfg.addClass(Class.forName(hibernateClass.getContent()));
            }
            catch (Exception ex) {
                Object[] params = {
                    hibernateClass.getContent()};
                if (required) {
                    logger.l7dlog(Level.FATAL,
                        "HibernateClassLoader.loadingHibernateClass.error",
                        params, null);
                    break;
                }
                else {
                    logger.l7dlog(Level.ERROR,
                        "HibernateClassLoader.loadingHibernateClass.error",
                        params, null);
                }
            }

        }
    }

    /**
     *
     */
    public static void buildHibernateSessionFactory() {
       try {
           sessionFactory = cfg.buildSessionFactory();
       }
       catch (HibernateException ex1) {
           logger.l7dlog(Level.FATAL, "HibernateClassLoader.buildSessionFactory.error", null, ex1);
           throw new RuntimeException(ex1);
       }
   }


}