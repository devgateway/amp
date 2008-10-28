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

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.metadata.ClassMetadata;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.Count;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;

public class PersistenceManager {

    private static SessionFactory sf;
    private static Logger logger = I18NHelper.getKernelLogger(
        PersistenceManager.class);

    public static String PRECACHE_REGION =
        "org.digijava.kernel.persistence.PersistenceManager.precache_region";

    private static ThreadLocal requestSession;
    private static Map sessionInstMap;

    static {
        requestSession = new ThreadLocal();
    }

    /**
     * initialize PersistenceManager
     * @deprecated use initialize(boolean) instead
     */
    public static void initialize() {
        initialize(true, null);
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

        sessionInstMap = Collections.synchronizedMap(new HashMap());
        DigiConfig config = null;
        HashMap modulesConfig = null;
        try {
            config = DigiConfigManager.getConfig();

            if (logger.isDebugEnabled()) {
                String debugKey = "PersistenceManager.initialize";
                logger.l7dlog(Level.DEBUG, debugKey, null, null);
            }

            // load kernel hibernate classes
            HibernateClassLoader.initialize(config);

            if (target != null) {
                if (!target.equalsIgnoreCase("kernel")) {
                    Object modConfig = DigiConfigManager.getModulesConfig().get(
                        target);
                    if (modConfig != null) {
                        modulesConfig = new HashMap();
                        modulesConfig.put(target, modConfig);
                    }
                }
            }
            else {
                modulesConfig = DigiConfigManager.getModulesConfig();
            }

            // load module hibernate classes
            if (modulesConfig != null) {
                HibernateClassLoader.initialize(modulesConfig);
            }

            HibernateClassLoader.buildHibernateSessionFactory();

            sf = HibernateClassLoader.getSessionFactory();

            if (precache) {
                precache();
            }
        }
        catch (Exception ex) {
            //String errKey = "PersistenceManager.intialize.error";
            //logger.l7dlog(Level.FATAL, errKey, null, ex);
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
                Iterator rowIter = session.find(precacheHql).
                    iterator();
                while (rowIter.hasNext()) {
                    Object item = rowIter.next();
                    Serializable id = meta.getIdentifier(item);
                    if (id == null) {
                        logger.error(
                            "One of the object identities is null for class: " +
                            className);
                        throw new DgException(
                            "One of the object identities is null " + className);
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
     * Opens a Database connection and returns a Session on that connection
     * @return The Hibernate Session
     * @throws java.sql.SQLException
     */
    public static Session getSession() throws SQLException, HibernateException {
        Session session = null;
        synchronized (sf) {
            session = sf.openSession();
        }

        if (DigiConfigManager.getConfig().isTrackSessions()) {
            try {
                throw new Exception("Trace Exception. This is not a real exception. It identifies unclosed session's caller");
            }
            catch (Exception ex) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                pw.flush();
                pw.close();

                // Session does not implement Comparable interface
                // so, its equals() and hashCode() methods operate using address
                // in the JVM. This is what we want to identify right session object
                String stackTrace = sw.getBuffer().toString();
                sessionInstMap.put(session, stackTrace);

                registerSession(session, stackTrace);
            }
        }
        return session;
    }

    /**
     * Releases a Hibernate Session, <b>without</b> committing the underlying
     * connection.
     * @throws cirrus.hibernate.HibernateException
     * @throws java.sql.SQLException
     */
    public static void releaseSession(Session session) throws SQLException,
        HibernateException {

        if (DigiConfigManager.getConfig().isTrackSessions()) {
            String stack = (String)sessionInstMap.get(session);
            logger.debug("Releasing session: " + stack);
            sessionInstMap.remove(session);
        } else {
            logger.debug("Releasing session");
        }

        try {
            session.close();
        }
        catch (HibernateException ex) {
            logger.warn("Failed to close session", ex);
            throw ex;
        }
    }

    /**
     * Gets a List of DBObjects based on the named query passed.
     * The List is retrieved from the Database and returned.
     * @param namedQuery The String that uniquely identifies the OQL Query
     * @return List of DBObjects
     * @throws WorkerException when any
     * error occurs while retrieving from the Database
     */
    public static List getList(String namedQuery) throws WorkerException {
        List objList = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.getNamedQuery(namedQuery);
            objList = q.list();

        }
        catch (SQLException sqle) {
            String errKey = "PersistenceManager.SQLExListFromDB.err";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(errKey, sqle);

        }
        catch (HibernateException hbe) {
            String errKey = "PersistenceManager.HibExListFromDB.err";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.ERROR, errKey, params, hbe);
            throw new WorkerException(errKey, hbe);

        }
        finally {
            try {
                if (session != null) {
                    releaseSession(session);
                }
            }
            catch (Exception ignoreMe) {
                //FIXME
            }
        }

        if (logger.isDebugEnabled()) {
            String debugKey = "PersistenceManager.ListFromDatabase.db";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.DEBUG, debugKey, params, null);
        }
        return objList;
    }

