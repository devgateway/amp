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

package org.digijava.kernel.translator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;

public class CachedTranslatorWorker extends TranslatorWorker {

	private static Logger logger = Logger.getLogger(CachedTranslatorWorker.class);

    private AbstractCache messageCache;

    CachedTranslatorWorker() {
        super();
        messageCache = DigiCacheManager.getInstance().getCache("org.digijava.kernel.entity.Message.id_cache");
        
        //cache the first 5000 entries based on their access date     
        logger.info("Caching the last accessed 5000 translation entries...");
       	Session session = PersistenceManager.openNewSession();
       	try {
       		Criteria criteria = session.createCriteria(Message.class);
       		criteria.setMaxResults(5000);
       		criteria.addOrder(Order.desc("lastAccessed"));
       		criteria.add(Restrictions.isNotNull("lastAccessed"));
       	
       		List<Message> lastAccessedMessages = criteria.list();
       		for (Message message : lastAccessedMessages) messageCache.put(message, message);
       	}
       	finally {
       		PersistenceManager.closeSession(session);
       	}

       	logger.info("Caching done.");

    }

    /**
     * Read translation from database and put to translation cache.
     * This is one should not update access times.
     * @param key translation key
     * @param locale locale
     * @param siteId owner site
     * @throws WorkerException if process was not completed successfully
     */
    public void refresh(String key, String locale, Long siteId) throws WorkerException {
        Session session = null;

        try {

            Message mesageKey = new Message();
            mesageKey.setKey(processKeyCase(key));
            mesageKey.setLocale(locale);
            mesageKey.setSite(SiteCache.lookupById(siteId));

            session = PersistenceManager.getSession();
            Message message = (Message) session.load(Message.class, mesageKey);
            processBodyChars(message);//if we run script on db which will do same action, do we need this here?
            
            messageCache.put(message, message);
            logger.debug("Refreshed translation for siteId="
                         + siteId + ", key = " + key + ",locale=" + locale);
        }
        catch (ObjectNotFoundException onfe) {
        }

        catch (Exception ex) {
            throw new WorkerException("Unable to refresh translation[key=" +
                                      key + ", locale=" + locale + ", siteId=" +
                                      siteId + "]", ex);
        }
    }
    
    //TODO may be bad idea!
    public void refresh(Message message) throws WorkerException {
        messageCache.put(message, message);
    }

    /**
     * Overrides method in parent worker.
     * This one searches in cache
     * @see TranslatorWorker#getByKey(String, String, String, String, String)
     */
    public Message getByKey(String key, String body, String keyWords, String locale, Long siteId) {
    	return getByKey(key, locale, siteId, true, keyWords);
    }

    public Message getByKey(String key, String locale, Long siteId, boolean overwriteKeywords,String keywords) {
        return internalGetByKey(key, locale, siteId, overwriteKeywords, keywords);
    }

    private Message internalGetByKey(String key, String locale, Long siteId, boolean overwriteKeywords, String keywords) {
    	Message message = new Message();
        message.setLocale(locale);
        message.setSite(SiteCache.lookupById(siteId));
        message.setKey(key);
        //search message
        Object obj = messageCache.get(message);   
        if (obj == null) {
        	//try loading it from db
        	Session ses;
			try {
				ses = PersistenceManager.getSession();
				Message realMsg = (Message) ses.get(Message.class, message);
				if (realMsg != null) {
					obj = realMsg;
					Serializable identifier =
                            PersistenceManager.getClassMetadata(Message.class).getIdentifier(realMsg, (SessionImplementor)ses);
					messageCache.put(identifier, realMsg);
				}
			} catch (HibernateException e) {
                logger.error("Failed reading message from database", e);
			}
        }
        
        if (obj == null) {
            logger.debug("No translation exists for siteId="+ siteId + ", key = " + key + ",locale=" + locale+", creating new");
            return null;
        } else {
        	Message foundMessage = (Message)obj;
        	if (overwriteKeywords && keywords != null) {
        		foundMessage.setKeyWords(keywords);
        	}
        	updateTimeStamp(foundMessage);
            return foundMessage;
        }
    }
    

    public void save(Message message) {

    	saveDb(message); //message key and body will be processed there 
        
        messageCache.put(message, message);
        fireRefreshAlert(message);
    }

    /**
     * Updates a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void update(Message message) {

        updateDb(message);//message key and body will be processed there

        messageCache.put(message, message);
        fireRefreshAlert(message);
    }

    /**
     * Deletes a particular message
     * @param message
     * @throws WorkerException
     */
    public void delete(Message message) throws WorkerException {
        deleteDb(message);//message key and body will be processed there
        messageCache.evict(message);
        fireRefreshAlert(message);
    }

	protected void setTimestamps(String key, Timestamp timestamp) throws WorkerException {
        if (key == null)
            return;

        Session ses = null;
        
        @SuppressWarnings("unchecked")
        List messages;
        
        String queryString = "from " + Message.class.getName() + " msg where msg.key=:msgKey";

        try {

            ses = PersistenceManager.getSession();
//beginTransaction();
            Query q = ses.createQuery(queryString);
            q.setString("msgKey", processKeyCase(key.trim()));
            
            messages = q.list();
            
            @SuppressWarnings("unchecked")
            Iterator it = messages.iterator();

            while (it.hasNext()) {
                Message msg = (Message) it.next();
                msg.setCreated(timestamp);
                ses.update(msg);
                messageCache.put(msg, msg);
            }

            //tx.commit();

        }
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key, e);
            throw new WorkerException("Error updating translations. key=" + key, e);
        }

    }
	
	public void cleanMessageCache()
	{
		this.messageCache.clear();
	}
}
