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
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;

public class PersistenceManager {

	private static SessionFactory sf;
	private static Configuration cfg;
	private static Logger logger = I18NHelper.getKernelLogger(
			PersistenceManager.class);


	
	public static String PRECACHE_REGION =
		"org.digijava.kernel.persistence.PersistenceManager.precache_region";


	public static HashMap<Session,StackTraceElement[]> sessionStackTraceMap= new HashMap<Session,StackTraceElement[]>();

	/**
	 * Invoked at the end of each request. Iterates and removes Hibernate closed sessions from the trace map.
	 * The {@link HashMap} is synchronized to prevent concurrency issues between HTTP threads 
	 */
	public static  void removeClosedSessionsFromTraceMap() {
		  //remove closed sessions
		synchronized (sessionStackTraceMap) {
			Iterator<Session> iterator = PersistenceManager.sessionStackTraceMap.keySet().iterator();
			while (iterator.hasNext()) {
				Session session = (Session) iterator.next();
				if(!session.isOpen()) iterator.remove();
			}
        }
	}
	
	
	/**
	 * Opens a new Hibernate session. Use this with caucion. 
	 * For servlets you will not require to use this, use {@link #getSession()} instead!
	 * This method returns you an unmanaged Hibernate session, that you need to close yourself! 
	 * @return
	 */
	public static Session openNewSession() {
		 org.hibernate.classic.Session openSession = sf.openSession();
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
					StackTraceElement[] stackTraceElements = sessionStackTraceMap.get(session);
					for (int i = 3; i < stackTraceElements.length && i < 8; i++) 
						logger.error(stackTraceElements[i].toString());					
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
					Serializable id = meta.getIdentifier(item, EntityMode.POJO);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
//		Session session = null;
//		synchronized (sf) {
//			session = sf.openSession();
//		}
//		session.setFlushMode(FlushMode.AUTO);
//
//		if (DigiConfigManager.getConfig().isTrackSessions()) {
//			try {
//				throw new Exception("Trace Exception. This is not a real exception. It identifies unclosed session's caller");
//			}
//			catch (Exception ex) {
//				StringWriter sw = new StringWriter();
//				PrintWriter pw = new PrintWriter(sw);
//				ex.printStackTrace(pw);
//				pw.flush();
//				pw.close();
//
//				// Session does not implement Comparable interface
//				// so, its equals() and hashCode() methods operate using address
//				// in the JVM. This is what we want to identify right session object
//				String stackTrace = sw.getBuffer().toString();
//				sessionInstMap.put(session, stackTrace);
//
//				registerSession(session, stackTrace);
//			}
//		}
//		return session;
	}

	/**
	 * Releases a Hibernate Session, <b>without</b> committing the underlying
	 * connection.
	 * @throws cirrus.hibernate.HibernateException
	 * @throws java.sql.SQLException
	 * @deprecated
	 */
	public static void releaseSession(Session session){

//		if(session.isOpen())
//		session.beginTransaction().commit();
//		
//		if (DigiConfigManager.getConfig().isTrackSessions()) {
//			String stack = (String)sessionInstMap.get(session);
//			logger.debug("Releasing session: " + stack);
//			sessionInstMap.remove(session);
//		} else {
//			logger.debug("Releasing session");
//		}
//
//		try {
//			if(session.isOpen()) session.close();
//		}
//		catch (HibernateException ex) {
//			logger.error("Failed to close session", ex);
//			throw ex;
//		}
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
		Transaction tx = null;
		Session session = null;

		try {
			session = getSession();
//beginTransaction();
			session.update(object);
			//tx.commit();
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
//beginTransaction();
			session.save(object);
			//tx.commit();
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
			if (sf.getCurrentSession().getTransaction().isActive()) {
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
	 * @param createNew
	 * @return
	 */
	public static Session getRequestDBSession(boolean createNew) {
//		Map resMap = (Map) requestSession.get();
//		if (resMap == null) {
//			resMap = new HashMap();
//			requestSession.set(resMap);
//		}
//		Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);
//
//		if (sess == null || !sess.isOpen()) {
//			logger.debug("RequestDBSession was not found or is closed in the current thread");
//			if (createNew) {
//				logger.debug("Creating new RequestDBSession");
//				try {
//					sess = getSession();
//				}
//				catch (Exception ex) {
//					throw new DgException("Ecxeption getting DB session", ex);
//				}
//				resMap.put(Constants.REQUEST_DB_SESSION, sess);
//			}
//		}
//		else {
//			logger.debug("Reusing old RequestDBSession");
//		}

		org.hibernate.classic.Session sess = PersistenceManager.sf.getCurrentSession();
		if(sess.getTransaction()==null || !sess.getTransaction().isActive()) sess.beginTransaction();
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
				sessionStackTraceMap.put(sess,Thread.currentThread().getStackTrace());
		}
	}

	/**
	 * Closes hibernate session if it exists and removes it from
	 * ThreadLocal resource map
	 * @throws DgException
	 * @deprecated
	 */
	public static void closeRequestDBSessionIfNeeded() throws DgException {
//		logger.debug("closeRequestDBSessionIfNeeded() called");
//		Map resMap = (Map) requestSession.get();
//
//		if (resMap != null) {
//			Session sess = (Session) resMap.get(Constants.REQUEST_DB_SESSION);
//			if (sess != null) {
//				try {
//					logger.debug("releasing RequestDBSession");
//					releaseSession(sess);
//				}
//				catch (Exception ex) {
//					throw new DgException("Exception closing session", ex);
//				}
//			}
//			resMap.remove(Constants.REQUEST_DB_SESSION);
//		}
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

}
