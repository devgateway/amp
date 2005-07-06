/*
 *   PersistenceManager.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Jul 2, 2003
 *	 CVS-ID: $Id: PersistenceManager.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.HibernateClass;
import org.digijava.kernel.config.HibernateClasses;
import org.digijava.kernel.dbentity.SignOn;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.Count;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.ProxyHelper;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.metadata.ClassMetadata;
import net.sf.swarmcache.ObjectCache;
import java.util.*;
import org.digijava.kernel.Constants;
import java.io.StringWriter;
import java.io.PrintWriter;

public class PersistenceManager {

    private static SessionFactory sf;
    private static Logger logger = I18NHelper.getKernelLogger(PersistenceManager.class);

    public static String PRECACHE_REGION = "org.digijava.kernel.persistence.PersistenceManager.precache_region";

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
                    Object modConfig = DigiConfigManager.getModulesConfig().get(target);
                    if (modConfig != null) {
                        modulesConfig = new HashMap();
                        modulesConfig.put(target, modConfig);
                    }
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

		logger.debug("Analyzing configuration for class: " + hibernateClass.getContent());
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
                            long delay = DigiConfigManager.getConfig().getJobDelaySec();
                            if (delay != 0) {
                                logger.debug("Suspending for " + delay + " sec(s)");
                                try {
                                    Thread.sleep(delay * 1000);
                                }
                                catch (InterruptedException iex) {
                                    logger.warn("Job delay exception",
                                                iex);
                                }
                            }
                            logger.debug("Running precache for " + hibernateClass.getContent() + " again");
                            query.list();
                        }
                    }
                    else {
                        precacheToRegion(session, hibernateClass.getContent(), hibernateClass.getRegion(), precacheHql);
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
                                         String precacheHql) throws
        HibernateException, DgException {
        ObjectCache region = DigiCacheManager.getInstance().
            getCache(regionName);
        if (region == null) {
            logger.debug("Unable to create cache region " +  regionName +
                " to precache class: " + className);
            throw new DgException("Unable to create cache region " +
                regionName + " to precache class: " + className);
        } else {
            logger.debug("Using region " + region.getType());
        }
        if (region.size() == 0) {
            Map precache = new HashMap();
            try {
                Class clazz = Class.forName(className);
                ClassMetadata meta = sf.getClassMetadata(clazz);
                if (meta == null) {
                    logger.warn("Unable to load hibernate metadata for class: " + className);
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
                    precache.put(id, item);
                }
                logger.debug("Map was prepared successfully. Putting into cache");
                if (region == null) {
                    logger.debug("region is null!!!");
                }
                region.precache(precache);
                logger.debug("precacheToRegion() complete");
            }
            catch (ClassNotFoundException ex2) {
                logger.error("Unable to load class " + className + " for precaching", ex2);
            }
        } else {
            logger.info("Region " + regionName + " is already filled, skiping precache");
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
                sessionInstMap.put(session, sw.getBuffer().toString());
            }
        }
        return session;
    }

    /**
     * Releases a Hibernate Session, after committing the underlying connection.
     * @throws cirrus.hibernate.HibernateException
     * @throws java.sql.SQLException
     */
    public static void releaseSession(Session session) throws SQLException,
        HibernateException {
        if (DigiConfigManager.getConfig().isTrackSessions()) {
            sessionInstMap.remove(session);
        }

        try {
            /**
             * @deprecated flush must be done by caller, not here. It
             * decreases performance.
             */
            session.flush();
//			session.connection().commit();
        }
        finally {
            session.close();
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
     * Gets siteId list through Hibernate
     *
     * @return
     */

    public static List getSites() {
        List sitelist = new java.util.ArrayList();
        String siteId;
        Session sess = null;
        /////////////////////////////////
        try {
            sess = sf.openSession();

            //
            Iterator iter = sess.iterate(
                "from org.digijava.kernel.request.Site");
            while (iter.hasNext()) {
                Site iterSite = (Site) iter.next();
                sitelist.add(iterSite.getSiteId().toString());
            }
        }
        catch (Exception ex) {}
        /////////////////////////////////

        return sitelist;
    }


    /**
     * Get Site by siteId
     *
     * @param siteId
     * @return
     */
    public static Site getSite( String siteId ) {
        Session sess = null;
        Site iterSite = null;
        /////////////////////////////////
        try {
            sess = sf.openSession();

            Iterator iter = sess.iterate("from rs in class " +
                                         Site.class.getName() +
                                         " where rs.siteId = ? ", siteId, Hibernate.STRING );
            while (iter.hasNext()) {
                iterSite = (Site) iter.next();
                break;
            }
        }
        catch (Exception ex) {
            logger.error("Unable to get Site from Database",ex);
        }
        /////////////////////////////////

        return iterSite;
    }

    /**
     * Get <code>SiteDomain</code> object for the given domain name and path
     * For example, if URL is http://www.newsite:com:8080/digijava/user
     * then domain is www.newsite.com and path is /user
     * @param domain Domain name
     * @param path path of the subdirectory
     * @return <code>SiteDomain</code> object
     * @throws Exception if error occurs
     * @deprecated
     */

    public static SiteDomain getSiteDomain(String domain, String path) throws
        Exception {
        long siteId;

        SiteDomain siteDomain = null;
        SiteDomain defaultSiteDomain = null;
        Session sess = null;
        try {
            sess = sf.openSession();

            //
            Iterator iter = sess.iterate("from rs in class " +
                                         SiteDomain.class.getName() +
                                         " where rs.siteDomain = ? " +
                                         " order by length(rs.sitePath) desc",
                                         domain, Hibernate.STRING);
            while (iter.hasNext()) {
                SiteDomain iterSite = (SiteDomain) iter.next();

                if ( (iterSite.getSitePath() == null) ||
                    (iterSite.getSitePath().trim().length() == 0)) {
                    defaultSiteDomain = iterSite;

                    if ( (path == null) || path.length() == 0) {
                        siteDomain = iterSite;
                        break;
                    }
                }
                else {
                    if ( (path != null) &&
                        path.startsWith(iterSite.getSitePath().trim())) {
                        siteDomain = iterSite;
                        break;
                    }
                }
            }
        }
        catch (HibernateException ex) {
            logger.error("Unable to get information from database",ex);
            throw new ServletException(
                "Unable to get information from database", ex);
        }
        finally {
            if (sess != null) {
                try {
                    sess.close();
                }
                catch (HibernateException ex1) {
                }
            }
        }
        if (siteDomain == null) {
            return defaultSiteDomain;
        }
        else {
            return siteDomain;
        }
    }


    public static SignOn isSignOn(String uid) throws Exception {

        Session sess = null;
        SignOn signon = null;
        try {
            sess = getSession();

            //
            Iterator iter = sess.iterate("from rs in class " +
                                         SignOn.class.getName() +
                                         " where rs.rid = '" + uid +"'");

            if (iter.hasNext()) {
                signon = (SignOn)iter.next();
            }
        }
        catch (HibernateException ex) {
            logger.error("Unable to get uid information from database",ex);
            throw new ServletException(
                "Unable to get uid information from database", ex);
        }
        finally {
            if (sess != null) {
                try {
                    releaseSession(sess);
                    return signon;
                }
                catch (HibernateException ex1) {
                }
            }
        }

        return null;
    }

    public static void updateUID(String uid, String email) throws Exception {


            Transaction tx = null;
            Session session = null;
            User user = null;
            try {
                session = PersistenceManager.getSession();

                Iterator iter = session.iterate("from rs in class " +
                                             User.class.getName() +
                                             " where rs.email = '" + email + "'");

                if (iter.hasNext()) {
                    user = (User) iter.next();
                }

                if( user != null ) {
                    tx = session.beginTransaction();
                    SignOn signon = new SignOn();
                    signon.setRid(uid);
                    signon.setUser(user);
                    session.save(signon);
                    tx.commit();
                }
            }
            catch (Exception ex) {
                if (tx != null) {
                    try {
                        tx.rollback();
                    } catch (Throwable cause) {
                        logger.error("Unable to update uid information into database",cause);
                    }
                }
                throw new Exception(
                    "Unable to update uid information into database", ex);
            }
            finally {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex2) {
                    logger.error("Unable to update uid information into database",ex2);
                }
            }

        }

    public static User isUser(String user, String password) throws Exception {

        Session sess = null;
        try {
            sess = sf.openSession();

            //
            Iterator iter = sess.iterate("from rs in class " +
                                         User.class.getName() +
                                         " where rs.email = '" + user +
                                         "' and rs.password = '" + password +
                                         "'");

            if (iter.hasNext()) {
                User userObj = (User) iter.next();
                ProxyHelper.initializeObject(userObj);
                return userObj;
            }
        }
        catch (HibernateException ex) {
            logger.error("Unable to get information from database",ex);
            throw new ServletException(
                "Unable to get information from database", ex);
        }
        finally {
            if (sess != null) {
                try {
                    sess.close();
                }
                catch (HibernateException ex1) {
                }
            }
        }

        return null;
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
    public static Session getRequestDBSession () throws DgException {
      Map resMap = (Map) requestSession.get();
      if (resMap == null) {
        resMap = new HashMap();
        requestSession.set(resMap);
      }
      Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);

      if (sess == null) {
        try {
          sess = getSession();
        }
        catch (Exception ex) {
          throw new DgException("Ecxeption getting DB session");
        }
        resMap.put(Constants.REQUEST_DB_SESSION, sess);
      }

      return sess;
    }

    /**
     * Closes hibernate session if it exists and removes it from
     * ThreadLocal resource map
     * @throws DgException
     */
    public static void closeRequestDBSessionIfNeeded () throws DgException {
      Map resMap = (Map) requestSession.get();

      if (resMap != null) {
        Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);
        if (sess != null) {
          try {
            releaseSession(sess);
          }
          catch (Exception ex) {
            throw new DgException ("Exception closing session", ex);
          }
        }
        resMap.remove(Constants.REQUEST_DB_SESSION);
      }
    }

    public static Map getUnclosedSessions() {
        return new HashMap(sessionInstMap);
    }
}
