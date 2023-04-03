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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.wicket.protocol.http.WebApplication;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.services.UrlTouchService;
import org.digijava.kernel.translator.util.TrnAccesTimeSaver;
import org.digijava.kernel.translator.util.TrnAccessUpdateQueue;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Shamanth Murthy
 *
 * Worker class for all translator related operations
 */
public class TranslatorWorker {

    public static boolean FREEZE_TIMESTAMP_UPDATING = false;
    
    private static Logger logger =
        Logger.getLogger(TranslatorWorker.class);

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
    private TrnAccessUpdateQueue timeStampQueue = TrnAccessUpdateQueue.getQueue();

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
    public void refresh(String key, String locale, Long siteId) throws WorkerException {
        // DO Nothing
    }
    
    //TODO may be bad idea!
    public void refresh(Message message) throws WorkerException {
        // DO Nothing
    }

    /**
     * Stub method for returning default language code.
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
    public static String translate(String key, String locale, Long siteId) throws WorkerException {
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
    public static String translateText(String text, String locale, Long siteId){
        return translateText(text, null, locale, siteId);
    }
    
    public static String translateText(String text, String locale, Site site){
        return translateText(text, null, locale, site == null ? null : Site.getIdOf(site));
    }
    
    /**
     * translates text using the TLS-stored locale and siteId
     * in case of an underlying exception, returns untranslated text, as this is what all the translateText(String, String, Long) users did anyway
     * @param text
     * @return
     */
    public static String translateText(String text)
    {
        try
        {
            String effectiveLangCode = TLSUtils.getEffectiveLangCode();
            Long siteId = TLSUtils.getSiteId();
            return translateText(text, null, effectiveLangCode, siteId);
        }
        catch(Exception e)
        {
            logger.error("cannot translate text " + text, e);
            return text;
        }
    }
    
    /**
     * Translates text to specified local.
     * If nothing found, it retries to translate the string using the alternate site_id (please see the mess described in AMP-14236 and AMP-14232 for more info)
     * see #translateText(String, String, String) for more details.
     * @param text
     * @param keyWords
     * @param locale
     * @param siteId
     * @return
     * @throws WorkerException
     */
    public static String translateText(String text, String keyWords, String locale, Long siteId) {
        if (text == null)
            return "";

        TranslatorWorker worker = getInstance("");

        Message message = findMessage(worker, text, keyWords, locale, siteId);

        processAmpOfflineMessage(worker, text, message);

        return message.getMessage();
    }

    /**
     * Mark message as used by AMP Offline if current request is issued by AMP Offline.
     *
     * @param worker worker used to update message
     * @param message the message to check
     */
    private static void processAmpOfflineMessage(TranslatorWorker worker, String text, Message message) {
        boolean updateRequired = false;
        if (AmpClientModeHolder.isOfflineClient() && !isAmpOfflineMessage(message)) {
            message.setAmpOffline(true);
            updateRequired = true;
        }
        if (message.getLocale().equals("en") && isOriginalMsgKeyIncorrect(message)) {
            message.setOriginalMessage(text);
            updateRequired = true;
        }
        if (updateRequired) {
            worker.update(message);
        }
    }

    private static boolean isOriginalMsgKeyIncorrect(Message message) {
        return (!TranslatorWorker.generateTrnKey(message.getOriginalMessage()).equals(message.getKey())
                && !TranslatorWorker.generateTrnKey(message.getOriginalMessage(), true).equals(message.getKey()));
    }

    private static boolean isAmpOfflineMessage(Message message) {
        return message.getAmpOffline() != null && message.getAmpOffline();
    }

