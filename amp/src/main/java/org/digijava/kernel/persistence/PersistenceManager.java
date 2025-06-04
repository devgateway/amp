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

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.startup.HibernateSessionRequestFilter;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.hibernate.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.FlushModeType;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PersistenceManager {

    private static final Logger logger = I18NHelper.getKernelLogger(PersistenceManager.class);
    private static SessionFactory sf;
    private static Configuration cfg;
    private static final AtomicInteger activeSessions = new AtomicInteger();

    public static final String PRECACHE_REGION =
            "org.digijava.kernel.persistence.PersistenceManager.precache_region";

    /**
     * The maximum allowed life for an opened hibernate session, in milliseconds
     */
    private static void precache() throws DgException {
        DigiConfig config = DigiConfigManager.getConfig();
        boolean doAgain = true;

        if (config == null) {
            throw new IllegalStateException(
                    "precache() must be called after initialize()");
        }

        String disablePrecache=System.getProperty("amp.disablePrecache");
        if(disablePrecache!=null && "true".equalsIgnoreCase(disablePrecache)) {
            logger.info("amp.disablePrecache is true. Pracache skipped. DO NOT USE THIS ON PRODUCTION!");
            return;
        }

        Session session = null;
        HibernateClasses classes = config.getHibernateClasses();
        try {
            session = sf.openSession();
            logger.info("Starting precache");
            Iterator iter = classes.iterator();
            while (iter.hasNext()) {
                HibernateClass hibernateClass = (HibernateClass) iter.next();

                logger.debug("Analyzing configuration for class: " +
                        hibernateClass.getContent());
                if (hibernateClass.isPrecache()) {
                    logger.debug("Precaching class:" +
                            hibernateClass.getContent() + " filter:" +
                            hibernateClass.getFilter() + " region:" +
                            hibernateClass.getRegion());
                    String precacheHql = "from " + hibernateClass.getContent() +
                            " obj ";
                    if (hibernateClass.getFilter() != null) {
                        precacheHql += " where " + hibernateClass.getFilter();
                    }

                    if (hibernateClass.getRegion() == null) {
                        Query query = session.createQuery(precacheHql);
                        query.setCacheable(true);
                        query.setCacheRegion(PRECACHE_REGION);

                        query.list();
                        if (doAgain) {
                            doAgain = false;
                            long delay = DigiConfigManager.getConfig().
                                    getJobDelaySec();
                            if (delay != 0) {
                                logger.debug("Suspending for " + delay +
                                        " sec(s)");
                                try {
                                    Thread.sleep(delay * 1000);
                                }
                                catch (InterruptedException iex) {
                                    logger.warn("Job delay exception",
                                            iex);
                                }
                            }
                            logger.debug("Running precache for " +
                                    hibernateClass.getContent() + " again");
                            query.list();
                        }
                    }
                    else {
                        precacheToRegion(session, hibernateClass.getContent(),
                                hibernateClass.getRegion(),
                                precacheHql, hibernateClass.isForcePrecache());
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Precache Exception", ex);
            throw new DgException(ex);
        }
        finally {
            if (session != null) {
                try {
                    session.close();
                }
                catch (Exception ex1) {
                    logger.error("Precache Exception", ex1);
                }
            }
        }
    }

    private static void precacheToRegion(Session session,
                                         String className, String regionName,
                                         String precacheHql, boolean forcePrecache) throws
            HibernateException, DgException {
        AbstractCache region = DigiCacheManager.getInstance().getCache(regionName);
        DigiConfig config = DigiConfigManager.getConfig();
        if (region == null) {
            logger.debug("Unable to create cache region " + regionName +
                    " to precache class: " + className);
            throw new DgException("Unable to create cache region " +
                    regionName + " to precache class: " +
                    className);
        }
        else {
            logger.debug("Using region " + region.getType());
        }
        if (forcePrecache || (!forcePrecache && region.getSize() <= 0)) {
            Map precache = new HashMap();
            try {
                Class clazz = Class.forName(className);
                ClassMetadata meta = sf.getClassMetadata(clazz);
                if (meta == null) {
                    logger.warn("Unable to load hibernate metadata for class: " +
                            className);
                    return;
                }
                List rows = session.createQuery(precacheHql).list();
                Iterator rowIter = rows.iterator();
                while (rowIter.hasNext()) {
                    Object item = rowIter.next();
                    Serializable id = meta.getIdentifier(item,(SessionImplementor)session);
                    if (id == null) {
                        String errMsg = "One of the object identities is null for class: " +className;
                        logger.error(errMsg);
                        throw new DgException(errMsg);
                    }
                    //Separate case for translations: we need to process translation keys.
                    if (Message.class.getName().equals(className) && !config.isCaseSensitiveTranslatioKeys()){
                        Message msgId = (Message) id;
                        Message msg = (Message) item;
                        TranslatorWorker.getInstance().processKeyCase(msg);
                        TranslatorWorker.getInstance().processKeyCase(msgId);
                    }
                    region.put(id, item);
                }
                logger.debug(
                        "Map was prepared successfully. Putting into cache");
                if (region == null) {
                    logger.debug("region is null!!!");
                }
                //region.precache(precache);
                logger.debug("precacheToRegion() complete");
            }
            catch (ClassNotFoundException ex2) {
                logger.error("Unable to load class " + className +
                        " for precaching", ex2);
            }
        }
        else {
            logger.info("Region " + regionName +
                    " is already filled, skiping precache");
        }
    }
    public static final long MAX_HIBERNATE_SESSION_LIFE_MILLIS = 60 * 60 * 1000;

    private static final HashMap<Session, Object[]> sessionStackTraceMap = new HashMap<>();
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> CURRENT_SESSION_IS_MANAGED = ThreadLocal.withInitial(() -> false);

    /**
     * Initialize PersistenceManager
     */
    public static void initialize(boolean precache) {
        initialize(precache, null);
    }

    public static synchronized void initialize(boolean precache, String target) {
        DigiConfig config = null;
        HashMap modulesConfig = null;
        try {
            config = DigiConfigManager.getConfig();
            logger.debug("Initializing persistence manager");

            // load kernel hibernate classes
            HibernateClassLoader.initialize(config);

            if (target != null && !target.equalsIgnoreCase("kernel")) {
                Object modConfig = DigiConfigManager.getModulesConfig().get(target);
                if (modConfig != null) {
                    modulesConfig = new HashMap();
                    modulesConfig.put(target, modConfig);
                }
            } else {
                modulesConfig = DigiConfigManager.getModulesConfig();
            }

            // load module hibernate classes
            if (modulesConfig != null) {
                HibernateClassLoader.initialize(modulesConfig);
            }

            HibernateClassLoader.buildHibernateSessionFactory();

            sf = HibernateClassLoader.getSessionFactory();
            cfg = HibernateClassLoader.getConfiguration();

            if (precache) {
                precache();
            }
        } catch (Exception ex) {
            logger.fatal("Unable to initialize PersistenceManager", ex);
        }
    }

    /**
     * Clean up resources
     */
    public static void cleanup() {
        logger.debug("cleanup() called");
        if (sf != null) {
            try {
                closeUnclosedSessionsFromTraceMap();
                sf.close();
            } catch (HibernateException ex) {
                logger.error("Error cleaning up persistence manager", ex);
            }
        }
    }

    /**
     * Clean up thread-local resources
     */
    public static void cleanupThread() {
        Session session = threadSession.get();
        if (session != null) {
            closeSession(session);
        }
        threadSession.remove();
        CURRENT_SESSION_IS_MANAGED.remove();
    }

    // [Previous methods like precache(), precacheToRegion(), etc. remain unchanged]
    // [... existing methods ...]

    /**
     * Open a new unmanaged Hibernate session
     */
    public static Session openNewSession() {
        Session session = sf.openSession();
        activeSessions.incrementAndGet();
        addSessionToStackTraceMap(session);
        logger.debug("Opened new unmanaged session. Active sessions: " + activeSessions.get());
        return session;
    }

    /**
     * Close an unmanaged Hibernate session
     */
    public static void closeSession(Session session) {
        if (session == null) return;

        try {
            if (session.isOpen()) {
                if (session.getTransaction().isActive()) {
                    try {
                        session.getTransaction().rollback();
                    } catch (HibernateException e) {
                        logger.error("Failed to rollback transaction during close", e);
                    }
                }
                session.close();
                activeSessions.decrementAndGet();
                logger.debug("Closed unmanaged session. Active sessions: " + activeSessions.get());
            }
        } catch (HibernateException e) {
            logger.error("Failed to close session", e);
        } finally {
            removeSessionFromMap(session);
            if (session.equals(threadSession.get())) {
                clearCurrentSession();
            }
        }
    }

    /**
     * Get current session or create new one if none exists
     */
    public static Session getSession() {
        Session session = threadSession.get();
        if (session == null || !session.isOpen()) {
            session = sf.openSession();
            session.setHibernateFlushMode(FlushMode.AUTO);
            threadSession.set(session);
            addSessionToStackTraceMap(session);
            activeSessions.incrementAndGet();
            logger.debug("Opened new managed session. Active sessions: " + activeSessions.get());
        }
        return session;
    }

    public static Session getRequestDBSession() {
        return getSession();
    }

    public static void setCurrentSession(Session session) {
        threadSession.set(session);
    }

    public static void clearCurrentSession() {
        threadSession.remove();
    }

    public static boolean isSessionManaged() {
        return CURRENT_SESSION_IS_MANAGED.get();
    }

    /**
     * Execute work in transaction
     */
    public static void inTransaction(Runnable runnable) {
        supplyInTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> T supplyInTransaction(Supplier<T> supplier) {
        Session session = getSession();
        boolean isSessionProvided = (session != null);
        boolean prevManagedFlag = CURRENT_SESSION_IS_MANAGED.get();

        try {
            CURRENT_SESSION_IS_MANAGED.set(true);

            if (!isSessionProvided || !session.isOpen()) {
                session = sf().openSession();
                session.beginTransaction();
                setCurrentSession(session);
            }

            T result = supplier.get();

            if (!isSessionProvided) {
                session.getTransaction().commit();
            }

            return result;

        } catch (Throwable e) {
            if (!isSessionProvided && session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            if (!isSessionProvided && session != null && session.isOpen()) {
                session.close();
                clearCurrentSession();
            }
            CURRENT_SESSION_IS_MANAGED.set(prevManagedFlag);
        }
    }

    // [Session tracking methods]
    private static void addSessionToStackTraceMap(Session sess) {
        synchronized (sessionStackTraceMap) {
            if (!sessionStackTraceMap.containsKey(sess)) {
                sessionStackTraceMap.put(sess, new Object[] {
                        System.currentTimeMillis(),
                        Thread.currentThread().getStackTrace(),
                        new Exception("Session creation point")
                });
            }
        }
    }

    private static void removeSessionFromMap(Session session) {
        synchronized (sessionStackTraceMap) {
            sessionStackTraceMap.remove(session);
        }
    }

    public static void checkClosedOrLongSessionsFromTraceMap() {
        synchronized (sessionStackTraceMap) {
            Iterator<Session> iterator = sessionStackTraceMap.keySet().iterator();
            while (iterator.hasNext()) {
                Session session = iterator.next();

                if (session.isOpen() && (System.currentTimeMillis() - (Long) sessionStackTraceMap.get(session)[0] > MAX_HIBERNATE_SESSION_LIFE_MILLIS)) {
                    StackTraceElement[] stackTrace = (StackTraceElement[]) sessionStackTraceMap.get(session)[1];
                    logger.warn("Forcing closure of long-running session " + session.hashCode());
                    for (int i = 0; i < stackTrace.length && i < 8; i++) {
                        logger.warn(stackTrace[i].toString());
                    }

                    try {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        session.close();
                    } catch (Throwable e) {
                        logger.error("Error forcing session closure", e);
                    }
                }

                if (!session.isOpen()) {
                    iterator.remove();
                }
            }
        }
    }

    public static void closeUnclosedSessionsFromTraceMap() {
        synchronized (sessionStackTraceMap) {
            Iterator<Session> iterator = sessionStackTraceMap.keySet().iterator();
            while (iterator.hasNext()) {
                Session session = iterator.next();
                if (session.isOpen()) {
                    Object[] o = sessionStackTraceMap.get(session);
                    logger.warn("Force closing session opened " + (System.currentTimeMillis() - (Long) o[0]) + "ms ago");
                    try {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        session.close();
                    } catch (Throwable t) {
                        logger.error("Error closing session", t);
                    }
                }
                iterator.remove();
            }
        }
    }

    // [Additional helper methods]
    public static Connection getJdbcConnection() throws SQLException {
        SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
        return sfi.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
    }

    public static ClassMetadata getClassMetadata(Class<?> clazz) {
        return sf.getClassMetadata(clazz);
    }

    public static PersistentClass getClassMapping(Class<?> clazz) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();
        MetadataSources sources = new MetadataSources(registry);
        Metadata metadata = sources.buildMetadata();
        return metadata.getEntityBinding(clazz.getName());
    }

    public static SessionFactory sf() {
        return sf;
    }

    // [Other existing methods...]
}
