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

package org.digijava.kernel.security;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
import org.digijava.kernel.Constants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.RequestProcessor;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.*;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public final class HttpLoginManager {

    private static Logger logger = Logger.getLogger(HttpLoginManager.class);

    final static String LOGIN_CACHE_REGION_ROOT =
        "org.digijava.kernel.login_region.";
    public final static String DIGI_SESSION_COOKIE = "digi_session_id";
    private final static String UNASSIGNED_SESSION_ID = "UNASSIGNED";


    public final static int LOGIN_RESULT_OK = 0;
    public final static int LOGIN_RESULT_INVALID = 1;
    public final static int LOGIN_RESULT_BANNED = 2;
    public final static int LOGIN_RESULT_LOGOUT = 3;

    public final static String LOGIN_ERROR_KEY =
        "org.digijava.kernel.login_error";

    public static final class RedirectRequiredException extends AuthenticationException {
        public RedirectRequiredException() {
            super("Redirect to login site is required");
        }
    };

    public static final class LoginInfo
        implements Serializable {

        private static final long serialVersionUID = 1;

        private Long userId;
        private int loginResult;
        private ActionMessages errors;
        private Long moduleId;
        private Long newUserId;
        private long sourceSiteId;
        private Subject subject;
        private Subject newSubject;

        public String toString() {
            return new ReflectionToStringBuilder(this).toString();
        }

        private LoginInfo() {
            this.newSubject = null;
            this.userId = null;
            this.errors = null;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private LoginInfo(int loginResult) {
            this.newSubject = null;
            this.userId = null;
            this.errors = null;
            this.loginResult = loginResult;
        }

        private LoginInfo(Subject subject) {
            this.newSubject = null;
            this.subject = subject;
            this.userId = getUserId(subject);
            this.errors = null;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private void becomeUser(Subject subject, long siteId) throws DgException {
            if (!isLoggedIn()) {
                throw new DgException("You must log in to become another user");
            }
            if (isBecomedUser()) {
                throw new DgException("You can not become user twise");
            }
            this.newUserId = getUserId(subject);
            this.newSubject = subject;
            this.sourceSiteId = siteId;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private void unBecomeUser() {
            this.newSubject = null;
        }

        public Subject getActualSubject() {
            if (newSubject != null) {
                return newSubject;
            } else {
                return subject;
            }
        }

        public Long getActualUserId() {
            if (newSubject != null) {
                return newUserId;
            } else {
                return userId;
            }
        }


        public boolean isBecomedUser() {
            return newSubject != null;
        }

        public long getSourceSiteId() {
            return sourceSiteId;
        }

        public boolean isLoggedIn() {
            return userId != null;
        }

        public int getLoginResult() {
            return loginResult;
        }

        public void setActionMessages(ActionMessages errors) {
            this.errors = errors;
    }

        public ActionMessages  getActionMessages() {
            return this.errors;
        }

        public void setModuleId(Long moduleId) {
            this.moduleId = moduleId;
        }

        public Long getModuleId() {
            return this.moduleId;
        }
    }

    /**
     * Save errors which will be displayed when login fails
     * @param request HttpServletRequest
     */
    public static void processErrors(HttpServletRequest request) {

        String dgSessionId = getDigiSessionId(request);
        if( dgSessionId != null ) {
        LoginInfo loginInfo = null;
            loginInfo = getLoginInfo(dgSessionId);
            if (loginInfo != null) {
                ActionMessages errors = loginInfo.getActionMessages();
                if (errors != null) {
                    logger.debug("Set action errors in request, error size is " + errors.size());
                    request.setAttribute(Globals.ERROR_KEY, errors);
                    request.setAttribute(Constants.DG_UM_MODULE_ID,loginInfo.getModuleId());
                    loginInfo.setActionMessages(null);
                    saveLoginInfo(dgSessionId, loginInfo);
                }
            }
        }
    }

    public static void saveErrors(HttpServletRequest request, String sessionId, Long moduleId, ActionMessages errors) {
        LoginInfo loginInfo = getLoginInfo(sessionId);
        if (loginInfo == null) {
            loginInfo = new LoginInfo();
        }
        loginInfo.setModuleId(moduleId);
        loginInfo.setActionMessages(errors);
        saveLoginInfo(sessionId, loginInfo);
    }

    public static HttpServletRequest processLogin(HttpServletRequest request, boolean digiStyleLogin) throws
        IOException, RedirectRequiredException {

        if (DgUtil.isIgnoredUserAgent(request)) {
            logger.debug("User agent (" + request.getHeader("user-agent") +
                         ") is ignored");
            return request;
        }

        /** @todo I don't like this solution. Mikheil */
        /*
        String requestURL = DgUtil.getRequestUrl(request).toString();
        if (requestURL.indexOf(".rss") != -1 ||
            requestURL.indexOf("logonAction.do") != -1 ||
            requestURL.indexOf("login.do") != -1) {
            logger.debug("It's a single sign on action");
            return request;
        }

        if (request.getParameter(Constants.DG_SESSION_ID_PARAMETER) != null) {
            logger.debug("Session id is set");
            invalidate(request);
            return request;
        } */

        if (digiStyleLogin) {
            String sessionId = getDigiSessionId(request);
            if (sessionId != null) {
                LoginInfo loginInfo = getLoginInfo(sessionId);
                if (loginInfo != null) {
                    logger.debug("Session is active");
                    if (loginInfo.isBecomedUser()) {
                        Site site = RequestUtils.getSite(request);
                        if (!DgSecurityManager.permitted(loginInfo.subject, site,
                                                         SitePermission.INT_ADMIN)) {
                            logger.debug("Restoring original user. The source subject " +
                                         loginInfo.subject +
                                         " does not have permission to admin site# " +
                                         site.getId());
                            loginInfo.unBecomeUser();
                            saveLoginInfo(sessionId, loginInfo);
                        }
                    }

                    //validateUserIntoSession(request, loginInfo);
                    //return new SSOHttpServletRequestWrapper(request, loginInfo);
                    return request;
                }
            }
        } else {
            throw new UnsupportedOperationException("Non-digi style login is not implemented yet");
        }

        throw new RedirectRequiredException();
    }

    /**
     * Get user from cookies, log in and return
     * @param request HttpServletRequest
     * @return User
     */
    public static Subject getUserFromCookie(HttpServletRequest request) {
        String userName = null;
        String password = null;

        // generate user object by cookie if any
        Cookie[] cookies = request.getCookies();
        String decode = null;
        if (cookies != null) {
            for (int loop = 0; loop < cookies.length; loop++) {
                if (cookies[loop].getName().equals(getLoginCookieName())) {
                    decode = DgUtil.decodeBase64(cookies[loop].getValue());
                    String userNamePassword[] = decode.split(":");
                    if (userNamePassword.length == 2) {
                        userName = userNamePassword[0];
                        password = userNamePassword[1];

                        logger.debug("User name from the cookie is: " +
                                     userName);
                        break;
                    }
                }
            }
        }

        if (userName == null) {
            logger.debug("No user found in the cookie");
            return null;
        }

        return loginUserByScrambledPassword(userName, password);
    }

    public static String getLoginCookieName() {
        DigiConfig config = null;
        try {
            config = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon Site", ex);
            throw new RuntimeException("Unable to determine login site", ex);
        }
        if (config == null || config.getDomainPrefix() == null) {
            return Constants.COOKIE_NAME;
        }
        else {
            String cookieName = config.getDomainPrefix() +
                Constants.COOKIE_NAME;
            return cookieName.trim();
        }
    }

    public static String getSessionIdCookieName() {
        DigiConfig config = null;
        try {
            config = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon Site", ex);
            throw new RuntimeException("Unable to determine login site", ex);
        }
        if (config == null || config.getDomainPrefix() == null) {
            return DIGI_SESSION_COOKIE;
        }
        else {
            String cookieName = config.getDomainPrefix() +
                DIGI_SESSION_COOKIE;
            return cookieName.trim();
        }
    }

    public static void redirectToLoginSite(HttpServletRequest request,
                                           HttpServletResponse response) throws
        java.io.IOException {
        saveSessionIdToCookie(request, response, UNASSIGNED_SESSION_ID);
        try {

            String url = DgUtil.getFullURL(request);
            logger.debug("Redirecting to login site. Full url=" + url);

            StringBuffer redirectUrl = new StringBuffer();
            redirectUrl.append(Constants.REFERRER_PARAM);
            redirectUrl.append("=");
            redirectUrl.append(generateReferrer(request));
            redirectUrl.append("&");
            redirectUrl.append("autoLogin=true");

            String destURL = getLoginURL(request, redirectUrl.toString());
            logger.debug("Redirecting to: " + destURL);
            response.sendRedirect(destURL);
        }
        catch (IOException ex1) {
            logger.error("Could not redirect to Login Site", ex1);
            throw ex1;
        }
    }

    public static String getLoginSiteURL(HttpServletRequest request) {
        DigiConfig config = null;
        try {
            config = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon Site", ex);
            throw new RuntimeException("Unable to determine login site", ex);
        }

        if (config == null) {
            throw new IllegalArgumentException("<logon-site> tag is not defined in the configuration file");
        }
        String loginSite = config.getLogonSite().getContent();

        if (loginSite != null && loginSite.trim().length() != 0) {
            logger.debug("Got login site from <logon-site> tag content: " + loginSite);
            if (!loginSite.endsWith("/")) {
                loginSite += '/';
            }
            return loginSite;
        }

        if (config.getLogonSite().getHost() != null &&
            config.getLogonSite().getHost().trim().length() != 0) {

            loginSite = SiteUtils.getSiteURL(SiteUtils.prefixDomainName(config.getLogonSite().getHost()),
                                             config.getLogonSite().getPath(),
                                             request.getScheme(),
                                             request.getServerPort(),
                                             request.getContextPath()) + "/";
        }

        if (loginSite != null && loginSite.trim().length() != 0) {
            logger.debug(
                "Assembled login site from <logon-site> tag's host and port attributes: " +
                loginSite);
            return loginSite;
        }

        if (config.getLogonSite().getId() != null || config.getLogonSite().getId().trim().length() !=0) {
            Site loginSiteObj = SiteCache.lookupByName(config.getLogonSite().getId().trim());
            if (loginSiteObj != null) {
                SiteDomain loginSiteDom = SiteUtils.getDefaultSiteDomain(loginSiteObj);
                loginSite = SiteUtils.getSiteURL(loginSiteDom,
                                                 request.getScheme(),
                                                 request.getServerPort(),
                                                 request.getContextPath()) + "/";
            }
        }
        if (loginSite == null && loginSite.trim().length() != 0) {
            logger.error("Login site URL is null in the configuration");
            throw new IllegalArgumentException(
                "Login site URL is null in the configuration");
        } else {
            logger.debug("Assembled login site from <logon-site> tag's siteId attribute: " +
                loginSite);
        }

        return loginSite;
    }

    /**
     * Perform automatic login and create DiGi session identifier
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return DiGi session identifier
     */
    public static String autoLogin(HttpServletRequest request,
                                   HttpServletResponse response) {
        // Get session id (if any)
        String oldSessionId = getDigiSessionId(request);
        logger.debug("Performing automatic login. Old session id is: " +
                     oldSessionId);
        boolean createNewSession = false;
        LoginInfo loginInfo = null;
        LoginInfo oldLoginInfo = null;
        if (oldSessionId != null) {
            oldLoginInfo = loginInfo = getLoginInfo(oldSessionId);
        }
        Subject subject = null;

        if (loginInfo != null) {
            // Checks if login info is valid
            if (loginInfo.isLoggedIn()) {
                subject = loginInfo.getActualSubject();
                // If user is null, it means that loginInfo is invalid
                // and session must be invalidated
                createNewSession = subject == null;
                logger.debug("User was logged in. createNewSession=" +
                             createNewSession);
            }
        }
        else {
            subject = getUserFromCookie(request);
            createNewSession = true;
            logger.debug("Creating new session, because loginInfo was empty");
        }

        /** @todo I think, we must add some code here to prevent appearing login fault messages after switching to other domain,  */
        if (createNewSession) {
            if (subject == null) {
                loginInfo = new LoginInfo();
            }
            else {
                loginInfo = new LoginInfo(subject);
            }
            String sessionId = generateSessionId(request, loginInfo);
            if (oldLoginInfo != null) {
                invalidateSession(oldSessionId);
            }
            saveSessionIdToCookie(request, response, sessionId);

            return sessionId;
        }

        return oldSessionId;
    }

    /**
     * Performs login based on username and open-text password, starts new
     * DiGi session and returns its id
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param userName name of the user (usually - email)
     * @param password open-text password
     * @param saveLogin if true, saves login cookie
     * @return DiGi session id
     */
    public static String loginByCredentials(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String userName, String password,
                                            boolean saveLogin) {
        String oldSessionId = getDigiSessionId(request);
        LoginInfo loginInfo;
        try {
            Subject subject = loginUser(userName, password);
            loginInfo = new LoginInfo(subject);
        }
        catch (GeneralLoginException ex) {
            logger.debug("General error while loging in user: " + userName, ex);
            loginInfo = new LoginInfo(LOGIN_RESULT_INVALID);
        }
        catch (UserBannedException ex) {
            logger.debug("User " + userName + " is banned");
            loginInfo = new LoginInfo(LOGIN_RESULT_BANNED);
        }
        catch (LoginException ex) {
            logger.debug("Can not log in user " + userName, ex);
            loginInfo = new LoginInfo(LOGIN_RESULT_INVALID);
        }
        if (saveLogin) {
            saveLoginCookie(request, response, userName, password);
        }
        else {
            removeLoginCookie(request, response);
        }

        String sessionId = generateSessionId(request, loginInfo);
        if (oldSessionId != null) {
            invalidateSession(oldSessionId);
        }
        saveSessionIdToCookie(request, response, sessionId);

        return sessionId;

    }

    /**
     * Performs logout and starts new DiGi session
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return DiGi session id
     */
    public static String logout(HttpServletRequest request,
                                HttpServletResponse response) {
        LoginInfo loginInfo = new LoginInfo(LOGIN_RESULT_LOGOUT);
        String oldSessionId = getDigiSessionId(request);

        String sessionId = generateSessionId(request, loginInfo);
        if (oldSessionId != null) {
            invalidateSession(oldSessionId);
        }
        saveSessionIdToCookie(request, response, sessionId);
        removeLoginCookie(request, response);

        return sessionId;

    }

    /**
     * Invalidates DiGi session
     * @param sessionId session identity
     */
    public static void invalidateSession(String sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException(
                "sessionId parameter must be not-null");
        }

        clearLoginInfo(sessionId);
    }

    /**
     * Assemble redirection URL to pass DiGi session ID to referrer site and
     * then perform referring
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param sessionId DiGi session ID
     * @param localReferrer if referrer is not defined by the request,
     * determines the destination URL which must be loaded when authenticaton
     * is done
     * @throws IOException if redirection is impossible
     */
    public static void passSessionIdToReferrer(HttpServletRequest request,
                                               HttpServletResponse response,
                                               String sessionId, String localReferrer) throws
        IOException {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId must be non-null");
        }
        String referrer = request.getParameter(Constants.REFERRER_PARAM);
        String referrerRoot;
        logger.debug("The original referrer is: " + referrer);
        if (referrer != null && referrer.trim().length() != 0) {
            // Get referrer URL
            referrerRoot = DgSecurityManager.getReferrerSite(request, true);
        }
        else {
            referrer = null;
            // Go to the site's root
            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            referrerRoot = SiteUtils.getSiteURL(currentDomain,
                                                request.getScheme(),
                                                request.getServerPort(),
                                                request.getContextPath());
        }
        logger.debug("referrerRoot is: " + referrerRoot);
        StringBuffer sb = new StringBuffer(255);
        sb.append(referrerRoot);
        sb.append("/j_acegi_digi_processing_filter?");
        sb.append(Constants.DG_SESSION_ID_PARAMETER).append("=").append(
            DgUtil.encodeString(sessionId));

        if( referrer == null ) {
            referrer = DgUtil.encodeString(localReferrer);
        }

        if (referrer != null) {
            sb.append("&").append(Constants.REFERRER_PARAM).append("=").append(
                DgUtil.encodeString(referrer));
        }
        String passURL = sb.toString();
        logger.debug("Passing session id to referrer using URL: " + passURL);

        response.sendRedirect(passURL);
    }

    /**
     * Log in based on DiGi session identifier. Session Id must be valid, i.e.
     * generated by login server, etc.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param sessionId String
     * @param replace when true, returns login result, stored into DiGi session,
     * but replaces login info in the DiGi session with the "right login" info
     * so that during next call, the other result will be returned
     * @return login result, stored into DiGi session
     * @throws DgException if it's unable to set user preferences
     */
    public static LoginInfo loginBySessionId(HttpServletRequest request,
                                             HttpServletResponse response,
                                             String sessionId, boolean replace) throws
        DgException {
        logger.debug("Logging in by session id:" + sessionId);
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId is null");
        }

        LoginInfo loginInfo = getLoginInfo(sessionId);
        if (loginInfo == null) {
            logger.warn("Invalid session id: " + sessionId + ". Starting session as guest.");
            loginInfo =  new LoginInfo();
            saveLoginInfo(sessionId, loginInfo);

        }
        LoginInfo prevLoginInfo = loginInfo;
        if (replace && !loginInfo.isLoggedIn() &&
            loginInfo.getLoginResult() != LOGIN_RESULT_OK) {
            loginInfo = new LoginInfo();
            loginInfo.setActionMessages(prevLoginInfo.getActionMessages());
            saveLoginInfo(sessionId, loginInfo);
        }
        invalidate(request);

        if (response != null) {
            saveSessionIdToCookie(request, response, sessionId);
        }

        if (loginInfo.isLoggedIn()) {
            validateUserIntoSession(request, loginInfo);
        }

        return prevLoginInfo;
    }

    /**
     * If alwaysLogin set to false, compares user, got from session to one
     * defined by loginInfo. If they are different, authenticates user by
     * credentials, defined by loginInfo. if alwaysLogin is set to true,
     * does this tasks despite of difference/equality of users in loginInfo and
     * session.
     * <br>
     * In the end, if new user was authenticated, puts it into session and loads
     * its preferences
     * @param request HttpServletRequest
     * @param loginInfo LoginInfo
     * @throws DgException if any error occur
     */
    private static void validateUserIntoSession(HttpServletRequest request,
                                                LoginInfo loginInfo) throws
        DgException {
        if (loginInfo.isLoggedIn()) {
            Long userId = loginInfo.getActualUserId();
            logger.debug("Validating user# " + userId + " into session");

            User user = getUser(userId);

            user.setSubject(loginInfo.getActualSubject());
            SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);

            initializeUser(request, user, siteDomain.getSite());
        } else {
            request.getSession().removeAttribute(Constants.USER);
        }
    }

    public static void validateUserFromRequest(HttpServletRequest request) throws
        DgException {
        request.getSession().removeAttribute(Constants.USER);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            throw new DgException("No SecurityContext is available. Please configure the system properly");
        }

        Authentication currentAuth = securityContext.getAuthentication();
        if (currentAuth == null) {
            return;
        }

        // do not provide any additional operations with anonymous user.
        // treat anonymous in the same way as empty user
        if (currentAuth.getPrincipal() == null || "anonymousUser".equals(currentAuth.getPrincipal())) {
            return;
        }

        User user;
        Object principal = currentAuth.getPrincipal();
        if (principal instanceof Long) {
            Long userId = (Long)principal;
            user = UserUtils.getUser(userId);
        } else {
            String userName;
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails)principal;
                userName = userDetails.getUsername();
            } else {
                userName = principal.toString();
            }
            user = getUser(userName);
        }

        initializeUser(request, user, null);
    }

    private static void initializeUser(HttpServletRequest request, User user, Site site) throws
        DgException {
        if (site == null) {
            site = RequestUtils.getSite(request);
        }
        //Set preferences for site
        UserPreferences userPreferences = getUserPreferences(user,site);
        if (userPreferences == null) {
            // try to get root site preferences
            userPreferences = getUserPreferences(user, DgUtil.getRootSite(site));
            if( userPreferences == null )
                userPreferences = new UserPreferences(user, site);
        }
        user.setUserPreference(userPreferences);

        Subject subject = UserUtils.fillUserSubject(null, user);
        user.setSubject(subject);

        request.getSession().setAttribute(Constants.USER, user);
        request.setAttribute(Constants.REQUEST_USER, user);
    }

    public static ActionForward getFailureForward(HttpServletRequest request,
                                                  ServletContext servletContext) throws
        Exception {
        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();

        // If AuthFailed layout is defined for site configuration
        // redirect there. If not - simply display error message
        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);

            if (viewConfig.isLayoutDefined("AuthFailed")) {
                return new ActionForward("/showLayout.do?layout=AuthFailed", true);

            }

        String path = DgSecurityManager.getReferrerSite(request, false); //getReferrerPath(request);
        return new ActionForward(path, true);
    }

    /**
     * Generates and returns unique DiGi session ID and puts value there
     * @param request HttpServletRequest
     * @param value value to assign to DiGi session. Its strongly recommended
     * to pass not-null values
     * @return unique DiGi session ID
     */
    private static String generateSessionId(HttpServletRequest request,
                                            LoginInfo value) {
        String sessionId = request.getSession().getId();
        String dgSessionId = DgUtil.generateUID(sessionId, true);

        while (!dgSessionId.equals(UNASSIGNED_SESSION_ID) && getLoginInfo(dgSessionId) != null) {
            dgSessionId = DgUtil.generateUID(sessionId, true);
        }
        if (value != null) {
            saveLoginInfo(dgSessionId, value);
        }

        return dgSessionId;
    }

    /**
     * Checks if action is "start new session" action
     * @param action action path
     * @return true, if it is a "start new session" action
     * @deprecated this must be removed at all
     */
    public static boolean isNewSessionAction(String action) {
        if (action.endsWith("/newSession")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if redirect to referrer is possible for guest users
     * @param request HttpServletRequest
     * @return true, if redirect is possible and false if not
     */
    public static boolean isRedirectPossible(HttpServletRequest request) {
        String actionMapping = DgSecurityManager.getActionMappingFromReferrer(
            request);

        String []values = DgUtil.parseUrltoModuleInstanceAction(actionMapping);

        logger.debug("befor actionMapping: " + actionMapping);

        if( values != null ) {
            actionMapping = "/" + values[0] + "/" + values[2];
            logger.debug("after actionMapping: " + actionMapping);
        }

        ArrayList requiredPermissions = null;
        ModuleConfig moduleConfig = ModuleUtils.getInstance().
            getModuleConfig(request, request.getSession().getServletContext());

        RequestProcessor processor = (RequestProcessor) request.getSession().
            getServletContext().
            getAttribute(Globals.REQUEST_PROCESSOR_KEY + moduleConfig.getPrefix());

        if (processor != null) {
            logger.debug("Request Processor " );
            HashMap actionPermissions = processor.getActionPermissions();
            requiredPermissions = (ArrayList) actionPermissions.get(
                actionMapping);
        }

        return (actionMapping != null && requiredPermissions == null);
    }


    /**
     * Returns DiGi session id, associated with the current request
     * @param request HttpServletRequest
     * @return DiGi session id
     */
    public static String getDigiSessionId(HttpServletRequest request) {
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        String decode = null;
        if (cookies != null) {
            for (int loop = 0; loop < cookies.length; loop++) {
                String cookieName = getSessionIdCookieName();
                if (cookies[loop].getName().equals(cookieName)) {
                    sessionId = DgUtil.decodeString(cookies[loop].getValue());
                    break;
                }
            }
        }

        return sessionId;
    }

    public static void saveSessionIdToCookie(HttpServletRequest request,
                                              HttpServletResponse response,
                                              String sessionId) {
        SiteDomain currDomain = RequestUtils.retreiveSiteDomain(request);
        /**
         * @todo add null value handling
         */

        // report save session id cookie
        logger.debug("Saving session id: " + sessionId);

        // set a cookie with the username in it
        Cookie sessionIdCookie = new Cookie(getSessionIdCookieName(),
                                            DgUtil.encodeString(sessionId));

        // set life time until browser exit
        sessionIdCookie.setMaxAge( -Integer.MAX_VALUE);
        sessionIdCookie.setDomain(currDomain.getSiteDomain());

        String cookiePath = request.getContextPath() +
            (currDomain.getSitePath() == null ? "" :
             currDomain.getSitePath());
        cookiePath = cookiePath.trim();
        if (cookiePath.length() == 0) {
            cookiePath = "/";
        }

        sessionIdCookie.setPath(cookiePath);

        response.addCookie(sessionIdCookie);
    }

    private static Long getUserId(Subject subject) {
        // get user from public credentials
        Set principals = subject.getPrincipals(UserPrincipal.class);
        if (principals == null) {
            return null;
        }
        Iterator iter = principals.iterator();
        while (iter.hasNext()) {
            UserPrincipal item = (UserPrincipal)iter.next();
            return new Long(item.getUserId());
        }
        return null;
    }


    /**
     * Log in user using original username and scrambled password. This method
     * must be used when logging in through cookie
     * @param userName name of the user (usually - email)
     * @param scrambledPassword scrambled password
     * @return Subject object if login is successfull and null on failure
     */
    private static Subject loginUserByScrambledPassword(String userName,
        String scrambledPassword) {
        LoginContext lc = null;
        try {

            lc = new LoginContext("digijava",
                                  new PassiveCallbackHandler(userName,
                scrambledPassword, true));

            // just login
            lc.login();

            Subject subject = lc.getSubject();

            return subject;
        }
        catch (LoginException ex) {
            logger.error("Unable to log in user by cookie" + userName, ex);
            return null;
        }
        catch (SecurityException ex) {
            logger.error("Unable to log in user by cookie" + userName, ex);
            return null;
        }
    }

    /**
     * Log in user using username and open-text password
     * @param userName name of the user (usually - email)
     * @param password open-text password
     * @return User object if login is successfull and null on failure
     * @throws LoginException if user is banned or username/password is invalid
     * @throws GeneralLoginException if general login error occured
     */
    private static Subject loginUser(String userName, String password) throws
        GeneralLoginException, LoginException {
        if (userName == null || password == null) {
            throw new GeneralLoginException(
                "Username and password must be non-null");
        }
        LoginContext lc = null;
        try {

            // create login context and put passive callback handle ,
            // see org.digijava.kernel.security.PassiveCallbackHandler
            lc = new LoginContext("digijava",
                                  new PassiveCallbackHandler(userName, password));

            // just login
            lc.login();

            Subject subject = lc.getSubject();

            return subject;
        }
        catch (LoginException ex) {
            throw ex;
        }
        catch (SecurityException ex) {
            throw new GeneralLoginException(ex);
        }
    }

    private static void removeLoginCookie(HttpServletRequest request,
                                          HttpServletResponse response) {
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);
        logger.debug("Removing login cookie");
        // Try to remove logon cookie

        // see if the cookie exists and remove accordingly
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int loop = 0; loop < cookies.length; loop++) {
                if (cookies[loop].getName().equals(getLoginCookieName())) {
                    cookies[loop].setMaxAge( -1);
                    cookies[loop].setDomain(currDomain.getSiteDomain());

                    String cookiePath = request.getContextPath() +
                        (currDomain.getSitePath() == null ? "" :
                         currDomain.getSitePath());
                    cookiePath = cookiePath.trim();
                    if (cookiePath.length() == 0) {
                        cookiePath = "/";
                    }

                    cookies[loop].setPath(cookiePath);

                    response.addCookie(cookies[loop]);
                    logger.debug("Removing cookie: " + cookies[loop].getDomain() +
                                 cookies[loop].getPath() + "/" +
                                 cookies[loop].getMaxAge());
                }
            }
        }
    }

    private static void saveLoginCookie(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String userName, String password) {
        /**
         * @todo cookie can be saved only for login site
         */
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);

        // set user cooke if checked save login
        String userSignOn = userName.trim() + ":" + password.trim();

        // report save logon cookie
        logger.debug("Saving login cookie for user: " + userName);

        // set a cookie with the username in it
        Cookie userNameCookie = new Cookie(getLoginCookieName(),
                                           DgUtil.encodeBase64(userSignOn));

        // set cookie to last for one month
        userNameCookie.setMaxAge(Integer.MAX_VALUE);
        userNameCookie.setDomain(currDomain.getSiteDomain());

        String cookiePath = request.getContextPath() +
            (currDomain.getSitePath() == null ? "" :
             currDomain.getSitePath());
        cookiePath = cookiePath.trim();
        if (cookiePath.length() == 0) {
            cookiePath = "/";
        }

        userNameCookie.setPath(cookiePath);

        response.addCookie(userNameCookie);
    }

    /**
     * Clear all keys from user's session
     * @param request HttpServletRequest
     */
    public static void invalidate(HttpServletRequest request) {
        HttpSession currentSess = request.getSession(true);
        /*
        Object redirectionFlag = currentSess.getAttribute(Constants.
            DG_REDIRECT_LOGON);
*/
        // WORKAROUND: the following, commented code does not work
        // for AJP connector and we have to reset session manually :((
        /*
                 // Invalidate old session
                 currentSess.invalidate();
                 // Start new session
                 currentSess = request.getSession(true);
         */

        ArrayList names = new ArrayList();
        Enumeration sessAttribs = currentSess.getAttributeNames();
        while (sessAttribs.hasMoreElements()) {
            String attrib = (String) sessAttribs.nextElement();
            names.add(attrib);
        }
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            currentSess.removeAttribute(item);
        }
    }

    /**
     * Generate URL in form http://domain[:port]/[context][/path]~[other-part]
     * @param request HttpServletRequest
     * @return String
     */
    public static String generateReferrer(HttpServletRequest request) {

        //SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        String url = SiteUtils.getSiteURL(siteDomain, request.getScheme(),
                                          request.getServerPort(),
                                          request.getContextPath());

        String urlFull = DgUtil.getFullURL(request);

        int index = urlFull.indexOf(url);
        if (index != -1) {
            index = url.length();
            String referrer = DgUtil.encodeString(url + "~" +
                                                  urlFull.substring(index,
                urlFull.length()));
            logger.debug("Referrer generated is: " + referrer);
            return referrer;
        }
        else {
            throw new RuntimeException("Unable to generate referrer");
        }
    }

    public static void becomeUser(long userId, HttpServletRequest request) throws DgException {
        LoginInfo loginInfo = null;

        String sessionId = getDigiSessionId(request);

        if (sessionId != null) {
            loginInfo = getLoginInfo(sessionId);
        }

        if (loginInfo == null) {
            throw new DgException("New DiGi session must be started to become user");
        }
        Site site = RequestUtils.getSite(request);
        User newUser = getUser(new Long(userId));
        Subject subject = UserUtils.getUserSubject(newUser);
        loginInfo.becomeUser(subject, site.getId().longValue());
        saveLoginInfo(sessionId, loginInfo);

        //get user from session
        User sourceUser = RequestUtils.getUser(request);
        //save user
        request.getSession(true).setAttribute(Constants.SOURCE_USER, sourceUser);
        validateUserIntoSession(request, loginInfo);
    }

    private static User getUser(Long id) throws DgException {
        User result = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            result = session.load(User.class, id);
        }

        catch (Exception ex) {
            throw new DgException("Unable to get User", ex);
        }
        return result;
    }

    private static User getUser(String email) throws DgException {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + User.class.getName() + " u where lower(u.email) =:email ");
            q.setParameter("email", email.toLowerCase());
            q.setCacheable(true);

            List results = q.list();
            if (results.size() == 0) {
                return null;
            } else {
                return (User)results.get(0);
            }
        }

        catch (Exception ex) {
            throw new DgException("Unable to get User", ex);
        }
    }

    /**
     * Clone of UserUtils.getUserPreferences. Difference is that this one uses
     * session-per-request pattern
     * @param user User
     * @param site Site
     * @return UserPreferences
     * @throws DgException
     */
    private static UserPreferences getUserPreferences(User user, Site site) throws
       DgException {

     logger.debug("Searching user preferences for user#" + user.getId() +
              " site#" + site.getId());

     UserPreferences result = null;
     org.hibernate.Session session = null;

     UserPreferencesPK key = new UserPreferencesPK(user, site);

     try {
         session = PersistenceManager.getRequestDBSession();
         result = (UserPreferences) session.get(UserPreferences.class, key);

     }
     catch (ObjectNotFoundException ex) {
         logger.debug("User preferences does not exist");
     }
     catch (Exception ex) {
         logger.warn("Unable to get user preferences", ex);
         throw new DgException(ex);
     }

     return result;
    }

    /**
     * Create URL used to log out
     * @param referrerUrl String
     * @return String
     */
    public static String getLogoutURL(HttpServletRequest request, String referrerUrl) {
        return getLoginSiteURL(request) + "um~user/logout.do?" + referrerUrl;
    }

    /**
     * Create URL used to log out
     * @param referrerUrl String
     * @return String
     */
    public static String getLoginURL(HttpServletRequest request, String referrerUrl) {
        return getLoginSiteURL(request) + "um~user/login.do?" + referrerUrl;
    }

    public static LoginInfo getLoginInfo(String sessionId) {
        AbstractCache sessionCache = DigiCacheManager.getInstance().getCache(LOGIN_CACHE_REGION_ROOT);
        return (LoginInfo) sessionCache.get(sessionId);
    }

    private static void saveLoginInfo(String sessionId, LoginInfo loginInfo) {
        AbstractCache sessionCache = DigiCacheManager.getInstance().getCache(LOGIN_CACHE_REGION_ROOT);
        sessionCache.put(sessionId, loginInfo);
    }

    private static void clearLoginInfo(String sessionId) {
        AbstractCache sessionCache = DigiCacheManager.getInstance().getCache(LOGIN_CACHE_REGION_ROOT);
        sessionCache.evict(sessionId);
    }

}

class GeneralLoginException
    extends Exception {
    GeneralLoginException(Throwable cause) {
        super(cause);
    }

    GeneralLoginException(String message) {
        super(message);
    }
}
