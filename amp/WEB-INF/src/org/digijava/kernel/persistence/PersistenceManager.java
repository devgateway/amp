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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
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
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;

public class PersistenceManager {

	private static SessionFactory sf;
	private static Configuration cfg;
	private static Logger logger = I18NHelper.getKernelLogger(
			PersistenceManager.class);


	
	public static String PRECACHE_REGION =
		"org.digijava.kernel.persistence.PersistenceManager.precache_region";


	/**
	 * The maximum allowed life for an opened hibernate session, in miliseconds
	 */
	public static final long MAX_HIBERNATE_SESSION_LIFE_MILLIS=60*60*1000;
			
	public static HashMap<Session,Object[]> sessionStackTraceMap= new HashMap<Session,Object[]>();

	/**
	 * Invoked at the end of each request. Iterates and removes Hibernate closed sessions from the trace map.
	 * It also checks the long running sessions and forces closure on ones that are longer than 
	 * The {@link HashMap} is synchronized to prevent concurrency issues between HTTP threads 
	 */
	public static void checkClosedOrLongSessionsFromTraceMap() {
		// remove closed sessions
		synchronized (sessionStackTraceMap) {
			Iterator<Session> iterator = PersistenceManager.sessionStackTraceMap
					.keySet().iterator();
			while (iterator.hasNext()) {
				Session session = (Session) iterator.next();

			
				// force closure of long running sessions
				Long millis = (Long) sessionStackTraceMap.get(session)[0];
				if (session.isOpen() && ( System.currentTimeMillis() - millis > MAX_HIBERNATE_SESSION_LIFE_MILLIS )) {
					StackTraceElement[] stackTrace = (StackTraceElement[]) sessionStackTraceMap
							.get(session)[1];
					logger.info("Forcing closure and removal of hibernate session "
							+ session.hashCode()
							+ " because it ran for longer than "
							+ MAX_HIBERNATE_SESSION_LIFE_MILLIS
							/ 1000
							+ " seconds");
					logger.info("Please review the code that generated the following recorded stack trace and ensure this session is closed properly: ");
					for (int i = 0; i < stackTrace.length && i < 8; i++) logger.info(stackTrace[i].toString());
					

					try {
						session.getTransaction().commit();
					} catch (Throwable e) {
						e.printStackTrace();
					}

					try {
						session.clear();
					} catch (Throwable e) {
						e.printStackTrace();
					}

					
					try {
						session.close();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				
				// remove closed sessions
				if (!session.isOpen()) iterator.remove();


			}
		}
	}
	/**
	 * Removes only the closed sessions from the map that tracks opened sessions. 
	 * No other check is being done.
	 */
	public static void removeClosedSessionsFromMap() {
		int count	= 0;
		synchronized (sessionStackTraceMap) {
			Iterator<Entry<Session, Object[]>> iterator = PersistenceManager.sessionStackTraceMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Session sess	= iterator.next().getKey();
				if ( !sess.isOpen() ) { 
					iterator.remove();
					count++;
				}
			}
		}
		logger.debug( count + " closed sessions were removed from 'sessionStackTraceMap' ");
	}
	
	/**
	 * Opens a new Hibernate session. Use this with caucion. 
	 * For servlets you will not require to use this, use {@link #getSession()} instead!
	 * This method returns you an unmanaged Hibernate session, that you need to close yourself! 
	 * @return
	 */
	public static Session openNewSession() {
		 org.hibernate.Session openSession = sf.openSession();
		 addSessionToStackTraceMap(openSession);
		 return openSession;
	}
	
	/**
	 * Gets the current thread session
	 * @return
	 */
	public static Session getCurrentSession() {
		return sf.getCurrentSession();
	}
	
	/**
	 * Returns the metadata for the given class from session factory
	 * @param clazz
	 * @return
	 */
	public static ClassMetadata getClassMetadata(Class<?> clazz) {
		return sf.getClassMetadata(clazz);
	}
	
	public static PersistentClass getClassMapping(Class<?> clazz)
	{
		return cfg.getClassMapping(clazz.getName());
	}
	
	/**
	 * Gets a RAW jdbc connection to the database. Use with caution ! Close it yourself manually when done!
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getJdbcConnection() throws SQLException {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
		return sfi.getConnectionProvider().getConnection();
	}


	
	/**
	 * Shows the open remaining hibernate sessions upon AMP server shutdown. It displays the
	 *  {@link Thread#currentThread#getStackTrace()} for the unclosed sessions.
	 *  It will attempt to force {@link Session#clear()} and {@link Session#close()} on them
	 *  This needs to be invoked (as Hibernate's APIs suggests) before {@link SessionFactory#close()} is invoked at the very end of AMP lifecycle
	 */
	public static void closeUnclosedSessionsFromTraceMap() {
		// print open sessions
		boolean found=false;
		synchronized (sessionStackTraceMap) {			
			Iterator<Session> iterator = PersistenceManager.sessionStackTraceMap.keySet().iterator();
			while (iterator.hasNext()) {
				Session session = (Session) iterator.next();
				if(session.isOpen()) {
					found=true;
					Object o[] = sessionStackTraceMap.get(session);
					StackTraceElement[] stackTraceElements = (StackTraceElement[]) o[1];
					logger.info("Session opened "+(System.currentTimeMillis()-(Long)o[0])+" miliseconds ago is still open. Will force closure, recorded stack trace: ");
					for (int i = 3; i < stackTraceElements.length && i < 8; i++) logger.info(stackTraceElements[i].toString());					
					logger.info("Forcing Hibernate session close...");
					try  {
						session.clear();
						session.close();
						logger.info("Hibernate Session Close succeeded");
					} catch (Throwable t) {
						logger.info("Error while forcing Hibernate session close:");
						logger.error(t);
					}
					logger.error("----------------------------------");
				}
			}	
			if(found) logger.info("Check the code around the above stack traces, commit transactions only if not using HTTP threads:");
		}
	}
	

//	/**
//	 * initialize PersistenceManager
//	 * @deprecated use initialize(boolean) instead
//	 */
//	public static void initialize() {
//		initialize(true, null);
//	}

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
			cfg = HibernateClassLoader.getConfiguration();

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
	

	public static Session getSession(){
		try {
			return getRequestDBSession();
		} catch (DgException e) {
			throw new RuntimeException(e);
		}
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
				sf.close();
			}
			catch (HibernateException ex) {
				logger.error("Error cleaning up persistence manager", ex);
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
		Session session = null;

		try {
			session = getSession();
//beginTransaction();
			session.update(object);
			//tx.commit();
		}
		catch (Exception ex) {
			throw new DgException(
					"Unable to update object into database", ex);
		}
	}

	/**
	 * Create hibernate-persisted object into database. This method gets session
	 * based on current configuration and creates object
	 * @param object Object Hibernate-persisted object
	 * @throws DgException If error occurs during update
	 */
	public static void createObject(Object object) throws DgException {
		Session session = null;

		try {
			session = getSession();
//beginTransaction();
			session.save(object);
			//tx.commit();
		}
		catch (Exception ex) {
			throw new DgException(
					"Unable to save object into database", ex);
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
	 * Managed by hibernate. Please do not use session.close nor transaction.commit over this session. 
	 * This is transparently managed by Hibernate on each request thread
	 * @see HibernateSessionRequestFilter
	 * @return Session object
	 * @throws DgException
	 */
	public static Session getRequestDBSession() throws DgException {
		return getRequestDBSession(true);
	}

//	private static void registerSession(Session session, String sessionData) {
//		Map resMap = (Map) requestSession.get();
//		if (resMap == null) {
//			resMap = new HashMap();
//			requestSession.set(resMap);
//		}
//
//		ArrayList regSessions =  (ArrayList)resMap.get("ThreadRegisteredSessions");
//		if (regSessions == null) {
//			regSessions = new ArrayList();
//		}
//
//		HashMap oneSessionData = new HashMap();
//		oneSessionData.put("session", session);
//		oneSessionData.put("stackTrace", sessionData);
//
//		regSessions.add(oneSessionData);
//
//		resMap.put("ThreadRegisteredSessions", regSessions);
//	}

//	public static List getThreadSessionData() {
//		Map resMap = (Map) requestSession.get();
//
//		if (resMap == null) {
//			return null;
//		}
//		;
//
//		ArrayList regSessions = (ArrayList) resMap.get(
//				"ThreadRegisteredSessions");
//		if (regSessions == null) {
//			return null;
//		}
//		else {
//			return regSessions.size() == 0 ? null :
//				new ArrayList(regSessions);
//		}
//	}

//	public static String getFormattedThreadSessionData() {
//		List sessionData = getThreadSessionData();
//		if (sessionData == null) {
//			return null;
//		}
//
//		StringBuffer sb = new StringBuffer(512);
//		boolean one = false;
//		sb.append("<thread-session-data>").append('\n');
//		Iterator iter = sessionData.iterator();
//		while (iter.hasNext()) {
//			HashMap item = (HashMap) iter.next();
//			Session session = (Session)item.get("session");
//			String stackTrace = (String)item.get("stackTrace");
//			//boolean isConnected = session.isConnected();
//			if (session.isOpen() || session.isConnected()) {
//				one = true;
//				sb.append("    ")
//				.append("<session isOpen=\"")
//				.append(session.isOpen())
//				.append("\" isConnected=\"")
//				.append(session.isConnected())
//				.append("\">")
//				.append('\n');
//				sb.append(stackTrace).append('\n');
//				sb.append("</session>").append('\n');
//			}
//		}
//
//		sb.append("</thread-session-data>");
//		if (one) {
//			return sb.toString();
//		} else {
//			return null;
//		}
//	}

	public static void rollbackCurrentSessionTx() {
		try {
			if (sf.getCurrentSession().getTransaction().isActive()
					&& sf.getCurrentSession().isOpen()
					&& sf.getCurrentSession().isConnected()) {
				logger.info("Trying to rollback database transaction after exception");
				sf.getCurrentSession().getTransaction().rollback();
			}

		} catch (Throwable rbEx) {
			logger.error("Could not rollback transaction after exception!",
					rbEx);
		}
	}
	
	/**
	 * @see #getRequestDBSession()
	 * @param createNew <b>ignored</b>
	 * @return
	 */
	public static Session getRequestDBSession(boolean createNew) {
		Session sess = PersistenceManager.sf.getCurrentSession();
		
		Transaction transaction=sess.getTransaction();
//		if(transaction!=null && transaction.isActive()) {
//			try {
//				SQLQuery testQuery = sess.createSQLQuery("SELECT 1");
//				testQuery.uniqueResult();				
//			} catch (RuntimeException e) {
//				transaction.rollback();				
//				logger.error("Transaction has been rolled back after exception "+ e);
//				sess = PersistenceManager.sf.getCurrentSession();
//				sess.beginTransaction(); 
//			}
//		}
	
		if(transaction==null || !transaction.isActive()) sess.beginTransaction(); 

		addSessionToStackTraceMap(sess);
		
		return sess;
	}
	
	/**
	 * Adds this session to the stack trace map, so its closing can be tracked later
	 * @param sess
	 */
	public static void addSessionToStackTraceMap(Session sess) {
		synchronized (sessionStackTraceMap){			
			if(sessionStackTraceMap.get(sess)==null) 
				sessionStackTraceMap.put(sess,new Object[] {new Long(System.currentTimeMillis()),Thread.currentThread().getStackTrace()});
		}
	}

	/**
	 * Closes hibernate session if it exists and removes it from
	 * ThreadLocal resource map
	 * @throws DgException
	 * @deprecated
	 */
	public static void closeRequestDBSessionIfNeeded() throws DgException {
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
	}
	
	/**
	 * closes a JDBC connection, swallowing any exceptions which have appeared in the meantime
	 * @param connection
	 */
	public static void closeQuietly(Connection connection)
	{
		if (connection == null)
			return;
		try
		{
			connection.close();
		}
		catch(Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * extracts a Long from an object returned by a Hibernate SQLQuery
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
	 * extracts a Long from an object returned by a Hibernate SQLQuery
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
	 * extracts a Long from an object returned by a Hibernate SQLQuery
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
	 * extracts a Long from an object returned by a Hibernate SQLQuery
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
	
	public static StatelessSession openNewStatelessSession() {
		return sf.openStatelessSession();
	}
	
	public static SessionFactory sf() {
		return sf;
	}
	
}