    /**
     * Gets a List of DBObjects based on the query parameters.
     * The List is retrieved from the Database and returned.
     * @param namedQuery The String that uniquely identifies the OQL Query
     * @param queryParams array of named parameters to be set while querying
     * @return List of DBObjects
     * @throws WorkerException when any
     * error occurs while retrieving from the Database
     */
    public static List getList(String namedQuery, Object[] queryParams) throws
        WorkerException {
        List objList = null;
        StringBuffer thisString = new StringBuffer(100);

        for (int i = 0; i < queryParams.length; i++) {
            String strParam = queryParams[i].toString();
            thisString.append("," + strParam);
        }

        String query = namedQuery + thisString.toString();

        Session session = null;
        try {
            session = getSession();
            Query q = session.getNamedQuery(namedQuery);

            for (int j = 0; j < queryParams.length; j++) {
                q.setParameter(j, queryParams[j]);
            }

            objList = q.list();

        }
        catch (SQLException sqle) {
            String errKey = "PersistenceManager.SQLExListFromDB.err";
            Object[] params = {
                query};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(errKey, sqle);

        }
        catch (HibernateException hbe) {
            String errKey = "PersistenceManager.HibExListFromDB.err";
            Object[] params = {
                query};
            logger.l7dlog(Level.ERROR, errKey, params, hbe);
            throw new WorkerException(errKey, hbe);

        }
        finally {
            try {
                if (session != null) {
                    releaseSession(session);
                }
            }
            catch (Exception ignoreMe) {
                //FIXME
            }
        }
        if (logger.isDebugEnabled()) {
            String debugKey = "PersistenceManager.ListFromDatabase.db";
            Object[] params = {
                query};
            logger.l7dlog(Level.DEBUG, debugKey, params, null);
        }
        return objList;
    }

    /**
     * Gets a List of top 'N' DBObjects based on the query parameter.
     * The List is retrieved from the Database and returned.
     * @param namedQuery The String that uniquely identifies the OQL Query
     * @param maxCount maximum number of records to be returned
     * @param queryParams array of named parameters to be set while querying
     * @return List of DBObjects
     * @throws WorkerException when any
     * error occurs while retrieving from the Database
     */
    public List getList(String namedQuery, int maxCount, Object[] queryParams) throws
        WorkerException {
        List objList = null;
        Integer intMaxCount = new Integer(maxCount);
        StringBuffer thisString = new StringBuffer(100);

        for (int i = 0; i < queryParams.length; i++) {
            String strParam = queryParams[i].toString();
            thisString.append("," + strParam);
        }

        String queryString = namedQuery + thisString.toString();
        String query = queryString + "," + maxCount;

        Session session = null;
        try {
            session = getSession();
            Query q = session.getNamedQuery(namedQuery);

            for (int j = 0; j < queryParams.length; j++) {
                q.setParameter(j, queryParams[j]);
            }

            q.setMaxResults(maxCount);
            objList = q.list();
        }
        catch (SQLException sqle) {
            String errKey = "PersistenceManager.SQLExTopNListFromDB.err";
            Object[] params = {
                queryString, intMaxCount};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(errKey, sqle);
        }
        catch (HibernateException hbe) {
            String errKey = "PersistenceManager.HibExTopNListFromDB.err";
            Object[] params = {
                queryString, intMaxCount};
            logger.l7dlog(Level.ERROR, errKey, params, hbe);
            throw new WorkerException(errKey, hbe);
        }
        finally {
            try {
                if (session != null) {
                    releaseSession(session);
                }
            }
            catch (Exception ignoreMe) {
                //FIXME
            }
        }

        if (logger.isDebugEnabled()) {
            String debugKey = "PersistenceManager.TopNListFromDatabase.db";
            Object[] params = {
                queryString, intMaxCount};
            logger.l7dlog(Level.DEBUG, debugKey, params, null);
        }

        return objList;
    }

