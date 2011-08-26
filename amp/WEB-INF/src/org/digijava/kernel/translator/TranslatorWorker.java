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

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.services.UrlTouchService;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Shamanth Murthy
 *
 * Worker class for all translator related operations
 */
public class TranslatorWorker {

    private static Logger logger =
        Logger.getLogger(TranslatorWorker.class);

    static {
        logger.setResourceBundle(I18NHelper.getKernelBundle());
    }

    private static String CURR_SITE_SRC_MSG = "SITE_SRCLANG";
    private static String CURR_SITE_TARGET_MSG = "SITE_TARGETLANG";
    private static String ROOT_SITE_SRC_MSG = "ROOTSITE_SRCLANG";
    private static String ROOT_SITE_TARGET_MSG = "ROOTSITE_TARGETLANG";

    private static long MILLI_SECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    private static TranslatorWorker defaultWorker;
    private static TranslatorWorker cachedWorker;

    private TranslateAlertConfig alertConfigBean;
    private UrlTouchService urlTouchService;

    public static final int TRNTYPE_LOCAL = 0;
    public static final int TRNTYPE_GROUP = 1;
    public static final int TRNTYPE_GLOBAL = 2;

    private final static Timestamp expTimestamp = new Timestamp(10000);
    private boolean caseSensitiveKeys = true;
    //private TrnAccessUpdateQueue timeStampQueue = TrnAccessUpdateQueue.getQueue();
    private Site ampSite = null;

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

    public TranslatorWorker() {
    	caseSensitiveKeys=DigiConfigManager.getConfig().isCaseSensitiveTranslatioKeys();//DGP-318
        setUpAlerts();
    }

    /**
     * Read translation from database and put to translation cache
     * @param key translation key
     * @param locale locale
     * @param siteId owner site
     * @throws WorkerException if process was not completed successfully
     */
    public void refresh(String key, String locale, String siteId) throws WorkerException {
        // DO Nothing
    }
    
    //TODO may be bad idea!
    public void refresh(Message message) throws WorkerException {
    	// DO Nothing
    }

    /**
     * Stab method for returning default language code.
     * Currently returns English local code, but it can get this from some settings if required.
     * @return
     */
    public static String getDefaultLocalCode(){
    	return "en";
    }
    
    
    /**
     * Returns a message string matching local,key,site
     * NOTE: This method expects that key is already hash code of some text. Use {@link #translateText(String, String, String)}
     *
     * @param key translation key.
     * @param locale
     * @param siteId site string id ( for example demosite )
     * @return
     * @throws WorkerException
     * @Deprecated uses key directly, use {@link #translateText(String, String, String)}
     */
    @Deprecated
    public static String translate(String key, String locale, String siteId) throws WorkerException {
        String retVal = null;
        Message msg = getInstance(key).getMessage(key, locale, siteId);
        if (msg != null) {
            retVal = msg.getMessage();
        }
        else {
            retVal = "";
        }
        return retVal;
    }
    
    public static String translateText(String text,HttpServletRequest request) throws WorkerException {
 	 	Site site = RequestUtils.getSite(request);
 	 	Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
 	 	                  
 	 	String siteId=site.getId().toString();
 	 	String locale=navigationLanguage.getCode();
 	 	
 	 	String translatedText= null;
 	 	try {
			translatedText = TranslatorWorker.translateText(text, locale,siteId);
		} catch (WorkerException e) {
			logger.error("Error:", e);
			throw new WorkerException(e);
		}
		if (translatedText == null || translatedText.trim().compareTo("") == 0 || translatedText.length() == 0)
			return text;
 	 	return translatedText;
    }
    
    /**
     * Translates text. Returns translation of the specified text in specified language.
     * Note that key is generated from text so it should be default English text hardcoded somewhere like in digi:trn tag
     * If specified local was not default language and nothing is found then it tries to find English translation.
     * If even English translation was not found then it is created using text, siteId specified and English local. 
     * @param text default text that should be translated, this is used to generate key
     * @param locale language code for which translation should be searched.
     * @param siteId id of digi site.
     * @return text translated text or default text 
     * @throws WorkerException
     */
    public static String translateText(String text,String locale, String siteId) throws WorkerException{
    	return translateText(text, null,locale, siteId);
    }
    
    public static String translateText(String text,String locale, Long siteId) throws WorkerException{
    	return translateText(text, null,locale, siteId.toString());
    }