    private static Message findMessage(TranslatorWorker worker, String text, String keyWords, String locale, Long siteId) {

        //Try to find translation
        Message msg = worker.getByBody(text, keyWords, locale, siteId);
        if (msg != null)
            return msg;
              
        // Then try to find in default language
        msg = worker.getByBody(text, keyWords, getDefaultLocalCode(), siteId);
        if (msg != null)
            return msg;
            
        // no translations found => create a default entry
        msg = new Message();
        msg.setSite(SiteCache.lookupById(siteId));
        msg.setLocale(getDefaultLocalCode());
        msg.setMessage(text);
        msg.setKeyWords(keyWords);
        msg.setKey(TranslatorWorker.generateTrnKey(text));
        worker.save(msg);

        return msg;
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
     * Gets all distinct keys with only one occurrence of ':'
     * for a given siteId and its root
     *
     * @param siteId
     * @param rootSiteId
     * @return
     * @throws WorkerException
     */
    public Set<String> getPrefixesForSite(Long siteId, Long rootSiteId) throws
        WorkerException {

        HashSet<String> returnList = new HashSet<String>();

        List<String> ls = getKeysForSite(siteId, rootSiteId);

        for(String msg:ls) {
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
    public Message getMessage(String key, String locale, Long siteId) throws
        WorkerException {

        if (siteId == null) {
            Long defaultSideId = getDefaultSite().getId();
            return getByKey(key, locale, defaultSideId);
        } else {
            Site site = SiteCache.getInstance().getSite(siteId);

            if (site == null) {
                logger.debug("Site Id not found for site " + siteId);
                return null;
            }

            return getByKey(key, locale, siteId);
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
        Long siteId = Site.getIdOf(site);

        Message trnMess = getFromGroup(key, locale, site);

        if (trnMess == null) {
            logger.debug("constructing default translation");
            trnMess = new Message();
            trnMess.setMessage(defaultMessage);
            trnMess.setKey(key);
            trnMess.setLocale(locale);
            trnMess.setSite(SiteCache.lookupById(siteId));
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
        Long siteId = Site.getIdOf(site);

        if (key != null && ! "".equals(key)) {
            try {
                // process the case when key is already hashed
                int keyAsHash = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                key = generateTrnKey(key);
            }
        }

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
        Long rootSiteId = SiteCache.getInstance().getRootSite(site).getId();
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
    public Message getByBody(String originalText, String local, Long siteId) {
        return getByBody(originalText, null, local, siteId);
    }

    public Message getByBody(String originalText, String keyWords, String local, Long siteId) {
        // try first using prefix.
        String hashCode = generateTrnKey(originalText, true);
        Message msg = getByKey(hashCode, originalText, keyWords, local, siteId);
        if (msg == null) {
            hashCode = generateTrnKey(originalText);
            msg = getByKey(hashCode, originalText, keyWords, local, siteId);
        }
        return msg;
    }
    
    public Message getByKey(String key, String locale, Long siteId) {
        return getByKey(key, "", null, locale, siteId);
    }
    
    public Message getByKey(String key, String locale, Site site) {
        return getByKey(key, "", null, locale, Site.getIdOf(site));
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
    public Message getByKey(String key, String defaultText, String keyWords, String locale, Long siteId) {
        return getByKey(key, defaultText, keyWords, locale, siteId, null);
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
     * @param dbSession database session to get the meesage from. In case null using session from persistence manager
     * @return
     * @throws WorkerException
     */
    public Message getByKey(String key, String defaultText, String keyWords, String locale, Long siteId,
                            Session dbSession) {

        /**
         * @todo This stuff needs to be changed. All developers should use
         * getInstance() / getInstance(String key) methods to get instance.
         *
         * THIS IS A WORKAROUND,
         * Instead of change DgMarket code to get precached translations,
         * we decided to use such small trick
         * TODO mag
         */
        TranslatorWorker realWorker = TranslatorWorker.getInstance(key);

        if (realWorker instanceof CachedTranslatorWorker) {
            return ((CachedTranslatorWorker) realWorker).getByKey(key, locale, siteId, true, null, dbSession);
        }
        // END OF WORKAROUND
        Session session = dbSession != null ? dbSession : PersistenceManager.getSession();
        try {

            Message messageKey = new Message();
            messageKey.setKey(processKeyCase(key));
            messageKey.setLocale(locale);
            messageKey.setSite(SiteCache.lookupById(siteId));
            Message message = (Message) session.load(Message.class, messageKey);
            message.setKeyWords(keyWords);
            updateTimeStamp(message);
            return message;
        }
        catch (ObjectNotFoundException onfe) {
            return null;
        }

        catch (HibernateException he) {
            logger.error(String.format("Error reading translation. siteId=%s, key=%s,locale=%s", siteId, key, locale),
                    he);
            throw new RuntimeException(he);
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
    protected List<String> getKeysForSite(Long siteId, Long rootSiteId) throws
        WorkerException {

        String query =
            "select distinct message.key from org.digijava.kernel.entity.Message message where message.siteId='"
            + siteId.toString()
            + "' or message.siteId='"
            + rootSiteId
            + "' order by message.key";
        Session session = null;

        try {

            session = PersistenceManager.getSession();
            Query q = session.createQuery(query);
            return (List<String>) q.list();
        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err ";
            logger.error(errKey + he.getMessage());
            throw new WorkerException(he.getMessage(), he);
        }
    }

    public static List<String> getAllPrefixes() {
        String query = "select distinct message.prefix from org.digijava.kernel.entity.Message "
                + "where message.prefix is not null";
        Session session = PersistenceManager.getSession();
        Query q = session.createQuery(query);
        return (List<String>) q.list();
    }

    private Map<String, Message> getMessagesForCriteria(
        String prefix,
        Long siteId,
        String lang
        ) throws WorkerException {
        Session session = null;
        Map<String, Message> messageMap = new HashMap<String, Message>();
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

            List<Message> ls = (List<Message>) q.list();
            for(Message currMsg:ls) {
                messageMap.put(currMsg.getKey(), currMsg);
            }
        }

        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err ";
            logger.error(errKey + he.getMessage());
            throw new WorkerException(he.getMessage(), he);
        }

        return messageMap;
    }

    private List<TranslatorBean> filterResults(List<TranslatorBean> input, int startFrom, int numResults) {
        List<TranslatorBean> filteredResult = new ArrayList<TranslatorBean>();
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

    public List<TranslatorBean> getMessagesForPrefix(
        String prefix,
        Long siteId,
        Long rootSiteId,
        String srcLang,
        String targetLang,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List<TranslatorBean> rtList;
        try {
            String query =
                "select distinct message.key from org.digijava.kernel.entity.Message message where ( message.siteId='"
                + siteId
                + "' or message.siteId='"
                + rootSiteId
                + "' ) and message.key like '"
                + processKeyCase(prefix)
                + "%'"
                + " order by message.key";

            Map<String, Map<String, Message>> allMessages = new HashMap<String, Map<String, Message>>();
            Map<String, Message> siteSrcMap = getMessagesForCriteria(prefix, siteId, srcLang);
            Map<String, Message> siteTargetMap = null;
            Map<String, Message> rootSiteSrcMap = null;
            Map<String, Message> rootSiteTargetMap = null;

            if (srcLang.equals(targetLang)) {
                siteTargetMap = siteSrcMap;
            }
            else {
                siteTargetMap = getMessagesForCriteria(prefix, siteId, targetLang);
            }

            if (siteId.equals(rootSiteId)) {
                rootSiteSrcMap = siteSrcMap;
            }
            else {
                rootSiteSrcMap = getMessagesForCriteria(prefix, rootSiteId, srcLang);
            }

            if (srcLang.equals(targetLang)) {
                rootSiteTargetMap = rootSiteSrcMap;
            }
            else {
                rootSiteTargetMap = getMessagesForCriteria(prefix, rootSiteId, targetLang);
            }

            allMessages.put(CURR_SITE_SRC_MSG, siteSrcMap);

            allMessages.put(CURR_SITE_TARGET_MSG, siteTargetMap);

            allMessages.put(ROOT_SITE_SRC_MSG, rootSiteSrcMap);

            allMessages.put(ROOT_SITE_TARGET_MSG, rootSiteTargetMap);

            session = PersistenceManager.getSession();
            Query q = session.createQuery(query);
            List<String> ls = (List<String>)q.list();

            rtList = checkMessages(ls, srcLang, targetLang, isExpired, allMessages);

            Set<TranslatorBean> col = compare(rtList);
            ArrayList<TranslatorBean> sortedList = new ArrayList<TranslatorBean>();
            sortedList.addAll(col);

            return filterResults(sortedList, startFrom, numResults);

        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err ";
            logger.error(errKey + he.getMessage());
            throw new WorkerException(he.getMessage(), he);
        }

    }

    private List<TranslatorBean> checkMessages(List<String> ls, String srcLang, String targetLang,
                               boolean isExpired, Map<String, Map<String, Message>> allMessages) throws
        WorkerException {

        List<TranslatorBean> rtList = new ArrayList<TranslatorBean>();
        Map<String, Message> siteSrcLangMap = allMessages.get(CURR_SITE_SRC_MSG);
        Map<String, Message> siteTargetLangMap = allMessages.get(CURR_SITE_TARGET_MSG);
        Map<String, Message> rootSiteSrcLangMap = allMessages.get(ROOT_SITE_SRC_MSG);
        Map<String, Message> rootSiteTargetLangMap = allMessages.get(ROOT_SITE_TARGET_MSG);

        //Check for each key started
        for(String strKey:ls) {
            if (strKey != null) {
                Message srcMsg = siteSrcLangMap.get(strKey);

                Message targetMsg = siteTargetLangMap.get(strKey);

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

                    Message targetRootMsg = rootSiteTargetLangMap.get(strKey);

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
                    Message srcRootMsg = rootSiteSrcLangMap.get(strKey);

                    Message targetRootMsg = rootSiteTargetLangMap.get(strKey);

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
    public List<TranslatorBean> searchKeysForPattern(
        String prefix,
        Long siteId,
        Long rootSiteId,
        String srcLang,
        String targetLang,
        String keyPattern,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List<TranslatorBean> rtList = new ArrayList<TranslatorBean>();
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
                + " order by message.key";

            Map<String, Map<String, Message>> allMessages = new HashMap<String, Map<String, Message>>();

            allMessages.put(CURR_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, siteId, srcLang));
            allMessages.put(CURR_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, siteId, targetLang));
            allMessages.put(ROOT_SITE_SRC_MSG,
                            getMessagesForCriteria(prefix, rootSiteId, srcLang));
            allMessages.put(ROOT_SITE_TARGET_MSG,
                            getMessagesForCriteria(prefix, rootSiteId,
                targetLang));

            rtList = new ArrayList<TranslatorBean>();

            session = PersistenceManager.getSession();

            Query q = session.createQuery(query);
            //q.setFirstResult(startFrom);
            //q.setMaxResults(numResults);
            List<String> ls = (List<String>)q.list();

            rtList = checkMessages(ls, srcLang, targetLang, isExpired, allMessages);

            return filterResults(rtList, startFrom, numResults);

        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err ";
            logger.error(errKey + he.getMessage());
            throw new WorkerException(he.getMessage(), he);
        }
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
    public List<TranslatorBean> searchMessageForPattern(
        String prefix,
        Long siteId,
        Long rootSiteId,
        String srcLang,
        String targetLang,
        String messagePattern,
        String locale,
        boolean isExpired,
        int startFrom,
        int numResults) throws WorkerException {

        Session session = null;
        List<TranslatorBean> rtList = new ArrayList<TranslatorBean>();
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
                + " order by message.key";

            Map<String, Map<String, Message>> allMessages = new HashMap<String, Map<String, Message>>();
            allMessages.put(CURR_SITE_SRC_MSG, getMessagesForCriteria(prefix, siteId, srcLang));
            allMessages.put(CURR_SITE_TARGET_MSG, getMessagesForCriteria(prefix, siteId, targetLang));
            allMessages.put(ROOT_SITE_SRC_MSG, getMessagesForCriteria(prefix, rootSiteId, srcLang));
            allMessages.put(ROOT_SITE_TARGET_MSG, getMessagesForCriteria(prefix, rootSiteId, targetLang));
            rtList = new ArrayList<TranslatorBean>();

            session = PersistenceManager.getSession();

            Query q = session.createQuery(query);
            List<String> ls = (List<String>)q.list();

            rtList = checkMessages(ls, srcLang, targetLang, isExpired, allMessages);

            return filterResults(rtList, startFrom, numResults);

        }
        catch (HibernateException he) {
            String errKey = "TranslatorWorker.HibExLoadingMessage.err ";
            logger.error(errKey + he.getMessage());
            throw new WorkerException(he.getMessage(), he);
        }
    }

    /**
     * Saves a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void save(Message message) {
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
       //   message.setKey(processKeyCase(message.getKey()));
       // }
    }
    
    public String processKeyCase(String key) {
        //commented out for speed. We do not need this with hash code keys
        //if (!isCaseSensitiveKeys()){
        //  return key.toLowerCase();
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
    public static String generateTrnKey(String text) {
        return generateTrnKey(text, false);
    }

    public static String generateTrnKey(String text, boolean usePrefix) {
        if (text != null) {
            String trnPrefix = usePrefix ? TrnUtil.getTrnPrefix() : "";
            return Integer.toString(((trnPrefix != null ? trnPrefix : "") + text).trim().toLowerCase().hashCode());
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
    protected void updateTimeStamp(Message message) {
        if (!FREEZE_TIMESTAMP_UPDATING)
            timeStampQueue.put(message);
    }
    
    /**
     * Saves message in db.
     * Removes form access time queue if this message is waiting update there too. 
     * @param message
     * @throws WorkerException
     */
    protected void saveDb(Message message) {
        logger.debug("Saving translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() +
                     ",locale=" + message.getLocale());
        Session ses = null;

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
                message.setLastAccessed(message.getCreated());
            } else {
                message.setCreated(new Timestamp( -1000));
                message.setLastAccessed(message.getCreated());
            }
            
            //Remove from queue if this message is there because here we are doing same.
            //timeStampQueue.remove(message);
            //we set the workspace prefix if present that was used to calculate the hash
            //if its null it remains null
            message.setPrefix(TrnUtil.getTrnPrefix());
            
            ses.saveOrUpdate(message);
            //tx.commit();
            
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
                   throw new RuntimeException("TranslatorWorker.HibExSaveMessage.err", e); 
            }
                
        }
    }
  

    /**
     * Updates a particular message in db.
     * @param message
     * @throws WorkerException
     */
    public void update(Message message) {
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
    protected void updateDb(Message message) {
        logger.debug("Updating translation. siteId="+ message.getSiteId() + ", key = " + message.getKey() + ",locale=" + message.getLocale());
        Session ses = null;

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
        catch (HibernateException e) {
            logger.error("Error updating translation. siteId="
                         + message.getSiteId() + ", key = " + message.getKey() +
                         ",locale=" + message.getLocale(), e);
//          //System.out.println("Error updating translation. msg="+message.getMessage()+" siteId="
//                  + message.getSiteId() + ", key = " + message.getKey() +
//                  ",locale=" + message.getLocale());
            throw new RuntimeException("TranslatorWorker.HibExUpdateMessage.err", e);
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
        catch (HibernateException e) {
            logger.error("Error updating translation. siteId="
                         + message.getSiteId() + ", key = " + message.getKey() +
                         ",locale=" + message.getLocale(), e);
            throw new WorkerException("TranslatorWorker.HibExUpdateMessage.err", e);
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
        List<Message> messages;
        String queryString = "from " + Message.class.getName() +
            " msg where msg.key=:msgKey";

        try {

            ses = PersistenceManager.getSession();
//beginTransaction();
            Query q = ses.createQuery(queryString);
            q.setString("msgKey", processKeyCase(key.trim()));

            messages = q.list();
            for(Message msg:messages)
            {
                msg.setCreated(timestamp);
                msg.setLastAccessed(timestamp);

                //Remove from queue if this message is there because here we are doing same.
                //timeStampQueue.remove(msg);
                
                ses.update(msg);
            }

            //tx.commit();

        }
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key, e);
            throw new WorkerException("Error updating translations. key=" + key, e);
        }
    }

    protected boolean isKeyExpired(String key) {
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
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key + ", HibernateException=" + e.getMessage());
            throw new RuntimeException("Error updating translations. key=" + key + ", "+e.getMessage(), e);
        }
        return result;
    }

    private TreeSet<TranslatorBean> compare(List<TranslatorBean> beans) {

        java.util.TreeSet<TranslatorBean> sort = new java.util.TreeSet<TranslatorBean>(new java.util.Comparator<TranslatorBean>() {
            public int compare(TranslatorBean tb1, TranslatorBean tb2) {

                if (tb1 != null && tb2 != null) {

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
    public static Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException {
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
    public static Collection<Message> getAllTranslationsOfKey(String key, Long siteId) throws WorkerException {
        return getInstance("").getAllTranslationsOfKeyInternal(key, siteId);
    }

    @SuppressWarnings("unchecked")
    public Collection<Message> getAllTranslationsOfKeyInternal(String key, Long siteId) throws WorkerException {
        Session session = null;
        List<Message> result = null;
        try {
            session = PersistenceManager.getSession();
            String oql = "from "+Message.class.getName()+" as m where m.key = :key and m.siteId = :SiteId";
            Query query = session.createQuery(oql);
            query.setString("key", key);
            query.setString("SiteId", siteId.toString());
            result = query.list();
        } catch (Exception e) {
            throw new WorkerException(e);
        }
        return result;
    }
 
    
    @SuppressWarnings("unchecked")
    public static List<String> getAllTranslationsKeys(Long siteId) throws WorkerException{
        Session session = null;
        List<String> keys = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String oql = "select distinct m.key from "+Message.class.getName()+" as m where m.siteId = :siteId order by  m.key";
            Query query =session.createQuery(oql);
            query.setString("siteId", siteId.toString());
            keys = query.list();
        } catch (Exception e) {
            throw new WorkerException(e);
        }
        return keys;
    }
   
   /**
    * Get messages that have values only in particular language 
    * @param site
    * @param locale
    * @return
    * @throws WorkerException
    */
    @SuppressWarnings("unchecked")
    public static List<Message> getOnlyLanguageTranslationsKeys(Site site,
            String locale) throws WorkerException {
        Session session = null;
        List<Message> messages =null;
        try {
            session = PersistenceManager.getRequestDBSession();
                String oql = " from " + Message.class.getName()
                + " as m where m.key in (select m1.key from " + Message.class.getName()
                    + " as m1 group by m1.key having count(m1.key)=1) and  m.siteId =:siteId and m.locale=:locale order by  m.key ";
                Query query = session.createQuery(oql);
                query.setString("siteId",  Site.getIdOf(site).toString());
                query.setString("locale", locale);
                messages=query.list();
        } catch (Exception e) {
            throw new WorkerException(e);
        }
        return messages;
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
    /**
     * Should be called from wicket part
     * @param key
     * @param siteId
     * @param langCode
     * @param defaultTrn
     * @param translationType
     * @param keyWords
     * @return
     * @throws WorkerException
     */

    public String translateFromTree(String key, Site site, String langCode,
            String defaultTrn, int translationType, String keyWords)
            throws WorkerException {
        ServletContext context = WebApplication.get().getServletContext();
        return translateFromTree(key, site, new String[] { langCode },
                defaultTrn, langCode, translationType, keyWords, context);
    }
    /**
     * Should be called from none wicket part to avoid WicketRuntimeException:
     * There is no application attached to current thread
     * @param key
     * @param siteId
     * @param langCode
     * @param defaultTrn
     * @param translationType
     * @param keyWords
     * @param context
     * @return
     * @throws WorkerException
     */

    public String translateFromTree(String key, Site site, String langCode,
            String defaultTrn, int translationType, String keyWords,
            ServletContext context) throws WorkerException {
        return translateFromTree(key, site, new String[] { langCode },
                defaultTrn, langCode, translationType, keyWords, context);
    }


    public String translateFromTree(String key, Site site, String[] langCodes,
                                    String defaultTrn, String defaultLocale, int translationType, String keyWords,ServletContext context) throws
        WorkerException {
        SiteCache siteCache = SiteCache.getInstance();
        //Site site = siteCache.getSite(siteId);
       
        Long regId = null;
        if (site == null) {
            site = getDefaultSite();
        }
        Long[] siteIds = null;
        if (translationType == TRNTYPE_LOCAL) {
            siteIds = new Long[] {
                site.getId()};
            regId = site.getId();
        }
        else {
            Site rootSite = siteCache.getRootSite(site);
            if (rootSite != null && rootSite.getId().equals(site.getId())) {
                rootSite = null;
            }
            if (translationType == TRNTYPE_GROUP) {
                if (rootSite == null) {
                    siteIds = new Long[] {
                        site.getId()};
                    regId = site.getId();
                }
                else {
                    siteIds = new Long[] {
                        site.getId(), rootSite.getId()};
                    regId = rootSite.getId();
                }
            }
            else {
                if (rootSite == null) {
                    siteIds = new Long[] {
                        site.getId(), 0L};
                    regId = site.getId();
                }
                else {
                    siteIds = new Long[] {
                        site.getId(), rootSite.getId(), 0L};
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
                Message msg = getByKey(key, defaultLocale, regId);
                if (msg == null) {
                    Message message = new Message();
                    message.setMessage(defaultTrn.trim());

                    message.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
                    message.setKey(key);
                    message.setSite(SiteCache.lookupById(regId));
                    message.setLocale(defaultLocale);
                    message.setKeyWords(keyWords);
                    
                    save(message);
                    if(context!=null){
                          String suffix =  message.getLocale();
                        try {
                            LuceneWorker.addItemToIndex(message, context,suffix);
                        } catch (DgException e) {
                            logger.debug("unable to add translation to lucene");
                        }
                    }
                  
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
        Map<String, String> parameters = new HashMap<String, String>();
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
    public Site getDefaultSite(){
        return SiteUtils.getDefaultSite();
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
    
    
    public boolean deleteMessages(Date date) throws WorkerException {

        Session ses = null;
        String queryString = "delete Message msg where msg.lastAccessed is null or msg.lastAccessed <=:stamp";
        boolean recreateLuceneIndex = false;

        ses = PersistenceManager.getSession();
        int deletedEntities = ses.createQuery(queryString).setTimestamp("stamp", new Timestamp(date.getTime())).executeUpdate();
        if (deletedEntities > 0) {
            recreateLuceneIndex = true;
        }
        return recreateLuceneIndex;
    }
    
    
    public void cleanTimeStampQueue()
    {
        this.timeStampQueue.clear();
    }

}
