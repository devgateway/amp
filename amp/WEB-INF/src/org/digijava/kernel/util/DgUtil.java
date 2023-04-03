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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.request.service.IgnoredAgentsService;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DgUtil {
    
    private static Logger logger = I18NHelper.getKernelLogger(DgUtil.class);

    private static final int FASTSPLIT_MAXSIZE = 2048;

    private static final Comparator userComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            return ( (User) o1).getId().compareTo( ( (User) o2).getId());
        }

    };

    /**
     * Returns current site domain, using which the request was performed, or
     * null, if request does not contain site information
     * @param request HttpServletRequest object
     * @return current site domain
     * @deprecated use RequstUtils.getSiteDomain() instead
     */
    public static SiteDomain getSiteDomain(HttpServletRequest request) {
        return RequestUtils.getSiteDomain(request);
        //return (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
    }

    /**
     * Returns language for current request.
     * @param request HttpServletRequest object
     * @return language for current request
     * @deprecated use RequestUtils.getNavigationLanguage instead
     */
    public static Locale getNavigationLanguage(HttpServletRequest request) {
        return RequestUtils.getNavigationLanguage(request);
        //return (Locale) request.getAttribute(Constants.NAVIGATION_LANGUAGE);
    }

    /**
     * Returns root site of the branch into site three for current site. This
     * may be the same as source site, if it does not have parent site.
     * @param site Site
     * @return root site
     */
    public static Site getRootSite(Site site) {
        Site rootSite = SiteCache.getInstance().getRootSite(site);
        return rootSite;
    }

    /**
     * Returns true, if user is logged in and has TRANSLATE right on the
     * current site
     * @param session
     * @return
     * @deprecated use isLocalTranslatorForSite() instead
     */
    public static boolean isSiteTranslator(HttpServletRequest request) {
        return isLocalTranslatorForSite(request);
    }

    public static Locale getSupportedLanguage(String langCode, Site site,
                                              boolean isTranslator) {
        Locale language = null;
        if (isTranslator) {
            for (Object o : SiteCache.getInstance().getTranslationLanguages(
                    site)) {
                Locale item = (Locale) o;
                if (item.getCode().equals(langCode)) {
                    language = item;
                    break;
                }
            }
        }
        else {
            for (Locale item : SiteCache.getInstance().getUserLanguages(site)) {
                if (item.getCode().equals(langCode)) {
                    language = item;
                    break;
                }
            }

        }

        return language;
    }

    protected static void saveUserLanguagePreferences(HttpServletRequest request, Locale language) {
        saveUserLanguagePreferences(null, request, language);
    }

    public static void saveUserLanguagePreferences(User user, HttpServletRequest request, Locale language) {
        if (user == null) {
            user = RequestUtils.getUser(request);
        }
        if (user == null) {
            return;
        }
        org.hibernate.Session session = PersistenceManager.getSession();
        Site rootSite = getRootSite(RequestUtils.getSite(request));
        UserLangPreferences preferences;

        try {
            UserPreferencesPK key = new UserPreferencesPK(user, rootSite);
            preferences = (UserLangPreferences) session.load(UserLangPreferences.class, key);

            logger.debug("Updating user language preferences");
            preferences.setNavigationLanguage(language);
            session.update(preferences);
        }
        catch (ObjectNotFoundException ex2) {
            preferences = createDefaultLangPreferences(language, rootSite, user);
            session.save(preferences);
        }
        // put preferences back
        user.setUserLangPreferences(preferences);
    }


    protected static void saveWorkspaceLanguagePreferences(HttpServletRequest request, Locale language) {
        TeamMember tm = (TeamMember) request.getSession(true).getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        if (tm == null || tm.getAppSettings() == null || tm.getMemberId() == null)
            return;
        if (language.getCode().equals(tm.getAppSettings().getLanguage()))
            return;
        tm.getAppSettings().setLanguage(language.getCode());
        AmpTeamMember atm = (AmpTeamMember) PersistenceManager.getSession().get(AmpTeamMember.class, tm.getMemberId());
        AmpTeam team = atm.getAmpTeam();
        PersistenceManager.getSession().createQuery("update " + AmpApplicationSettings.class.getName() + " aas SET language='" + language.getCode() + "' where aas.team.ampTeamId = " + team.getAmpTeamId()).executeUpdate();
    }

    public static void saveWorkspaceLanguagePreferences(HttpServletRequest request, AmpTeam ampTeam, User user) {
        AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeam.getAmpTeamId());

        UserPreferencesPK key = new UserPreferencesPK(user, RequestUtils.getSite(request));
        org.hibernate.Session session = PersistenceManager.getSession();
        UserLangPreferences preferences = (UserLangPreferences) session.load(UserLangPreferences.class, key);

        if (preferences.getNavigationLanguage() != null) {
            ampAppSettings.setLanguage(preferences.getNavigationLanguage().getCode());
        }
        session.saveOrUpdate(ampAppSettings);
    }
    
    /**
     *
     * @param language
     * @param request
     * @param response
     */
    public static void switchLanguage(Locale language,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        logger.debug("Switching to language: " + language.getCode() + "(" +
                     language.getName() + ")");
        if (SpecificUtils.isDgMarket(request)) {
            SpecificUtils.setDgMarketLangCookie(language, request, response);
        }

        Site currentSite = RequestUtils.getSite(request);
        if (currentSite != null) {            
            if (getSupportedLanguage(language.getCode(), currentSite,
                                     isLocalTranslatorForSite(request)) == null) {
                // Language is not supported
                logger.warn("Language " + language.getCode() + "(" +
                            language.getName() + ") is not supported");
                DgUtil.setUserLanguage(request, response);
                return;
            }
           
            saveUserLanguagePreferences(request, language);
            saveWorkspaceLanguagePreferences(request, language);
            request.setAttribute(Constants.NAVIGATION_LANGUAGE, language);
            request.getSession().setAttribute(Constants.NAVIGATION_LANGUAGE, language);
            setLanguageCookie(language, request, response);
        }
    }
    
    public static void setSessionLanguage(HttpServletRequest request, HttpServletResponse response, Locale language) {
        request.setAttribute(Constants.NAVIGATION_LANGUAGE, language);
        if (request.getSession() != null)
            request.getSession().setAttribute(Constants.NAVIGATION_LANGUAGE, language);
        setLanguageCookie(language, request, response);
    }
    
    private static void setLanguageCookie(Locale language, HttpServletRequest request, HttpServletResponse response) {
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);
        Cookie cookie = new Cookie("digi_language", language.getCode());
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setDomain(currDomain.getSiteDomain());
        String cookiePath = request.getContextPath() +
            (currDomain.getSitePath() == null ? "" :
             currDomain.getSitePath());
        cookiePath = cookiePath.trim();
        if (cookiePath.length() == 0) {
            cookiePath = "/";
        }
        cookie.setPath(cookiePath);
        response.addCookie(cookie);
    }

    /**
     * Create default language preferences for the given user on the site
     * @param language navigation language. if null - site's default language
     * @param rootSite Site
     * @param user User
     * @return UserLangPreferences object
     */
    private static UserLangPreferences createDefaultLangPreferences(Locale
        language, Site rootSite, User user) {

        Locale navigLanguage = language;
        if (navigLanguage == null) {
            navigLanguage = SiteCache.getInstance().getDefaultLanguage(rootSite);
        }
        UserLangPreferences preferences = new UserLangPreferences(user,
            rootSite);
        preferences.setNavigationLanguage(navigLanguage);
        preferences.setAlertsLanguage(user.getRegisterLanguage());

        preferences.setContentLanguages(new HashSet(SiteCache.getInstance().
            getUserLanguages(rootSite)));

        return preferences;
    }