    /**
     * Gets the Count of records based on the query parameter.
     * The Count is retrieved from the Database and the Cache is updated.
     * @param namedQuery The String that uniquely identifies the OQL Query
     * @return The Count object
     * @throws WorkerException when any
     * error occurs while retrieving from the Database
     */
    public static Count getCount(String namedQuery) throws WorkerException {
        Count cnt = null;
        Session session = null;
        try {
            session = getSession();
            Query q = session.getNamedQuery(namedQuery);
            Iterator results = q.iterate();
            if (results.hasNext()) {
                Integer count = (Integer) results.next();
                cnt = new Count();
                cnt.setCount(count.intValue());
                return cnt;
            }
        }
        catch (SQLException sqle) {
            String errKey = "PersistenceManager.SQLExCountFromDB.err";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(errKey, sqle);
        }
        catch (HibernateException hbe) {
            String errKey = "PersistenceManager.HibExCountFromDB.err";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.ERROR, errKey, params, hbe);
            throw new WorkerException(errKey, hbe);
        }
        finally {
            try {
                if (session != null) {
                    releaseSession(session);
                }
            }
            catch (Exception ignoreMe) {
                //FIXME
            }
        }

        if (logger.isDebugEnabled()) {
            String debugKey = "PersistenceManager.CountFromDatabase.db";
            Object[] params = {
                namedQuery};
            logger.l7dlog(Level.DEBUG, debugKey, params, null);
        }

