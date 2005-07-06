/*
 *   DbUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: DbUtil.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

package org.digijava.module.syndication.util;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.syndication.dbentity.CollectorFeed;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.dbentity.PublicationFeed;
import org.digijava.module.syndication.exception.SyndicationException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class DbUtil {

    /**
     * logging tracer
     */
    private static Logger logger = Logger.getLogger(DbUtil.class);

    /**
     *
     * @param siteId String
     * @param firstResult int
     * @param maxResult int
     * @return List
     * @throws SyndicationException
     */
    public static List getFeeds(String siteId, int firstResult,
                                int maxResult) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                CollectorFeedItem.class.getName() +
                " s where s.siteId=:siteId  ";

            q = session.createQuery(query);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get feeds from database ", ex);
            throw new SyndicationException("Unable to get feeds from database",
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

        return list;
    }


    /**
     *
     * @param siteId
     * @param instanceId
     * @param contentType
     * @return
     * @throws SyndicationException
     */
    public static List getFeeds(String siteId, String instanceId,
                                String contentType, int firstResult,
                                int maxResult) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                CollectorFeedItem.class.getName() +
                " s where s.siteId=:siteId and s.instanceId=:instanceId and s.contentType=:contentType ";

            q = session.createQuery(query);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setParameter("contentType", contentType, Hibernate.STRING);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get feeds from database ", ex);
            throw new SyndicationException("Unable to get feeds from database",
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

        return list;
    }

    /**
     *
     * @param id
     * @return
     * @throws SyndicationException
     */
    public static CollectorFeedItem getCollectorFeedItem(Long id) throws
        SyndicationException {

        Session session = null;
        CollectorFeedItem feedItem = null;
        try {
            session = PersistenceManager.getSession();

            feedItem = (CollectorFeedItem) session.load(CollectorFeedItem.class,
                id);

        }
        catch (Exception ex) {
            logger.debug("Unable to get news item from database ", ex);
            throw new SyndicationException(
                "Unable to get news item from database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
        }

        return feedItem;
    }

    /**
     *
     * @return
     * @throws SyndicationException
     */
    public static List getActiveFeeds() throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                CollectorFeedItem.class.getName() +
                " s where s.status =1 ";

            q = session.createQuery(query);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get feeds from database ", ex);
            throw new SyndicationException("Unable to get feeds from database",
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

        return list;
    }

    /**
     *
     * @param siteId
     * @param instanceId
     * @param contentType
     * @param firstResult
     * @param maxResult
     * @return
     * @throws SyndicationException
     */
    public static List getPublishedFeeds(String siteId, String instanceId,
                                         String contentType, int firstResult,
                                         int maxResult) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                PublicationFeed.class.getName() +
                " s where s.siteId=:siteId and s.instanceId=:instanceId and s.contentType=:contentType ";

            q = session.createQuery(query);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setParameter("contentType", contentType, Hibernate.STRING);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get published feeds from database ", ex);
            throw new SyndicationException(
                "Unable to get published feeds from database",
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

        return list;
    }

    /**
     *
     * @param siteId
     * @param instanceId
     * @param contentType
     * @param firstResult
     * @param maxResult
     * @return
     * @throws SyndicationException
     */
    public static List getPublishedFeeds(String siteId, String instanceId,
                                         String contentType, String countryIso,
                                         String langCode) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                PublicationFeed.class.getName() +
                " s where s.siteId=:siteId and s.instanceId=:instanceId and s.contentType=:contentType and s.country=:countryIso and s.language=:langCode";

            q = session.createQuery(query);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setParameter("contentType", contentType, Hibernate.STRING);
            q.setParameter("countryIso", countryIso, Hibernate.STRING);
            q.setParameter("langCode", langCode, Hibernate.STRING);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get published feeds from database ", ex);
            throw new SyndicationException(
                "Unable to get published feeds from database",
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

        return list;
    }

    /**
     *
     * @param id
     * @return
     * @throws SyndicationException
     */
    public static PublicationFeed getPublishedFeed(Long id) throws
        SyndicationException {

        Session session = null;
        PublicationFeed feed = null;

        try {
            session = PersistenceManager.getSession();
            feed = (PublicationFeed) session.load(PublicationFeed.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get feed from database ", ex);
            throw new SyndicationException("Unable to get feed from database",
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

        return feed;
    }

    /**
     *
     *
     * @param id
     * @return
     * @throws SyndicationException
     */
    public static CollectorFeed getCollectorFeed(Long id) throws
        SyndicationException {

        Session session = null;
        CollectorFeed feed = null;

        try {
            session = PersistenceManager.getSession();
            feed = (CollectorFeed) session.load(CollectorFeed.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get feed from database ", ex);
            throw new SyndicationException("Unable to get feed from database",
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

        return feed;
    }



    /**
     *
     * @param id
     * @return
     * @throws SyndicationException
     */
    public static void deleteFeed(Long id) throws
        SyndicationException {

        Session session = null;
        Transaction tx = null;


        try {
            String queryString = "select from " +
                PublicationFeed.class.getName() +
                " fd where fd.id = " + String.valueOf(id);

            session = PersistenceManager.getSession();
            PublicationFeed feed = getPublishedFeed(id);
            if( feed != null ) {
                tx = session.beginTransaction();
                session.delete(feed);
                tx.commit();
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to delete feed from database ", ex);
            throw new SyndicationException(
                "Unable to delete feed from database",
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
    }

    /**
     *
     * @param id
     * @throws SyndicationException
     */
    public static void deleteCollectorFeed(Long id) throws
        SyndicationException {


        Session session = null;
        Transaction tx = null;

        try {
/*            String queryString = "select from " +
                CollectorFeedItem.class.getName() +
                " fd where fd.id = " + String.valueOf(id);
*/
            CollectorFeedItem item = getCollectorFeedItem(id);
            if(item != null ) {
                session = PersistenceManager.getSession();
                CollectorFeed feed = getCollectorFeed(item.getFeed().getId());
                if( feed != null ) {
                    if( feed.getItems() != null ) {
                        int size = feed.getItems().size();
                        tx = session.beginTransaction();
                        session.delete(item);
                        if( size == 1 ) {
                            session.delete(feed);
                        }
                        tx.commit();
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to delete feed from database ", ex);
            throw new SyndicationException(
                "Unable to delete feed from database",
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
    }

    /**
     *
     * @param feedUrl
     * @return
     * @throws SyndicationException
     */
    public static CollectorFeed getFeed(String feedUrl) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";
        CollectorFeed feed = null;

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                CollectorFeedItem.class.getName() +
                " s where s.feed.feedUrl=:feedUrl";

            q = session.createQuery(query);

            q.setParameter("feedUrl", feedUrl, Hibernate.STRING);

            list = q.list();
            if (list != null) {

                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    CollectorFeedItem item = (CollectorFeedItem) iter.next();
                    feed = item.getFeed();
                    break;
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get feeds from database ", ex);
            throw new SyndicationException("Unable to get feeds from database",
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

        return feed;
    }

    /**
     *
     * @param feedUrl
     * @return
     * @throws SyndicationException
     */
    public static CollectorFeed getFeed(String siteId, String instanceId,
                                        String contentType,
                                        String feedUrl) throws
        SyndicationException {

        Session session = null;
        List list = null;
        Query q = null;
        String query = "";
        CollectorFeed feed = null;

        try {
            session = PersistenceManager.getSession();

            query = "select s from " +
                CollectorFeedItem.class.getName() +
                " s where s.siteId=:siteId and s.instanceId=:instanceId and s.contentType=:contentType and " +
                "s.feed.feedUrl=:feedUrl";

            q = session.createQuery(query);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setParameter("contentType", contentType, Hibernate.STRING);
            q.setParameter("feedUrl", feedUrl, Hibernate.STRING);

            list = q.list();
            if (list != null) {

                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    CollectorFeedItem item = (CollectorFeedItem) iter.next();
                    feed = item.getFeed();
                    break;
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get feeds from database ", ex);
            throw new SyndicationException("Unable to get feeds from database",
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

        return feed;
    }

    /**
     *
     * @param feed
     * @throws SyndicationException
     */
    public static void updateFeed(CollectorFeed feed) throws
        SyndicationException {

        Session session = null;
        Transaction tx = null;
        List list = null;
        try {

            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(feed);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update feed information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SyndicationException(
                "Unable to update feed information into database", ex);
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
     *
     * @param feed
     * @throws SyndicationException
     */
    public static void updateFeed(PublicationFeed feed) throws
        SyndicationException {

        Session session = null;
        Transaction tx = null;
        List list = null;
        try {

            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(feed);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update feed information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SyndicationException(
                "Unable to update feed information into database", ex);
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
     *
     * @param feedItem
     * @throws SyndicationException
     */
    public static void updateFeedCollector(CollectorFeedItem feedItem) throws
        SyndicationException {

        Session session = null;
        Transaction tx = null;
        List list = null;
        try {

            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(feedItem);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug(
                "Unable to update feed collector item information into database",
                ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SyndicationException(
                "Unable to update feed collector item information into database",
                ex);
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

}
