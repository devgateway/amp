/*
 *   TranslatorBean.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created: Jul 24, 2003
 *   CVS-ID: $Id: TranslatorWorker.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.util.I18NHelper;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.exception.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache;

/**
 * @author Shamanth Murthy
 *
 * Worker class for all translator related operations
 */
public class TranslatorWorker {

    private static Logger log =
        Logger.getLogger(TranslatorWorker.class);

    static {
        log.setResourceBundle(I18NHelper.getKernelBundle());
    }

    private static String CURR_SITE_SRC_MSG = "SITE_SRCLANG";
    private static String CURR_SITE_TARGET_MSG = "SITE_TARGETLANG";
    private static String ROOT_SITE_SRC_MSG = "ROOTSITE_SRCLANG";
    private static String ROOT_SITE_TARGET_MSG = "ROOTSITE_TARGETLANG";

    private static long MILLI_SECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    private static TranslatorWorker defaultWorker;
    private static TranslatorWorker cachedWorker;

    // Factory methods
    private static synchronized TranslatorWorker getDefaultWorker() {
        if (defaultWorker == null) {
            defaultWorker = new TranslatorWorker();
        }

        return defaultWorker;
    }

    private static synchronized TranslatorWorker getCachedWorker() {
        if (cachedWorker == null) {
            cachedWorker = new CachedTranslatorWorker();
        }

        return cachedWorker;
    }

    public static TranslatorWorker getInstance() {
        return getDefaultWorker();
    }

    /**
     * Returns a message string matching local,key,site
     *
     * @param key site string id
     * @param locale
     * @param siteId ( for example demosite )
     * @return
     * @throws WorkerException
     */
    public static String translate (String key, String locale, String siteId)
        throws WorkerException {
      String retVal = null;
      Message msg = getInstance(key).getMessage(key, locale, siteId);
      if (msg != null) {
        retVal = msg.getMessage();
      } else {
        retVal = "";
      }
      return retVal;
    }

    public static TranslatorWorker getInstance(String key) {
        /** @todo temporary solution. needs to be read from digi.xml */
        //if (key.startsWith("cpv:")) {
        //    return getDefaultWorker();
        //}
        //else {
        return getCachedWorker();
        //}
    }

    /**
     * Gets all distinct keys with only one occurence of ':'
     * for a given siteId and its root
     *
     * @param siteId
     * @param rootSiteId
     * @return
     * @throws WorkerException
     */
    public Set getPrefixesForSite(String siteId, String rootSiteId) throws
        WorkerException {

        HashSet returnList = new HashSet();

        List ls = getKeysForSite(siteId, rootSiteId);

        Iterator it = ls.iterator();

        while (it.hasNext()) {
            String msg = (String) it.next();
            String[] str = msg.split(":");

            if (str[0] != null) {
                returnList.add(str[0]);
            }
        }

        return returnList;
    }

    /**
     * Returns a message object matching local,key,site and type
     * @param key
     * @param locale
     * @return
     * @throws WorkerException
     */
    /*
         public Message getLocal(String key, String locale, String siteId) throws
        WorkerException {
        Message message = new Message();
        message.setKey(key);
        message.setLocale(locale);
        message.setSiteId(siteId);
        Object obj = messageCache.get(message);
        if (obj == null) {
            log.debug("No translation exists for siteId="
                      + siteId + ", key = " + key + ",locale=" + locale);
            return null;
        }
        else {
            return (Message) obj;
        }
      }
     */

    /**
     * Returns a message object matching local,key,site
     *
     * @param key
     * @param locale
     * @param siteId ( for example demosite )
     * @return
     * @throws WorkerException
     */
    public Message getMessage(String key, String locale, String siteId) throws
        WorkerException {

        Site site = SiteCache.getInstance().getSite(siteId);

        if (site == null) {
            log.debug("Site Id not found for site " + siteId);
            return null;
        }

        return get(key, locale, String.valueOf(site.getId()));
    }

