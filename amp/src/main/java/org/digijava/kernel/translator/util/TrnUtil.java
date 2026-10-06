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

package org.digijava.kernel.translator.util;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.entity.PatcherMessageGroup;
import org.digijava.module.translation.lucene.TrnLuceneModule;
import org.digijava.module.translation.util.ListChangesBuffer;
import org.digijava.module.translation.util.ListChangesBuffer.OperationFixer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class TrnUtil {

    private static final String CACHE_REGION = "org.digijava.kernel.translator.util.TranslatorUtil.Query";

    private static Logger logger = Logger.getLogger(TrnUtil.class);

    private static final HashMap defaultMonths;

    public static final Comparator countryNameComparator;
    public static final Comparator localeNameComparator;

    public static final String PREFIX = "prefix";
    public static final String DEFAULT = "default";
    public static final String PREFIXES = "prefixes";

    /**
     * These are languages that has its own weight for sorting
     * Weight of the language is array length- its index (position in array)
     * so first language is heavier then second in this array.
     * other languages that do not appear here will still have weights but same value.
     * NOTE: this is fast workaround, it would be better to make this configurable. 
     */
    private static String[] locales = {"en", "fr", "sp"};

    /**
     * Returns weight of the locale.
     * uses {@link #locales} array for calculating weight.
     * If no weight is found null is returned.
     * @param locale Locale string values like this 'en' 'fr' etc.
     * @return Integer value which is weight of local, NULL if no weight.
     */
    private static Integer getLocaleWeight(String locale){
        for (int i = 0; i < locales.length; i++) {
            if (locale.equalsIgnoreCase(locales[i])){
                return new Integer(i);
            }
        }
        return null;

//      int w = locales.length;
//      for (String language : locales) {
//          if (language.equals(locale)){
//              break;
//          }
//          w--;
//      }
//      return new Integer(w);
    }


    /**
     * Returns collection of TrnLocale objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnLocale objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnLocale
     */
    public static Set<TrnLocale> getLanguages(String isoLanguage) throws DgException {
        HashSet<TrnLocale> languages = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select l.code, msg.message from " +
                Locale.class.getName() + " l, " +
                Message.class.getName() + " msg " +
                " where msg.key=l.messageLangKey and msg.siteId='0' and l.available=true and msg.locale=:locale"
                );
            query.setParameter("locale", isoLanguage, StringType.INSTANCE);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            languages = new HashSet<TrnLocale>();

            Iterator iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnLocale locale = new TrnLocale((String)item[0],(String)item[1]);
                languages.add(locale);
            }

            query = session.createQuery(
                "select l.code, l.name from " +
                Locale.class.getName() +
                " l where l.available=true"
                );
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);

            iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnLocale locale = new TrnLocale((String)item[0],(String)item[1]);
                if (!languages.contains(locale)) {
                    languages.add(locale);
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get Languages ",ex);
            throw new DgException("Unable to get Languages ",ex);
        }
        return languages;
    }

    /**
     * Returns collection of TrnLocale objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnLocale objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnLocale
     */
    public static Collection getLanguages() throws DgException {
        HashSet languages = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select l.code, msg.message from " +
                Locale.class.getName() + " l, " +
                Message.class.getName() + " msg " +
                " where msg.key=l.messageLangKey and msg.siteId='0' and l.available=true and msg.locale=l.code"
                );
            query.setCacheable(true);
            query.setCacheRegion(Constants.KERNEL_QUERY_CACHE_REGION);
            languages = new HashSet();

            List list = query.list();
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnLocale locale = new TrnLocale((String)item[0],(String)item[1]);
                languages.add(locale);
            }

            query = session.createQuery(
                "select l.code, l.name from " +
                Locale.class.getName() +
                " l where l.available=true"
                );
            query.setCacheable(true);
            query.setCacheRegion(Constants.KERNEL_QUERY_CACHE_REGION);

            iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnLocale locale = new TrnLocale((String)item[0],(String)item[1]);
                if (!languages.contains(locale)) {
                    languages.add(locale);
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get Languages ",ex);
            throw new DgException("Unable to get Languages ",ex);
        }
        return languages;
    }


    /**
     * Returns collection of TrnCountry objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnCountry objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnCountry
     */
    public static Set<TrnCountry> getCountries(String isoLanguage) throws DgException {
        HashSet<TrnCountry> countries = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select c.iso, msg.message from " +
                Country.class.getName() + " c, " +
                Message.class.getName() + " msg " +
                " where msg.key=c.messageLangKey and msg.siteId='0' and c.available=true and msg.locale=:locale"
                );
            query.setParameter("locale", isoLanguage,StringType.INSTANCE);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            countries = new HashSet<TrnCountry>();

            Iterator iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnCountry country = new TrnCountry((String)item[0],(String)item[1]);
                countries.add(country);
            }

            query = session.createQuery(
                "select c.iso, c.countryName from " +
                Country.class.getName() +
                " c where c.available=true"
                );
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);

            iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnCountry country = new TrnCountry((String)item[0],(String)item[1]);
                if (!countries.contains(country)) {
                    countries.add(country);
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get Countries ",ex);
            throw new DgException("Unable to get Countries ",ex);
        }
        return countries;
    }

    /**
     * Returns collection of TrnMonth objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnMonth objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnMonth
     */
    public static Set<TrnMonth> getMonths(String isoLanguage) throws DgException {
        HashSet<TrnMonth> months = null;

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select msg.key, msg.message from " +
                Message.class.getName() + " msg " +
                " where msg.key like \'month%\' and msg.siteId='0' and msg.locale=:locale"
                );
            query.setParameter("locale", isoLanguage,StringType.INSTANCE);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            months = new HashSet<TrnMonth>();

            Iterator iter = query.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                TrnMonth month = (TrnMonth)defaultMonths.get((String)item[0]);
                TrnMonth translatedMonth = new TrnMonth(month.getIso(),(String)item[1]);
                months.add(translatedMonth);
            }


            iter = defaultMonths.values().iterator();
            while (iter.hasNext()) {
                TrnMonth month = (TrnMonth)iter.next();
                if (!months.contains(month)) {
                    months.add(month);
                }
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get Months ",ex);
            throw new DgException("Unable to get Months ",ex);
        }
        return months;
    }

    /**
     * Returns sorted and translated user languages for the given site. This list is formed
     * using using language inheritance business logic (see DiGi Multilangual
     * document for more details)
     * @param site
     * @return sorted languages
     */
    public static List<TrnLocale> getSortedUserLanguages(HttpServletRequest request) throws
        DgException {
            Set<Locale> languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
            HashMap<String, TrnLocale> translations = new HashMap<String, TrnLocale>();
            Set<TrnLocale> langs = TrnUtil.getLanguages(RequestUtils.
                                            getNavigationLanguage(request).
                                            getCode());
            for(TrnLocale item:langs)
                translations.put(item.getCode(), item);

            //sort languages
            List<TrnLocale> sortedLanguages = new ArrayList<TrnLocale>();
            for(Locale item:languages)
                sortedLanguages.add(translations.get(item.getCode()));

            Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

            return sortedLanguages;
    }


    /**
     * Returns list of query results sorted according translated sitenames.
     * If there is no appropreate resource for one of the sitenames, sorting
     * will process according default sitename.
     * @param source Collection of query results
     * @param locale Selected locale for translation
     * @param callback TranslationCallback implementation, which defines how to
     * use query result item
     * @param groupTranslation <p>When true sorting is proceeded according to root
     * translations if local translations are null</p>
     * <p>When false sorting is
     * proceeded according to local translations only.</p><p>In both cases, when local or
     * root translations are null sorting is proceeded according default
     * sitename</p>
     * @return new sorted List, compatible with source collection
     * @throws DgException
     */
    public static List sortByTranslation(Collection source,
                                         Locale locale,
                                         TranslationCallback callback,
                                         boolean groupTranslation) throws
        DgException {
        List sortedList = new ArrayList();
        TranslatorWorker trnWork = TranslatorWorker.getInstance();

        if (source != null && !source.isEmpty()) {
            Iterator it = source.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                String siteKey = callback.getTranslationKey(obj);
                String siteId = callback.getSiteId(obj);
                String defTrans = callback.getDefaultTranslation(obj);
                String trnString = null;

                /**
                 * @todo when translation will be moved on string ids, the following line must be removed
                 */
                Site site = null;
                if (siteId != null) {
                  site = SiteCache.lookupByName(siteId);
                }

                Message trnMess = null;
                if (site != null) {
                    trnMess = trnWork.getByBody(defTrans, locale.getCode(), site.getId());
                    if (trnMess == null && groupTranslation && site.getParentId() != null) {
                        Site root = SiteCache.getInstance().getRootSite(site);
                        trnMess = trnWork.getByBody(defTrans, locale.getCode(), root.getId());
                    }
                } else {
                    trnMess = trnWork.getByBody(defTrans, locale.getCode(), 0L);
                }
                if (trnMess == null) {
                    trnString = defTrans;
                }
                else {
                    trnString = trnMess.getMessage();
                }

                Object[] sortObj = new Object[] {trnString, obj};
                sortedList.add(sortObj);
            }
        }
        Comparator sortComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                String s1 = (String) ( (Object[]) o1)[0];
                String s2 = (String) ( (Object[]) o2)[0];
                int retVal = 0;
                if (s1 == null && s2 == null) {
                    retVal = 0;
                }
                else if (s1 == null) {
                    retVal = -1;
                }
                else if (s2 == null) {
                    retVal = 1;
                }
                else {
                    retVal = s1.compareTo(s2);
                }
                return retVal;
            }
        };
        Collections.sort(sortedList, sortComparator);
        ArrayList retVal = new ArrayList();
        Iterator sortedIt = sortedList.iterator();
        while (sortedIt.hasNext()) {
            Object[] tmpObj = (Object[]) sortedIt.next();
            retVal.add(tmpObj[1]);
        }
        return retVal;
    }
    /**
     * Returns modules for all supported languages including English.
     * @see LangSupport
     * @return
     */
    public static List<LucModule<?>> getLuceneModules(){
        List<LucModule<?>> modules = new ArrayList<LucModule<?>>();
        LangSupport[] langs = LangSupport.values();
        for (LangSupport lang : langs) {
            modules.add(new TrnLuceneModule(lang));
        }
        return modules;
    }

    /**
     * Returns messages for specified set of keys.
     * This is used when we need to load translations only by keys not looking at site_id and language.
     * @param keys set of keys to search fore.
     * @return list of found {@link Message} beans.
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<Message> getMessagesForKeys(Set<String> keys) throws DgException{
        List<Message> messages = null;
        if (keys!=null){
            StringBuffer buff = new StringBuffer("from ");
            buff.append(Message.class.getName());
            buff.append(" as m where m.key in ( :keys )");

            String oql = buff.toString();
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setParameterList("keys", keys);
            messages = query.list();
        }
        return messages;
    }

    /**
     * Returns all messages of specified locale code.
     * @param locales set of locale codes like en, fr, es
     * @return list of Message beans - translations.
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<Message> getMessagesForLocales(EnumSet<LangSupport> locales, boolean exclude) throws DgException{
        List<Message> messages = null;
        if (locales!=null && locales.size()>0){
            StringBuffer buff = new StringBuffer("from ");
            buff.append(Message.class.getName());
            if (exclude){
                buff.append(" as m where m.locale not in ( :langs )");
            }else{
                buff.append(" as m where m.locale in ( :langs )");
            }
            String oql = buff.toString();
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setParameterList("langs", LangSupport.toCodeList(locales));
            messages = query.list();
        }
        return messages;
    }


    /**
     * Returns list of query results sorted according translated sitenames.
     * If there is no appropreate resource for one of the sitenames, sorting will
     * process according default sitename.
     * @param source Collection of query results
     * @param locale Selected locale for translation
     * @param callback TranslationCallback implementation, which defines how to
     * use query result item
     * @return new sorted List, compatible with source collection
     * @throws DgException
     */
    public static List sortByTranslation(Collection source,
                                         Locale locale,
                                         TranslationCallback callback) throws
        DgException {

        return sortByTranslation(source,locale,callback,false);
    }

    /**
     * Groups messages with key field in {@link MessageGroup} beans. 
     * @param messages
     * @return
     * @throws DgException
     */
    public static Collection<MessageGroup> groupByKey(Collection<Message> messages) throws DgException{
        return groupByKey(messages,new StandardMessageGroupFactory());
    }

    /**
     * Groups messages  with key field and r
     * @param <E> any subclass of {@link MessageGroup}
     * @param messages collection of messages.
     * @param factory concrete message group factory. 
     * @return collection of message group beans. Each of them will contain messages with same key.
     * @throws DgException
     */
    public static <E extends MessageGroup> Collection<E> groupByKey(Collection<Message> messages, MessageGroupFactory<E> factory) throws DgException{
        return groupByKey(messages, factory, null);
    }

    /**
     * Groups translations by keys, also sets scores for the groups.
     * Scores are some values used for sorting translations, e.g. lucene hit scores.
     * Which type of message group beans should be created is defined by factory parameter.
     * @param <E> {@link MessageGroup} or any its offspring. 
     * @param messages collection of messages which should be grouped by keys.
     * @param factory factory for creating message group beans.
     * @param scoresByKey map of scores by keys.
     * @return list of message group beans.
     * @throws DgException
     */
    public static <E extends MessageGroup> Collection<E> groupByKey(Collection<Message> messages, MessageGroupFactory<E> factory, Map<String, Float> scoresByKey) throws DgException{
        Map<String, E> groupByKey = new HashMap<String, E>(messages.size());
        for (Message message : messages) {
            //get group for current message (by key)
            E group = groupByKey.get(message.getKey());
            //if we do not have group yet
            if (null == group){
                //create new group for current message
                group = factory.createGroup(message.getKey(),message.getPrefix());
                //put it in map to find next time for same key
                groupByKey.put(message.getKey(), group);
            }
            //add current message to the group.
            group.addMessage(message);

            //need to work with scores?
            if (scoresByKey!=null){
                //get score for the message
                Float score = scoresByKey.get(message.getKey());
                if (score == null) score = new Float(0);
                //check if we need to update score value of the group
                if (score !=null && (group.getScore()==null || group.getScore().floatValue() < score.floatValue())){
                    group.setScore(score);
                }
            }
        }
        //return list of the groups
        return groupByKey.values();
    }

    /**
     * Message group object factory
     * @author Irakli Kobiashvili
     *
     * @param <E>
     */
    public static interface MessageGroupFactory<E extends MessageGroup>{
        public E createGroup(String key,String prefix);
    }

    /**
     * Factory of standard message groups.
     * @author Irakli Kobiashvili
     *
     */
    public static class StandardMessageGroupFactory implements MessageGroupFactory<MessageGroup>{
        @Override
        public MessageGroup createGroup(String key,String prefix) {
            return new MessageGroup(key,prefix);
        }
    }

    /**
     * Factory of patcher message groups.
     * @author Irakli Kobiashvili
     *
     */
    public static class PatcherMessageGroupFactory implements MessageGroupFactory<PatcherMessageGroup>{
        @Override
        public PatcherMessageGroup createGroup(String key, String prefix) {
            return new PatcherMessageGroup(key);
        }

    }

    /**
     * Merges groups messages with same hash code key resulting in no duplicates.
     * Used only during patching old translations using {@link PatcherMessageGroup}, with normal {@link MessageGroup} will not have any effect.
     * @param <E>
     * @param groups
     * @return
     */
    public static <E extends MessageGroup> Collection<E> removeDuplicateHashCodes(Collection<E> groups){
        Map<String, E> hashKeyMap = new HashMap<String, E>();
        if (groups != null){
            for (E nextGroup : groups) {
                E hashGroup = hashKeyMap.get(nextGroup.getHashKey());
                if (hashGroup == null){
                    hashKeyMap.put(nextGroup.getHashKey(), nextGroup);
                }else {
                    hashGroup.addMessagesFrom(nextGroup);
                }
            }
        }
        return hashKeyMap.values();
    }
    /**
     * Fixes all changes to translations in db.
     * @author Irakli Kobiashvili
     *
     */
    public static class TrnDb implements OperationFixer<Message>{

        private Session session = null;
        private Transaction tx = null;
        private TranslatorWorker worker = null;

        public TrnDb() throws DgException{
            this.session = PersistenceManager.getRequestDBSession();
            this.worker = TranslatorWorker.getInstance("");
        }

        @Override
        public void add(Message element) throws WorkerException {
            worker.save(element);
        }

        @Override
        public void update(Message element) {
            worker.update(element);
        }

        @Override
        public void delete(Message elemenet) throws WorkerException {
            worker.delete(elemenet);
        }

        @Override
        public void start() throws WorkerException {
            try {
//beginTransaction();
            } catch (HibernateException e) {
                throw new WorkerException("Cannot start trasaction to fix translation changes.");
            }
        }

        @Override
        public void end() throws WorkerException {
            if (tx!=null){
                try {
                    //tx.commit();
                } catch (HibernateException e) {
                    throw new WorkerException("Cannot fix buffered changes.",e);
                }
            }
        }

        @Override
        public void error() throws WorkerException {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e) {
                    throw new WorkerException("Cannot rollback transaction chnages",e);
                }
            }
        }


    }


    static {
        countryNameComparator = new Comparator () {
            public int compare(Object o1, Object o2) {
                TrnCountry c1 = (TrnCountry)o1;
                TrnCountry c2 = (TrnCountry)o2;

                return c1.getName().compareTo(c2.getName());
            }
        };

        localeNameComparator = new Comparator () {
            public int compare(Object o1, Object o2) {
                TrnLocale l1 = (TrnLocale)o1;
                TrnLocale l2 = (TrnLocale)o2;

                return l1.getName().compareTo(l2.getName());
            }
        };

        defaultMonths = new HashMap();
        String monthNames[] = new String[] {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

        for (int i = 0; i < monthNames.length; i++) {
            String code = monthNames[i].substring(0, 3).toLowerCase();
            defaultMonths.put("month:" + code, new TrnMonth(code, monthNames[i]));
        }
    }
    /**
     * Returnse message group store from session.
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ListChangesBuffer<String, Message> getBuffer(HttpSession session){
        String sessionKey = "amp.translations.newAdvancedMode.changesList";
        ListChangesBuffer<String, Message> result = (ListChangesBuffer<String, Message>)session.getAttribute(sessionKey);
        if (result == null){
            result = new ListChangesBuffer<String, Message>(new TrnUtil.MessageKeyLocaleResolver());
            session.setAttribute(sessionKey, result);
        }
        return result;
    }

    /**
     * Resolvs ony string key of Message.
     * @author Irakli Kobiashvili
     *
     */
    public static class MessageShortKeyResolver implements KeyResolver<String, Message>{
        public String resolveKey(Message element) {
            return element.getKey();
        }
    }
    /**
     * Resolves locale_key as key of the message
     * @author Irakli Kobiashvili
     *
     */
    public static class MessageKeyLocaleResolver implements KeyResolver<String, Message>{
        public String resolveKey(Message element) {
            return element.getLocale()+"_"+element.getKey();
        }
    }

    /**
     * Resolves locale_siteid_key as key of the message
     * @author Irakli Kobiashvili
     *
     */
    public static class MessageFullKeyResolver implements KeyResolver<String, Message>{
        public String resolveKey(Message element) {
            return element.getLocale()+"_"+element.getSiteId()+"_"+element.getKey();
        }
    }
    /**
     * Compares {@link MessageGroup} by score.
     * @author Irakli Kobiashvili
     *
     */
    public static class MsgGroupScoreComparator implements Comparator<MessageGroup>{
        @Override
        public int compare(MessageGroup m1, MessageGroup m2) {
            return m2.getScore().compareTo(m1.getScore());
        }
    }

    /**
     * Compares messages by weight of their locals.
     * If both message locales do not have weights 
     * then messages are compared simply by locale names.
     * If only one of the two compared locales has weight
     * then one with weight is always greater then one without weight.
     * This guarantees that locales with weight always appear in front.
     * @author Irakli Kobiashvili
     * @see TrnUtil#getLocaleWeight(String)
     * @see MessageGroup#getSortedMessages()
     *
     */
    public static class MessageLocaleWeightComparator implements Comparator<Message>{
        @Override
        public int compare(Message m1, Message m2) {
            Integer w1 = getLocaleWeight(m1.getLocale());
            Integer w2 = getLocaleWeight(m2.getLocale());
            if (w1==null && w2==null){
                //if both do not have weights then compare by string value 
                return m1.getLocale().compareTo(m2.getLocale());
            }else if (w1 == null || w2 == null){
                //if only one has weight than that one wins.
                if (w1!=null) return -1;
                if (w2!=null) return 1;
            }
            return w1.compareTo(w2);
        }
    }

    public static void setTrnPrefix(String prefix) {
        TLSUtils.getRequest().setAttribute(PREFIX, prefix);
    }

    /**
     * Returns the current workspace prefix
     * @return ws prefix
     */
    public static String getTrnPrefix(){
        if (TLSUtils.getRequest() != null && TLSUtils.getRequest().getSession() != null){
            HttpSession session = TLSUtils.getRequest().getSession();
            TeamMember tm = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            if (tm != null){
                AmpCategoryValue trnPrefix = tm.getWorkspacePrefix();
                if (trnPrefix != null){
                    String prefix = trnPrefix.getValue();
                    return prefix;
                }
            } else {
                Object prefix = TLSUtils.getRequest().getAttribute(PREFIX);
                return prefix != null ? prefix.toString() : null;
            }
        }
        return null;
    }

}
