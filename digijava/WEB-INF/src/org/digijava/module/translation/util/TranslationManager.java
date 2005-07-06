/*
 *   TranslationManager.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: May 9, 2004
 * 	 CVS-ID: $Id: TranslationManager.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.translation.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.form.TranslationForm;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import java.util.Collections;

public class TranslationManager {
    private static Logger logger = Logger.getLogger(TranslationManager.class);

    private final static String localeQuery;
    private final static String trnQuery;
    private final static String domainQuery;

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

            session = PersistenceManager.getSession();

            String queryString = localeQuery + rightPart;
            logger.debug(queryString);

            Query query = session.createQuery(queryString);
            query.setCacheable(true);

            List locales = query.list();

            queryString = trnQuery + rightPart;
            logger.debug(queryString);

            query = session.createQuery(queryString);
            query.setCacheable(true);

            List translations = query.list();

            queryString = domainQuery + rightPart;
            logger.debug(queryString);

            query = session.createQuery(queryString);
            query.setCacheable(true);
            query.setParameter(0, site.getId());

            List domains = query.list();

            int trnCounter = 0, domCounter = 0;
            Iterator iter = locales.iterator();
            while (iter.hasNext()) {
                Object[] localeRecord = (Object[]) iter.next();

                TranslationForm.TranslationInfo ti = new TranslationForm.
                    TranslationInfo();
                String langCode = (String) localeRecord[0];
                ti.setLangCode(langCode);

                String langName = (String) localeRecord[1];

                if (trnCounter < translations.size()) {
                    Object[] trnRow = (Object[]) translations.get(trnCounter);
                    if (langCode.equals( (String) trnRow[0])) {
                        langName = (String) trnRow[1];
                        trnCounter++;
                    }
                }

                String siteDomain = null, sitePath = null;
                if (domCounter < domains.size()) {
                    SiteDomainItem domRow = (SiteDomainItem) domains.get(domCounter);
                    if (langCode.equals(domRow.getCode())) {
                        siteDomain = domRow.getDomain();
                        sitePath = domRow.getPath();
                        domCounter++;
                    }
                }

                if (siteDomain != null) {
                    ti.setReferUrl(SiteUtils.getSiteURL(siteDomain,
                        sitePath, request.getScheme(),
                        request.getServerPort(), request.getContextPath()) +
                                   relativeUrl);
                }
                else {
                    ti.setReferUrl(DgUtil.getSiteUrl(defaultDomain, request) +
                                   "/translation/switchLanguage.do?code=" +
                                   langCode + "&rfr=" + relativeUrlEncode);
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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ", ex1);
            }
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
            "select new org.digijava.module.translation.util.SiteDomainItem(l.code, d.siteDbDomain, d.sitePath) from ").append(
            Locale.class.getName());
        domainQueryBuff.append(" l, ").append(SiteDomain.class.getName()).
            append(
            " d where d.site.id=? and l.code=d.language and l.available=true");

        localeQuery = localeQueryBuff.toString();
        trnQuery = trnQueryBuff.toString();
        domainQuery = domainQueryBuff.toString();
    }

}