    /**
     *
     * @param key
     * @param locale
     * @param siteId
     * @return
     * @throws WorkerException
     */
    public Message getFromGroup(String key, String locale, Site site,
                                String defaultMessage) throws WorkerException {
        log.debug("getFromGroup() called");
        String siteId = site.getId().toString();

        Message trnMess = getFromGroup(key, locale, site);

        if (trnMess == null) {
            log.debug("constructing default translation");
            trnMess = new Message();
            trnMess.setMessage(defaultMessage);
            trnMess.setKey(key);
            trnMess.setLocale(locale);
            trnMess.setSiteId(siteId);
        }

        return trnMess;
    }

    /**
     *
     * @param key
     * @param locale
     * @param site
     * @return
     * @throws WorkerException
     */
    public Message getFromGroup(String key, String locale, Site site) throws
        WorkerException {
        log.debug("getFromGroup() called");
        String siteId = site.getId().toString();

        Message trnMess = get(key, locale, siteId);
        if (trnMess != null) {
            log.debug("local translation exists");
            return trnMess;
        }
        if (!locale.equals("en")) {
            trnMess = get(key, "en", siteId);
            if (trnMess != null) {
                log.debug("default local translation exists");
                return trnMess;
            }
        }
        String rootSiteId = SiteCache.getInstance().getRootSite(site).getId().
            toString();
        if (!rootSiteId.equals(siteId)) {
            trnMess = get(key, locale, rootSiteId);
            if (trnMess != null) {
                log.debug("group translation exists");
                return trnMess;
            }
            if (!locale.equals("en")) {
                trnMess = get(key, "en", rootSiteId);
                if (trnMess != null) {
                    log.debug("default group translation exists");
                    return trnMess;
                }
            }
        }

        return trnMess;
    }

