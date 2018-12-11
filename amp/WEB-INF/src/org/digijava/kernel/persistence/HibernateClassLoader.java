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

package org.digijava.kernel.persistence;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.services.AmpOfflineVersion;
import org.digijava.kernel.services.AmpOfflineVersionType;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.persistence.interceptors.AmpEntityInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.ClassicAvgFunction;
import org.hibernate.dialect.function.ClassicCountFunction;
import org.hibernate.dialect.function.ClassicSumFunction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.Entity;

/**
 * Hibernate Class loader, see digi.xml file for more details.
 * 
 * @author Lasha Dolidze
 * @version 1.0
 */
public class HibernateClassLoader {

    private static Logger logger = I18NHelper
            .getKernelLogger(HibernateClassLoader.class);

    private static SessionFactory sessionFactory;
    private static Configuration cfg = null;
    public static String HIBERNATE_CFG_XML = "/hibernate.cfg.xml";

    /**
     * for testcases - override the database defined in the
     * {@link #HIBERNATE_CFG_XML} conf file, if not null
     */
    public static String HIBERNATE_CFG_OVERRIDE_DATABASE;

    /**
     * get Hibernate SessionFactory object, you will call openSession(); to
     * obtain a JDBC connection and instantiate a new Session. form example:
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
     * initialize hibernate classes for kernel see module configuration file for
     * more details
     * 
     */
    public static void initialize(DigiConfig config) {

        loadHibernateClasses(config.getHibernateClasses());
    }

    /**
     * initialize hibernate classes for module see module configuration file for
     * more details
     * 
     * @param config
     */
    public static void initialize(HashMap config) {

        Iterator iterModules = config.keySet().iterator();
        while (iterModules.hasNext()) {
            String moduleName = (String) iterModules.next();
            ModuleConfig moduleConfig = (ModuleConfig) config.get(moduleName);

            HibernateClasses classes = moduleConfig.getHibernateClasses();
            if (classes != null) {
                loadHibernateClasses(classes);
            } else {
                logger.warn("No hibernate classes for module " + moduleName);
            }
        }

        /* Add support for annotates classes not finish yet */
        // the following will detect all classes that are annotated as @Entity
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        /*
         * only register classes within package
         * org.digijava.kernel.ampapi.postgis.entity need to take the
         * configuration file /repository/hibernate-annotated.xml
         */

        for (BeanDefinition bd : scanner
                .findCandidateComponents("org.digijava.kernel.ampapi.postgis.entity")) {
            String name = bd.getBeanClassName();
            try {
                // Add each class to the to the persistence content
                cfg.addAnnotatedClass(Class.forName(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param classes
     */
    public static void loadHibernateClasses(HibernateClasses classes) {

        boolean required = false;

        if (cfg == null) {
            cfg = new LocalHibernateConfig();
            cfg.addSqlFunction("count", new ClassicCountFunction());
            cfg.addSqlFunction("avg", new ClassicAvgFunction());
            cfg.addSqlFunction("sum", new ClassicSumFunction());
            cfg.registerTypeOverride(new AmpOfflineVersionType(), new String[]{AmpOfflineVersion.class.getName()});
        }

        if (classes == null) {
            throw new IllegalArgumentException(
                    "classes parameter must be not-null");
        }

        Iterator iter = classes.iterator();
        while (iter.hasNext()) {
            HibernateClass hibernateClass = (HibernateClass) iter.next();
            try {

                // check if class is critical resource to load
                // see catch block
                required = (((classes.getRequired() == null || classes
                        .getRequired().equalsIgnoreCase("true")) && (hibernateClass
                        .getRequired() == null || !hibernateClass.getRequired()
                        .equalsIgnoreCase("false"))) || ((classes.getRequired() != null && classes
                        .getRequired().equalsIgnoreCase("false")) && (hibernateClass
                        .getRequired() != null && hibernateClass.getRequired()
                        .equalsIgnoreCase("true"))));

                if (logger.isDebugEnabled()) {
                    Object[] params = { hibernateClass.getContent() };
                    logger.l7dlog(Level.DEBUG,
                            "HibernateClassLoader.loadingHibernateClass",
                            params, null);
                }

                // adding class to load
                cfg.addClass(Class.forName(hibernateClass.getContent()));
            } catch (Exception ex) {
                Object[] params = { hibernateClass.getContent() };
                if (required) {
                    logger.l7dlog(Level.FATAL,
                            "HibernateClassLoader.loadingHibernateClass.error",
                            params, ex);
                    break;
                } else {
                    logger.l7dlog(Level.ERROR,
                            "HibernateClassLoader.loadingHibernateClass.error",
                            params, ex);
                }
            }

        }
    }

    /**
     *
     */
    public static void buildHibernateSessionFactory() {
        InputStream inp = HibernateClassLoader.class
                .getResourceAsStream(HIBERNATE_CFG_XML);
        try {
            if (inp == null) {
                cfg.setInterceptor(new AmpEntityInterceptor());
                sessionFactory = cfg.buildSessionFactory();
            } else {
                Configuration newConfig = cfg.configure(HIBERNATE_CFG_XML);
                newConfig.setInterceptor(new AmpEntityInterceptor());
                if (HIBERNATE_CFG_OVERRIDE_DATABASE != null)
                    newConfig.setProperty("hibernate.connection.url",
                            HIBERNATE_CFG_OVERRIDE_DATABASE);
                HIBERNATE_CFG_OVERRIDE_DATABASE = null; // reset after each
                                                        // session
                sessionFactory = newConfig.buildSessionFactory();
            }
        } catch (Exception ex1) {
            logger.fatal("Unable to build hibernate session factory", ex1);
            throw new RuntimeException(ex1);
        }
    }
}

class LocalHibernateConfig extends Configuration {

    /**
     * Default UID
     */
    private static final long serialVersionUID = -6375194723506753313L;

    private static Logger logger = Logger.getLogger(HibernateClassLoader.class);

    /**
     * @see org.hibernate.cfg.Configuration#getConfigurationInputStream(java.lang.String)
     */
    protected InputStream getConfigurationInputStream(String resource)
            throws HibernateException {

        logger.info("Configuration resource: " + resource);

        InputStream stream = HibernateClassLoader.class
                .getResourceAsStream(resource);
        if (stream == null) {
            logger.warn(resource + " not found");
            throw new HibernateException(resource + " not found");
        }
        return stream;
    }

}