//    /**
//     * Rreturns the current navigation language
//     * <br /><i><b>Implementor's Note:</b> Some code by other teams used this
//     * method to determine the current navigation language. This was not
//     * correct, but it worked. The problem appeared when tests were done on
//     * domains with assigned navigation languages. This function was
//     * re-implemented and the old logic was moved to
//     * <code>getLanguageFromRequest()</code></i>
//     * @param request HttpServletRequest
//     * @return Locale the current navigation language
//     * @deprecated use RequestUtils.getNavigationLanguage() instead
//     */
//    public static Locale determineLanguage(HttpServletRequest request) {
//        return RequestUtils.getNavigationLanguage(request);
//    }

    /**
     * This method implements business logic, which determines language based
     * on current site, user preferences, browser and cookie settings.
     * @param request
     * @return
     */
    private static Locale getLanguageFromRequest(HttpServletRequest request) {
        Locale language = null;
        // synchronize user settings in request
        synchronizeLangPreferences(request);

        if (SpecificUtils.isDgMarket(request)) {
            return SpecificUtils.getDgMarketLangFromRequest(request);
        }

        Site currentSite = RequestUtils.getSite(request);
        if (currentSite != null) {
            Site rootSite = getRootSite(currentSite);
            User user = RequestUtils.getUser(request);

            if (user != null) {
                if (user.getUserLangPreferences() != null) {
                    language = getSupportedLanguage(user.getUserLangPreferences().
                        getNavigationLanguage().getCode(),
                        currentSite,
                        isLocalTranslatorForSite(request));

                    if (language != null) {
                        logger.debug(
                            "Language, determined from user preferences (site#" +
                            rootSite.getId() + ", user#" + user.getId() +
                            ") is: " + language.getCode());
                        return language;
                    }
                }

            }
            
            if(request.getParameter("language")!=null){
                language = getSupportedLanguage(request.getParameter("language"),
                        currentSite,
                        isLocalTranslatorForSite(request));
            }
            
            if (language != null) {
                logger.debug("Language, determined from request parameter: " +
                             language.getCode());
                return language;
            }
            
            // Determine language using cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("digi_language")) {
                        language = getSupportedLanguage(cookies[i].getValue(),
                            currentSite,
                            isLocalTranslatorForSite(request));
                        if (language != null) {
                            break;
                        }
                    }
                }
            }

            if (language != null) {
                logger.debug("Language, determined from cookies is: " +
                             language.getCode());
                return language;
            }
            
            //using session attribute
            if (request.getSession() != null){
                language = (Locale)request.getSession().getAttribute(Constants.NAVIGATION_LANGUAGE);
                if(language != null){
                    logger.debug("Language, determined from session attribute is: " +
                            language.getCode());
                    return language;
                }
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

                    language = getSupportedLanguage(locale.getLanguage(),
                        currentSite,
                        isLocalTranslatorForSite(request));
                    if (language != null) {
                        break;
                    }

                }
            }

            if (language != null) {
                logger.debug("Language, determined from browser settings is: " +
                             language.getCode());
                return language;
            }

            // Determine language from root site
            if (rootSite.getDefaultLanguage() != null) {
                language = getSupportedLanguage(rootSite.getDefaultLanguage().
                                                getCode(),
                                                currentSite,
                                                isLocalTranslatorForSite(
                    request));
            }
            if (language != null) {
                logger.debug(
                    "Language, determined from root site's default language is: " +
                    language.getCode());
                return language;
            }
            // Return english language
            language = new Locale();
            language.setCode(java.util.Locale.ENGLISH.getLanguage());
            language.setName(java.util.Locale.ENGLISH.getDisplayName());

            logger.debug("Navigation language is English");
        }
        else {
            throw new IllegalStateException(
                "Unable to get site from request to determine language");
        }

        return language;
    }

    /**
     * For logged in users synchronize language preferences. For guests, do
     * nothing
     * @param request HttpServletRequest
     */
    private static void synchronizeLangPreferences(HttpServletRequest
        request) {
        User currentUser = RequestUtils.getUser(request);
        Site rootSite = DgUtil.getRootSite(RequestUtils.getSite(request));

        // rootSite must always be different then null.
        if (currentUser == null) {
            return;
        }

        Session session = null;
        try {

            UserLangPreferences preferences;

            session = PersistenceManager.getSession();

            UserPreferencesPK key = new UserPreferencesPK(currentUser, rootSite);
            try {
                preferences = (UserLangPreferences) session.load(
                    UserLangPreferences.class, key);
                logger.debug("Updating user language preferences");
            }
            catch (ObjectNotFoundException ex2) {
                logger.debug(
                    "User language preference record was not found. Creating new one");
                preferences = createDefaultLangPreferences(null, rootSite,
                    currentUser);
            }
            // put preferences back
            currentUser.setUserLangPreferences(preferences);
        }
        catch (Exception ex) {

            logger.debug("Unable to switch Language ", ex);

            throw new RuntimeException("Unable to switch Language ", ex);
        }
    }

    public static String generateUID(String value, boolean hashIt) {
        MessageDigest md = null;
        StringBuffer buf = null;

        String data = (new Double(Math.random()).toString() +
                       new Long(System.currentTimeMillis()).toString());
        if (hashIt && value != null) {
            data += value;
        }

        try {
            md = MessageDigest.getInstance("MD5");

            md.update(data.getBytes());
            byte[] digest = md.digest();

            buf = new StringBuffer();

            for (int i = 0; i < digest.length; i++) {
                buf.append( (Character.forDigit( (digest[i] & 0xF0) >> 4, 16)));
                buf.append( (Character.forDigit( (digest[i] & 0xF), 16)));
            }
        }
        catch (NoSuchAlgorithmException ex) {
            logger.warn("Unable to generate UID", ex);
            throw new java.lang.IllegalArgumentException(
                "Unable to generate UID");
        }

        return (value == null || hashIt) ? buf.toString() :
            value + buf.toString();

    }

    public static String generateUID(String value) {
        return generateUID(value, false);
    }

    /**
     *
     * @param string
     * @return
     */
    public static String encodeBase64(String string) {
        try {
            return URLEncoder.encode(new sun.misc.BASE64Encoder().encodeBuffer(
                string.getBytes()), "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            logger.error("Could not encode Base64", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param string
     * @return
     */
    public static String decodeBase64(String string) {
        try {
            try {
                return new String(new sun.misc.BASE64Decoder().decodeBuffer(
                    URLDecoder.decode(string, "UTF-8")));
            }
            catch (UnsupportedEncodingException ex) {
                logger.error("Could not decode Base64", ex);
                throw new RuntimeException(ex);
            }
        }
        catch (IOException ex) {
            logger.error("Could not decode Base64", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param string
     * @return
     */
    public static String encodeString(String string) {
        if (string == null) {
            return null;
        }
        try {
            return URLEncoder.encode(string, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            logger.error("Could not encode string", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param string
     * @return
     */
    public static String decodeString(String string) {
        if (string == null) {
            return null;
        }
        try {
            return new String(URLDecoder.decode(string, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            logger.error("Could not decode string", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns current site or null, if request does not contain site
     * information
     * @param request HttpServletRequest
     * @return current site
     * @deprecated use RequestUtils.getSite() instead
     */
    public static Site getCurrentSite(HttpServletRequest request) {
        // redirecting to RequestUtils.getSite()
        return RequestUtils.getSite(request);
    }

    /**
     * @param request
     * @return SiteId Unique identifocator of a site
     * @todo why do we need this method? I think, it's tame to make it
     * deprecated. Mikheil
     */
//    public static String getCurrentSiteId(HttpServletRequest request) {
//        Site site = RequestUtils.getSite(request);
//        if (site == null)
//            return null;
//
//        String siteId = site.getSiteId();
//
//        return siteId;
//    }

    /**
     * Returns user, currently logged in into system or null, if user is not
     * logged in
     * @param session HttpSession object
     * @return logged user
     * @deprecated use RequestUtils.getUser() instead
     */
//    public static User getCurrentUser(HttpServletRequest request) {
//        return RequestUtils.getUser(request);
//        //User user = (User) request.getSession(true).getAttribute(Constants.USER);
//        //return user;
//    }

    /**
     * Gets current site from request and returns its user languages
     * @param request
     * @return user languages for the current site
     * @deprecated use SiteUtils.getUserLanguages() instead
     */
//    public static java.util.Set getCurrSiteUserLangs(HttpServletRequest request) {
//        Site site = RequestUtils.getSite(request);
//        return SiteUtils.getUserLanguages(site);
//    }

    /**
     * Gets current site from request and returns its translation languages
     * @param request
     * @return translation languages for the current site
     * @deprecated use SiteUtils.getTransLanguages() instead
     */
//    public static java.util.Set getCurrSiteTransLangs(HttpServletRequest
//        request) {
//        Site site = RequestUtils.getSite(request);
//        return SiteUtils.getTransLanguages(site);
//    }

//    public static Locale getCurrSiteDefLang(HttpServletRequest request) {
//        Site site = RequestUtils.getSite(request);
//        if (site == null)
//            return null;
//
//        Locale defLang = null;
//
//        try {
//            defLang = site.getDefaultLanguage();
//        }
//        catch (NullPointerException ex) {
//            logger.warn("Unable to get Sites Default Language", ex);
//        }
//
//        return defLang;
//    }

    public static ModuleInstance getRequiredInstance(HttpServletRequest request,
        String moduleName, String moduleInstance) {

        Site currentSite = RequestUtils.getSite(request);
        ModuleInstance requiredInstance = new ModuleInstance();
        requiredInstance.setSite(currentSite);

        requiredInstance.setModuleName(moduleName);
        requiredInstance.setInstanceName(moduleInstance);

        List allowedInstances = SiteCache.getInstance().getInstances(
            currentSite);
        logger.debug("Allowed instances: " + allowedInstances);
        int index = Collections.binarySearch(allowedInstances, requiredInstance,
                                             SiteCache.moduleInstanceComparator);
        if (index >= 0) {
            requiredInstance = (ModuleInstance) allowedInstances.get(index);
        }
        else {
            List sharedInstances = SiteCache.getInstance().getSharedInstances();
            logger.debug("Shared instances: " + sharedInstances);
            index = Collections.binarySearch(sharedInstances, requiredInstance,
                                             SiteCache.moduleInstanceComparator);
            if (index >= 0) {
                requiredInstance = (ModuleInstance) sharedInstances.get(index);
            }
            else {
                requiredInstance = null;
            }
        }

        return requiredInstance;
    }

    /**
     * Set current user language preference
     *
     * @param request
     */
    public static void setUserLanguage(HttpServletRequest request, HttpServletResponse response) {

        // determine current user language
        Locale language = DgUtil.getLanguageFromRequest(request);
        logger.debug("Navigation language, determined from request is: " +
                     language == null ? null : language.getCode());        
        
       setSessionLanguage(request, response, language);

        User user = RequestUtils.getUser(request);
        if (user != null && user.getUserLangPreferences() == null) {
            throw new IllegalStateException(
                "User language preferences for logged in user are empty");
        }
        /*
          if (user != null) {
           if (user.getUserLangPreferences() == null) {
            Site currentSite = getCurrentSite(request);
            if (currentSite != null) {
             Site rootSite = getRootSite(currentSite);
          UserLangPreferences preferences = new UserLangPreferences(
              user, rootSite);
             preferences.setNavigationLanguage(language);
          preferences.setAlertsLanguage(user.getRegisterLanguage());
          preferences.setContentLanguages(new HashSet(SiteCache.
              getInstance().getUserLanguages(rootSite)));
             user.setUserLangPreferences(preferences);
            }
           }
          }
         */
    }

    /**
     * Returns true, if logged user has translator rights on the current site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isLocalTranslatorForSite(HttpServletRequest request) {
        Site currentSite = RequestUtils.getSite(request);
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, currentSite,
                                           ResourcePermission.INT_TRANSLATE);
    }

    /**
     * Returns true, if logged user has translator rights on the given site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isLocalTranslatorForSite(HttpServletRequest request,
        Site site) {
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, site,
                                           ResourcePermission.INT_TRANSLATE);
    }

    /**
     * Returns true, if logged user has administrator rights on the current site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isSiteAdministrator(HttpServletRequest request) {
        Site currentSite = RequestUtils.getSite(request);
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, currentSite,
                                           ResourcePermission.INT_ADMIN);

    }

    /**
     * Returns true, if logged user has administrator rights on the given site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isSiteAdministrator(HttpServletRequest request,
                                              Site site) {
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, site,
                                           ResourcePermission.INT_ADMIN);
    }

    /**
     * Returns true, if logged user has administrator rights on the current site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isModuleInstanceAdministrator(HttpServletRequest
        request) {

        User user = RequestUtils.getUser(request);

        if (user != null && user.isGlobalAdmin()) {
            return true;
        }

        Site currentSite = RequestUtils.getSite(request);
        Subject subject = DgSecurityManager.getSubject(request);
        ModuleInstance moduleInstance = getRealModuleInstance(request);

        return DgSecurityManager.permitted(subject, currentSite, moduleInstance,
                                           ResourcePermission.INT_ADMIN);

    }

    /**
     * Returns true, if logged user has translator rights on the current site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isGroupTranslatorForSite(HttpServletRequest request) {
        Site currentSite = RequestUtils.getSite(request);
        Site rootSite = getRootSite(currentSite);
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, rootSite,
                                           ResourcePermission.INT_TRANSLATE);
    }

    /**
     * Returns true, if logged user has translator rights on the given site
     * or false if user does not have such rights.
     * @param request request
     * @return
     */
    public static boolean isGroupTranslatorForSite(HttpServletRequest request,
        Site site) {
        Site rootSite = getRootSite(site);
        Subject subject = DgSecurityManager.getSubject(request);

        return DgSecurityManager.permitted(subject, rootSite,
                                           ResourcePermission.INT_TRANSLATE);
    }

    /**
     * Returns root url for the current site, to be used in hyperlinks and stuff.
     * as for the current request (if site has multiple urls attached in the configuration
     * then this matters.
     */
    public static String getCurrRootUrl(HttpServletRequest request) {
        SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
        return getSiteUrl(siteDomain, request);
    }

    /**
     * Returns site url , to be used in hyperlinks and stuff.
     * as for the current request (if site has multiple urls attached in the configuration
     * then this matters.
     */
    public static String getSiteUrl(SiteDomain siteDomain,
                                    HttpServletRequest request) {
        String url = SiteUtils.getSiteURL(siteDomain, request.getScheme(),
                                          request.getServerPort(),
                                          request.getContextPath());

        return url;
    }

    /**
     * Returns site url , to be used in hyperlinks and stuff.
     * as for the current request (if site has multiple urls attached in the configuration
     * then this matters.
     */
    public static String getSiteUrl(Site site, HttpServletRequest request) {
        SiteDomain defaultDomain = SiteUtils.getDefaultSiteDomain(site);
        return getSiteUrl(defaultDomain, request);

    }

    /**
     * Returns site context url , to be used in hyperlinks and stuff.
     * as for the current request (if site has multiple urls attached in the configuration
     * then this matters.
     */
    public static String getContextUrl(Site site, HttpServletRequest request) {

        Iterator domainIterator = site.getSiteDomains().iterator();
        String path = "";
        String server = "";
        while (domainIterator.hasNext()) {
            SiteDomain sd = (SiteDomain) domainIterator.next();
            if (sd.isDefaultDomain()) {

                path = sd.getSitePath();
                server = sd.getSiteDomain();
                if (path == null) {
                    path = "";
                }
                break;
            }
            if ( (server.equals("")) || (server == null)) {
                path = sd.getSitePath();

                if (path == null) {
                    path = "";
                }
            }

        }

        String url;
        url = request.getContextPath() + path;

        return url;
    }

    /**
     * Returns ModuleInstance object, for which request was caled. Return value
     * is the REAL ModuleInstance object, for mapped instances it returns
     * original module instance, not the link.
     * @param request request
     * @return ModuleInstance object
     * @deprecated use RequestUtils.getModuleInstance() instead
     */
    public static ModuleInstance getModuleInstance(HttpServletRequest request) {
        ComponentContext context = ComponentContext.getContext(request);
        ModuleInstance moduleInstance = null;

        if (context != null) {
            moduleInstance = (ModuleInstance) context.getAttribute(
                Constants.
                MODULE_INSTANCE_OBJECT);
        }
        if (moduleInstance == null) {
            moduleInstance = (ModuleInstance) request.getAttribute(
                Constants.
                MODULE_INSTANCE_OBJECT);
        }
        /*
         if ((moduleInstance != null) && (moduleInstance.getRealInstance() != null)) {
         moduleInstance = moduleInstance.getRealInstance();
           }*/
        return moduleInstance;
    }

    /**
     * Get module name
     * return for example news, pool
     *
     * @param request
     * @return
     */
    public static String getModuleName(HttpServletRequest request) {

        String moduleName = null;
        ComponentContext context = ComponentContext.getContext(request);

        if (context != null) {
            moduleName = (String) context.getAttribute(Constants.MODULE_NAME);
            if (moduleName == null) {
                moduleName = (String) request.getAttribute(Constants.
                    MODULE_NAME);
            }
        }
        else {
            moduleName = (String) request.getAttribute(Constants.MODULE_NAME);
        }

        return moduleName;
    }

    /**
     * Get param separator from digi.xml config file
     * <param-separator>-</param-separator>
     *
     * @return separator if no separator defined return "-"
     */
    public static String getParamSeparator() {

        String separator = null;
        try {
            DigiConfig config = DigiConfigManager.getConfig();
            if (config.getParamSeparator() != null) {
                separator = config.getParamSeparator().getContent();
            }
        }
        catch (Exception ex) {
            logger.warn("Could not get param separator ", ex);
        }

        return separator == null ? "-" : separator;
    }

    /**
     * Get param safeHTML from digi.xml config file
     * <param-safeHTML>-</param-safeHTML>
     *
     * @return separator if no separator defined return "-"
     */
    public static String getParamSafehtml() {

        String safehtml = null;
        try {
            DigiConfig config = DigiConfigManager.getConfig();
            if (config.getParamSafehtml() != null) {
                safehtml = config.getParamSafehtml().getContent();
            }
        }
        catch (Exception ex) {
            logger.warn("Could not get param safeHTML", ex);
        }

        return safehtml == null ? "b,u,i,pre" : safehtml;
    }

    /**
     * Get param bbTag from digi.xml config file
     * <param-bbTag>-</param-bbTag>
     *
     * @return separator if no separator defined return "-"
     */
    public static String getParamBbTag() {

        String bbTag = null;
        try {
            DigiConfig config = DigiConfigManager.getConfig();
            if (config.getParamSafehtml() != null) {
                bbTag = config.getParamBbTag().getContent();
            }
        }
        catch (Exception ex) {
            logger.warn("Could not get param bbTag", ex);
        }

        return bbTag == null ? "b,u,i,quote,code,list,list=,img,url" : bbTag;
    }

    /**
     * Check if cache enabled
     * <res-cache>true</res-cache>
     *
     * @return true if cached else false
     */
    public static boolean isResourceCached() {

        String cached = null;
        try {
            DigiConfig config = DigiConfigManager.getConfig();
            if (config.getResCache() != null) {
                cached = config.getResCache().getContent();
            }
        }
        catch (Exception ex) {
            logger.warn("isResourceCached() failed", ex);
        }

        return cached == null ? false : (cached.equalsIgnoreCase("true") ? true : false);
    }

    /**
     * Parse url to module name , instance name and action without .do
     * for example if mainPath contain "/news/political/showNewsDetails.do", return
     * String[0] = news
     * String[1] = political
     * String[2] = showNewsDetails
     *
     * @param mainPath
     * @return
     */
    public static String[] parseUrltoModuleInstanceAction(String mainPath) {
        return parseUrltoModuleInstanceAction(mainPath, true);
    }

    /**
     * Parse url to module name , instance name and action. For example, if
     * mainPath is "/news/political/showNewsDetails", then:
     * String[0] = news
     * String[1] = political
     * String[2] = showNewsDetails
     * String[3] = /news/political/showNewsDetails
     * If instance can not be determined (when mainPath is
     * "/news/showNewsDetails"), then depending on instanceIsNotNull parameter,
     * it is left to null or filled from configuration, using module's default
     * instance
     * @param mainPath String
     * @param instanceIsNotNull boolean if this is null and mainPath does not
     * contain instance name, null will be returned. If not null, default
     * instance is used
     * @return String[]
     */
    public static String[] parseUrltoModuleInstanceAction(String mainPath, boolean instanceIsNotNull) {

        int i = 1;
        int a = 1;
        String rightPart = null;
        String[] moduleInstance = null;
        StringBuffer url = null;

        if (mainPath.startsWith("/") && mainPath.substring(1).indexOf("/") >= 0) {

            moduleInstance = new String[4];

            // process if mainPath is like /module/something
            StringTokenizer tokenizer = new StringTokenizer(mainPath, "/");
            while (tokenizer.hasMoreTokens()) {
                String part = tokenizer.nextToken();
                if (i == 1) {
                    StringTokenizer tokenizer2 = new StringTokenizer(part,
                        getParamSeparator());
                    while (tokenizer2.hasMoreTokens()) {
                        if (a == 1) {
                            moduleInstance[0] = tokenizer2.nextToken();
                        }
                        else if (a == 2) {
                            moduleInstance[1] = tokenizer2.nextToken();
                        }
                        else
                            break;
                        a++;
                    }
                }
                else {
                    if (i == 2) {
                        if (moduleInstance[1] == null)
                            moduleInstance[1] = part;
                        else
                            moduleInstance[2] = part;
                    }
                    else if (i == 3) {
                        moduleInstance[2] = part;
                    }
                    else {
                        break;
                    }
                }
                i++;
            }

            if (moduleInstance[2] == null) {
                url = new StringBuffer();
                moduleInstance[2] = moduleInstance[1];
                if (instanceIsNotNull) {
                    moduleInstance[1] = ModuleUtils.getModuleDefaultInstance(
                        moduleInstance[0]);
                } else {
                    moduleInstance[1] = null;
                }
                for (int d = 0; d < moduleInstance.length; d++) {
                    if (moduleInstance[d] != null) {
                        url.append("/");
                        url.append(moduleInstance[d]);
                    }
                }

                moduleInstance[3] = url.toString();

            }
        }

        return moduleInstance;
    }

    public static String formatDate(java.util.Date date) {
        if (date == null)
            return null;

        String formatedDate = new String();

        formatedDate = new SimpleDateFormat("MMMMM").format(date) + " " +
            new SimpleDateFormat("d").format(date) + ", " +
            new SimpleDateFormat("yyyy").format(date);

        return formatedDate;
    }

    public static String formatDateShortly(java.util.Date date) {
        if (date == null)
            return null;

        String formatedDate = new String();

        formatedDate = new SimpleDateFormat("MMMMM").format(date).substring(0,
            3) + " " +
            new SimpleDateFormat("d").format(date) + ", " +
            new SimpleDateFormat("yyyy").format(date);

        return formatedDate;
    }

    /**
     * Returns use by user id
     * @param activeUserId Long
     * @return User
     * @deprecated use UserUtils.getUser() instead
     */
    public static User getUser(Long activeUserId) {
        return UserUtils.getUser(activeUserId);
    }

    public static UserInfo getUserInfo(Long activeUserId) throws
        DgException {
        UserInfo result = null;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery(
                "select u.firstNames, u.lastName, u.email from " +
                User.class.getName() +
                " as u where u.id=?");
            q.setCacheable(true);
            q.setLong(0, activeUserId.longValue());
            Iterator iter = q.list().iterator();
            while (iter.hasNext()) {
                Object[] results = (Object[]) iter.next();
                result = new UserInfo( (String) results[0], (String) results[1],
                                      (String) results[2]);
                break;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get User from database", ex);
            throw new DgException("Unable to get User from database", ex);
        }

        if (result == null) {
            logger.debug("Unable to get User#" + activeUserId + "from database");
            /*      throw new DgException("Unable to get User#" + activeUserId +
                  "from database");*/
            result = new UserInfo();
        }

        return result;

    }

    /**
     * Returns ModuleInstance object, for which request was caled. Return value
     * is the REAL ModuleInstance object, for mapped instances it returns
     * original module instance, not the link.
     * @param request request
     * @return ModuleInstance object
     * @deprecated use RequstUtils.getRealModuleInstance() instead
     */
    public static ModuleInstance getRealModuleInstance(HttpServletRequest
        request) {
        return RequestUtils.getRealModuleInstance(request);
    }

    /**
     * Returns users, which have given permission to the specified site
     * @param site Site
     * @param permission action (ADMIN, READ, WRITE, TRANSLATE)
     * @return set of users
     * @throws DgException
     */
    public static Set getSitePermittedUsers(Site site, String permission) throws
        DgException {
        SitePermission sp = new SitePermission(site, permission);
        return DigiSecurityManager.getUsers(sp);
    }

    /**
     * get users by given module instance and permission
     * @param moduleInstance moduleInstance descriptor
     * @param includeSitePermittedUsers true, if this list will include users,
     * which have this permission through site permission
     * @return User object list
     */
    public static Set getModuleInstancePermittedUsers(ModuleInstance
        moduleInstance, String permission, boolean includeSitePermittedUsers) throws
        DgException {
        ModuleInstancePermission mop = new ModuleInstancePermission(
            moduleInstance, permission);
        Set users = DigiSecurityManager.getUsers(mop);

        if (includeSitePermittedUsers) {
            users.addAll(getSitePermittedUsers(moduleInstance.getSite(),
                                               permission));
        }

        return users;
    }

    /**
     * Returns request URL for the given request. This is a workaround of STRUTS
     * issue: MultipartRequestWrapper.getRequestURL returns null
     * @param request
     * @return
     */
    public static StringBuffer getRequestUrl(HttpServletRequest request) {
        StringBuffer url;

        if (request instanceof MultipartRequestWrapper) {
            url = ( (MultipartRequestWrapper) request).getRequestURL();
        }
        else {
            url = request.getRequestURL();
        }
        if (url == null)
            throw new IllegalArgumentException(
                "getRequestURL() returns null for the given request. Request object type is " +
                request.getClass().getName());
        return url;
    }

    /**
     * Get full URL with params
     *
     * @param request
     * @return
     */
    public static String getFullURL(HttpServletRequest request) {

        // Calculate full url with query string
        StringBuffer url = getRequestUrl(request);
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }

        return url.toString();
    }

    /*
     * @param request
     * @return
     */
    public static String generateReferrer(HttpServletRequest request,
                                          String fullUrl) {

        String urlFull = null;
        SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
        String url = SiteUtils.getSiteURL(siteDomain, request.getScheme(),
                                          request.getServerPort(),
                                          request.getContextPath());

        if (fullUrl != null)
            urlFull = fullUrl;
        else
            urlFull = getFullURL(request);

        int index = urlFull.indexOf(url);
        if (index != -1) {
            index = url.length();
            return Constants.REFERRER_PARAM + "=" +
                encodeString(url + "~" +
                             urlFull.substring(index, urlFull.length()));
        }
        else {
            logger.error(MessageFormat.format("Error merging Urls {0} to {1}", url, urlFull));
        }

        return null;
    }

    /**
     *
     * String class's split() method uses regular expression, hence
     * is quite slow.  Even a short string takes about 16 milliseconds.
     * fastSplit() is a fast alternative which takes less than 1 millisecond.
     *
     * @author inadareishvili@worldbank.org
     * @param szInit string to split
     * @param splitChar char to split on
     * @return string array
     */

    public static String[] fastSplit(String szInit, char splitChar) {

        long szInitLen = szInit.length();

        if (szInitLen > FASTSPLIT_MAXSIZE) {
            return szInit.split(String.valueOf(splitChar));
        }

        Object buffers[] = new Object[FASTSPLIT_MAXSIZE];
        char[] tmpBuf = new char[FASTSPLIT_MAXSIZE];

        int counter = 0, k = 0;
        char cTmp;
        String[] tmp;
        boolean isLastCharSeparator = false;

        for (int i = 0; i <= szInitLen; i++) {

            //-- it does not matter what wil be here, we won't process it
            cTmp = (i < szInitLen) ? szInit.charAt(i) : '\n';

            //-- if the string ends with "~" we should not return
            //-- an empty last entry. Java's split() ignores last
            //-- separator
            isLastCharSeparator = (cTmp == splitChar && i == szInitLen - 1);

            if ( (cTmp == splitChar || i == szInitLen) && !isLastCharSeparator) {
                buffers[counter] = (char[])new char[k];
                for (int j = 0; j < k; j++) {
                    ( (char[]) buffers[counter])[j] = tmpBuf[j];
                }
                counter++;
                k = 0;
            }
            else if (!isLastCharSeparator) {
                tmpBuf[k] = cTmp;
                k++;
            }
        }

        tmp = new String[counter];

        for (int i = 0; i < counter; i++) {
            tmp[i] = new String( ( (char[]) buffers[i]));
        }

        return tmp;
    }

    /**
     *
     * String class's replaceAll() method uses regular expression, hence
     * is quite slow.  fastReplaceAll() is a fast alternative which
     * takes less than 1 millisecond. Used in simple, time-critical places.
     *
     * <p> If you want to pass empty char (replace with removal) you need to
     * pass '\0' or 0, because Java will not allow you to pass argument like
     * '' for char type.
     *
     * @author inadareishvili@worldbank.org
     * @param szInit string to modify
     * @param cFrom character to be replaced.
     * @param cTo character to replace with.
     * @return new String
     *
     */

    public static String fastReplaceAll(String szInit, char cFrom, char cTo) {

        int szInitLen = szInit.length();

        /* Did user pass null char as the replacement and do we have
         * to recalculate the new length of the string?
         * Performance-tests show that this extra step does not introduce
         * any significant overhead. Overhead is order of magnitude of
         * 1/1000 of a millisecond on a PIV 2GHz computer.
         */
        int numOfReplacements = 0;
        if (cTo == 0) {
            for (int i = 0; i < szInitLen; i++) {
                if (szInit.charAt(i) == cFrom) {
                    numOfReplacements++;
                }
            }
        }

        int szResultLen = (numOfReplacements == 0) ? szInitLen :
            szInitLen - numOfReplacements;

        char cTmp;
        char[] buf = new char[szResultLen];

        int k = 0;
        for (int i = 0; i < szInitLen; i++) {
            cTmp = szInit.charAt(i);
            if (cTo != 0 || (cTmp != cFrom)) {
                if (cTmp == cFrom) {
                    buf[k] = cTo;
                }
                else {
                    buf[k] = cTmp;
                }
                k++;
            }
        }

        return new String(buf);

    }

    /**
     * tests if one string contains the other
     * @param source the source string in which the other string is to be found
     * @param other the other string, which should be found in source
     * @param ignoreCase true if case sensitivity should be considered, false otherwise
     * @return true if in source string at least one occurance of other string was found, false otherwise
     */
    public static boolean fastContains(String source, String other,
                                       boolean ignoreCase) {

        int sourceLen = source.length();
        int otherLen = other.length();

        if (sourceLen < otherLen) {
            return false;
        }

        boolean contains = false;

        //------------
        int m = 0;
        for (int i = 0; i < sourceLen; i++) {
            int j;
            char ch1 = other.charAt(0);
            for (j = m; j < sourceLen; j++) {
                char ch2 = source.charAt(j);

                if (ch1 == ch2) {
                    break;
                }

                if (ignoreCase) {
                    ch1 = Character.toLowerCase(ch1);
                    ch2 = Character.toLowerCase(ch2);
                    if (ch1 == ch2) {
                        break;
                    }
                }
            }
            //
            if ( (j + otherLen) > sourceLen) {
                break;
            }
            int k;
            for (k = 1; k < otherLen; k++) {
                ch1 = source.charAt(k + j);
                char ch2 = other.charAt(k);
                if (ignoreCase) {
                    ch1 = Character.toLowerCase(ch1);
                    ch2 = Character.toLowerCase(ch2);
                }

                if (ch1 != ch2) {
                    break;
                }
            }
            if (k == otherLen) {
                contains = true;
                break;
            }
            m = j + 1;
        }
        //--------
        return contains;
    }

    /**
     * Check "User-Agent" request header. if ignored return true
     *
     * @param request
     * @return
     */
    public static boolean isIgnoredUserAgent(HttpServletRequest request) {
        DigiConfig config = DigiConfigManager.getConfig();
        String userAgent = request.getHeader("User-Agent");

        IgnoredAgentsService service = (IgnoredAgentsService) ServiceManager.
            getInstance().getService("ignoredAgentsService");
        if (service != null) {
            return service.isIgnoredAgent(userAgent);
        }

        if (userAgent == null) {
            userAgent = "";
        }

        Set agents = getUserAgents();
        if (agents == null) {
            return false;
        }

        Iterator iter = agents.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            if (userAgent.indexOf(item) != -1) {
                logger.debug("User-Agent is ignored");
                return config.isIgnore();
            }
        }

        if (userAgent == null || userAgent.trim().length() == 0) {
            logger.debug("User-Agent is not set");
            return!config.isIgnore();
        }

        return!config.isIgnore();
    }

    /**
     * check if user-agent have cookie
     *
     * @param request
     * @return
     */
    public static boolean isCookieEnable(HttpServletRequest request) {
        return true;
        /*
           String userCookie = request.getHeader("Cookie");
           if( userCookie == null ) {
         return false;
           }
         return true;
         */
    }

    /**
     * Get User Agents list from digi.cml file.
     * example:
     *
     * <user-agents>
     *   <user-agent>googlebot.com</user-agent>
     * </user-agents>
     *
     * @return agents list
     */
    public static Set getUserAgents() {

        DigiConfig config = DigiConfigManager.getConfig();
        if (config.getAgents() != null) {
            return config.getAgents();
        }

        return null;
    }

    public static Set getNoneSSOPath() {

        DigiConfig config = DigiConfigManager.getConfig();
        if (config.getNoneSSOPath() != null) {
            return config.getNoneSSOPath();
        }

        return null;
    }

    /**
     * Analyzes passed <code>pattern</code>, if there is a fragment like
     * "{some-key}", searches in the <code>parameters</code> map, gets its value
     * and replaces this fragment by this value. If key was not found in the map,
     * leaves the fragment as it is
     * @param pattern String, containing parameters. For example, "Hello, my name
     * is {name}, I'm {age}, what's your name?"
     * @param parameters Map of parameters. For the pattern above: name - Mikheil,
     * age - 26 <small><i>It's my real age for this moment. Mikheil</i></small>
     * @return String, assembled by the replacement procedure
     */
    public static String fillPattern(String pattern, Map<String, ? extends Object> parameters) {
        return fillPattern(pattern, parameters, true);
    }

    /**
     * Analyzes passed <code>pattern</code>, if there is a fragment like
     * "{some-key}", searches in the <code>parameters</code> map, gets its value
     * and replaces this fragment by this value. If key was not found in the map,
     * leaves the fragment as it is
     * @param pattern String, containing parameters. For example, "Hello, my name
     * is {name}, I'm {age}, what's your name?"
     * @param parameters Map of parameters. For the pattern above: name - Mikheil,
     * age - 26 <small><i>It's my real age for this moment. Mikheil</i></small>
     * @param useParamNames when true, if pattern part is unmatched, put
     * parameter name there. When false - skip
     * @return String, assembled by the replacement procedure
     */
    public static String fillPattern(String pattern, Map<String, ? extends Object> parameters,
                                     boolean useParamNames) {
        if ( (pattern == null) || (parameters == null)) {
            return null;
        }
        StringBuffer sb = new StringBuffer(500);
        StringBuffer paramBuffer = new StringBuffer(100);
        boolean isOpen = false;

        StringBuffer targetBuffer = sb;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '{') {
                targetBuffer = paramBuffer;
            }
            else {
                if (c == '}') {
                    String paramName = paramBuffer.toString();
                    targetBuffer = sb;
                    paramBuffer = new StringBuffer();
                    Object value = parameters.get(paramName);
                    if (value == null) {
                        if (useParamNames) {
                            value = "{" + paramName + "}";
                            sb.append(value);
                        }
                    }
                    else {
                        sb.append(value);
                    }
                }
                else {
                    targetBuffer.append(c);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Returns new string representing url with removed "jsessionid" parameter
     * @param calcUrl Source url
     * @return processed url
     */
    public static String removeJSessionId(String calcUrl) {
        String removeRegExp = ";jsessionid=[a-zA-Z_0-9]{32}?";
        return calcUrl.replaceAll(removeRegExp, "");
    }

    /**
     * Replaces html special character codes by appropreate characters
     * @param src String
     * @return String
     */
    public static String htmlize(String src) {
        String retVal = src.
            replaceAll("&lt;", "<").
            replaceAll("&gt;", ">").
            replaceAll("&quot;", "\"").
            replaceAll("\'", "&#39;").
            replaceAll("&amp;", "&");
        return retVal;
    }

    /**
     * Replaces html special characters by appropriate codes
     * @param src String
     * @return String
     */
    public static String dehtmlize(String src) {
        return dehtmlize(src, false);
    }

    /**
     * Replaces html special characters by appropriate codes
     * @param src String
     * @param newLines boolean if true, replace newline characters with br tags
     * @return String
     */
    public static String dehtmlize(String src, boolean newLines) {
        if (src == null)
            return null;
        String retVal = src.
            replaceAll("&", "&amp;").
            replaceAll("<", "&lt;").
            replaceAll(">", "&gt;").
            replaceAll("\"", "&quot;").
            replaceAll("&#039;", "\'"). // JSTL renders ' sign this way
            replaceAll("&#39;", "\'");
        if (newLines) {
            retVal = retVal.replaceAll("(\r\n)|(\n)|(\r)", "<br />");
        }
        return retVal;
    }
    
    /**
     * precompile these patterns (a slow process) for some speedup and for copy-paste avoidance
     */
    private static Pattern ptr  = Pattern.compile("<!--.*-->", Pattern.DOTALL);
    private static Pattern ptr2 = Pattern.compile("<[^<]*>", Pattern.DOTALL);
    private static Pattern[] monotonousPatterns = new Pattern[]{
                                    Pattern.compile("Version:[1-9]\\.[0-9]+"), // no DOTALL needed
                                    Pattern.compile("StartHTML:[0-9]+"), // no DOTALL needed
                                    Pattern.compile("EndHTML:[0-9]+"), // no DOTALL needed
                                    Pattern.compile("StartFragment:[0-9]+"), // no DOTALL needed
                                    Pattern.compile("EndFragment:[0-9]+") // no DOTALL needed
    };
    
    
    private static Set<Character> trimmableChars = new HashSet<Character>() {{add(' '); add('\n'); add('\t');}};

    /**
     * trims any chars appearing at the right or left of a string. the set of the trimmable chars is in trimmableChars
     * @param src
     * @return
     */
    public static String trimChars(String src)
    {
        if (src == null)
            return src;
        src = src.replace("\r\n", "\n");
        int begPos = 0, len = src.length(), endPos = len - 1;
        while(begPos < len)
            if (trimmableChars.contains(src.charAt(begPos)))
                begPos ++;
            else
                break;
        while (endPos > begPos)
            if (trimmableChars.contains(src.charAt(endPos)))
                endPos --;
            else
                break;
        if (begPos == len)
            return "";
        return src.substring(begPos, endPos + 1);
    }
    
    /**
     * cleans a text copy-pasted from Word from all of its tags and returns the plain text
     * also does sanity cleanups, like replacing tabs with spaces and multiple spaces with a single one
     * @param src
     * @return
     */
    public static String cleanWordTags(String src)
    {
        if (src == null)
            return src;
        src = ptr.matcher(src).replaceAll("").trim();
        src = src.replaceAll("<style.*</style>", "");
        src = src.replaceAll("\\<.*?>", "");
        src = ptr2.matcher(src).replaceAll("");
        for(Pattern pattern:monotonousPatterns)
            src = pattern.matcher(src).replaceFirst(" "); // replaceFirst is enough, as there is one or none matches
        src = src.replace('\t', ' ').replace("&nbsp;", " ").trim();
        src = trimChars(src);
        while (src.indexOf("  ") >= 0)
            src = src.replace("  ", " ");
        return src;
    }

    public static String cleanHtmlTags(String content) {
        if (content == null) {
            return null;
        }
        String noTags = Jsoup.clean(content, Whitelist.none());
        String noNbsp = noTags.replace("&nbsp;", " ");
        return StringUtils.normalizeSpace(StringEscapeUtils.unescapeHtml4(noNbsp));
    }
}
