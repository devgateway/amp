/*
 *   CachedTranslatorWorker.java
 * 	 Created by Mikheil Kapanadze mikheil@digijava.org
 * 	 Date: Jan 04, 2004
 * 	 CVS-ID: $Id: CachedTranslatorWorker.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.translator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.persistence.PersistenceManager;

import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.entity.Message;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.swarmcache.ObjectCache;
import org.digijava.kernel.util.DigiCacheManager;

public class CachedTranslatorWorker
    extends TranslatorWorker {
    private static Logger logger =
        Logger.getLogger(CachedTranslatorWorker.class);

    private ObjectCache messageCache;

    CachedTranslatorWorker() {
        messageCache = DigiCacheManager.getInstance().getCache(
            "org.digijava.kernel.entity.Message.id_cache");
    }

    public Message get(String key, String locale, String siteId) throws
        WorkerException {

        Message message = new Message();

        message.setKey(key);
        message.setLocale(locale);
        message.setSiteId(siteId);

        Object obj = messageCache.get(message);
        if (obj == null) {
            logger.debug("No translation exists for siteId="
                         + siteId + ", key = " + key + ",locale=" + locale);
            return null;
        }
        else {
            return (Message) obj;
        }
    }

    public void save(Message message) throws WorkerException {
        logger.debug("Saving translation. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        Session ses = null;
        Transaction tx = null;

        try {

            ses = PersistenceManager.getSession();
            tx = ses.beginTransaction();
            ses.save(message);
            tx.commit();
        }
        catch (SQLException se) {
            String errKey = "TranslatorWorker.SqlExSaveMessage.err";
            Object[] params = {
                se.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, se);

        }
        catch (HibernateException e) {
            String errKey = "TranslatorWorker.HibExSaveMessage.err";
            Object[] params = {
                e.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, e);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }

            throw new WorkerException(errKey, e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};

                logger.l7dlog(Level.WARN, errKey, params, e);
                e.printStackTrace();
            }
        }
        // Put into cache
        logger.debug("Putting translation into cache. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        messageCache.put(message, message);
    }

    /**
     * Updates a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void update(Message message) throws WorkerException {
        logger.debug("Saving translation. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        Session ses = null;
        Transaction tx = null;

        try {
            ses = PersistenceManager.getSession();
            tx = ses.beginTransaction();
            ses.update(message);
            tx.commit();
        }
        catch (SQLException se) {
            String errKey = "TranslatorWorker.SqlExUpdateMessage.err";
            Object[] params = {
                se.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, se);
        }
        catch (HibernateException e) {
            String errKey = "TranslatorWorker.HibExUpdateMessage.err";
            Object[] params = {
                e.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, e);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                logger.l7dlog(Level.WARN, errKey, params, e);
            }
        }

        logger.debug("Putting translation into cache. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        messageCache.put(message, message);

    }

}