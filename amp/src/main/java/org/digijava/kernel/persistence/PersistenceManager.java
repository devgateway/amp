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

    private static SessionFactory sf;
    private static Configuration cfg;
    private static Logger logger = I18NHelper.getKernelLogger(PersistenceManager.class);
    private static final AtomicInteger activeSessions = new AtomicInteger();


    private static final HashMap<Session, Object[]> sessionStackTraceMap = new HashMap<>();

    public static String PRECACHE_REGION =
            "org.digijava.kernel.persistence.PersistenceManager.precache_region";


    /**
     * The maximum allowed life for an opened hibernate session, in miliseconds
     */
    public static final long MAX_HIBERNATE_SESSION_LIFE_MILLIS=60*60*1000;




    /**
     * Invoked at the end of each request. Iterates and removes Hibernate closed sessions from the trace map.
     * It also checks the long running sessions and forces closure on ones that are longer than
     * The {@link HashMap} is synchronized to prevent concurrency issues between HTTP threads
     */
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
    /**
     * Removes only the closed sessions from the map that tracks opened sessions.
     * No other check is being done.
     */
    private static void removeClosedSessionsFromMap() {
        int count   = 0;
        synchronized (sessionStackTraceMap) {
            Iterator<Entry<Session, Object[]>> iterator = PersistenceManager.sessionStackTraceMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Session sess    = iterator.next().getKey();
                if ( !sess.isOpen() ) {
                    iterator.remove();
                    count++;
                }
            }
        }
        logger.debug( count + " closed sessions were removed from 'sessionStackTraceMap' ");
    }

    public static void cleanupThread() {
        Session session = threadSession.get();
        if (session != null) {
            closeSession(session);
        }
        threadSession.remove();
        CURRENT_SESSION_IS_MANAGED.remove();
    }

    /**
     * Opens a new Hibernate session. Use this with caution.
     * For servlets you will not require to use this, use {@link #getSession()} instead!
     * This method returns you an unmanaged Hibernate session, that you need to close yourself!
     * @return
     */
    public static Session openNewSession() {
        Session session = sf.openSession();
        activeSessions.incrementAndGet();
        addSessionToStackTraceMap(session);
        logger.debug("Opened new unmanaged session. Active sessions: " + activeSessions.get());
        return session;
    }


    /**
     * Returns the metadata for the given class from session factory
     * @param clazz
     * @return
     */
    public static ClassMetadata getClassMetadata(Class<?> clazz) {
        return sf.getClassMetadata(clazz);
    }
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<>();

    public static PersistentClass getClassMapping(Class<?> clazz) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();
        MetadataSources sources = new MetadataSources(registry);
        Metadata metadata = sources.buildMetadata();
        return metadata.getEntityBinding(clazz.getName());
    }

    /**
     * Gets a RAW jdbc connection to the database. Use with caution ! Close it yourself manually when done!
     * @return
     * @throws SQLException
     */
