/*
 *   DbUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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

package org.digijava.module.news.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.exception.NewsException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import java.util.*;

public class DbUtil {

    /**
     * logging tracer
     */
    private static Logger logger = Logger.getLogger(DbUtil.class);

    /**
     * Returns list of news for selected status
     *
     * @param status status of retrieved news
     * @param request Http Servlet Request
     * @return list of news
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String status, HttpServletRequest request) throws
	  NewsException {

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	return getNews(moduleInstance.getSite().getSiteId(),
		       moduleInstance.getInstanceName(), status, true);
    }

    /**
     * Returns list of news for selected status
     *
     * @param status status of retrieved news
     * @param request Http Servlet Request
     * @param orderBy when true news ordered descending by creation date are retrieved, when false news ordered descending by release date are retrieved
     * @return list of news
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String status, HttpServletRequest request,
			       boolean orderBy) throws
	  NewsException {

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	return getNews(moduleInstance.getSite().getSiteId(),
		       moduleInstance.getInstanceName(), status, orderBy);
    }

    /**
     * <p>Returns list of news for selected status considering pagination: </p>
     * only the sublist of news,correspondence to the current page is retrieved
     *
     * @param status status of retrieved news
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
	   * @return sublist of news for selected status correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String status, HttpServletRequest request,
			       int firstResult, int maxResult) throws
	  NewsException {

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	return getNews(moduleInstance.getSite().getSiteId(),
		       moduleInstance.getInstanceName(), status, firstResult,
		       maxResult, false);
    }

    /**
     * <p>Returns list of news for selected status considering pagination: </p>
     * only the sublist of news,correspondence to the current page is retrieved
     *
     * @param status status of retrieved news
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param orderBy when true retrieved news items are ordered by creation date(milliseconds), when false news items are ordered by release date
	   * @return sublist of news for selected status correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String status, HttpServletRequest request,
			       int firstResult, int maxResult, boolean orderBy) throws
	  NewsException {

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	return getNews(moduleInstance.getSite().getSiteId(),
		       moduleInstance.getInstanceName(), status, firstResult,
		       maxResult, orderBy);
    }

    /**
     * <p>Returns list of news  for selected site,instance and status:</p>
     * <p>when retrieving archived messages list of news with publication date after current date(including) and archive date before current date(including) or null, ordered descending by news item's creation date are returned</p>
     * when retrieving news having status other then archive, news with archive date before curret date ordered descending by news item's creation date are returned
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved news
     * @param orderBy when true news ordered descending by creation date are retrieved, when false news ordered descending by release date are retrieved
     * @return ordered list of news for selected site,instance and status
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String siteId, String instanceId, String status,
			       boolean orderBy) throws
	  NewsException {

	Session session = null;
	List list = null;
	Query q = null;
	String query = "";

	try {
	    session = PersistenceManager.getSession();

	    query = "select n from " +
		  News.class.getName() + " n, n.newsItem " +
		  " ni where n.siteId=:siteId and n.instanceId=:instanceId ";

	    if (orderBy) {
		if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    query += " and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) order by n.releaseDate desc,ni.creationDate desc";
		}
		else {
		    query += " and ((n.status=:status) or (n.archiveDate < :curDate1)) order by n.releaseDate desc,ni.creationDate desc";
		}
	    }
	    else {
		if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    query += " and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) order by n.releaseDate desc";
		}
		else {
		    query += " and ((n.status=:status) or (n.archiveDate < :curDate1)) order by n.archiveDate desc";
		}
	    }
	    q = session.createQuery(query);

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    q.setParameter("status", status, Hibernate.STRING);
	    q.setCalendar("curDate1", currentDate);

	    if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		q.setCalendar("curDate2", currentDate);
	    }

	    list = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return list;
    }

    public static List getTeaserNewsList(String siteId,
					 String instanceId,
					 Long numOfItems) throws
	  NewsException {

	Session session = null;
	List list = null;
	Query q = null;
	String query = "";

	try {
	    session = PersistenceManager.getRequestDBSession();

	    query = "select n from " +
		  News.class.getName() + " n, n.newsItem " +
		  " ni where n.siteId=:siteId and n.instanceId=:instanceId ";

	    StringBuffer queryString = new StringBuffer();
	    queryString.append("select new org.digijava.module.news.form.NewsTeaserItem(n.id, ni.title, n.releaseDate, n.sourceUrl, ni.description) from ");
	    queryString.append(News.class.getName());
	    queryString.append(" n, n.newsItem ");
	    queryString.append(
		  " ni where n.siteId=:siteId and n.instanceId=:instanceId ");

	    queryString.append(" and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) order by n.releaseDate desc,ni.creationDate desc");

	    q = session.createQuery(queryString.toString());

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    q.setParameter("status", ItemStatus.PUBLISHED, Hibernate.STRING);
	    q.setCalendar("curDate1", currentDate);
	    q.setCalendar("curDate2", currentDate);

	    q.setFirstResult(0);
	    if (numOfItems != null) {
		q.setMaxResults(numOfItems.intValue());
	    }

	    q.setCacheable(true);
	    list = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}

	return list;
    }

    /**
     * <p>Returns list of news  for selected site,instance and status considering pagination:</p>
     * <p>only the sublist of news,correspondence to the current page is retrieved</p>
     * <p>when retrieving archived messages list of news with publication date after current date(including) and archive date before current date(including) or null, ordered descending by news item's creation date are returned</p>
     * when retrieving news having status other then archive, news with archive date before current date ordered descending by news item's creation date are returned
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved news
     * @param firstResult beggining index of retrieved news sublist
     * @param maxResult ending index of retrieved news sublist
     * @param orderBy when true retrieved news items are ordered by creation date(milliseconds), when false news items are ordered by release date
     * @return ordered sublist of news for selected site,instance and status correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String siteId, String instanceId, String status,
			       int firstResult, int maxResult, boolean orderBy) throws
	  NewsException {

	Session session = null;
	List list = null;
	Query q = null;
	String query = "";
	try {
	    session = PersistenceManager.getSession();

	    if (!orderBy) {
		query = "select n from " +
		      News.class.getName() + " n" +
		      " where n.siteId=:siteId and n.instanceId=:instanceId ";
		if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    query += " and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) order by n.releaseDate desc";
		}
		else {
		    query += " and ((n.status=:status) or (n.archiveDate < :curDate1)) order by n.releaseDate desc";
		}
	    }
	    else {
		query = "select n from " +
		      News.class.getName() + " n, n.newsItem " +
		      " ni where (n.siteId=:siteId) and (n.instanceId=:instanceId) ";

		if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    query += " and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) order by ni.creationDate desc";
		}
		else {
		    query += " and ((n.status=:status) or (n.archiveDate < :curDate1)) order by ni.creationDate desc";
		}
	    }
	    //
	    q = session.createQuery(query);

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);

	    q.setFirstResult(firstResult);
	    q.setMaxResults(maxResult);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    q.setParameter("status", status, Hibernate.STRING);
	    q.setCalendar("curDate1", currentDate);
	    if (!status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		q.setCalendar("curDate2", currentDate);
	    }

	    list = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return list;
    }

    /**
     * Returns list of news  by user identity and status(if not null) ordered descending by news item's publication date
     *
     * @param userId user identity
     * @param status status of retrieved news or null
     * @return ordered list of news
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(Long userId, String status) throws NewsException {

	Session session = null;
	Iterator iterator = null;
	List list = new ArrayList();
	Query q = null;
	try {
	    session = PersistenceManager.getSession();

	    if (status != null) {

		q = session.createQuery("select n from " +
					News.class.getName() +
					" n, n.newsItem ni where (ni.news.id=n.id) and (n.status = :status) and (ni.userId = :userId) order by n.releaseDate desc,ni.creationDate desc");
		q.setParameter("userId", userId, Hibernate.LONG);
		q.setParameter("status", status, Hibernate.STRING);

	    }
	    else {
		q = session.createQuery("select n from " +
					News.class.getName() +
					" n, n.newsItem ni " +
		      " where (ni.userId = :userId) order by n.releaseDate desc,ni.creationDate desc");
		q.setParameter("userId", userId, Hibernate.LONG);
	    }

	    list = q.list();
	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return list;
    }

    /**
     * <p>Returns list of news  by user identity and status(if not null) ordered descending by news item's publication date considering pagination:</p>
     * only the sublist of news,correspondence to the current page is retrieved
     *
     * @param userId user identity
     * @param status status of retrieved news or null
     * @param firstResult beggining index of retrieved news sublist
     * @param maxResult ending index of retrieved news sublist
     * @return ordered sublist of news correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(Long userId, String status, int firstResult,
			       int maxResult) throws NewsException {

	Session session = null;
	Iterator iterator = null;
	List list = new ArrayList();
	Query q = null;
	try {
	    session = PersistenceManager.getSession();

	    if (status != null) {

		q = session.createQuery("select n from " +
					News.class.getName() +
					" n, n.newsItem ni " +
					" where (n.status = :status) and (ni.userId = :userId) order by n.releaseDate desc");
		q.setParameter("userId", userId, Hibernate.LONG);
		q.setParameter("status", status, Hibernate.STRING);

	    }
	    else {
		q = session.createQuery("select n from  " +
					News.class.getName() +
					" n, n.newsItem ni " +
		      " where (ni.userId = :userId) order by n.releaseDate desc");
		q.setParameter("userId", userId, Hibernate.LONG);
	    }

	    q.setFirstResult(firstResult);
	    q.setMaxResults(maxResult);
	    list = q.list();
	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return list;
    }

    /**
     * Returns list of news for selected site and instance ordered descending by news item's publication date
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @return ordered list of news for selected site and instance
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String siteId, String instanceId) throws
	  NewsException {
	List news = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("from rs in class " +
					  News.class.getName() +
					  " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) order by rs.releaseDate desc");

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    news = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return news;
    }

    /**
     *<p>Returns list of news for selected site and instance ordered descending by news item's publication date considering pagination:</p>
     * only the sublist of news,correspondence to the current page is retrieved
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param firstResult beggining index of retrieved news sublist
     * @param maxResult ending index of retrieved news sublist
     * @return ordered sublist of news for selected site and instance correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */
    public static List getNews(String siteId, String instanceId,
			       int firstResult, int maxResult) throws
	  NewsException {
	List news = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("from rs in class " +
					  News.class.getName() +
					  " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) order by rs.releaseDate desc");

	    q.setFirstResult(firstResult);
	    q.setMaxResults(maxResult);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    news = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return news;
    }

    /**
     * Get News settings by Http Servlet Request
     *
     * @param request Http Servlter Request
     * @return News settings
     * @throws NewsException if db-access errror occurs
     */
    public static NewsSettings getNewsSettings(HttpServletRequest request) throws
	  NewsException {

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	return getNewsSettings(moduleInstance.getSite().getSiteId(),
			       moduleInstance.getInstanceName());
    }

    public static int getNumberOfCharsInTitle(String siteId, String instanceId) throws
	  NewsException {

	Session session = null;
	List list;
	Long numberOfCharsInTitle = new Long(100);
	try {
	    session = PersistenceManager.getSession();

	    StringBuffer queryString = new StringBuffer();

	    queryString.append(
		  "select rs.numberOfCharsInTitle from rs in class " +
		  NewsSettings.class.getName() +
		  " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");

	    Query q = session.createQuery(queryString.toString());

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    q.setCacheable(true);
	    list = q.list();

	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		numberOfCharsInTitle = (Long) iter.next();
		break;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get news setting from database ", ex);
	    throw new NewsException("Unable to get news setting from database",
				    ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return numberOfCharsInTitle.intValue();

    }

    /**
     * Get News settings by site identity and instance name
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @return News settings
     * @throws NewsException if db-access errror occurs
     */
    public static NewsSettings getNewsSettings(String siteId, String instanceId) throws
	  NewsException {

	Session session = null;
	Iterator iter = null;
	NewsSettings settings = null;
	try {
	    session = PersistenceManager.getSession();

	    iter = session.iterate("from rs in class " +
				   NewsSettings.class.getName() +
				   " where (rs.siteId=?) and (rs.instanceId=?)",
				   new Object[] {
		siteId, instanceId

	    }

	    ,
		  new Type[] {
		  Hibernate.STRING, Hibernate.STRING});

	    while (iter.hasNext()) {
		settings = (NewsSettings) iter.next();
		break;
	    }

	    // Get default settings
	    if (settings == null) {
		settings = new NewsSettings(siteId, instanceId);
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news setting from database ", ex);
	    throw new NewsException("Unable to get news setting from database",
				    ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return settings;
    }

    /**
     * Get news item by identity from database
     *
     * @param id identity if news item to be retrieved
     * @return News, identified by identity
     * @throws NewsException if db-access errror occurs
     */
    public static News getNewsItem(Long id) throws NewsException {

	Session session = null;
	News news = null;
	try {
	    session = PersistenceManager.getSession();

	    news = (News) session.load(News.class, id);

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news item from database ", ex);
	    throw new NewsException("Unable to get news item from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return news;
    }

    /**
     * Adds/Updates News item into Database
     *
     * @param news News instance to be updated
     * @throws NewsException if db-access errror occurs
     */
    public static void updateNews(News news) throws NewsException {

	Session session = null;
	Transaction tx = null;
	List list = null;
	try {

	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();

	    Iterator iter = news.getNewsItem().iterator();
	    while (iter.hasNext()) {
		NewsItem item = (NewsItem) iter.next();
		if (item.getNews() == null) {
iter.remove();
		    session.delete(item);
		}
	    }

	    session.saveOrUpdate(news);
	    tx.commit();

	}
	catch (Exception ex) {
	    logger.debug("Unable to update news information into database", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed", ex1);
		}
	    }
	    throw new NewsException(
		  "Unable to update news information into database", ex);
	}
	finally {
	    if (session != null) {
		try {
		    PersistenceManager.releaseSession(session);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed", ex1);
		}
	    }

	}
    }

    /**
     * Add/Update News settings into Database
     *
     * @param setting NewsSettings instance to be updated
     * @throws NewsException if db-access errror occurs
     */
    public static void updateSetting(NewsSettings setting) throws NewsException {

	Session session = null;
	Transaction tx = null;
	try {

	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();
	    session.saveOrUpdate(setting);
	    tx.commit();

	}
	catch (Exception ex) {
	    logger.debug(
		  "Unable to update news setting information into database", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed", ex1);
		}
	    }
	    throw new NewsException(
		  "Unable to update news setting information into database", ex);
	}
	finally {
	    if (session != null) {
		try {
		    PersistenceManager.releaseSession(session);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed", ex1);
		}
	    }

	}
    }

    /**
     * Update News item's status into Database
     *
     * @param news News instance to be updated
     * @param status status to be updated
     * @throws NewsException if db-access errror occurs
     */
    public static void updateStatus(News news, String status) throws
	  NewsException {

	// set new Status
	news.setStatus(new ItemStatus(status));

	updateNews(news);
    }

    /**
     * Creates News instance by user identity,status,language,title, description and Http Servlet Request
     *
     * @param userId user identity
     * @param statusCode status of news being created
     * @param languageCode language code of news being created
     * @param title title of news being created
     * @param description description of news being created
     * @param request Http Servlet Request
     * @return new News instance
     * @throws NewsException if db-access errror occurs
     */
    public static News createNews(Long userId, String statusCode,
				  String languageCode, String title,
				  String description,
				  HttpServletRequest request) {

	News news = createNews(userId, statusCode, languageCode, title,
			       description);
	NewsItem newsItem = news.getFirstNewsItem();

	// get module instance
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	news.setInstanceId(moduleInstance.getInstanceName());
	news.setSiteId(moduleInstance.getSite().getSiteId());

	// fill news item object
	newsItem.setCreationIp(RequestUtils.getRemoteAddress(request));
	newsItem.setCreationDate(new Date());

	return news;
    }

    /**
	   * Creates News instance by user identity,status,language,title and description
     *
     * @param userId user identity
     * @param statusCode status of news being created
     * @param languageCode language code of news being created
     * @param title title of news being created
     * @param description description of news being created
     * @return new News instance
     * @throws NewsException if db-access errror occurs
     */
    public static News createNews(Long userId, String statusCode,
				  String languageCode, String title,
				  String description) {

	News news = new News();
	NewsItem newsItem = new NewsItem();

	// populate news object
	HashSet newsItems = new HashSet();
	newsItems.add(newsItem);
	news.setNewsItem(newsItems);
	news.setStatus(new ItemStatus(statusCode));

	// populate news item object
	newsItem.setNews(news);
	newsItem.setDescription(description);
	newsItem.setLanguage(languageCode);
	newsItem.setTitle(title);
	newsItem.setUserId(userId);

	return news;
    }

    /**
     *
     * @param siteId
     * @param instanceId
     * @param sourceUrl
     * @return
     * @throws NewsException
     */
    public static News getNewsBySourceUrl(String siteId, String instanceId,
					  String sourceUrl) throws
	  NewsException {
	Session session = null;
	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("from rs in class " +
					  News.class.getName() +
					  " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.sourceUrl=:sourceUrl) ");

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);
	    q.setParameter("sourceUrl", sourceUrl, Hibernate.STRING);
	    List list = q.list();
	    if (list != null) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
		    News item = (News) iter.next();
		    return item;
		}
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return null;
    }

    /**
     * <p>Returns ordered list of news items status and selected site and instance considering pagination: </p>
     * <p>only the sublist,correspondence to the current page is retrieved</p>
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved items
     * @param firstResult beggining index of retrieved news sublist
     * @param maxResult ending index of retrieved news sublist
     * @return ordered sublist of news items for selected status,site and instance correspondence to the current page
     * @throws NewsException if db-access errror occurs
     */

    public static List getNewsItems(String siteId,
				    String instanceId,
				    String status, int firstResult,
				    int maxResult) throws
	  NewsException {

	Session session = null;
	List list = null;
	try {
	    session = PersistenceManager.getSession();

	    String queryString = "from n in class " +
		  News.class.getName() +
		  " where (n.siteId=:siteId) and (n.instanceId=:instanceId)";

	    if (status != null) {
		if (status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
queryString +=
			  "  and ((n.status=:status) or (n.archiveDate <= :curDate2 and n.status='" +
			  ItemStatus.PUBLISHED + "'))";
		}
		else {
		    if (status.equalsIgnoreCase(ItemStatus.PUBLISHED)) {

			queryString +=
			      " and (n.status=:status) and (n.archiveDate > :curDate3 or n.archiveDate is null)";
		    }
		    else {
			queryString += " and (n.status=:status)";
		    }
		}

	    }
	    //queryString += " and (n.releaseDate <= :curDate1) order by n.releaseDate desc";
	    queryString += " order by n.releaseDate desc";

	    logger.debug("News DButil Query: " + queryString);

	    Query q = session.createQuery(queryString);

	    // set query parameters -----
	    q.setFirstResult(firstResult);
	    q.setMaxResults(maxResult);

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.HOUR, 0);
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);
	    currentDate.set(java.util.Calendar.MILLISECOND, 0);
	    //
//	    q.setCalendar("curDate1", currentDate);

	    if (status != null) {
		q.setParameter("status", status, Hibernate.STRING);

		if (status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    q.setCalendar("curDate2", currentDate);
		}
		else {
		    if (status.equalsIgnoreCase(ItemStatus.PUBLISHED)) {
			q.setCalendar("curDate3", currentDate);
		    }
		}
	    }

	    // -----------------
	    list = q.list();
	    if (list != null) {
		////System.out.println("ARCHIVE SIZEEEEEE: " + list.size());
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news items list from database ", ex);
	    throw new NewsException(
		  "Unable to get news items list from database ", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return list;
    }

    /**
     * Gets Number of News items for site and instance by specified status
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved items
     * @return Number of News items for site and instance by specified status
     * @throws NewsException if db-access errror occurs
     */
    public static int getNumOfNewsItems(String siteId,
					String instanceId,
					String status) throws
	  NewsException {

	Session session = null;
	List list = null;
	int result = 0;

	try {
	    session = PersistenceManager.getSession();

	    String queryString = "select count(n.status) from " +
		  News.class.getName() +
		  " n, where (n.siteId=:siteId) and (n.instanceId=:instanceId) ";

	    if (status != null) {
		if (status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
queryString +=
			  "  and ((n.status=:status) or (n.archiveDate <= :curDate2 and n.status='" +
			  ItemStatus.PUBLISHED + "'))";
		}
		else {
		    if (status.equalsIgnoreCase(ItemStatus.PUBLISHED)) {

			queryString +=
			      " and (n.status=:status) and (n.archiveDate > :curDate3 or n.archiveDate is null)";
		    }
		    else {
			queryString += " and (n.status=:status)";
		    }
		}

	    }
	    queryString +=
		  " and (n.releaseDate <= :curDate1) order by n.releaseDate desc";

	    logger.debug("News DButil Query: " + queryString);

	    Query q = session.createQuery(queryString);

	    // set query parameters

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.HOUR, 0);
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);
	    currentDate.set(java.util.Calendar.MILLISECOND, 0);

	    q.setCalendar("curDate1", currentDate);

	    if (status != null) {
		q.setParameter("status", status, Hibernate.STRING);

		if (status.equalsIgnoreCase(ItemStatus.ARCHIVED)) {
		    q.setCalendar("curDate2", currentDate);
		}
		else {
		    if (status.equalsIgnoreCase(ItemStatus.PUBLISHED)) {
			q.setCalendar("curDate3", currentDate);
		    }
		}
	    }

	    Integer uniqueResult = ( (Integer) q.uniqueResult());

	    if (uniqueResult != null) {
		result = uniqueResult.intValue();
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get number of news items from database ",
			 ex);
	    throw new NewsException(
		  "Unable to get number of news items from database ", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return result;
    }

    public static List getNewsListForPublishing(String siteId,
						String instanceId,
						String countryIso,
						String langCode,
						Long numOfItems) throws
	  NewsException {

    Session session = null;
    List list = null;
    Query q = null;
    String countryWhere = "= :countryIso";

	try {
	    session = PersistenceManager.getRequestDBSession();

        if( countryIso == null ) {
            countryWhere = "is null";
        }
	    StringBuffer queryString = new StringBuffer();
	    queryString.append("select new org.digijava.module.news.form.NewsTeaserItem(n.id, ni.title, ni.creationDate, n.sourceUrl, ni.description, n.enableHTML, n.enableSmiles) from ");
	    queryString.append(News.class.getName());
	    queryString.append(" n, n.newsItem ");
	    queryString.append(
		  " ni where n.siteId=:siteId and n.instanceId=:instanceId and n.syndication != 1 ");

        queryString.append(" and (n.status=:status) and ((n.releaseDate <=:curDate1) and ((n.archiveDate is null) or (n.archiveDate >=:curDate2))) and (n.country " +countryWhere+ " and ni.language = :langCode) order by ni.creationDate desc");

	    q = session.createQuery(queryString.toString());

	    java.util.Calendar currentDate = java.util.Calendar.getInstance();
	    currentDate.set(java.util.Calendar.MINUTE, 0);
	    currentDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	    currentDate.set(java.util.Calendar.SECOND, 0);

        q.setParameter("siteId", siteId, Hibernate.STRING);
        q.setParameter("instanceId", instanceId, Hibernate.STRING);
        q.setParameter("status", ItemStatus.PUBLISHED, Hibernate.STRING);
        q.setCalendar("curDate1", currentDate);
        q.setCalendar("curDate2", currentDate);
        if( countryIso != null ) {
            q.setParameter("countryIso", countryIso, Hibernate.STRING);
        }
        q.setParameter("langCode", langCode, Hibernate.STRING);
	    q.setFirstResult(0);
	    if (numOfItems != null) {
            q.setMaxResults(numOfItems.intValue());
	    }
	    q.setCacheable(true);
	    list = q.list();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get news list from database ", ex);
	    throw new NewsException("Unable to get news list from database", ex);
	}

	return list;
    }

//
    public static String getShortVersionDelimiter(String siteId,
						  String instanceId) throws
	  NewsException {

	Session session = null;
	List list;
	String shortVersionDelimiter = "!#";
	try {
	    session = PersistenceManager.getSession();

	    StringBuffer queryString = new StringBuffer();

	    queryString.append(
		  "select rs.shortVersionDelimiter from rs in class " +
		  NewsSettings.class.getName() +
		  " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");

	    Query q = session.createQuery(queryString.toString());

	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    q.setCacheable(true);
	    list = q.list();

	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		shortVersionDelimiter = (String) iter.next();
		break;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get news setting from database ", ex);
	    throw new NewsException("Unable to get news setting from database",
				    ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return shortVersionDelimiter;

    }

}
