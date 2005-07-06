/*
 *   DbUtil.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 10, 2003
 * 	CVS-ID: $Id: DbUtil.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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

package org.digijava.module.highlights.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.exception.HighlightsException;
import org.digijava.module.highlights.form.HighlightTeaserItem;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);

    /**
     * Returns active Highlight for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @return active Highlight or null
     * @throws HighlightsException if db-access errror occurs
     */
    public static Highlight getHighlight(String siteId, String instanceId) throws
	 HighlightsException {

	Session session = null;
	Highlight highlight = new Highlight();
	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("from h in class " +
					  Highlight.class.getName() +
		 " where (h.siteId=:siteId) and (h.instanceId=:instanceId) and (h.active=true)");

//            q.setCacheable(true);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    List result = q.list();
	    if (result.size() != 0) {
		highlight = (Highlight) result.get(0);
	    }
	    else {
		highlight = null;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlights list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return highlight;
    }

    /**
     * Returns ordered Highlights list for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @param order indicates by which property and direction(i.e. ascending or descending) should be ordered retrieved Highlights list
     * @return List of ordered Highlights
     * @throws HighlightsException if db-access errror occurs
     */
    public static List getHighlightList(String siteId, String instanceId,
					String order) throws
	 HighlightsException {

	Session session = null;
	Highlight highlight;
	Query q = null;
	List list = new ArrayList();

	try {
	    session = PersistenceManager.getSession();
	    String queryString = new String();

	    if ( (order.equals("order by ur.firstNames desc")) ||
		(order.equals("order by ur.firstNames asc"))) {

		queryString = "select h.id, h.siteId, h.instanceId, h.title, h.description, h.topic, h.active, h.authorUserId, " +
		     "h.shortTopicLength,h.imageHeight, h.imageWidth, h.image, h.showImage, h.layout, h.isPublic  from " +
		     Highlight.class.getName() + " h, " + User.class.getName() +
		     " ur where (h.authorUserId=ur.id) and (h.siteId=:siteId) and (h.instanceId=:instanceId) " +
		     order;
	    }
	    else {
		queryString = "select h.id, h.siteId, h.instanceId, h.title, h.description, h.topic, h.active, h.authorUserId, " +
		     "h.shortTopicLength,h.imageHeight, h.imageWidth, h.image, h.showImage, h.layout, h.isPublic from h in class " +
		     Highlight.class.getName() +
		     " where (h.siteId=:siteId) and (h.instanceId=:instanceId) " +
		     order;
	    }

//            logger.debug("Query String is:" + queryString);

	    q = session.createQuery(queryString);
//            q.setCacheable(true);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    Iterator iter = q.list().iterator();
	    while (iter.hasNext()) {
		Object[] results = (Object[]) iter.next();
		highlight = new Highlight();
		highlight.setId( (Long) results[0]);
		highlight.setSiteId( (String) results[1]);
		highlight.setInstanceId( (String) results[2]);
		highlight.setTitle( (String) results[3]);
		highlight.setDescription( (String) results[4]);
		highlight.setTopic( (String) results[5]);
		highlight.setActive( ( (Boolean) results[6]).booleanValue());
		highlight.setAuthorUserId( (Long) results[7]);
		highlight.setShortTopicLength( ( (Integer) results[8]).intValue());
		highlight.setImageHeight( ( (Integer) results[9]).intValue());
		highlight.setImageWidth( ( (Integer) results[10]).intValue());
		highlight.setShowImage( ( (Boolean) results[12]).booleanValue());
		highlight.setLayout( ( (Integer) results[13]).intValue());
		highlight.setIsPublic( ( (Boolean) results[14]).booleanValue());

		list.add(highlight);
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlights list from database", ex);
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
     * Returns Highlight by identity
     * @param id Highlight identity
     * @return Highlight with specified identity or null
     * @throws HighlightsException if db-access errror occurs
     */
    public static Highlight getHighlight(Long id) throws HighlightsException {

	Highlight highlight = null;
	Session session = null;

	try {
	    session = PersistenceManager.getSession();
	    highlight = (Highlight) session.load(Highlight.class, id);
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlight from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlight from database", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return highlight;
    }

    /**
     * Saves Highlight instance into Database
     * @param highlight Highlight instance to be saved
     * @throws HighlightsException if db-access errror occurs
     */
    public static void createHighlight(Highlight highlight) throws
	 HighlightsException {

	Session sess = null;
	Transaction tx = null;

	try {
	    sess = PersistenceManager.getSession();
	    tx = sess.beginTransaction();

	    sess.save(highlight);
	    Iterator iter = highlight.getLinks().iterator();
	    while (iter.hasNext()) {
		HighlightLinks item = (HighlightLinks) iter.next();
		item.setHighlight(highlight);
		sess.save(item);
	    }
	    tx.commit();
	}
	catch (Exception ex) {

	    logger.debug("Unable to create highlight", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex1);
		}
	    }
	    throw new HighlightsException(
		 "Unable to create highlight", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed ", ex1);
		}
	    }

	}

    }

    /**
     * Updates Highlight instance into Database
     * @param highlight Highlight instance to be updated
     * @throws HighlightsException if db-access errror occurs
     */
    public static void updateHighlight(Highlight highlight) throws
	 HighlightsException {
	Transaction tx = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();

	    Highlight oldHighlight = (Highlight) session.load(Highlight.class,
		 highlight.getId());

	    oldHighlight.setTitle(highlight.getTitle());
	    oldHighlight.setDescription(highlight.getDescription());
	    oldHighlight.setTopic(highlight.getTopic());
	    oldHighlight.setActive(highlight.isActive());
	    oldHighlight.setAuthorUserId(highlight.getAuthorUserId());
	    oldHighlight.setUpdaterUserId(highlight.getUpdaterUserId());
	    oldHighlight.setCreationDate(highlight.getCreationDate());
	    oldHighlight.setUpdationDate(highlight.getUpdationDate());
	    oldHighlight.setLayout(highlight.getLayout());
	    oldHighlight.setShowImage(highlight.isShowImage());
	    oldHighlight.setShortTopicLength(highlight.getShortTopicLength());
	    if (highlight.getImage() != null) {
		oldHighlight.setImage(highlight.getImage());
		oldHighlight.setImageHeight(highlight.getImageHeight());
		oldHighlight.setImageWidth(highlight.getImageWidth());
	    }
	    else {
		oldHighlight.setImage(null);
		oldHighlight.setImageHeight(0);
		oldHighlight.setImageWidth(0);
	    }
	    ArrayList oldLinks = new ArrayList(oldHighlight.getLinks());
	    ArrayList newLinks = new ArrayList(highlight.getLinks());
	    if (oldLinks == null) {
		oldLinks = new ArrayList();
	    }
	    if (newLinks == null) {
		newLinks = new ArrayList();
	    }
	    //
	    if (oldLinks.size() >= newLinks.size()) {
		logger.debug("Some link(s) has been removed");
		Iterator iterOld = oldLinks.iterator();
		Iterator iterNew = newLinks.iterator();
		int i = 0;
		while (iterOld.hasNext()) {
		    HighlightLinks itemOld = (HighlightLinks) iterOld.next();
		    HighlightLinks itemNew = new HighlightLinks();
		    if (iterNew.hasNext()) {
			itemNew = (HighlightLinks) iterNew.next();
		    }
		    if (i >= newLinks.size()) {
			logger.debug("removing item:" + itemOld.getOffset() +
				     ":" + itemOld.getName() + ":" +
				     itemOld.getUrl());
			session.delete(itemOld);
		    }
		    else {
			session.delete(itemOld);

			itemOld.setOffset(itemNew.getOffset());
			itemOld.setName(itemNew.getName());
			itemOld.setUrl(itemNew.getUrl());
			itemOld.setHighlight(itemNew.getHighlight());

//                        session.update(itemOld);
			session.save(itemOld);
		    }
		    ++i;
		}
	    }
	    else {
		logger.debug("Some link(s) are added");
		Iterator iterNew = newLinks.iterator();
		Iterator iterOld = oldLinks.iterator();
		ArrayList saveOrUpdate = new ArrayList();
		while (iterNew.hasNext()) {
		    HighlightLinks itemNew = (HighlightLinks) iterNew.next();
		    boolean saveOrUpdateItem = true; //save
		    iterOld = oldLinks.iterator();
		    while (iterOld.hasNext()) {
			HighlightLinks itemOld = (HighlightLinks) iterOld.next();
			if (itemOld.getOffset() == itemNew.getOffset()) {
			    saveOrUpdateItem = false;
			    break;
			}
		    }
		    saveOrUpdate.add(new Boolean(saveOrUpdateItem));
		}
		iterNew = newLinks.iterator();
		iterOld = oldLinks.iterator();
		Iterator iterSaveOrUpdate = saveOrUpdate.iterator();
		int i = 0;
		while (iterNew.hasNext()) {
		    HighlightLinks itemNew = (HighlightLinks) iterNew.next();
		    HighlightLinks itemOld = new HighlightLinks();
		    boolean saveOrUpdateItem = true; //save
		    if (iterSaveOrUpdate.hasNext()) {
			saveOrUpdateItem = ( (Boolean) iterSaveOrUpdate.next()).
			     booleanValue();
		    }
		    if (!saveOrUpdateItem) {
			session.delete(itemOld);

			itemOld.setOffset(itemNew.getOffset());
			itemOld.setName(itemNew.getName());
			itemOld.setUrl(itemNew.getUrl());
			itemOld.setHighlight(itemNew.getHighlight());
//                        session.update(itemOld);
			session.save(itemOld);
		    }
		    else {
			itemOld.setOffset(itemNew.getOffset());
			itemOld.setName(itemNew.getName());
			itemOld.setUrl(itemNew.getUrl());
			itemOld.setHighlight(itemNew.getHighlight());

			session.save(itemOld);
		    }
		    ++i;
		}
	    }
	    //
	    oldHighlight.setLinks(highlight.getLinks());
	    //
	    if (highlight.getContentType() != null) {
		oldHighlight.setContentType(highlight.getContentType());
	    }
	    oldHighlight.setIsPublic(highlight.isIsPublic());
	    //
	    session.update(oldHighlight);
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to update highlight information into database",
			 ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (Throwable cause) {
		    logger.warn("rollback() failed ", cause);
		}
	    }
	    throw new HighlightsException(
		 "Unable to update highlight information into database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}
    }

    /**
     * Archives all active Highlights(if any) for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @throws HighlightsException if db-access errror occurs
     */
    public static void ArchiveHighlights(String siteId, String instanceId) throws
	 HighlightsException {

	Session session = null;
	Transaction tx = null;

	try {
	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();

	    Query q = session.createQuery("from h in class " +
					  Highlight.class.getName() +
		 " where (h.siteId=:siteId) and (h.instanceId=:instanceId) and (h.active=true)");

//            q.setCacheable(true);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    List result = q.list();
	    if ( (result != null) && (result.size() != 0)) {
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
		    Highlight highlight = (Highlight) iterator.next();
		    highlight.setActive(false);
		    session.update(highlight);
		}
	    }
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug(
		 "Unable to get or update highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get or update highlights list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

    }

    /**
     * Determines existance of active Highlight for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @return true if active Highlight exists, false otherwise
     * @throws HighlightsException if db-access errror occurs
     */
    public static boolean isArchiveHighlightList(String siteId,
						 String instanceId) throws
	 HighlightsException {

	Session session = null;
	Highlight highlight = new Highlight();
	Query q = null;
	boolean haveArchive = false;

	try {
	    session = PersistenceManager.getSession();
	    String queryString = new String();

	    queryString = "select count(h.active) from " +
		 Highlight.class.getName() +
		 " h, where (h.siteId=:siteId) and (h.instanceId=:instanceId) and (h.active=false)";

	    q = session.createQuery(queryString);

	    q.setCacheable(true);
	    q.setParameter("siteId", siteId, Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    Integer uniqueResult = ( (Integer) q.uniqueResult());
	    if (uniqueResult != null && uniqueResult.intValue() > 0) {
		haveArchive = true;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlights list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}
	return haveArchive;
    }

    /**
     * Determines is highlight with specified identity active or not
     * @param id highlight identity
	  * @return true, if highlight of specified identity is active, false otherwise
     * @throws HighlightsException if db-access errror occurs
     */
    public static boolean isHighlightActive(Long id) throws
	 HighlightsException {

	Session session = null;
	Query q = null;
	boolean active = false;

	try {
	    session = PersistenceManager.getSession();
	    String queryString = new String();

	    queryString = "select h.active from " +
		 Highlight.class.getName() +
		 " h, where (h.id=:id)";

	    q = session.createQuery(queryString);
	    q.setParameter("id", id, Hibernate.LONG);

	    Boolean uniqueResult = ( (Boolean) q.uniqueResult());
	    if (uniqueResult != null && uniqueResult.booleanValue()) {
active = true;
	    }
	    logger.debug("IS HIGHLIGHT ACTIVE?:" + active);
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlight from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlight from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}
	return active;
    }

    /**
     * Returns Highlight by identity
     * @param id Highlight identity
     * @return Highlight with specified identity or null
     * @throws HighlightsException if db-access errror occurs
     */
    public static byte[] getHighlightImage(Long id) throws HighlightsException {

	byte[] image = null;
	Session session = null;

	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("select h.image from " +
					  Highlight.class.getName() + " h, " +
					  " where (h.id=:id)");

	    q.setParameter("id", id, Hibernate.LONG);

	    List result = q.list();
	    if (result.size() != 0) {
		image = (byte[]) result.get(0);
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlight from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlight from database", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return image;
    }

    /**
     * Returns TeaserItem for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @return active TeaserItem or null
     * @throws HighlightsException if db-access errror occurs
     */
    public static HighlightTeaserItem getTeaserItem(String siteId,
	 String instanceId) throws
	 HighlightsException {

	Session session = null;
	HighlightTeaserItem teaserItem = new HighlightTeaserItem();

	try {
	    session = PersistenceManager.getSession();

	    Query q1 = session.createQuery("select new org.digijava.module.highlights.form.HighlightTeaserItem(h.id,h.title,h.description,h.topic,h.shortTopicLength,h.layout,h.imageWidth,h.imageHeight,h.contentType,h.showImage) from " +
					   Highlight.class.getName() + " h, " +
		 " where (h.siteId=:siteId) and (h.instanceId=:instanceId) and (h.active=true)");

	    q1.setCacheable(true);
	    q1.setParameter("siteId", siteId, Hibernate.STRING);
	    q1.setParameter("instanceId", instanceId, Hibernate.STRING);

	    List result1 = q1.list();
	    if (result1.size() != 0) {
		teaserItem = (HighlightTeaserItem) result1.get(0);

		Query q2 = session.createQuery("select new org.digijava.module.highlights.dbentity.HighlightLinks(hl.offset,hl.url,hl.name) from " +
					       HighlightLinks.class.getName() +
					       " hl, " +
		     " where (hl.highlight=:hId) order by hl.offset asc");

		q2.setCacheable(true);
		q2.setParameter("hId", teaserItem.getId(), Hibernate.LONG);

		List result2 = q2.list();
		if (result2.size() != 0) {
		    List hLinks = result2;
		    Iterator iter = hLinks.iterator();
		    while (iter.hasNext()) {
			HighlightLinks item = (HighlightLinks) iter.next();
			teaserItem.getLinksArray().add(item);
		    }
		}
	    }
	    else {
		teaserItem = null;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlights list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return teaserItem;
    }

    /**
     * Returns number active Highlights list for selected site and instance
     * @param siteId site identity
     * @param instanceId instance name
     * @return active Highlight or null
     * @throws HighlightsException if db-access errror occurs
     */
    public static int getNumOfActiveHighlights(String siteId, String instanceId) throws
	 HighlightsException {

	Session session = null;
	int result = 0;
	try {
	    session = PersistenceManager.getSession();

	    Query q = session.createQuery("select count(h.active) from " +
					  Highlight.class.getName() +
					  " h, where (h.siteId=:siteId) and (h.instanceId=:instanceId) and (h.active=true)");
	    q.setParameter("siteId", siteId,Hibernate.STRING);
	    q.setParameter("instanceId", instanceId, Hibernate.STRING);

	    Integer uniqueResult = ( (Integer) q.uniqueResult());
	    if (uniqueResult != null) {
		result = uniqueResult.intValue();
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get highlights list from database", ex);
	    throw new HighlightsException(
		 "Unable to get highlights list from database", ex);
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

}