//    public static Connection getJdbcConnection() throws SQLException {
//        SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
//        return sfi.getConnectionProvider().getConnection();
//    }

    public static Connection getJdbcConnection() throws SQLException {
        SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
        return sfi.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
    }



    /**
     * Shows the open remaining hibernate sessions upon AMP server shutdown. It displays the
     *  {@link Thread#currentThread#getStackTrace()} for the unclosed sessions.
     *  It will attempt to force {@link Session#clear()} and {@link Session#close()} on them
     *  This needs to be invoked (as Hibernate's APIs suggests) before {@link SessionFactory#close()} is invoked at the very end of AMP lifecycle
     */
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

    /**
     * initialize PersistenceManager
     * @param precache if true, do "empty" query for precached objects to put
     * them in the cache
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
     * precache hibernate classes
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

    /**
     * Gets the Hibernate configuration used to build the immutable SessionFactory.
     * Invoke this after initialize()
     * @return the Hibernate Configuration object
     */
    public static synchronized Configuration getHibernateConfiguration() {
        return cfg;
    }

    private PersistenceManager() {
    }


    /**
     * Close session factory
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
     * Loads all objects of T from database, using request (thread) session.
     * @param <T>
     * @param object
     * @return
     * @throws DgException
     */
    public static <T> Collection<T> loadAll(Class<T> object) throws DgException{
        return loadAll(object, getRequestDBSession());
    }

    /**
     * Loads all objects of T from database.
     * Client should care about opening and releasing session which is passed as parameter to this method.
     * @param <T>
     * @param object class object of T
     * @param session database session. Client should handle session - opening and releasing, including transactions if required.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> loadAll(Class<T> object, Session session) throws DgException{
        Collection<T> col = null;
        String queryString = null;
        try {
            queryString = "from " + object.getName();
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("cannot execute query: "+queryString, e);
            throw new DgException(e);
        }
        return col;
    }

    /**
     *
     */
    public static void rollbackCurrentSessionTx() {
        if (sf.getCurrentSession().getTransaction().getStatus().equals(TransactionStatus.ACTIVE)
                && sf.getCurrentSession().isOpen()
                && sf.getCurrentSession().isConnected()) {
            logger.info("Trying to rollback database transaction after exception");
            sf.getCurrentSession().getTransaction().rollback();
        }
    }

    /**
     * A flag set to true when current session is managed. I.e. there are guarantees that the session will be closed
     * after some arbitrary work is done.
     */
    private static final ThreadLocal<Boolean> CURRENT_SESSION_IS_MANAGED = ThreadLocal.withInitial(() -> false);

    /**
     * Execute runnable and ensures that if an active hibernate transaction exists it is committed or rolled back.
     * Will always close the current session before returning.
     *
     * <p>If the runnable throws an exception, then transaction is rolled back and same exception is thrown again.</p>
     *
     * <p>Transaction is not created before calling the runnable. For actual semantics when the transaction is created
     * please check {@link #getSession()}.</p>
     *
     * <p>Neither active session nor transaction are nested. Calling this method recursively will ensure that active
     * transaction and session are closed upon exiting the method. This is not very useful nor a good way to reason
     * about nested transaction semantics but this is how it worked before. Known to be used by
     * {@link HibernateSessionRequestFilter} which is itself invoked recursively during error processing.</p>
     *
     * @param runnable the runnable to execute with open session in view context
     */
    public static void inTransaction(Runnable runnable) {
        supplyInTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> T supplyInTransaction(Supplier<T> supplier) {
        Session session = getSession();
        boolean existingTransaction = session.getTransaction().isActive();
        Transaction transaction = null;

        try {
            if (!existingTransaction) {
                transaction = session.beginTransaction();
            }

            T result = supplier.get();

            if (!existingTransaction && transaction != null) {
                transaction.commit();
            }

            return result;
        } catch (Exception e) {
            if (!existingTransaction && transaction != null && transaction.isActive()) {
                try {
                    logger.info("Rolling back after exception");
                    transaction.rollback();
                } catch (HibernateException he) {
                    logger.error("Failed to rollback transaction", he);
                }
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            if (!existingTransaction && session != null && session.isOpen()) {
                try {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    session.close();
                } catch (HibernateException e) {
                    logger.error("Failed to close session", e);
                }
                clearCurrentSession();
            }
        }
    }


    /**
     * Returns the current Session. If there is none, creates one and returns it
     * upon creating a new session, a transaction is created.
     */
    public static Session getSession() {
        Session session = threadSession.get();
        if (session == null || !session.isOpen()) {
            session = sf.openSession();
            session.setFlushMode(FlushModeType.AUTO);
            threadSession.set(session);
            addSessionToStackTraceMap(session);
        }

        // Ensure we have an active transaction for write operations
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }

        return session;
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
     * an alias for {@link #getSession()}
     */
    public static Session getRequestDBSession() {
        return getSession();
    }

    /**
     * Adds this session to the stack trace map, so its closing can be tracked later
     * @param sess
     */
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
    /**
     * Removes object from Hibernate second-level cache, loads it from database
     * and initializes
     * @param objectClass target class
     * @param primaryKey primary key for object
     * @throws DgException
     */
    public static void refreshCachedObject(Class objectClass,
                                           Serializable primaryKey) throws
            DgException {
        Session session = null;

        try {
//            sf.evict(objectClass, primaryKey);

            session = getSession();
            session.evict(primaryKey);
            Object obj = session.load(objectClass, primaryKey);
            Hibernate.initialize(obj);
        }
        catch (Exception ex) {
            throw new DgException(
                    "Unable to refresh object into cache", ex);
        }
    }

    /**
     * closes a JDBC connection, swallowing any exceptions which have appeared in the meantime
     * @param connection
     */
    public static void closeQuietly(AutoCloseable connection)
    {
        if (connection == null)
            return;
        try
        {
            connection.close();
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * extracts a Long from an object
     * @param obj
     * @return
     */
    public final static Long getLong(Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Long)
            return (Long) obj;
        if (obj instanceof Number)
            return ((Number) obj).longValue();
        if (obj instanceof String)
            return Long.parseLong((String) obj);
        throw new RuntimeException("cannot convert object of class " + obj.getClass().getName() + " to long");
    }

    /**
     * extracts an Integer from an object
     * @param obj
     * @return
     */
    public final static Integer getInteger(Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Integer)
            return (Integer) obj;
        if (obj instanceof Number)
            return ((Number) obj).intValue();
        if (obj instanceof String)
            return Integer.parseInt((String) obj);
        throw new RuntimeException("cannot convert object of class " + obj.getClass().getName() + " to int");
    }

    /**
     * extracts a String from an object
     * @param obj
     * @return
     */
    public final static String getString(Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof String)
            return (String) obj;
        return obj.toString();
    }

    /**
     * extracts a Double from an object
     * @param obj
     * @return
     */
    public final static Double getDouble(Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Double)
            return (Double) obj;
        if (obj instanceof Number)
            return ((Number) obj).doubleValue();
        if (obj instanceof String)
            return Double.parseDouble((String) obj);
        throw new RuntimeException("cannot convert object of class " + obj.getClass().getName() + " to double");
    }

    public final static Boolean getBoolean(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof Boolean)
            return (Boolean) obj;
        if (obj.toString().equals("true") || obj.toString().equals("yes"))
            return true;
        if (obj.toString().equals("false") || obj.toString().equals("no"))
            return false;
        throw new RuntimeException("cannot convert object " + obj + " to boolean");
    }

    public static StatelessSession openNewStatelessSession() {
        return sf.openStatelessSession();
    }

    public static SessionFactory sf() {
        return sf;
    }

    /**
     * cleanly closes an unmanaged Hibernate session
     * ONLY USE IT FOR CONNECTIONS CREATED WITH {@link #openNewSession()}
     * @param session
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
     * Flushes the session and commits the transaction. It will not close the session and allows to rollback if an
     * exception is raised here (as opposed to {@link #closeSession(Session)} which does not allow rollback).
     */
    public static void flushAndCommit(Session session) {
        Transaction transaction = session.getTransaction();
        if (transaction != null) {
            if (transaction.isActive()) {
//                try {
//                    // note: flushing is needed only if session uses FlushMode.MANUAL
//                    session.flush();
//                } catch (HibernateException e) {
//                    // logging the error since finally may throw another exception and this one will be lost
//                    logger.error("Failed to flush the session.", e);
//                    throw e;
//                } finally {
//                    // do we really want to attempt commit if flushing fails?
                //session will be flushed automatically on transaction commit since we set the FlusmodeType as AUTO
                transaction.commit();
//                }
            }
        }
    }

    /**
     * <strong>This is a lifecycle management function</strong><br />
     * this function should ONLY be called at the end of a Session's lifecycle, being the last a "cycle" sees.
     * UNDER NO CIRCUMSTANCES CALL IT IN A "USER" (non-framework, non-job) ENVIRONMENT
     * @see #cleanupSession(Session)
     */
    public static void endSessionLifecycle() {
        cleanupSession(getSession());
    }

    /**
     * <strong>This is a lifecycle management function</strong><br />
     * commits & closes a session and then removes it from the SessionStackTraceMap
     * this function should ONLY be called at the end of a Session's lifecycle, being the last a "cycle" sees.
     * UNDER NO CIRCUMSTANCES CALL IT IN A "USER" (non-framework, non-job) ENVIRONMENT
     * @see PersistenceManager#endSessionLifecycle()
     * @param session
     */
    public static void cleanupSession(Session session) {
        try {
            closeSession(session);
        } finally {
            try {
                removeClosedSessionsFromMap();
            } catch (Exception e) {
                // ignore exceptions
            }
            try {
                removeSessionFromMap(session);
            } catch (Exception e) {
                // ignore exceptions
            }
        }
    }

    private static void removeSessionFromMap(Session session) {
        synchronized (sessionStackTraceMap) {
            sessionStackTraceMap.remove(session);
        }
    }

    /**
     * Execute Work in a new session and wrapped by a transaction.
     * Useful for accessing db outside of http request.
     * @param work work to be executed
     */
    public static void doWorkInTransaction(Work work) {
        doInTransaction(session -> {
            session.doWork(work);
            return Void.class;
        });
    }

    /**
     * Execute ReturningWork in a new session and wrapped by a transaction.
     * Useful for accessing db outside of http request.
     * @param returningWork returning work to be executed
     */
    public static <T> T doReturningWorkInTransaction(ReturningWork<T> returningWork) {
        return doInTransaction(session -> {
            return session.doReturningWork(returningWork);
        });
    }

    /**
     * Execute a block of code in new session.
     */
    public static void doInTransaction(Consumer<Session> consumer) {
        doInTransaction(s -> {
            consumer.accept(s);
            return Void.class;
        });
    }

    /**
     * Execute a function inside a transaction.
     * @param fn takes as input hibernate session
     * @param <R> return type
     * @return result of the function
     */
    public static <R> R doInTransaction(Function<Session, R> fn) {
        Session session = PersistenceManager.openNewSession();
        Transaction tx = session.beginTransaction();
        try {
            return fn.apply(session);
        } catch (Throwable e) {
            tx.rollback();
            throw e;
        } finally {
            PersistenceManager.closeSession(session);
        }
    }
}
