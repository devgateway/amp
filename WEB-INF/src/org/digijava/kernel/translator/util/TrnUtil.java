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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.entity.PatcherMessageGroup;
import org.hibernate.Query;
import org.hibernate.Session;

public class TrnUtil {

    private static final String CACHE_REGION = "org.digijava.kernel.translator.util.TranslatorUtil.Query";

    private static Logger logger = Logger.getLogger(TrnUtil.class);

    private static final HashMap defaultMonths;

    public static final Comparator countryNameComparator;
    public static final Comparator localeNameComparator;


    /**
     * Returns collection of TrnLocale objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnLocale objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnLocale
     */
    public static Collection getLanguages(String isoLanguage) throws DgException {
        HashSet languages = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select l.code, msg.message from " +
                Locale.class.getName() + " l, " +
                Message.class.getName() + " msg " +
                " where msg.key=l.messageLangKey and msg.siteId='0' and l.available=true and msg.locale=:locale"
                );
            query.setString("locale", isoLanguage);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            languages = new HashSet();

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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
        }
        return languages;
    }

    /**
     * Returns list of countries sorted according spcified langiage.
     * @param language ISO code
     * @return sorted countries
     */
    public static Collection getSortedCountries(String isoLanguage) throws DgException {
      Collection retVal = null;
      Locale locale = new Locale(isoLanguage, "");

      Collection cuntries = getCountries (isoLanguage);

      TranslationCallback countryTranslateCallback = new TranslationCallback() {
        public String getSiteId(Object o) {
            return null;
        }

        public String getTranslationKey(Object o) {
            TrnCountry country = (TrnCountry) o;
            return "cn:" + country.getIso();
        }

        public String getDefaultTranslation(Object o) {
          TrnCountry country = (TrnCountry) o;
          return country.getName();
        }
      };
      retVal = sortByTranslation(cuntries, locale, countryTranslateCallback);
      return retVal;
    }



    /**
     * Returns collection of TrnCountry objects, translated to the given language
     * @param isoLanguage ISO code of the language
     * @return collection of TrnCountry objects
     * @throws DgException
     * @see org.digijava.kernel.translator.util.TrnCountry
     */
    public static Collection getCountries(String isoLanguage) throws DgException {
        HashSet countries = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select c.iso, msg.message from " +
                Country.class.getName() + " c, " +
                Message.class.getName() + " msg " +
                " where msg.key=c.messageLangKey and msg.siteId='0' and c.available=true and msg.locale=:locale"
                );
            query.setString("locale", isoLanguage);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            countries = new HashSet();

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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
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
    public static Collection getMonths(String isoLanguage) throws DgException {
        HashSet months = null;

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery(
                "select msg.key, msg.message from " +
                Message.class.getName() + " msg " +
                " where msg.key like \'month%\' and msg.siteId='0' and msg.locale=:locale"
                );
            query.setString("locale", isoLanguage);
            query.setCacheable(true);
            query.setCacheRegion(CACHE_REGION);
            months = new HashSet();

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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
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
    public static Collection getSortedUserLanguages(HttpServletRequest request) throws
        DgException {
            Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
            HashMap translations = new HashMap();
            Iterator iterator = null;
            iterator = TrnUtil.getLanguages(RequestUtils.
                                            getNavigationLanguage(request).
                                            getCode()).iterator();
            while (iterator.hasNext()) {
                TrnLocale item = (TrnLocale) iterator.next();
                translations.put(item.getCode(), item);
            }
            //sort languages
            List sortedLanguages = new ArrayList();
            iterator = languages.iterator();
            while (iterator.hasNext()) {
                Locale item = (Locale) iterator.next();
                sortedLanguages.add(translations.get(item.getCode()));
            }
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
                  site = SiteCache.getInstance().getSite(siteId);
                }

                try {
                  Message trnMess = null;
                  if (site != null) {
                    trnMess = trnWork.getByBody(defTrans, locale.getCode(),
                                          String.valueOf(site.getId()));
                    if (trnMess == null && groupTranslation && site.getParentId() != null) {
                      Site root = SiteCache.getInstance().getRootSite(site);
                      trnMess = trnWork.getByBody(defTrans, locale.getCode(),
                                            String.valueOf(root.getId()));
                    }
                  } else {
                    trnMess = trnWork.getByBody(defTrans, locale.getCode(), "0");
                  }
                    if (trnMess == null) {
                        trnString = defTrans;
                    }
                    else {
                        trnString = trnMess.getMessage();
                    }
                }
                catch (WorkerException ex) {
                    logger.debug("Unable translation for specified key ", ex);
                    throw new DgException(
                        "Unable translation for specified key ", ex);
                }
                Object[] sortObj = new Object[] {
                    trnString, obj};
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
    	Map<String, E> groupByKey = new HashMap<String, E>(messages.size());
    	for (Message message : messages) {
			E group = groupByKey.get(message.getKey());
			if (null == group){
				group = factory.createGroup(message.getKey());
				groupByKey.put(message.getKey(), group);
			}
			group.addMessage(message);
		}
    	return groupByKey.values();
    }
    
    /**
     * Message group object factory
     * @author Irakli Kobiashvili
     *
     * @param <E>
     */
    public static interface MessageGroupFactory<E extends MessageGroup>{
    	public E createGroup(String key);
    }
    
    /**
     * Factory of standard message groups.
     * @author Irakli Kobiashvili
     *
     */
    public static class StandardMessageGroupFactory implements MessageGroupFactory<MessageGroup>{
		@Override
		public MessageGroup createGroup(String key) {
			return new MessageGroup(key);
		}
    }
    
    /**
     * Factory of patcher message groups.
     * @author Irakli Kobiashvili
     *
     */
    public static class PatcherMessageGroupFactory implements MessageGroupFactory<PatcherMessageGroup>{
		@Override
		public PatcherMessageGroup createGroup(String key) {
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
     * Resolvs ony string key of Message.
     * @author Irakli Kobiashvili
     *
     */
    public static class MessageShortKeyResolver implements KeyResolver<String, Message>{
		public String resolveKey(Message element) {
			return element.getKey();
		}
    }

}
