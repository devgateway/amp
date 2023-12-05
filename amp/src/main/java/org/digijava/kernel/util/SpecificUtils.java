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

package org.digijava.kernel.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OracleLocale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * This class was created to concentrate <b>ALL</b> utillities, needed for
 * compatibility with old TCL-based system, etc.<br>
 * I believe that <i>some day</i> this class will become deprecated and we will
 * safely remove it from the system.
 * @author Mikheil Kapanadze
 */
public class SpecificUtils {

    private static final Logger logger = Logger.getLogger(SpecificUtils.class);

    public static String getDgMarketDomain(String fullDomain) {
        int pos = fullDomain.indexOf(".dgmarket.");
        if (pos < 0) {
            return null;
        } else {
            return fullDomain.substring(pos);
        }
    }

    /**
     * Determines, are we surfing on DgMarket site or not
     * @param request HttpServletRequest the current request
     * @return true, if we're surfing on DgMarket, false - if not
     */
    public static boolean isDgMarket(HttpServletRequest request) {
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);
        if (currDomain == null) {
            return false;
        } else {
            return getDgMarketDomain(currDomain.getSiteDomain()) != null;
        }
    }

    /**
     * Sets language cookie for DgMarket
     * @param locale Locale
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void setDgMarketLangCookie(Locale locale,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        OracleLocale oracleLocale = null;
        try {
            oracleLocale = getOracleLocale(locale);
        }
        catch (DgException ex) {
            logger.error("Unable to get oracle locale for " + locale.getCode() +
                         "(" + locale.getName() + ")", ex);
            return;
        }
        String localeAbbrev;
        if (oracleLocale == null) {
            localeAbbrev = "us"; // English
        }
        else {
            localeAbbrev = oracleLocale.getOracleLocale();
        }

        SiteDomain currDomain = RequestUtils.getSiteDomain(request);
        String domain = null;
        if (currDomain == null) {
            logger.error("Site is not DgMarket site");
            return;
        } else {
            domain = getDgMarketDomain(currDomain.getSiteDomain());
        }

        Cookie cookie = new Cookie("locale_abbrev_pref", localeAbbrev);
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    /**
     * Determines language from DgMarket cookie/browser settings/Root Site's
     * navigation language or returns English
     * @param request HttpServletRequest
     * @return navigation language
     */
    public static Locale getDgMarketLangFromRequest(HttpServletRequest request) {
        Locale language = null;
        Site currentSite = RequestUtils.getSite(request);
        if (currentSite != null) {
            Site rootSite = DgUtil.getRootSite(currentSite);

            // Determine language using cookies
            try {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        if (cookies[i].getName().equals("locale_abbrev_pref")) {
                            logger.debug("locale_abbrev_pref = " + cookies[i].getValue());
                            Locale cookieLocale = getLocale(cookies[i].getValue());
                            if (cookieLocale != null) {
                                language = DgUtil.getSupportedLanguage(
                                    cookieLocale.
                                    getCode(),
                                    currentSite,
                                    DgUtil.isLocalTranslatorForSite(request));
                            } else {
                                logger.debug("Unable to determine correct locale, which corresponds to " + cookies[i].getValue());
                            }
                            break;
                        }
                    }
                }
            }
            catch (DgException ex) {
                logger.warn("Unable to get locale from cookie", ex);
            }

            if (language != null) {
                logger.debug("DgMarket locale, determined from cookie is " +
                             language.getCode() + "(" +
                             language.getName() + ")");
                return language;
            }
            // Determine list of accepted languages from request

            // request.getLocales() contains at least one value: if
            // Accept-Language was not set in header, container puts server's
            // default locale there. That's why we need this check
            if (request.getHeader("Accept-Language") != null) {
                Enumeration enumLocales = request.getLocales();
                while (enumLocales.hasMoreElements()) {
                    java.util.Locale locale = (java.util.Locale) enumLocales.
                        nextElement();

                    language = DgUtil.getSupportedLanguage(locale.getLanguage(),
                        currentSite,
                        DgUtil.isLocalTranslatorForSite(request));
                    if (language != null) {
                        break;
                    }

                }
            }

            if (language != null) {
                logger.debug("DgMarket locale, determined from browser setting is " +
                             language.getCode() + "(" +
                             language.getName() + ")");
                return language;
            }

            // Determine language from root site
            if (rootSite.getDefaultLanguage() != null) {
                language = DgUtil.getSupportedLanguage(rootSite.
                    getDefaultLanguage().
                    getCode(),
                    currentSite,
                    DgUtil.isLocalTranslatorForSite(request));
            }
            if (language != null) {
                logger.debug("DgMarket locale, determined from root site is " +
                             language.getCode() + "(" +
                             language.getName() + ")");
                return language;
            }
            // Return english language
            language = new Locale();
            language.setCode(java.util.Locale.ENGLISH.getLanguage());
            language.setName(java.util.Locale.ENGLISH.getDisplayName());

        }

        logger.debug("DgMarket locale is " + language.getCode() + "(" +
                     language.getName() + ")");
        return language;

    }

    private static OracleLocale getOracleLocale(Locale locale) throws
        DgException {
        OracleLocale result = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from " +
                                          OracleLocale.class.getName() +
                                          " rs where rs.locale.code=:code");
            q.setParameter("code", locale.getCode(), StringType.INSTANCE);
            q.setCacheable(true);
            q.setCacheRegion(SpecificUtils.class.getName() + ".queries");
            for (Object o : q.list()) {
                result = (OracleLocale) o;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get language list from database", ex);
            throw new DgException(
                "Unable to get language list from database", ex);
        }

        return result;
    }

    private static Locale getLocale(String oracleLocaleKey) throws DgException {
        Locale result = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            OracleLocale oraLocale = (OracleLocale) session.load(OracleLocale.class,
                oracleLocaleKey);
            result = oraLocale.getLocale();
        }
        catch (ObjectNotFoundException ex) {
            logger.warn("No locale was found for oracle key " + oracleLocaleKey);
        }
        catch (Exception ex) {
            logger.debug("Unable to get language list from database", ex);
            throw new DgException(
                "Unable to get language list from database", ex);
        }

        return result;

    }
}
