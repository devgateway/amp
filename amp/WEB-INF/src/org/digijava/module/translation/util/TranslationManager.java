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

package org.digijava.module.translation.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.dto.Language;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.entity.AdvancedTrnItem;
import org.digijava.module.translation.form.TranslationForm;
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class TranslationManager {
    private static Logger logger = Logger.getLogger(TranslationManager.class);

    private final static String localeQuery;
    private final static String trnQuery;
    private final static String unSecDomainQuery;
    private final static String secDomainQuery;
    private final static Timestamp expTimestamp = new Timestamp(10000);
    private final static Comparator LOCALE_COMPARATOR;

    public static void generateLanguages(boolean generateRoots,
                                         HttpServletRequest request,
                                         TranslationForm actionForm) throws
        DgException {
        ArrayList languages = new ArrayList();
        Session session = null;
        String relativeUrl;
        String currLangCode = RequestUtils.getNavigationLanguage(request).
            getCode();

        Site site = RequestUtils.getSite(request);
        SiteDomain defaultDomain = SiteUtils.getDefaultSiteDomain(site);

        if (generateRoots) {
            //logger.debug("Generating root pathes in the language switch");
            relativeUrl = "/";
        }
        else {
            //logger.debug("Generating full pathes in the language switch");
            String currentUrl = RequestUtils.getSourceURL(request);
            //logger.debug("Current URL is: " + currentUrl);

            String sitetUrl = DgUtil.getSiteUrl(RequestUtils.getSiteDomain(
                request), request);

            relativeUrl = currentUrl.substring(sitetUrl.length(),
                                               currentUrl.length());
            if (relativeUrl.trim().length() == 0) {
                relativeUrl = "/";
            }
        }

        String rightPart = getRightPart(site,
                                        DgUtil.isLocalTranslatorForSite(request));

        try {
            String relativeUrlEncode = URLEncoder.encode(relativeUrl, "UTF-8");
            actionForm.setReferrerParameter(relativeUrlEncode);

            session = PersistenceManager.getRequestDBSession();

            List<String[]> locales = getLocale(session, rightPart);

            String queryString  = trnQuery + rightPart;
            logger.debug(queryString);

            Query query  = session.createQuery(queryString);
            query.setCacheable(true);
            List<String[]> translations = query.list();

            if (request.isSecure()) {
                queryString = unSecDomainQuery + rightPart;
            } else {
                queryString = secDomainQuery + rightPart;
            }
            logger.debug(queryString);

            query = session.createQuery(queryString);
            query.setCacheable(true);
            query.setParameter(0, site.getId());

            List domains = query.list();

            int trnCounter = 0, domCounter = 0;
            for (String[] localeRecord:locales) {
                TranslationForm.TranslationInfo ti = new TranslationForm.TranslationInfo();
                String langCode = localeRecord[0];
                ti.setLangCode(langCode);
                String langName = localeRecord[1];

                if (trnCounter < translations.size()) {
                    String[] trnRow = translations.get(trnCounter);
                    if (langCode.equals(trnRow[0])) {
                        langName = trnRow[1];
                        trnCounter++;
                    }
                }

                String siteDomain = null, sitePath = null;
                if (domCounter < domains.size()) {
                    SiteDomainItem domRow = (SiteDomainItem) domains.get(
                        domCounter);
                    if (langCode.equals(domRow.getCode())) {
                        siteDomain = domRow.getDomain();
                        sitePath = domRow.getPath();
                        domCounter++;
                    }
                }

                if (siteDomain != null) {
                    ti.setReferUrl(SiteUtils.getSiteURL(siteDomain,
                        sitePath, request.getScheme(),
                        request.getServerPort(), request.getContextPath())/* +
                                   relativeUrl*/);
                }
                else {
                    ti.setReferUrl(
                                   "/translation/switchLanguage.do?code=" +
                                   langCode + "&rfr="/* + relativeUrlEncode*/);
                }

                if (currLangCode.equals(ti.getLangCode())) {
                    actionForm.setReferUrl(ti.getReferUrl());
                }

                ti.setKey("ln:" + langCode);
                ti.setLangName(langName);

                languages.add(ti);
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get Languages ", ex);
            throw new DgException("Unable to get Languages ", ex);
        }

        Collections.sort(languages);

        actionForm.setLanguages(languages);
    }

    private static String getRightPart(Site site, boolean trnLanguages) {
        StringBuffer sb = new StringBuffer(30);

        Collection codes;
        if (trnLanguages) {
            //logger.debug("Generating translation languages");
            codes = SiteUtils.getTransLanguages(site);
        }
        else {
            //logger.debug("Generating user languages");
            codes = SiteUtils.getUserLanguages(site);
        }

        if (codes != null) {
            sb.append(" and l.code in(");
            boolean first = true;

            Iterator iter = codes.iterator();
            while (iter.hasNext()) {
                Locale item = (Locale) iter.next();
                if (first) {
                    first = false;
                }
                else {
                    sb.append(',');
                }
                sb.append('\'').append(item.getCode()).append('\'');
            }
            sb.append(")");
        }

        sb.append(" order by l.code");
        String rightPart = sb.toString();
        return rightPart;
    }

    static {
        StringBuffer localeQueryBuff = new StringBuffer(255);
        StringBuffer trnQueryBuff = new StringBuffer(255);
        StringBuffer domainQueryBuff = new StringBuffer(255);

        localeQueryBuff.append("select l.code, l.name from ").append(Locale.class.
            getName());
        localeQueryBuff.append(" l where l.available=true");

        trnQueryBuff.append("select l.code, m.message from ").append(Locale.class.
            getName());
        trnQueryBuff.append(" l, ").append(Message.class.getName()).
            append(" m where l.messageLangKey=m.key and l.code=m.locale and m.siteId='0' and l.available=true");

        domainQueryBuff.append(
            "select new org.digijava.module.translation.util.SiteDomainItem(l.code, d.siteDbDomain, d.sitePath) from ").
            append(
                Locale.class.getName());
        domainQueryBuff.append(" l, ").append(SiteDomain.class.getName()).
            append(
                " d where d.site.id=? and l.code=d.language and l.available=true");

        localeQuery = localeQueryBuff.toString();
        trnQuery = trnQueryBuff.toString();
        unSecDomainQuery = domainQueryBuff.toString() + " and (d.enableSecurity is null or d.enableSecurity=false)";
        secDomainQuery = domainQueryBuff.toString() + " and (d.enableSecurity is null or d.enableSecurity=true)";
    }

    public static void expireTranslation(String key) throws DgException {
        try {
            TranslatorWorker.getInstance(key).markKeyExpired(key);
        }
        catch (WorkerException ex) {
            throw new DgException(ex);
        }
    }

    public static void unExpireTranslation(String key) throws DgException {
        try {
            TranslatorWorker.getInstance(key).markKeyUnexpired(key);
        }
        catch (WorkerException ex) {
            throw new DgException(ex);
        }
    }

    public static List getAdvancedTranslations(long siteId, String srcLocale,
                                               String destLocale, String prefix,
                                               boolean showExpired,
                                               String keyPattern,
                                               String srcMsgPattern,
                                               String destMsgPattern) throws
        DgException {
        ArrayList result = new ArrayList();

        // Trim search patterns
        if (keyPattern != null && keyPattern.trim().length() != 0) {
            keyPattern = keyPattern.trim().toLowerCase();
        } else {
            keyPattern = null;
        }

        if (srcMsgPattern != null && srcMsgPattern.trim().length() != 0) {
            srcMsgPattern = srcMsgPattern.trim().toLowerCase();
        } else {
            srcMsgPattern = null;
        }

        if (destMsgPattern != null && destMsgPattern.trim().length() != 0) {
            destMsgPattern = destMsgPattern.trim().toLowerCase();
        } else {
            destMsgPattern = null;
        }

        List keys = getAdvancedTranslationKeys(siteId, srcLocale, prefix,
                                               showExpired, keyPattern);

        Long rootSiteId = null;
        if (siteId != 0) {
            SiteCache siteCache = SiteCache.getInstance();
            Site site = siteCache.getSite(new Long(siteId));

            rootSiteId = site == null ? null :
                siteCache.getRootSite(site).getId();
        }

        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String oneKey = ( (String) iter.next()).trim();
            AdvancedTrnItem item = new AdvancedTrnItem();

            try {
                TranslationType srcItem = getTrn(siteId, rootSiteId, oneKey,
                                                 srcLocale, 0);
                TranslationType destItem = getTrn(siteId, rootSiteId, oneKey,
                                                  destLocale, srcItem.timestamp-1);
                boolean addItem = true;
                if (keyPattern != null) {
                    addItem = oneKey.toLowerCase().indexOf(keyPattern) >= 0;
                }
                if (!addItem) {
                    continue;
                }

                if (srcMsgPattern != null) {
                    addItem = srcItem.trnText != null &&
                        srcItem.trnText.toLowerCase().indexOf(srcMsgPattern) >= 0;
                }
                if (!addItem) {
                    continue;
                }

                if (destMsgPattern != null) {
                    addItem = destItem.trnText != null &&
                        destItem.trnText.toLowerCase().indexOf(destMsgPattern) >= 0;
                }
                if (!addItem) {
                    continue;
                }

                item.setKey(oneKey);
                item.setSourceType(srcItem.trnType);
                item.setSourceValue(DgUtil.dehtmlize(srcItem.trnText, true));
                item.setTargetType(destItem.trnType);
                item.setTargetValue(destItem.trnText);
                item.setSrcChanged(srcItem.timestamp > destItem.timestamp);
                result.add(item);
            }
            catch (WorkerException ex) {
                throw new DgException(ex);
            }
        }
        Collections.sort(result);
        return result;
    }

    private static TranslationType getTrn(Long siteId, Long rootSiteId,
                                          String oneKey, String srcLocale,
                                          long emptyTimestamp) throws
        WorkerException {
        TranslationType item = new TranslationType();
        TranslatorWorker trnWorker = TranslatorWorker.getInstance(oneKey);

        Message srcMsg = null;
        srcMsg = trnWorker.getByKey(oneKey, srcLocale, siteId);
        if (srcMsg != null) {
            if (siteId != 0) {
                item.trnType = AdvancedTrnItem.LOCAL_TRN;
            }
            else {
                item.trnType = AdvancedTrnItem.GLOBAL_TRN;
            }
        }
        else {
            if (rootSiteId != null) {
                srcMsg = trnWorker.getByKey(oneKey, srcLocale, rootSiteId);
                if (srcMsg != null) {
                    item.trnType = AdvancedTrnItem.GROUP_TRN;
                }
                else {
                    srcMsg = trnWorker.getByKey(oneKey, srcLocale, 0L);
                    if (srcMsg != null) {
                        item.trnType = AdvancedTrnItem.GLOBAL_TRN;
                    }
                }
            }
            item.trnType = AdvancedTrnItem.GLOBAL_TRN;
        }
        if (srcMsg == null) {
            item.trnType = AdvancedTrnItem.LOCAL_TRN;
            item.trnText = null;
            item.timestamp = emptyTimestamp;
        }
        else {
            item.trnText = srcMsg.getMessage();
            item.timestamp = srcMsg.getCreated() == null ? emptyTimestamp :
                srcMsg.getCreated().getTime();
        }
        return item;
    }

    private static List getAdvancedTranslationKeys(long siteId,
        String srcLocale, String prefix, boolean showExpired,
        String keyPattern) throws
        DgException {

        StringBuffer queryHql = new StringBuffer(512);
        queryHql.append("select distinct msg.key from ").
            append(Message.class.getName()).append(" msg where ");
        queryHql.append("msg.locale=:localeId and msg.key is not null and length(msg.key) != 0 ");
        if (prefix != null) {
            queryHql.append("and msg.key like :prefix ");
        }
        if (keyPattern != null) {
            queryHql.append("and lower(msg.key) like :keyPattern ");
        }

        if (showExpired) {
            queryHql.append(
                "and msg.created is not null and msg.created <=:stamp ");
        }
        else {
            queryHql.append("and (msg.created is null or (msg.created >:stamp and msg.created is not null)) ");
        }
        if (siteId == 0) {
            queryHql.append(" and msg.siteId='0'");
        }
        else {
            SiteCache siteCache = SiteCache.getInstance();
            Site site = siteCache.getSite(new Long(siteId));

            Site rootSite = site == null ? null :
                siteCache.getRootSite(site);
            if (rootSite == null) {
                queryHql.append(" and msg.siteId in ('0','").append(siteId).
                    append("')");
            }
            else {
                queryHql.append(" and msg.siteId in ('0','").append(siteId).
                    append("','").append(rootSite.getId()).append("')");
            }
        }
        //queryHql.append(" order by msg.key");
        if (logger.isDebugEnabled()) {
            logger.debug("Executing query: " + queryHql.toString());
        }

        Session session = PersistenceManager.getRequestDBSession();
        List keys = null;
        try {
            Query query = session.createQuery(queryHql.toString());

            query.setParameter("localeId", srcLocale);
            if (prefix != null) {
                query.setString("prefix", prefix + ":%");
            }
            query.setTimestamp("stamp", expTimestamp);

            if (keyPattern != null) {
                query.setString("keyPattern", "%" + keyPattern + "%");
            }

            keys = query.list();
        }
        catch (HibernateException ex) {
            throw new DgException(ex);
        }
        return keys;
    }

    public static List getLanguagesToDisplay(Site currentSite, Locale locale,
                                             boolean addAll) throws DgException {
        Set languages = SiteUtils.getTransLanguages(currentSite);
        return getTranslatedLanguageList(languages, locale, addAll);
    }

    public static List getTreeLanguagesToDisplay(Site currentSite, Locale locale,
                                             boolean addAll) throws DgException {
        Set languages = getTreeLanguages(currentSite);
        return getTranslatedLanguageList(languages, locale, addAll);
    }

    private static Set getTreeLanguages(Site currentSite) throws DgException {
        TreeSet result = new TreeSet(LOCALE_COMPARATOR);
        result.addAll(SiteUtils.getTransLanguages(currentSite));

        if (currentSite.getParentId() != null) {
            Site parentSite = SiteCache.lookupById(currentSite.getParentId());
            result.addAll(getTreeLanguages(parentSite));
        }

        return result;
    }

    private static List getTranslatedLanguageList(Set languages, Locale locale,
                                             boolean addAll) throws DgException {
        //get translation languages for Site
        HashMap translations = new HashMap();
        String localeCode = "en";

        if (locale != null) {
            localeCode = locale.getCode();
        }

        Iterator iterator = TrnUtil.getLanguages(localeCode).iterator();
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
        if (addAll) {
            sortedLanguages.add(0,
                                new TrnLocale(TranslateObject.LOCALE_CODE_ALL,
                                              "all"));
        }
        return sortedLanguages;
    }

    public static ArrayList getPermissions(Principal principal) {
        ArrayList result = new ArrayList();
        PermissionCollection permissions = DigiSecurityManager.
            getPermissions(
                principal);
        if (permissions != null) {
            Enumeration permissionEnum = permissions.elements();
            while (permissionEnum.hasMoreElements()) {
                Permission permission = (Permission) permissionEnum.
                    nextElement();
                if (permission instanceof TranslatePermission) {

                    TranslatePermission tp = (TranslatePermission)
                        permission;
                    TranslateObject to = tp.getTranslateId();

                    if (to != null) {
                        TranslationPermissionsForm.PermissionInfo
                            pi = new
                            TranslationPermissionsForm.
                            PermissionInfo();

                        pi.setId(0);
                        if (to.getSiteId() != null) {
                            pi.setSiteId(to.getSiteId());
                        }
                        else {
                            pi.setSiteId(new Long(0));
                        }
                        if (to.getLocaleId() != null) {
                            pi.setLocaleId(to.getLocaleId());
                        }
                        else {
                            pi.setLocaleId(TranslateObject.LOCALE_CODE_ALL);
                        }

                        result.add(pi);
                    }
                    else {
                        TranslationPermissionsForm.PermissionInfo pi = new
                            TranslationPermissionsForm.
                            PermissionInfo();
                        pi.setSiteId(new Long(0));
                        pi.setLocaleId(TranslateObject.LOCALE_CODE_ALL);
                        result.add(pi);
                    }
                }
            }
        }
        return result;
    }

    public static List getSiteInfos(Subject subject, Site currentSite) {
        ArrayList result = new ArrayList();

        if (DigiSecurityManager.isGlobalAdminSubject(subject)) {
            result.add(new TranslationPermissionsForm.SiteInfo(
                new Long(-1),
                "all"));
            result.add(new TranslationPermissionsForm.SiteInfo(
                new Long(0),
                TranslateObject.SITE_NAME_GLOBAL));
        }
        TranslationPermissionsForm.SiteInfo si = new
            TranslationPermissionsForm.
            SiteInfo();

        si.setId(currentSite.getId());
        si.setName(currentSite.getName());
        result.add(si);
        return result;
    }

    public static boolean isTranslationPermitted(Subject subject, long siteId,
                                                 Collection allLanguages) {
        Iterator it = allLanguages.iterator();

        while (it.hasNext()) {
            TrnLocale locale = (TrnLocale) it.next();
            if (TranslateSecurityManager.checkPermission(subject,
                siteId, locale.getCode())) {
                return true;
            }
        }
        return false;
    }

    private static class TranslationType {
        int trnType;
        String trnText;
        long timestamp;
    }

    static {
        LOCALE_COMPARATOR = new Comparator() {
            public int compare(Object o1,Object o2) {
                Locale l1 = (Locale)o1;
                Locale l2 = (Locale)o2;

                return l1.getCode().compareTo(l2.getCode());
            }
        };
    }
    
    
    public static List<String[]> getLocale(Session session) {
        String righPart = getRightPart(RequestUtils.getSite(TLSUtils.getRequest()), DgUtil.isLocalTranslatorForSite(TLSUtils.getRequest()));
        return getLocale(session,righPart);
    }
    
    /**
     * 
     * @param session
     * @param rightPart
     * @return List[locale.code, locale.name]
     */
    public static List<String[]> getLocale(Session session, String rightPart) {
        String queryString = localeQuery + rightPart;
        logger.debug(queryString);

        Query query = session.createQuery(queryString);
//        query.setCacheable(true);

        List<Object[]> locales = query.list();
        List<String[]> res = new ArrayList<>();
        for(Object[] entry:locales)
            res.add(new String[] {(String) entry[0], (String) entry[1]});
        
        return res;
    }
    
    public static List<Language> getAmpLanguages() {
        Session session = PersistenceManager.getSession();
        
        String rightPart = getRightPart(RequestUtils.getSite(TLSUtils.getRequest()),
                DgUtil.isLocalTranslatorForSite(TLSUtils.getRequest()));
        
        String localeQuery = "FROM " + Locale.class.getName() + " l WHERE l.available = true" + rightPart;
        
        Query query = session.createQuery(localeQuery);
        query.setCacheable(true);
        List<Locale> locales = query.list();
        
        List<Language> languages = locales.stream()
                .map(l -> new Language(l.getCode(), l.getName(), l.getLeftToRight()))
                .collect(Collectors.toList());
        
        return languages;
    }
    
}
