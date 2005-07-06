/*
 *   HttpLoginManager.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Feb 20, 2004
 * 	 CVS-ID: $Id: HttpLoginManager.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.kernel.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.RequestProcessor;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import net.sf.swarmcache.ObjectCache;
import org.digijava.kernel.security.principal.UserPrincipal;

public final class HttpLoginManager {

    private static Logger logger = Logger.getLogger(HttpLoginManager.class);

    final static String LOGIN_CACHE_REGION =
        "org.digijava.kernel.login_region";
    private final static String DIGI_SESSION_COOKIE = "digi_session_id";

    public final static int LOGIN_RESULT_OK = 0;
    public final static int LOGIN_RESULT_INVALID = 1;
    public final static int LOGIN_RESULT_BANNED = 2;
    public final static int LOGIN_RESULT_LOGOUT = 3;

    public final static String LOGIN_ERROR_KEY =
        "org.digijava.kernel.login_error";

    public static final class LoginInfo
        implements Serializable {

        private static final long serialVersionUID = 1;

        private Long userId;
        private String userName;
        private String password;
        private int loginResult;
        private ActionErrors errors;
        private Long moduleId;
        private Long sourceUserId;
        private long sourceSiteId;

        private LoginInfo() {
            this.sourceUserId = null;
            this.userId = null;
            this.errors = null;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private LoginInfo(int loginResult) {
            this.sourceUserId = null;
            this.userId = null;
            this.errors = null;
            this.loginResult = loginResult;
        }

        private LoginInfo(long userId, String userName, String password) {
            this.sourceUserId = null;
            this.userId = new Long(userId);
            this.userName = userName;
            this.password = password;
            this.errors = null;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private void becomeUser(long userId, long siteId) throws DgException {
            if (!isLoggedIn()) {
                throw new DgException("You must log in to become another user");
            }
            if (sourceUserId != null) {
                throw new DgException("You can not become user twise");
            }
            this.sourceUserId = this.userId;
            this.userId = new Long(userId);
            this.sourceSiteId = siteId;
            this.loginResult = LOGIN_RESULT_OK;
        }

        private void unBecomeUser() {
            if (sourceUserId != null) {
                userId = sourceUserId;
                sourceUserId = null;
            }
        }

        public boolean isBecomedUser() {
            return sourceUserId != null;
        }

        public long getSourceSiteId() {
            return sourceSiteId;
        }

        public boolean isLoggedIn() {
            return userId != null;
        }

        public long getUserId() {
            if (userId == null) {
                throw new IllegalStateException("User is not authenticated");
            }
            return userId.longValue();
        }

        public String getUserName() {
            if (userId == null) {
                throw new IllegalStateException("User is not authenticated");
            }
            return userName;
        }

        public String getPassword() {
            if (userId == null) {
                throw new IllegalStateException("User is not authenticated");
            }
            return password;
        }

        public int getLoginResult() {
            return loginResult;
        }

        public void setActionErrors(ActionErrors errors) {
            this.errors = errors;
        }

        public ActionErrors  getActionErrors() {
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
     *
     * @param request
     * @param response
     */
    public static void processErrors(HttpServletRequest request) {

        String dgSessionId = getSessionIdFromCookie(request);
        if( dgSessionId != null ) {
        LoginInfo loginInfo = null;
            ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(LOGIN_CACHE_REGION);
            loginInfo = (LoginInfo) sessionCache.get(dgSessionId);
            if (loginInfo != null) {
                ActionErrors errors = loginInfo.getActionErrors();
                if (errors != null) {
                    logger.debug("Set action errors in request, error size is " + errors.size());
                    request.setAttribute(Globals.ERROR_KEY, errors);
                    request.setAttribute(Constants.DG_UM_MODULE_ID,loginInfo.getModuleId());
                    loginInfo.setActionErrors(null);
                    sessionCache.put(LOGIN_CACHE_REGION, loginInfo);
                }
            }
        }
    }

    public static boolean processLogin(HttpServletRequest request,
                                       HttpServletResponse response) throws
        IOException, DgException {

        /**
         * @todo validate user object here
         */
        if (DgUtil.isIgnoredUserAgent(request) ) {
            logger.debug("User agent (" + request.getHeader("user-agent") +
                         ") is ignored");
            return false;
        }

        if (!DgUtil.isCookieEnable(request) ) {
            logger.debug("User agent (" + request.getHeader("user-agent") +
                         ") cookie disabled");
            return false;
        }

        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        String sessionId = getSessionIdFromCookie(request);
        //(String) request.getSession().getAttribute(Constants.DG_SESSION_ID);
        if (sessionId != null) {
            LoginInfo loginInfo = (LoginInfo) sessionCache.get(sessionId);
            if (loginInfo != null) {
                logger.debug("Session is active");
                if (loginInfo.isBecomedUser()) {
                    Site site = RequestUtils.getSite(request);
                    if (site.getId().longValue() != loginInfo.getSourceSiteId()) {
                        logger.debug("Restoring original user");
                        loginInfo.unBecomeUser();
                        sessionCache.put(sessionId, loginInfo);
                    }
                }

                validateUserIntoSession(request, loginInfo, false);
                return false;
            }
        }

        /** @todo I don't like this solution. Mikheil */
        String requestURL = DgUtil.getRequestUrl(request).toString();
        if (SiteConfigUtils.isLogonSite(request) /* &&
                        (requestURL.indexOf("login.do") != -1 ||
                         requestURL.indexOf("logonAction.do") != -1)*/) {
            logger.debug("It's a single sign on action");
            return false;
        }

        if (request.getParameter(Constants.DG_SESSION_ID_PARAMETER) != null) {
            logger.debug("Session id is set");
            invalidate(request);
            return false;
        }

        redirectToLoginSite(request, response);
        return true;
    }

    /**
     * Get user from cookies, log in and return
     * @param request HttpServletRequest
     * @return User
     */
    public static User getUserFromCookie(HttpServletRequest request) {
        String userName = null;
        String password = null;

        // generate user object by cookie if any
        Cookie[] cookies = request.getCookies();
        String decode = null;
        if (cookies != null) {
            for (int loop = 0; loop < cookies.length; loop++) {
                if (cookies[loop].getName().equals(Constants.COOKIE_NAME)) {
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

    public static void redirectToLoginSite(HttpServletRequest request,
                                           HttpServletResponse response) throws
        java.io.IOException {
        try {

            String url = DgUtil.getFullURL(request);
            logger.debug("Redirecting to login site. Full url=" + url);

            StringBuffer redirectUrl = new StringBuffer();
            redirectUrl.append(getLoginSiteURL());
            redirectUrl.append("um");
            redirectUrl.append(DgUtil.getParamSeparator());
            redirectUrl.append("user/login.do?");
            redirectUrl.append(Constants.REFERRER_PARAM);
            redirectUrl.append("=");
            redirectUrl.append(generateReferrer(request));
            redirectUrl.append("&");
            redirectUrl.append("autoLogin=true");

            String destURL = redirectUrl.toString();
            logger.debug("Redirecting to: " + destURL);
            response.sendRedirect(destURL);
        }
        catch (IOException ex1) {
            logger.error("Could not redirect to Logon Site", ex1);
            throw ex1;
        }
    }

    public static String getLoginSiteURL() {
        DigiConfig config = null;
        try {
            config = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon Site", ex);
            throw new RuntimeException("Unable to determine login site", ex);
        }

        String loginSite = config == null ? null :
            config.getLogonSite().getContent();
        if (loginSite == null) {
            logger.error("Login site URL is null in the configuration");
            throw new IllegalArgumentException(
                "Login site URL is null in the configuration");
        }
        logger.debug("Login site URL is: " + loginSite);

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
        String oldSessionId = getSessionIdFromCookie(request);
        logger.debug("Performing automatic login. Old session id is: " +
                     oldSessionId);
        boolean createNewSession = false;
        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        LoginInfo loginInfo = null;
        LoginInfo oldLoginInfo = null;
        if (oldSessionId != null) {
            oldLoginInfo = loginInfo = (LoginInfo) sessionCache.get(
                oldSessionId);
        }
        User user = null;

        if (loginInfo != null) {
            // Checks if login info is valid
            if (loginInfo.isLoggedIn()) {
                user = getUserFromLoginInfo(loginInfo);
                // If user is null, it means that loginInfo is invalid
                // and session must be invalidated
                createNewSession = user == null;
                logger.debug("User was logged in. createNewSession=" +
                             createNewSession);
            }
        }
        else {
            user = getUserFromCookie(request);
            createNewSession = true;
            logger.debug("Creating new session, because loginInfo was empty");
        }

        /** @todo I think, we must add some code here to prevent appearing login fault messages after switching to other domain,  */
        if (createNewSession) {
            if (user == null) {
                loginInfo = new LoginInfo();
            }
            else {
                loginInfo = new LoginInfo(user.getId().longValue(),
                                          user.getEmail(), user.getPassword());
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
        String oldSessionId = getSessionIdFromCookie(request);
        LoginInfo loginInfo;
        try {
            User user = loginUser(userName, password);
            loginInfo = new LoginInfo(user.getId().longValue(), user.getEmail(),
                                      user.getPassword());
        }
        catch (LoginFailedException ex) {
            logger.debug("Failed to log in user: " + userName, ex);
            loginInfo = new LoginInfo(LOGIN_RESULT_INVALID);
        }
        catch (UserBannedException ex) {
            logger.debug("User " + userName + " is banned");
            loginInfo = new LoginInfo(LOGIN_RESULT_BANNED);
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
        String oldSessionId = getSessionIdFromCookie(request);

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
        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        sessionCache.clear(sessionId);
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
        if (referrer != null) {
            // Get referrer URL
            referrerRoot = DgSecurityManager.getReferrerSite(request, true);
        }
        else {
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
        sb.append("/um~user/newSession.do?");
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

        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        LoginInfo loginInfo = (LoginInfo) sessionCache.get(sessionId);
        if (loginInfo == null) {
            logger.warn("Invalid session id: " + sessionId + ". Starting session as guest.");
            loginInfo =  new LoginInfo();
            sessionCache.put(sessionId, loginInfo);

            /*
            throw new IllegalArgumentException("Invalid session id: " +
                                               sessionId);
             */
        }
        LoginInfo prevLoginInfo = loginInfo;
        if (replace && !loginInfo.isLoggedIn() &&
            loginInfo.getLoginResult() != LOGIN_RESULT_OK) {
            loginInfo = new LoginInfo();
            /*
            sessionCache.put(LOGIN_CACHE_REGION, loginInfo);
*/
        }
        invalidate(request);

        //request.getSession().setAttribute(Constants.DG_SESSION_ID, sessionId);
        saveSessionIdToCookie(request, response, sessionId);

        validateUserIntoSession(request, loginInfo, true);

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
     * @param alwaysLogin if true, do force user switch
     * @throws DgException if any error occur
     */
    private static void validateUserIntoSession(HttpServletRequest request,
                                                LoginInfo loginInfo,
                                                boolean alwaysLogin) throws
        DgException {
        if (loginInfo.isLoggedIn()) {
            User sessionUser = (User) request.getSession().getAttribute(
                Constants.USER);
            if (!alwaysLogin && sessionUser != null &&
                sessionUser.getId().equals(new Long(loginInfo.getUserId()))) {
                logger.debug(
                    "User, logged in into system, is valid and can be used");
                User user = UserUtils.getUser(sessionUser.getId());
                initializeUser(request, user);
                user.setSubject(sessionUser.getSubject());
                return;
            }

            logger.debug("Logging in user by login information to validate");
            User user = getUserFromLoginInfo(loginInfo);
            if (user != null) {
                initializeUser(request, user);
            }
        }
    }

    private static void initializeUser(HttpServletRequest request, User user) throws
        DgException {
        Site site = RequestUtils.getSite(request);
        //Set preferences for site
        UserPreferences userPreferences = UserUtils.getUserPreferences(user,site);
        if (userPreferences == null) {
            // try to get root site preferences
            userPreferences = UserUtils.getUserPreferences(user,DgUtil.getRootSite(site));
            if( userPreferences == null )
                userPreferences = new UserPreferences(user, site);
        }
        user.setUserPreference(userPreferences);
        request.getSession().setAttribute(Constants.USER, user);
    }

    public static ActionForward getFailureForward(HttpServletRequest request,
                                                  ServletContext servletContext) throws
        Exception {
        Site site = RequestUtils.getSite(request);

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
                                            Object value) {
        String sessionId = request.getSession().getId();
        String dgSessionId = DgUtil.generateUID(sessionId, true);

        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        while (sessionCache.get(dgSessionId) != null) {
            dgSessionId = DgUtil.generateUID(sessionId, true);
        }
        if (value != null) {
            sessionCache.put(dgSessionId, value);
        }

        return dgSessionId;
    }

    /**
     * Checks if action is "start new session" action
     * @param action action path
     * @return true, if it is a "start new session" action
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
        ModuleConfig moduleConfig = org.apache.struts.util.RequestUtils.
            getRequestModuleConfig(request);

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


    private static String getSessionIdFromCookie(HttpServletRequest request) {
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        String decode = null;
        if (cookies != null) {
            for (int loop = 0; loop < cookies.length; loop++) {
                if (cookies[loop].getName().equals(DIGI_SESSION_COOKIE)) {
                    sessionId = DgUtil.decodeString(cookies[loop].getValue());
                    break;
                }
            }
        }

        return sessionId;
    }

    private static void saveSessionIdToCookie(HttpServletRequest request,
                                              HttpServletResponse response,
                                              String sessionId) {
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);

        // report save session id cookie
        logger.debug("Saving session id: " + sessionId);

        // set a cookie with the username in it
        Cookie sessionIdCookie = new Cookie(DIGI_SESSION_COOKIE,
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

    /**
     * Authenticates user by LoginInfo. At first, checks login (by login name
     * and password). If it is successfull, verifies, if this user is
     * <b>same</b> as included in LoginInfo (check equality of user ids)
     * @param loginInfo LoginInfo
     * @return User if authentication was successfull, null on failure
     */
    private static User getUserFromLoginInfo(LoginInfo loginInfo) {
        String userName = loginInfo.getUserName();
        String password = loginInfo.getPassword();

        User user = loginUserByScrambledPassword(userName, password);
        if (user == null) {
            return null;
        }
        if (user.getId().longValue() != loginInfo.getUserId()) {
            logger.debug("Login is successfull but user id is different");
            return null;
        }
        else {
            return user;
        }
    }

    private static User getUserFromSubject(Subject subject) {
        // get user from public credentials
        User user = null;
        Set principals = subject.getPrincipals(UserPrincipal.class);
        if (principals == null) {
            return null;
        }
        Iterator iter = principals.iterator();
        while (iter.hasNext()) {
            UserPrincipal item = (UserPrincipal)iter.next();
            user = UserUtils.getUser(new Long(item.getUserId()));
            break;
        }

        // set subject to user,
        // it is use in the future to get subject from User object
        user.setSubject(subject);
        return user;
    }

    /**
     * Log in user using original username and scrambled password. This method
     * must be used when logging in through cookie
     * @param userName name of the user (usually - email)
     * @param scrambledPassword scrambled password
     * @return User object if login is successfull and null on failure
     */
    private static User loginUserByScrambledPassword(String userName,
        String scrambledPassword) {
        LoginContext lc = null;
        try {

            // create login context and put passive callback handle ,
            // see org.digijava.kernel.security.PassiveCallbackHandler
            /** @todo other callback handler must be used */
            lc = new LoginContext("digijava",
                                  new PassiveCallbackHandler(userName,
                scrambledPassword));

            // just login
            lc.login();

            Subject subject = lc.getSubject();

            User user = getUserFromSubject(subject);

            // check if user baned then return false
            if (user.isBanned()) {
                logger.debug("User " + userName + "is banned");
                return null;
            }
            return user;
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
     * @throws UserBannedException if user is banned
     * @throws LoginFailedException if general login error occured
     */
    private static User loginUser(String userName, String password) throws
        UserBannedException, LoginFailedException {
        if (userName == null || password == null) {
            throw new LoginFailedException(
                "Username and password must be non-null");
        }
        LoginContext lc = null;
        try {

            // create login context and put passive callback handle ,
            // see org.digijava.kernel.security.PassiveCallbackHandler
            /** @todo other callback handler must be used */
            lc = new LoginContext("digijava",
                                  new PassiveCallbackHandler(userName, password));

            // just login
            lc.login();

            Subject subject = lc.getSubject();

            User user = getUserFromSubject(subject);

            // check if user baned then return false
            if (user.isBanned()) {
                throw new UserBannedException();
            }
            return user;
        }
        catch (LoginException ex) {
            throw new LoginFailedException(ex);
        }
        catch (SecurityException ex) {
            throw new LoginFailedException(ex);
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
                if (cookies[loop].getName().equals(Constants.COOKIE_NAME)) {
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
        SiteDomain currDomain = RequestUtils.getSiteDomain(request);

        // set user cooke if checked save login
        String userSignOn = userName.trim() + ":" + password.trim();

        // report save logon cookie
        logger.debug("Saving login cookie for user: " + userName);

        // set a cookie with the username in it
        Cookie userNameCookie = new Cookie(Constants.COOKIE_NAME,
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
    private static void invalidate(HttpServletRequest request) {
        HttpSession currentSess = request.getSession(true);

        Object redirectionFlag = currentSess.getAttribute(Constants.
            DG_REDIRECT_LOGON);

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

        // Restore redirection flag
        if (redirectionFlag != null) {
            currentSess.setAttribute(Constants.DG_REDIRECT_LOGON,
                                     redirectionFlag);
        }

    }

    /**
     * Generate URL in form http://domain[:port]/[context][/path]~[other-part]
     * @param request HttpServletRequest
     * @return String
     */
    public static String generateReferrer(HttpServletRequest request) {

        SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
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

    /**
     * Returns path from Request's referrer parameter. For example, if referrer
     * is http://domain[:port]/[context][/path]~[other-part], it returns
     * [other-part]. If return value is empty or equals to "/", returns
     * "/index.do"
     * @param request HttpServletRequest
     * @return path from Request's referrer parameter
     */
    private static String getReferrerPath(HttpServletRequest request) {
        String refferer = DgUtil.decodeString(request.getParameter(Constants.
            REFERRER_PARAM));
        logger.debug("Referrer is: " + refferer);
        if (refferer != null && refferer.trim().length() > 0) {
            int index = refferer.indexOf("~");
            if (index != -1) {
                String referrerPath = refferer.substring(index + 1).trim();
                if (referrerPath.equals("/") || referrerPath.length() == 0) {
                    referrerPath = "/index.do";
                }
                logger.debug("Referrer path is: " + referrerPath);
                return referrerPath;
            }
        }

        logger.debug("Returning root site");
        return new String("/index.do");
    }

    private static User loginUserBySessionId(String sessionId) {
        LoginContext lc = null;
        try {

            // create login context and put passive callback handle ,
            // see org.digijava.kernel.security.PassiveCallbackHandler
            lc = new LoginContext("digijava",
                                  new PassiveCallbackHandler(sessionId));

            // just login
            lc.login();

            Subject subject = lc.getSubject();

            User user = getUserFromSubject(subject);

            // check if user baned then return false
            if (user.isBanned()) {
                logger.debug("User " + user.getName() + "is banned");
                return null;
            }
            return user;
        }
        catch (LoginException ex) {
            logger.error("Unable to log in user by session id" + sessionId, ex);
            return null;
        }
        catch (SecurityException ex) {
            logger.error("Unable to log in user by session id" + sessionId, ex);
            return null;
        }
    }

    public static void becomeUser(long userId, HttpServletRequest request) throws DgException {
        LoginInfo loginInfo = null;
        ObjectCache sessionCache = DigiCacheManager.getInstance().getCache(
            LOGIN_CACHE_REGION);
        String sessionId = getSessionIdFromCookie(request);
        //(String) request.getSession().getAttribute(Constants.DG_SESSION_ID);
        if (sessionId != null) {
            loginInfo = (LoginInfo) sessionCache.get(sessionId);
        }

        if (loginInfo == null) {
            throw new DgException("New DiGi session must be started to become user");
        }
        Site site = RequestUtils.getSite(request);
        loginInfo.becomeUser(userId, site.getId().longValue());
        sessionCache.put(sessionId, loginInfo);

        User user = loginUserBySessionId(sessionId);
        if (user == null) {
            loginInfo.unBecomeUser();
            sessionCache.put(sessionId, loginInfo);

            throw new DgException("becomeUser() failed");
        }

        //get user from session
        User sourceUser = RequestUtils.getUser(request);
        //save user
        request.getSession(true).setAttribute(Constants.SOURCE_USER, sourceUser);
        request.getSession(true).setAttribute(Constants.USER, user);

    }

}

class UserBannedException
    extends Exception {};

class LoginFailedException
    extends Exception {
    LoginFailedException(Throwable cause) {
        super(cause);
    }

    LoginFailedException(String message) {
        super(message);
    }
}

class SessionIdCallbackHandler implements CallbackHandler {
    private String sessionId;

    public SessionIdCallbackHandler(String sessionId) {
        this.sessionId = sessionId;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            Callback callback = callbacks[i];
            if (callback instanceof SessionIdCallback) {
                SessionIdCallback idCallback = (SessionIdCallback)callback;
                idCallback.setSessionId(sessionId);
            } else {
                throw new UnsupportedCallbackException(callback, "This callback is not supported: " + callback.getClass().getName());
            }
        }

    }

}