    /**
     * Returns a message object matching local,key,site and type
     * @param key
     * @param locale
     * @return
     * @throws WorkerException
     */
    public Message get(String key, String locale, String siteId) throws
        WorkerException {

        /**
         * @todo This stuff needs to be changed. All developers should use
         * getInstance() / getInstance(String key) methods to get instance.
         *
         * THIS IS A WORKAROUND,
         * Instead of change DgMarket code to get preceched translations,
         * we decided to use such small trick
         */
        TranslatorWorker realWorker = TranslatorWorker.getInstance(key);
        if (realWorker instanceof CachedTranslatorWorker) {
            return realWorker.get(key, locale, siteId);
        }
        // END OF WORKAROUND

        Session session = null;

        try {

            Message mesageKey = new Message();
            mesageKey.setKey(key);
            mesageKey.setLocale(locale);
            mesageKey.setSiteId(siteId);

            session = PersistenceManager.getSession();
            Message message = (Message) session.load(Message.class, mesageKey);

            return message;
        }
        catch (ObjectNotFoundException onfe) {
            return null;
        }

        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }
        finally {
            try {
                if (session != null) {

                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

    }

    /**
     * Returns all keys for a site and its root
     *
     * @param siteId
     * @param rootSiteId
     * @return List of matching site ids
     * @throws WorkerException
     */
    protected List getKeysForSite(String siteId, String rootSiteId) throws
        WorkerException {

        List messages = null;
        String query =
            "select distinct message.key from message in class org.digijava.kernel.entity.Message where message.siteId='"
            + siteId.trim()
            + "' or message.siteId='"
            + rootSiteId
            + "' order by message.key";
        Session session = null;

        try {

            session = PersistenceManager.getSession();
            messages = session.find(query);

            return messages;

        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

    }

    private Map getMessagesForCriteria(
        String prefix,
        String siteId,
        String lang
        ) throws WorkerException {
        Session session = null;
        Map messageMap = new HashMap();
        try {
            String query =
                "select  message from message in class org.digijava.kernel.entity.Message where message.siteId='"
                + siteId
                + "' and message.key like '"
                + prefix
                + "%'"
                + " and message.locale='"
                + lang
                + "' order by message.key";

            session = PersistenceManager.getSession();

            Query q = session.createQuery(query);

            List ls = q.list();
            Iterator it = ls.iterator();
            while (it.hasNext()) {
                Message currMsg = (Message) it.next();
                messageMap.put(currMsg.getKey(), currMsg);
            }
        }

        catch (HibernateException he) {

            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }
        finally {
            try {
                if (session != null) {
                    //returnList.add(session);
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

        return messageMap;
    }

    private List filterResults(List input, int startFrom, int numResults) {
        List filteredResult = new ArrayList();
        if (input == null || input.size() < startFrom) {
            return filteredResult;
        }

        int size = input.size();

        for (int i = startFrom, j = 0; (i < size && j < numResults); i++, j++) {
            filteredResult.add(input.get(i));
        }

        return filteredResult;
    }

    /**
     * Gets a List of Translator Beans that match
     * the passed criteria
     *
     * @param prefix
     * @param siteId
     * @param rootSiteId
     * @param srcLang
     * @param targetLang
     * @param isExpired
     * @param startFrom
     * @param numResults
     * @return
     * @throws WorkerException
     */

    public List getMessagesForPrefix(
        String prefix,
        String siteId,
        String rootSiteId,
        String srcLang,
        String targetLang,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List rtList;
        try {
            String query =
                "select distinct message.key from message in class org.digijava.kernel.entity.Message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + prefix
                + "%'"
                + addClause(isExpired)
                + " order by message.key";

            Map allMessages = new HashMap();
            Map siteSrcMap = getMessagesForCriteria(prefix, siteId, srcLang);
            Map siteTargetMap = null;
            Map rootSiteSrcMap = null;
            Map rootSiteTargetMap = null;

            if (srcLang.equals(targetLang)) {
                siteTargetMap = siteSrcMap;
            }
            else {
                siteTargetMap = getMessagesForCriteria(prefix, siteId,
                    targetLang);
            }

            if (siteId.equals(rootSiteId)) {
                rootSiteSrcMap = siteSrcMap;
            }
            else {
                rootSiteSrcMap = getMessagesForCriteria(prefix, rootSiteId,
                    srcLang);
            }

            if (srcLang.equals(targetLang)) {
                rootSiteTargetMap = rootSiteSrcMap;
            }
            else {
                rootSiteTargetMap = getMessagesForCriteria(prefix, rootSiteId,
                    targetLang);
            }

            allMessages.put(CURR_SITE_SRC_MSG, siteSrcMap);

            allMessages.put(CURR_SITE_TARGET_MSG, siteTargetMap);

            allMessages.put(ROOT_SITE_SRC_MSG, rootSiteSrcMap);

            allMessages.put(ROOT_SITE_TARGET_MSG, rootSiteTargetMap);

            session = PersistenceManager.getSession();
            Query q = session.createQuery(query);
            List ls = q.list();

            rtList = checkMessages(ls, srcLang, targetLang, siteId, rootSiteId,
                                   isExpired, allMessages);

            java.util.Collection col = compare(rtList);
            ArrayList sortedList = new ArrayList();
            sortedList.addAll(col);

            return filterResults(sortedList, startFrom, numResults);

        }
        catch (HibernateException he) {

            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }

        finally {
            try {
                if (session != null) {

                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

    }

    private List checkMessages(List ls, String srcLang, String targetLang,
                               String siteId, String rootSiteId,
                               boolean isExpired, Map allMessages) throws
        WorkerException {

        List rtList = new ArrayList();
        Map siteSrcLangMap = (Map) allMessages.get(CURR_SITE_SRC_MSG);
        Map siteTargetLangMap = (Map) allMessages.get(CURR_SITE_TARGET_MSG);
        Map rootSiteSrcLangMap = (Map) allMessages.get(ROOT_SITE_SRC_MSG);
        Map rootSiteTargetLangMap = (Map) allMessages.get(ROOT_SITE_TARGET_MSG);

        Iterator it = ls.iterator();

        //Check for each key started
        while (it.hasNext()) {
            String strKey = (String) it.next();
            if (strKey != null) {
                Message srcMsg =
                    (Message) siteSrcLangMap.get(strKey);

                Message targetMsg =
                    (Message) siteTargetLangMap.get(strKey);

                Message validMsg = (srcMsg != null ? srcMsg : targetMsg);

                if (validMsg != null &&
                    ( (isExpired &&
                       (validMsg.getCreated() == null ||
                        validMsg.getCreated().getTime() >=
                        MILLI_SECONDS_PER_DAY))
                     ||
                     (!isExpired && validMsg.getCreated() != null &&
                      validMsg.getCreated().getTime() < MILLI_SECONDS_PER_DAY)))

                    continue;

                if (srcMsg != null && targetMsg != null) {

                    Message targetRootMsg =
                        (Message) rootSiteTargetLangMap.get(strKey);

                    boolean flag = isOlder(targetMsg, targetRootMsg);

                    rtList.add(
                        new TranslatorBean(srcMsg, targetMsg, flag));

                }
                else if (srcMsg != null && targetMsg == null) {

                    rtList.add(new TranslatorBean(
                        srcMsg,
                        new Message()));

                }
                else if (srcMsg == null && targetMsg != null) {

                }
                else {
                    Message srcRootMsg =
                        (Message) rootSiteSrcLangMap.get(strKey);

                    Message targetRootMsg =
                        (Message) rootSiteTargetLangMap.get(strKey);

                    if (srcRootMsg != null) {

                        rtList.add(
                            new TranslatorBean(
                            srcRootMsg,
                            targetRootMsg != null ? targetRootMsg : new Message()));
                    }
                    else if (
                        srcRootMsg == null && targetRootMsg == null) {
                        //Donot add into retunList
                    }
                    else {

                        //Do Nothing Here
                    }

                }
            }
        }

        return rtList;

    }

    private boolean isOlder(Message msg1, Message msg2) {
        boolean isOlder = false;

        if (msg1 != null && msg2 != null && msg1.getCreated() != null &&
            msg2.getCreated() != null) {
            if (msg1.getCreated().getTime() < msg2.getCreated().getTime())
                isOlder = true;
        }
        return isOlder;
    }

    /**
     * Returns a List of translator Beans that
     * match the given criteria
     *
     * @param prefix
     * @param siteId
     * @param rootSiteId
     * @param srcLang
     * @param targetLang
     * param keyPattern
     * @param isExpired
     * @param startFrom
     * @param numResults
     * @return
     * @throws WorkerException
     */
    public List searchKeysForPattern(
        String prefix,
        String siteId,
        String rootSiteId,
        String srcLang,
        String targetLang,
        String keyPattern,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List rtList = new ArrayList();
        if (keyPattern != null) {
            keyPattern = keyPattern.trim().toLowerCase();
        }
        try {
            String query =
                "select distinct message.key from message in class org.digijava.kernel.entity.Message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + prefix.trim()
                + "%' and lower(message.key) like '%"
                + keyPattern
                + "%'"
                + addClause(isExpired)
                + " order by message.key";

            Map allMessages = new HashMap();

            allMessages.put(CURR_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, siteId, srcLang));
            allMessages.put(CURR_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, siteId, targetLang));
            allMessages.put(ROOT_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, rootSiteId, srcLang));
            allMessages.put(ROOT_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, rootSiteId,
                targetLang));

            rtList = new ArrayList();

            session = PersistenceManager.getSession();

            Query q = session.createQuery(query);
            //q.setFirstResult(startFrom);
            //q.setMaxResults(numResults);
            List ls = q.list();

            rtList = checkMessages(ls, srcLang, targetLang, siteId, rootSiteId,
                                   isExpired, allMessages);

            return filterResults(rtList, startFrom, numResults);

        }
        catch (HibernateException he) {

            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }
        finally {
            try {
                if (session != null) {
                    //returnList.add(session);
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

    }

    private String addClause(boolean isExpired) {
        String query = "";
        /*
           if(isExpired){
         query = " and message.created = '" + new Timestamp(0) + "'";
           }else{
         query = "  and ( message.created > '" + new Timestamp(0) + "' or message.created is null )";
           }
         */
        return query;
    }

    /**
     * Returns a List of translator Beans that
     * match the given criteria
     *
     * @param prefix
     * @param siteId
     * @param rootSiteId
     * @param srcLang
     * @param targetLang
     * param messagePattern
     * @param locale
     * @param isExpired
     * @param startFrom
     * @param numResults
     * @return
     * @throws WorkerException
     */
    public List searchMessageForPattern(
        String prefix,
        String siteId,
        String rootSiteId,
        String srcLang,
        String targetLang,
        String messagePattern,
        String locale,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List rtList = new ArrayList();
        if (messagePattern != null) {
            messagePattern = messagePattern.trim().toLowerCase();
        }

        try {
            String query =
                "select distinct message.key from message in class org.digijava.kernel.entity.Message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + prefix
                + "%' and lower(message.message) like'%"
                + messagePattern
                + "%' and message.locale='"
                + locale.toString()
                + "'"
                + addClause(isExpired)
                + " order by message.key";

            Map allMessages = new HashMap();
            allMessages.put(CURR_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, siteId, srcLang));
            allMessages.put(CURR_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, siteId, targetLang));
            allMessages.put(ROOT_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, rootSiteId, srcLang));
            allMessages.put(ROOT_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, rootSiteId,
                targetLang));
            rtList = new ArrayList();

            session = PersistenceManager.getSession();

            Query q = session.createQuery(query);
            List ls = q.list();

            rtList = checkMessages(ls, srcLang, targetLang, siteId, rootSiteId,
                                   isExpired, allMessages);

            return filterResults(rtList, startFrom, numResults);

        }
        catch (HibernateException he) {

            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, sqle);
            throw new WorkerException(sqle.getMessage(), sqle);
        }
        finally {
            try {
                if (session != null) {
                    //returnList.add(session);
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                String errKey = "TranslatorWorker.SessionRelease.warn";
                Object[] params = {
                    e.getMessage()};
                log.l7dlog(Level.WARN, errKey, params, e);
                throw new WorkerException(e.getMessage(), e);
            }
        }

    }

    /**
     * Saves a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void save(Message message) throws WorkerException {
        log.debug("Saving translation. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() +
                  ",locale=" + message.getLocale());
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
            log.l7dlog(Level.ERROR, errKey, params, se);
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
            log.l7dlog(Level.ERROR, errKey, params, e);

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

                log.l7dlog(Level.WARN, errKey, params, e);
                e.printStackTrace();
            }
        }

        return;
    }

    /**
     * Updates a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void update(Message message) throws WorkerException {
        log.debug("Updating translation. siteId="
                  + message.getSiteId() + ", key = " + message.getKey() +
                  ",locale=" + message.getLocale());
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
            log.l7dlog(Level.ERROR, errKey, params, se);
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
            log.l7dlog(Level.ERROR, errKey, params, e);
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
                log.l7dlog(Level.WARN, errKey, params, e);
            }
        }

    }

    /**
     * Deletes a particular message
     * @param message
     * @throws WorkerException
     */
    public void delete(Message message) throws WorkerException {
        Session ses = null;
        Transaction tx = null;
        try {

            ses = PersistenceManager.getSession();
            tx = ses.beginTransaction();
            ses.delete(message);
            tx.commit();

        }
        catch (SQLException se) {
            String errKey = "TranslatorWorker.SqlExDeleteMessage.err";
            Object[] params = {
                se.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, se);
        }
        catch (HibernateException e) {
            String errKey = "TranslatorWorker.HibExDeleteMessage.err";
            Object[] params = {
                e.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, e);
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
                log.l7dlog(Level.WARN, errKey, params, e);
            }
        }

    }

    /**
     * This method updates the creation time of expired messages(pertaining to the key passed)
     * to the current server time.
     * @param key
     * @throws WorkerException
     */
    public void markKeyUnexpired(String key) throws WorkerException {
        if (key == null)
            return;

        Session ses = null;
        Transaction tx = null;
        List messages;
        String query =
            "from message in class org.digijava.kernel.entity.Message where message.key='"
            + key.trim()
            + "'";

        try {

            ses = PersistenceManager.getSession();
            tx = ses.beginTransaction();
            messages = ses.find(query);
            Iterator it = messages.iterator();

            while (it.hasNext()) {
                Message msg = (Message) it.next();

                msg.setCreated(new Timestamp(System.currentTimeMillis()));

                ses.update(msg);
            }

            tx.commit();

        }
        catch (SQLException se) {
            String errKey = "TranslatorWorker.SqlExDeleteMessage.err";
            Object[] params = {
                se.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, se);
        }
        catch (HibernateException e) {
            String errKey = "TranslatorWorker.HibExDeleteMessage.err";
            Object[] params = {
                e.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, e);
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
                log.l7dlog(Level.WARN, errKey, params, e);
                e.printStackTrace();
            }
        }

    }

    private java.util.Collection compare(List beans) {

        java.util.TreeSet sort = new java.util.TreeSet(new java.util.Comparator() {
            public int compare(Object obj1, Object obj2) {

                if (obj1 instanceof TranslatorBean &&
                    obj2 instanceof TranslatorBean) {
                    TranslatorBean tb1 = (TranslatorBean) obj1;
                    TranslatorBean tb2 = (TranslatorBean) obj2;

                    //check for the red keys
                    if (tb1.getTragetMsg() == null && tb2.getTragetMsg() != null) {
                        return -1;
                    }

                    if (tb1.getTragetMsg() != null && tb2.getTragetMsg() == null) {
                        return 1;
                    }

                    if (tb1.getTragetMsg().getMessage() == null &&
                        tb2.getTragetMsg().getMessage() != null) {
                        return -1;
                    }
                    if (tb1.getTragetMsg().getMessage() != null &&
                        tb2.getTragetMsg().getMessage() == null) {
                        return 1;
                    }

                    //check for the red target MSG
                    if (tb1.isNeedsUpdate() && !tb2.isNeedsUpdate()) {
                        return -1;
                    }
                    if (!tb1.isNeedsUpdate() && tb2.isNeedsUpdate()) {
                        return 1;
                    }
                    //check for both red messages which need to be sorted on key

                    return tb1.getSrcMsg().getKey().compareTo(tb2.getSrcMsg().
                        getKey());
                    //sort on the normal messages by key

                }
                return -1;
            }
        });

        sort.addAll(beans);
        return sort;

    }

    /**
     * This method changes timestamp of the message to 1970-1-1
     *
     * @param key
     * @throws WorkerException
     */
    public void markKeyExpired(String key) throws WorkerException {
        if (key == null)
            return;

        if (key == null)
            return;

        Session ses = null;
        Transaction tx = null;
        List messages;

        String query =
            "from message in class org.digijava.kernel.entity.Message where message.key='"
            + key.trim()
            + "'";

        try {

            ses = PersistenceManager.getSession();

            messages = ses.find(query);
            Iterator it = messages.iterator();
            tx = ses.beginTransaction();
            while (it.hasNext()) {

                Message msg = (Message) it.next();
                msg.setCreated(new Timestamp( -1000));
                ses.update(msg);

            }

            tx.commit();

        }
        catch (SQLException se) {
            String errKey = "TranslatorWorker.SqlExDeleteMessage.err";
            Object[] params = {
                se.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {}
            }
            throw new WorkerException(errKey, se);
        }
        catch (HibernateException e) {
            String errKey = "TranslatorWorker.HibExDeleteMessage.err";
            Object[] params = {
                e.getMessage()};
            log.l7dlog(Level.ERROR, errKey, params, e);
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
                log.l7dlog(Level.WARN, errKey, params, e);
                e.printStackTrace();
            }
        }

    }

    /**
     * Verify, is on-site translation mode active or not
     * @param request HttpServletRequest
     * @return true, if on-site translation mode is active and false if not
     */
    public static boolean isTranslationMode(HttpServletRequest request) {
        boolean active = false;
        HttpSession session = request.getSession(true);
        if (session.getAttribute("mode") != null &&
            session.getAttribute("mode").toString().equalsIgnoreCase("true")) {
            active = true;
        }

        return active;
    }

    /**
     * Sets on-site translation mode on/off
     * @param request HttpServletRequest
     * @param mode on-site translation mode
     */
    public static void setTranslationMode(HttpServletRequest request,
                                          boolean mode) {
        String attrib = mode ? "true" : "false";
        request.getSession(true).setAttribute("mode", attrib);
    }

    /*
      private Query prepareQueryForCaching(Session session, String oql, String regionPrefix,  boolean forceRecache,boolean enableCaching) throws HibernateException
     {
      if (forceRecache)
      {
       try
       {
       session.getSessionFactory().evictQueries(regionPrefix + oql);
       }
       catch(NullPointerException npe)
       {
        // Hibernate throws this exception when trying to evict
        // queries from a non-existent cache. Hence the error is
        // caught and ignored.
       }
      }
      Query q = session.createQuery(oql);
      q.setCacheable(enableCaching);
      q.setCacheRegion(regionPrefix + oql);
      return q;
     }
     */
}