    /**
     * Translates text to specified local.
     * see #translateText(String, String, String) for more details.
     * @param text
     * @param keyWords
     * @param locale
     * @param siteId
     * @return
     * @throws WorkerException
     */
    public static String translateText(String text, String keyWords, String locale, String siteId) throws WorkerException{
        String retVal = null;
        TranslatorWorker worker = getInstance("");
        //Try to find translation
        Message msg = worker.getByBody(text, keyWords, locale, siteId);
        if (msg != null) {
            retVal = msg.getMessage();
        }else {
        	//Then try to find in default language
        	msg = worker.getByBody(text, keyWords, getDefaultLocalCode(), siteId);
        	if (msg!=null) {
        		retVal=msg.getMessage();
        	}else{
        		//then create new default and return searched text as result
                retVal = text;
                msg=new Message();
                msg.setSiteId(siteId);
                msg.setLocale(getDefaultLocalCode());
                msg.setMessage(text);
                msg.setKeyWords(keyWords);
                msg.setKey(TranslatorWorker.generateTrnKey(text));
                worker.save(msg);
        	}
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
     * Returns a message object matching local,key,site
     *
     * @param key
     * @param locale
     * @param siteId ( for example demosite ) or null if you want to get global
     * translation
     * @return
     * @throws WorkerException
     */
    public Message getMessage(String key, String locale, String siteId) throws
        WorkerException {

        if (siteId == null) {
        	String defaultSideId=getDefaultSite().getId().toString();
            return getByKey(key, locale, defaultSideId);
        } else {
            Site site = SiteCache.getInstance().getSite(siteId);

            if (site == null) {
                logger.debug("Site Id not found for site " + siteId);
                return null;
            }

            return getByKey(key, locale, String.valueOf(site.getId()));
        }
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
        logger.debug("getFromGroup() called");
        String siteId = site.getId().toString();

        Message trnMess = getFromGroup(key, locale, site);

        if (trnMess == null) {
            logger.debug("constructing default translation");
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
        logger.debug("getFromGroup() called");
        String siteId = site.getId().toString();

        Message trnMess = getByKey(key, locale, siteId);
        if (trnMess != null) {
            logger.debug("local translation exists");
            return trnMess;
        }
        if (!locale.equals("en")) {
            trnMess = getByKey(key, "en", siteId);
            if (trnMess != null) {
                logger.debug("default local translation exists");
                return trnMess;
            }
        }
        String rootSiteId = SiteCache.getInstance().getRootSite(site).getId().
            toString();
        if (!rootSiteId.equals(siteId)) {
            trnMess = getByKey(key, locale, rootSiteId);
            if (trnMess != null) {
                logger.debug("group translation exists");
                return trnMess;
            }
            if (!locale.equals("en")) {
                trnMess = getByKey(key, "en", rootSiteId);
                if (trnMess != null) {
                    logger.debug("default group translation exists");
                    return trnMess;
                }
            }
        }

        return trnMess;
    }


    /**
     * Retrieves translation using original text for searching.  
     * @param originalText used to generate key for searching, this should be always be default text in English, tag body or string constant from code. 
     * @param local language code to witch original text should be translated.
     * @param siteId site ID of for which translation should be searched.
     * @return message bean which contains translated text or default text if translation was not not found.
     * @throws WorkerException
     */
    public Message getByBody(String originalText, String local, String siteId) throws WorkerException{
    	return getByBody(originalText,null,local,siteId);
    }

    public Message getByBody(String originalText, String keyWords, String local, String siteId) throws WorkerException{
    	String hashCode = generateTrnKey(originalText);
    	return getByKey(hashCode,originalText, keyWords,local,siteId);
    }
    
    public Message getByKey(String key, String locale, String siteId) throws WorkerException {
    	return getByKey(key, "", null, locale, siteId);
    }

    /**
     * Returns message by key, site and local.
     * This method searches db. See overloaded pair in {@link CachedTranslatorWorker}
     * Note that there is old workaround in this method which breaks this description.
     * @param key mandatory.
     * @param defaultText default text. Should not be null.
     * @param keyWords used for grouping. not mandatory
     * @param locale specifies in which language this message should be searched. part of the key, mandatory.
     * @param siteId part of the key. mandatory.
     * @return
     * @throws WorkerException
     */
    public Message getByKey(String key, String defaultText, String keyWords, String locale, String siteId) throws WorkerException {

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
            return realWorker.getByKey(key, locale, siteId);
        }
        // END OF WORKAROUND

        Session session = null;

        try {

            Message mesageKey = new Message();
            mesageKey.setKey(processKeyCase(key));
            mesageKey.setLocale(locale);
            mesageKey.setSiteId(siteId);

            session = PersistenceManager.getSession();
            Message message = (Message) session.load(Message.class, mesageKey);
            message.setKeyWords(keyWords);
            //updateTimeStamp(message);
            return message;
        }
        catch (ObjectNotFoundException onfe) {
            return null;
        }

        catch (HibernateException he) {
            logger.error("Error reading translation. siteId="
                         + siteId + ", key = " + key +
                         ",locale=" + locale, he);
            throw new WorkerException(he);
        }
        catch (SQLException sqle) {
            logger.error("Error reading translation. siteId="
                         + siteId + ", key = " + key +
                         ",locale=" + locale, sqle);
            throw new WorkerException(sqle);
        }
        finally {
            try {
                if (session != null) {
                	
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. siteId="
                             + siteId + ", key = " + key +
                             ",locale=" + locale, e);
                throw new WorkerException(e);
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

        String query =
            "select distinct message.key from org.digijava.kernel.entity.Message message where message.siteId='"
            + siteId.trim()
            + "' or message.siteId='"
            + rootSiteId
            + "' order by message.key";
        Session session = null;

        try {

            session = PersistenceManager.getSession();
            Query q = session.createQuery(query);
			return q.list();
        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err";
            Object[] params = {
                he.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
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
                logger.l7dlog(Level.WARN, errKey, params, e);
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
                "select  message from org.digijava.kernel.entity.Message message where message.siteId='"
                + siteId
                + "' and message.key like '"
                + processKeyCase(prefix)
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
            logger.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
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
                logger.l7dlog(Level.WARN, errKey, params, e);
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
     * @{@link Deprecated} we do not use prefixes because of hash codes as keys.
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
                "select distinct message.key from org.digijava.kernel.entity.Message message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + processKeyCase(prefix)
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
            logger.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
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
                logger.l7dlog(Level.WARN, errKey, params, e);
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
                "select distinct message.key from org.digijava.kernel.entity.Message message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + processKeyCase(prefix.trim())
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
            logger.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
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
                logger.l7dlog(Level.WARN, errKey, params, e);
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
                "select distinct message.key from org.digijava.kernel.entity.Message message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + processKeyCase(prefix)
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
            logger.l7dlog(Level.ERROR, errKey, params, he);
            throw new WorkerException(he.getMessage(), he);
        }
        catch (SQLException sqle) {
            String errKey = "TranslatorWorker.SqlExLoadingMessage.err";
            Object[] params = {
                sqle.getMessage()};
            logger.l7dlog(Level.ERROR, errKey, params, sqle);
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
                logger.l7dlog(Level.WARN, errKey, params, e);
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
        saveDb(message);

        fireRefreshAlert(message);
    }

    /**
     * Changes message key if necessary.
     * If DiGi is configured for case insensitive keys then key will be changed to lower case. 
     * @param message
     */
    public void processKeyCase(Message message) {
    	//commented out for speed. We do not need this with hash code keys
       // if (!isCaseSensitiveKeys()){
       // 	message.setKey(processKeyCase(message.getKey()));
       // }
    }
    
    public String processKeyCase(String key) {
    	//commented out for speed. We do not need this with hash code keys
        //if (!isCaseSensitiveKeys()){
        //	return key.toLowerCase();
        //}
        return key;
    }
    
    /**
     * Removes new line chars from message body.
     * Fore more details see AMP-4611
     * @param message
     */
    protected void processBodyChars(Message message){
    	message.setMessage(processSpecialChars(message.getMessage()));
    }

    /**
     * Sets original message value to value of message.
     * This will be done only if original message is null and
     * key generated from message is same as key field value.
     * this will mean that key was generated from that same message 
     * and hence it is the original message.
     * We need to store this to avoid problems when default translation is changed.
     * See AMP-6663 for details.
     * @param message
     */
    protected void processOriginalMessage(Message message){
    	//Temporary solution for AMP-6663 to not write patch which runs more then 5 min.
    	if (message.getOriginalMessage()==null || "".equals(message.getOriginalMessage().trim())){
    		//if hash generated from text is same as key then this is the original text from which key was generated.
    		if (generateTrnKey(message.getMessage()).equals(message.getKey())){
    			message.setOriginalMessage(message.getMessage());
    		}
    	}
    }
    /**
     * Processes special characters for translations to make it compatible with translations rules.
     * currently removes new line and carriage return symbols. 
     * @param text
     * @return
     */
    public static String processSpecialChars(String text){
    	if (text == null) return null;
    	return text.replace("\r", "").replace("\n", "");    
    }
    
    /**
     * Makes string compatible for using within JavaScript string definition.
     * Adds slashes in front of string start-end symbols.
     * @param text
     * @return
     */
    public static String makeTextJSFriendly(String text){
    	if (text == null) return null;
    	return text.replaceAll("'","\\\\'").replace("\"", "\\\"");    
    }
    /**
     * Generates hash code from message body and sets it as key.
     * WARN: Use for non-English messages ONLY if there is no English record for same key. 
     * Hash key should be generated from English translation because it is default language.  
     * @param message translation entity to update
     */
    protected void setHash(Message message){
    	String hash = generateTrnKey(message.getMessage());
    	message.setKey(hash);
    }
    
    /**
     * Generates translation key from message body.
     * Currently {@link String#hashCode()} is used for this purpose.
     * @param text any text that but usually this should be body for trn tag or default translation text.
     * @return key for translation, actually it is hash code of the text.
     */
    public static String generateTrnKey(String text){
    	if(text != null) {
    		return Integer.toString(text.trim().toLowerCase().hashCode());
    	} else {
    		return "";
    	}
    }
    
    /**
     * Puts message in update queue.
     * This is done every time message is accessed. 
     * When message is put in queue this also updates last access time field of the message with current time.
     * Another lower priority worker thread will take this message out from queue and update it in db.  
     * @param message
     * @see TrnAccessUpdateQueue
     * @see TrnAccesTimeSaver
     */
//    protected void updateTimeStamp(Message message){
//    	timeStampQueue.put(message);
//    }
    
    /**
     * Saves message in db.
     * Removes form access time queue if this message is waiting update there too. 
     * @param message
     * @throws WorkerException
     */
    protected void saveDb(Message message) throws WorkerException {
        logger.debug("Saving translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() +
                     ",locale=" + message.getLocale());
        Session ses = null;
        Transaction tx = null;

        try {
        	message.setKey(message.getKey().trim());
            ses = PersistenceManager.getSession();
//beginTransaction();
            //TODO if we add hash codes as keys, then we do not need key case correction method on next line
            processKeyCase(message);
            processBodyChars(message);
            processOriginalMessage(message);
            //generateHash(message);//TODO what if French translation is current.
          
            
            if (!isKeyExpired(message.getKey())) {
                message.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
            } else {
                message.setCreated(new Timestamp( -1000));
            }
            
            //Remove from queue if this message is there because here we are doing same.
            //timeStampQueue.remove(message);
            
            ses.saveOrUpdate(message);
            //tx.commit();
            
        } 
        catch (SQLException se) {
            logger.error("Error saving translation. siteId="
                         + message.getSiteId() + ", key = " + message.getKey() +
                         ",locale=" + message.getLocale(), se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("TranslatorWorker.SqlExSaveMessage.err", se);

        }
        catch (HibernateException e) {
        	logger.warn("saveOrUpdate() failed for Message with siteId=" + message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale(), e);
        	try {
        		ses.save(message);
                //tx.commit();
        	} catch (Exception e1) {
        		logger.error("Error saving translation. siteId="
                        + message.getSiteId() + ", key = " + message.getKey() +
                        ",locale=" + message.getLocale(), e1);
        		//
              	 if (tx != null) {
                       try {
                           tx.rollback();
                       }
                       catch (HibernateException ex1) {
                           logger.warn("rollback() failed", ex1);
                       }
                   }

                   throw new WorkerException("TranslatorWorker.HibExSaveMessage.err", e); 
			}
       	        
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. siteId="
                             + message.getSiteId() + ", key = " + message.getKey() +
                             ",locale=" + message.getLocale(), e);
            }
        }
    }
  

    /**
     * Updates a particular message in db.
     * @param message
     * @throws WorkerException
     */
    public void update(Message message) throws WorkerException {
        updateDb(message);
        fireRefreshAlert(message);
    }

    /**
     * Does actual update of the message in db.
     * Also if same message waits for update in last access time queue, then it is removed from that queue
     * to not overwrite latest changes.
     * @param message
     * @throws WorkerException
     * @see TrnAccessUpdateQueue
     * @see TrnAccesTimeSaver
     */
    protected void updateDb(Message message) throws WorkerException {
        logger.debug("Updating translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        Session ses = null;
        Transaction tx = null;

        try {
            //TODO if we add hash codes as keys, then we do not need key case correction method on next line
        	processBodyChars(message);
        	processOriginalMessage(message);
            ses = PersistenceManager.getSession();
//beginTransaction();
            if (!isKeyExpired(message.getKey())) {
                message.setCreated(new Timestamp(System.currentTimeMillis()));
            }
            message.setLastAccessed(new Timestamp(System.currentTimeMillis()));
            //Remove from queue if this message is there because here we are doing same. note, this is not transactional
            //timeStampQueue.remove(message);
            
            ses.update(message);
            //tx.commit();
        }
        catch (SQLException se) {
            logger.error("Error updating translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() +",locale=" + message.getLocale(), se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("TranslatorWorker.SqlExUpdateMessage.err", se);
        }
        catch (HibernateException e) {
            logger.error("Error updating translation. siteId="
                         + message.getSiteId() + ", key = " + message.getKey() +
                         ",locale=" + message.getLocale(), e);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("TranslatorWorker.HibExUpdateMessage.err", e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. siteId="
                             + message.getSiteId() + ", key = " + message.getKey() +
                             ",locale=" + message.getLocale(), e);
            }
        }
    }

    /**
     * Deletes a particular message
     * @param message
     * @throws WorkerException
     */
    public void delete(Message message) throws WorkerException {
        deleteDb(message);
        fireRefreshAlert(message);
    }

    protected void deleteDb(Message message) throws WorkerException {
        Session ses = null;
        Transaction tx = null;
        try {
            ses = PersistenceManager.getSession();
//beginTransaction();
            ses.createQuery("delete from " +Message.class.getName()+"  msg "+
            		" where  msg.key=:key" +
            		" and  msg.locale=:locale " +
            		" and  msg.siteId=:siteId")
            		.setString("key",message.getKey())
            		.setString("locale",message.getLocale())
            		.setString("siteId",message.getSiteId())
            		.executeUpdate(); 

            //Remove from queue too.
            //timeStampQueue.remove(message);
            
           // ses.delete(message);
            //tx.commit();

        }
        catch (SQLException se) {
            logger.error("Error updating translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() +",locale=" + message.getLocale(), se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("TranslatorWorker.SqlExUpdateMessage.err", se);
        }
        catch (HibernateException e) {
            logger.error("Error updating translation. siteId="
                         + message.getSiteId() + ", key = " + message.getKey() +
                         ",locale=" + message.getLocale(), e);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("TranslatorWorker.HibExUpdateMessage.err", e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. siteId="
                             + message.getSiteId() + ", key = " + message.getKey() +
                             ",locale=" + message.getLocale(), e);
            }
        }
    }

    /**
     * This method changes timestamp of the message to 1970-1-1
     *
     * @param key
     * @throws WorkerException
     */
    public void markKeyExpired(String key) throws WorkerException {
        setTimestamps(key, new Timestamp( -1000));
    }

    /**
     * This method updates the creation time of expired messages(pertaining to the key passed)
     * to the current server time.
     * @param key
     * @throws WorkerException
     */
    public void markKeyUnexpired(String key) throws WorkerException {
        setTimestamps(key, new Timestamp(System.currentTimeMillis()));
    }

    protected void setTimestamps(String key, Timestamp timestamp) throws WorkerException {
        if (key == null)
            return;

        Session ses = null;
        Transaction tx = null;
        List messages;
        String queryString = "from " + Message.class.getName() +
            " msg where msg.key=:msgKey";

        try {

            ses = PersistenceManager.getSession();
//beginTransaction();
            Query q = ses.createQuery(queryString);
            q.setString("msgKey", processKeyCase(key.trim()));

            messages = q.list();
            Iterator it = messages.iterator();

            while (it.hasNext()) {
                Message msg = (Message) it.next();
                msg.setCreated(timestamp);
                msg.setLastAccessed(timestamp);

                //Remove from queue if this message is there because here we are doing same.
                //timeStampQueue.remove(msg);
                
                ses.update(msg);
            }

            //tx.commit();

        }
        catch (SQLException se) {
            logger.error("Error updating translations. key=" + key, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("Error updating translations. key=" + key, se);
        }
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key, e);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("Error updating translations. key=" + key, e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. key=" + key, e);
            }
        }
    }

    protected boolean isKeyExpired(String key) throws
        WorkerException {
        if (key == null)
            return false;

        boolean result = false;

        Session ses = null;
        String queryString = "select count(msg.key) from " + Message.class.getName() +
            " msg where msg.key=:msgKey and msg.created <=:stamp";

        try {

            ses = PersistenceManager.getSession();
            Query q = ses.createQuery(queryString);
            if (isCaseSensitiveKeys()) {
                q.setString("msgKey", key.trim());
            }else{
                q.setString("msgKey", processKeyCase(key.trim()));
            }
            q.setTimestamp("stamp", expTimestamp);

            Integer count = (Integer)q.uniqueResult();
            result = count.intValue() > 0;

        }
        catch (SQLException se) {
            logger.error("Error updating translations. key=" + key, se);
            throw new WorkerException("Error updating translations. key=" + key, se);
        }
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key, e);
            throw new WorkerException("Error updating translations. key=" + key, e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. key=" + key, e);
            }
        }
        return result;
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
     * Returns list of all language codes currently used in message table.
     * @return
     * @throws WorkerException
     */
    @SuppressWarnings("unchecked")
	public static List<String> getAllUsedLanguages() throws WorkerException{
    	List<String> result = null;
    	String oql = "select m.locale from "+Message.class.getName()+" as m group by m.locale";
    	Session session = null;
    	try {
			session = PersistenceManager.getSession();
			Query query = session.createQuery(oql);
			result = query.list();
		} catch (Exception e) {
			throw new WorkerException(e);
		}finally{
			if (session != null){
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e2) {
					throw new WorkerException(e2);
				}
			}
		}
    	return result;
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
     *
     * @param key
     * @param locale
     * @param site
     * @param param
     * @param defaultMessage
     * @return
     */
    public static String getMessage(String key, String locale, Site site,
                                    HashMap param, String defaultMessage) throws
        WorkerException {

        String message;

        // get default worker instance
        TranslatorWorker worker = getInstance(key);

        message = worker.getFromGroup(key, locale, site, defaultMessage).
            getMessage();

        if (message != null) {
            message = DgUtil.fillPattern(message, param);
        }

        return message;
    }
    
    /**
     * Same as {@link #getAllTranslationsOfKey(String, String)} but uses some text, 
     * usually body of trn tag or default text to generate key.
     * @param text
     * @param siteId
     * @return
     * @throws WorkerException
     */
	public static Collection<Message> getAllTranslationOfBody(String text, String siteId) throws WorkerException {
		String hashKey = generateTrnKey(text);
		return getAllTranslationsOfKey(hashKey, siteId);
	}

    /**
     * Returns all translations for specified key on specified site.
     * If any some translation has been translated in 3 languages, then this will find all 3 records for the key.
     * @param key
     * @param siteId
     * @return
     * @throws WorkerException
     */
    @SuppressWarnings("unchecked")
	public static Collection<Message> getAllTranslationsOfKey(String key, String siteId) throws WorkerException{
    	Session session = null;
    	List<Message> result = null;
		try {
			session = PersistenceManager.getSession();
			String oql = "from "+Message.class.getName()+" as m where m.key = :key and m.siteId = :SiteId";
			Query query = session.createQuery(oql);
			query.setString("key", key);
			query.setString("SiteId", siteId);
			result = query.list();
		} catch (Exception e) {
			throw new WorkerException(e);
		}finally{
			if (null != session){
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e1) {
					throw new WorkerException(e1);
				}
			}
		}
    	return result;
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

    public String translateFromTree(String key, long siteId, String langCode,
                                    String defaultTrn, int translationType, String keyWords) throws
        WorkerException {
        return translateFromTree(key, siteId, new String[] {langCode},
                                 defaultTrn, langCode, translationType, keyWords);
    }

    public String translateFromTree(String key, long siteId, String[] langCodes,
                                    String defaultTrn, String defaultLocale, int translationType, String keyWords) throws
        WorkerException {
        SiteCache siteCache = SiteCache.getInstance();
        Site site = siteCache.getSite(new Long(siteId));

        Long regId = null;
        if (site == null) {
            return null;
        }
        String[] siteIds = null;
        if (translationType == TRNTYPE_LOCAL) {
            siteIds = new String[] {
                site.getId().toString()};
            regId = site.getId();
        }
        else {
            Site rootSite = siteCache.getRootSite(site);
            if (rootSite != null && rootSite.getId().equals(site.getId())) {
                rootSite = null;
            }
            if (translationType == TRNTYPE_GROUP) {
                if (rootSite == null) {
                    siteIds = new String[] {
                        site.getId().toString()};
                    regId = site.getId();
                }
                else {
                    siteIds = new String[] {
                        site.getId().toString(), rootSite.getId().toString()};
                    regId = rootSite.getId();
                }
            }
            else {
                if (rootSite == null) {
                    siteIds = new String[] {
                        site.getId().toString(), "0"};
                    regId = site.getId();
                }
                else {
                    siteIds = new String[] {
                        site.getId().toString(), rootSite.getId().toString(),
                        "0"};
                    regId = rootSite.getId();
                }
            }
        }
        for (int i = 0; i < siteIds.length; i++) {
            for (int j = 0; j < langCodes.length; j++) {
                Message msg = getByKey(key, langCodes[j], siteIds[i]);
                if (msg != null) {
                    return msg.getMessage();
                }
            }
        }
        synchronized (this) {
            if (regId != null && defaultLocale != null && defaultTrn != null &&
                defaultTrn.trim().length() != 0) {
                Message msg = getByKey(key, defaultLocale, regId.toString());
                if (msg == null) {
                    Message message = new Message();
                    message.setMessage(defaultTrn.trim());

                    message.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
                    message.setKey(key);
                    message.setSiteId(regId.toString());
                    message.setLocale(defaultLocale);
                    message.setKeyWords(keyWords);
                    
                    save(message);

                }
            }
        }
        return defaultTrn;
    }

    protected void setUpAlerts() {
        Object configBean = DigiConfigManager.getConfigBean("translateAlert");
        if (configBean == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Translation alerts are disabled");
            }
        }
        else

        if (! (TranslateAlertConfig.class.isAssignableFrom(configBean.getClass()))) {
            logger.warn("Translation alert config is not of type " +
                        TranslateAlertConfig.class);
        }
        else {

            alertConfigBean = (TranslateAlertConfig) configBean;
            if (alertConfigBean.getAlertUrl() == null ||
                alertConfigBean.getAlertUrl().trim().length() == 0) {

                logger.warn("Translation alert config is not of type " +
                            TranslateAlertConfig.class);
                alertConfigBean = null;
            }
        }
        urlTouchService = (UrlTouchService)ServiceManager.getInstance().getService("urlTouchService");

    }

    protected void fireRefreshAlert(Message message) {
        if (alertConfigBean == null) {
            return;
        }
        Map parameters = new HashMap();
        parameters.put("locale", message.getLocale());
        parameters.put("siteId", message.getSiteId());
        parameters.put("key", DgUtil.encodeString(message.getKey()));

        String urlString = DgUtil.fillPattern(alertConfigBean.getAlertUrl(),
                                              parameters);
        if (urlTouchService == null) {
            UrlTouchService.touchUrl(urlString, alertConfigBean.getUserAgent());
        }
        else {
            urlTouchService.asyncTouchUrl(urlString);
        }
    }

	public boolean isCaseSensitiveKeys() {
		return caseSensitiveKeys;
	}
	
	/**
	 * TODO Review and move to better class.
	 * Note, this will return AMP site.
	 * @return
	 * @throws WorkerException
	 */
	public Site getDefaultSite() throws WorkerException{
		try {
			if (ampSite == null){
				ampSite = SiteUtils.getSite("amp");
			}
			return ampSite;
		} catch (DgException e) {
			throw new WorkerException("Cannot load AMP Site!",e);
		}
	}
	
	/**
	 * Converts an Unicode string into UTF-8 string.
	 * @param original
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String unicodeToUTF8(String original) throws UnsupportedEncodingException{
		String newString = null;
		byte[] tempBytes = original.getBytes("UTF8");
		newString = new String(tempBytes);
		return newString;
	}
	
}