        return cnt;
    }

    /**
     * Gets the Count of records based on a query parameter.
     * The named parameters can be passed to this query at runtime.
     * These parameters are passed in form of an object array.
     * the Count is retrieved from the Database and returned.
     * @param namedQuery The String that uniquely identifies the OQL Query
     * @param queryParams The array of named parameters for this query
     * @return The Count object
     * @throws WorkerException when any
     * error occurs while retrieving from the Database
     */
    public static Count getCount(String namedQuery, Object[] queryParams) throws
        WorkerException {
        Count cnt = null;
        StringBuffer thisString = new StringBuffer(100);

        for (int i = 0; i < queryParams.length; i++) {
            String strParam = queryParams[i].toString();
            thisString.append("," + strParam);
        }

        String query = namedQuery + thisString.toString();

        Session session = null;

        try {
            session = getSession();
            Query q = session.getNamedQuery(namedQuery);

            for (int j = 0; j < queryParams.length; j++) {
                q.setParameter(j, queryParams[j]);
            }

            Iterator results = q.iterate();
            if (results.hasNext()) {
                Integer count = (Integer) results.next();
                cnt = new Count();
                cnt.setCount(count.intValue());
                return cnt;
            }
        }
        catch (SQLException sqle) {
            String errKey = "PersistenceManager.SQLExCountFromDB.err";
            Object[] params = {
                query};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(errKey, sqle);
        }
        catch (HibernateException hbe) {
            String errKey = "PersistenceManager.HibExCountFromDB.err";
            Object[] params = {
                query};
            logger.l7dlog(Level.ERROR, errKey, params, hbe);
            throw new WorkerException(errKey, hbe);
        }
        finally {
            try {
                if (session != null) {
                    releaseSession(session);
                }
            }
            catch (Exception ignoreMe) {
            }
        }

        if (logger.isDebugEnabled()) {
            String debugKey = "PersistenceManager.CountFromDatabase.db";
            Object[] params = {
                query};
            logger.l7dlog(Level.DEBUG, debugKey, params, null);
        }
        return cnt;
    }

    private PersistenceManager() {
    }


    /**
     * Get Site by siteId
     *
     * @param siteId
     * @return
     */
    public static Site getSite(String siteId) {
        Session sess = null;
        Site iterSite = null;
        /////////////////////////////////
        try {
            sess = sf.openSession();
            Query query=sess.createQuery("from "+Site.class.getName()+" s where s.siteId=:site_id");
            query.setString("site_id", siteId);
            return (Site)query.uniqueResult();
        }
        catch (Exception ex) {
            logger.error("Unable to get Site from Database", ex);
        } finally{
            try {
                releaseSession(sess);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }
        /////////////////////////////////

        return iterSite;
    }


    /**
     * Close session factory
     */
    public static void cleanup() {
        logger.debug("cleanup() called");
        if (sf != null) {
            try {
                sf.close();
            }
            catch (HibernateException ex) {
                logger.error("Error cleaning up persistence manager", ex);
            }
        }

        if (DigiConfigManager.getConfig().isTrackSessions()) {
            Iterator iter = sessionInstMap.values().iterator();
            while (iter.hasNext()) {
                String item = (String) iter.next();
                logger.warn("Unclosed connection's call stack\n" + item);
                logger.warn(
                    "======================================================");
            }
        }
    }

    /**
     * Update hibernate-persisted object into database. This method gets session
     * based on current configuration and updates objet
     * @param object Object Hibernate-persisted object
     * @throws DgException If error occurs during update
     */
    public static void updateObject(Object object) throws DgException {
        Transaction tx = null;
        Session session = null;

        try {
            session = getSession();

            tx = session.beginTransaction();

            session.update(object);

            tx.commit();

        }
        catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ", cause);
                }
            }
            throw new DgException(
                "Unable to update object into database", ex);
        }
        finally {
            try {
                releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

    }

    /**
     * Create hibernate-persisted object into database. This method gets session
     * based on current configuration and creates object
     * @param object Object Hibernate-persisted object
     * @throws DgException If error occurs during update
     */
    public static void createObject(Object object) throws DgException {
        Transaction tx = null;
        Session session = null;

        try {
            session = getSession();

            tx = session.beginTransaction();

            session.save(object);

            tx.commit();

        }
        catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ", cause);
                }
            }
            throw new DgException(
                "Unable to save object into database", ex);
        }
        finally {
            try {
                releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

    }

    /**
     * Returns hibernate Session related with current request if it exists,
     * or creates new one.
     * @return Session object
     * @throws DgException
     */
    public static Session getRequestDBSession() throws DgException {
        return getRequestDBSession(true);
    }

    private static void registerSession(Session session, String sessionData) {
        Map resMap = (Map) requestSession.get();
        if (resMap == null) {
            resMap = new HashMap();
            requestSession.set(resMap);
        }

        ArrayList regSessions =  (ArrayList)resMap.get("ThreadRegisteredSessions");
        if (regSessions == null) {
            regSessions = new ArrayList();
        }

        HashMap oneSessionData = new HashMap();
        oneSessionData.put("session", session);
        oneSessionData.put("stackTrace", sessionData);

        regSessions.add(oneSessionData);

        resMap.put("ThreadRegisteredSessions", regSessions);
    }

    public static List getThreadSessionData() {
        Map resMap = (Map) requestSession.get();

        if (resMap == null) {
            return null;
        }
        ;

        ArrayList regSessions = (ArrayList) resMap.get(
            "ThreadRegisteredSessions");
        if (regSessions == null) {
            return null;
        }
        else {
            return regSessions.size() == 0 ? null :
                new ArrayList(regSessions);
        }
    }

    public static String getFormattedThreadSessionData() {
        List sessionData = getThreadSessionData();
        if (sessionData == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer(512);
        boolean one = false;
        sb.append("<thread-session-data>").append('\n');
        Iterator iter = sessionData.iterator();
        while (iter.hasNext()) {
            HashMap item = (HashMap) iter.next();
            Session session = (Session)item.get("session");
            String stackTrace = (String)item.get("stackTrace");
            //boolean isConnected = session.isConnected();
            if (session.isOpen() || session.isConnected()) {
                one = true;
                sb.append("    ")
                    .append("<session isOpen=\"")
                    .append(session.isOpen())
                    .append("\" isConnected=\"")
                    .append(session.isConnected())
                    .append("\">")
                    .append('\n');
                sb.append(stackTrace).append('\n');
                sb.append("</session>").append('\n');
            }
        }

        sb.append("</thread-session-data>");
        if (one) {
            return sb.toString();
        } else {
            return null;
        }
    }

    public static Session getRequestDBSession(boolean createNew) throws
        DgException {
        Map resMap = (Map) requestSession.get();
        if (resMap == null) {
            resMap = new HashMap();
            requestSession.set(resMap);
        }
        Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);

        if (sess == null) {
            logger.debug("RequestDBSession was not found in the current thread");
            if (createNew) {
                logger.debug("Creating new RequestDBSession");
                try {
                    sess = getSession();
                }
                catch (Exception ex) {
                    throw new DgException("Ecxeption getting DB session", ex);
                }
                resMap.put(Constants.REQUEST_DB_SESSION, sess);
            }
        }
        else {
            logger.debug("Reusing old RequestDBSession");
        }

        return sess;
    }

    /**
     * Closes hibernate session if it exists and removes it from
     * ThreadLocal resource map
     * @throws DgException
     */
    public static void closeRequestDBSessionIfNeeded() throws DgException {
        logger.debug("closeRequestDBSessionIfNeeded() called");
        Map resMap = (Map) requestSession.get();

        if (resMap != null) {
            Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);
            if (sess != null) {
                try {
                    logger.debug("releasing RequestDBSession");
                    releaseSession(sess);
                }
                catch (Exception ex) {
                    throw new DgException("Exception closing session", ex);
                }
            }
            resMap.remove(Constants.REQUEST_DB_SESSION);
        }
    }

    public static Map getUnclosedSessions() {
        return new HashMap(sessionInstMap);
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
            sf.evict(objectClass, primaryKey);

            session = getSession();
            Object obj = session.load(objectClass, primaryKey);
            Hibernate.initialize(obj);
        }
        catch (Exception ex) {
            throw new DgException(
                "Unable to refresh object into cache", ex);
        }
        finally {
            try {
                releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

    }

    /**
     * @return Returns the SessionFactory
     */
    public static synchronized final SessionFactory getSessionFactory() {
        return sf;
    }
}
