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

package org.digijava.kernel.security.auth;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForward;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.digijava.kernel.util.ModuleUtils;

public class DigiSSOProcessingFilter
    extends AbstractProcessingFilter implements Filter, InitializingBean{

    private ServletContext servletContext;

    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(getFilterProcessesUrl(),
                         "filterProcessesUrl must be specified");
        /*
        Assert.notNull(getAuthenticationManager(),
                       "authenticationManager must be specified");
*/
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.servletContext = filterConfig.getServletContext();
    }


    /**
     * attemptAuthentication
     *
     * @param httpServletRequest HttpServletRequest
     * @return Authentication
     * @throws AuthenticationException
     * @todo Implement this org.acegisecurity.ui.AbstractProcessingFilter
     *   method
     */
    public Authentication attemptAuthentication(HttpServletRequest
                                                request) throws
        AuthenticationException {
        // Authenticate by session id
        String dgSessionId = getNewSessoionId(request);
        logger.debug("Starting new session with id " + dgSessionId);

        HttpLoginManager.LoginInfo loginInfo = null;
        try {
            loginInfo = HttpLoginManager.loginBySessionId(
                request, null, dgSessionId, true);
        }
        catch (DgException ex) {
            throw new GeneralAuthenticationException(ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Login information, passed to the action is " +
                         loginInfo);
        }
        if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_BANNED ||
            loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_INVALID) {

            throw new NotLoggedInException(dgSessionId,
                                           loginInfo.getLoginResult());
        }
        DigiSSOAuthentication auth = new DigiSSOAuthentication(loginInfo,
            dgSessionId);

        return auth;
    }

    private String getNewSessoionId(HttpServletRequest request) {
        String dgSessionId = DgUtil.decodeString(request.getParameter(Constants.
            DG_SESSION_ID_PARAMETER));
        return dgSessionId;
    }

    protected void onPreAuthentication(HttpServletRequest request,
                                       HttpServletResponse response) throws
        AuthenticationException, IOException {
        String oldSessionId = HttpLoginManager.getDigiSessionId(request);
        if (oldSessionId == null) {
            throw new NoCookieException();
        }

        if (request.getParameter(Constants.DG_SESSION_ID_PARAMETER) == null) {
            throw new NoParameterException();
        }
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authResult) throws
        IOException {
    }

    protected String getUnuccessfulRedirectUrl(HttpServletRequest request, AuthenticationException failed) {
        if (failed instanceof NoCookieException) {
            return  "/noSessionCookie.jsp?cookieName=" + HttpLoginManager.getSessionIdCookieName();
        }


        ActionMessages errors = new ActionMessages();
        ActionForward failureForward = null;
        try {
            failureForward = HttpLoginManager.getFailureForward(
                request,
                this.servletContext);
        }
        catch (Exception ex1) {
            throw new RuntimeException(ex1);
        }

        NotLoggedInException ex = (NotLoggedInException) failed;
        switch (ex.reason) {
            case HttpLoginManager.LOGIN_RESULT_INVALID:
                errors.add(HttpLoginManager.LOGIN_ERROR_KEY,
                           new ActionMessage("error.logon.invalid"));
                break;
            case HttpLoginManager.LOGIN_RESULT_BANNED:
                errors.add(HttpLoginManager.LOGIN_ERROR_KEY,
                           new ActionMessage("error.logon.banned"));
                break;
        }

        ModuleInstance moduleInstance =
            ModuleUtils.getSharedModuleInstance("um", "user");

        Long moduleId = null;
        if (moduleInstance != null) {
            moduleId = moduleInstance.getModuleInstanceId();
        }
        /** @todo change it! */
        HttpLoginManager.saveErrors(request, ex.dgSessionId, moduleId,
                                    errors);
        logger.debug("Login failed. Forwarding to failure info " +
                     failureForward.getPath() + " redirect" +
                     failureForward.getRedirect());
        return failureForward.getPath();

    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
                                                HttpServletResponse response,
                                                AuthenticationException failed) throws
        IOException {

        if (failed instanceof NoParameterException) {
            throw new IllegalArgumentException(failed.getMessage());
        }

        if (failed instanceof GeneralAuthenticationException) {
            throw new RuntimeException(failed.getCause());
        }
    }

    /**
     * getDefaultFilterProcessesUrl
     *
     * @return String
     * @todo Implement this org.acegisecurity.ui.AbstractProcessingFilter
     *   method
     */
    public String getDefaultFilterProcessesUrl() {
        return "j_acegi_digi_processing_filter";
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        return uri.endsWith(getFilterProcessesUrl());
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authResult) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success: " + authResult.toString());
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Store session id
        String dgSessionId = getNewSessoionId(request);
        HttpLoginManager.saveSessionIdToCookie(request, response, dgSessionId);

        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain the following Authentication: '" + authResult + "'");
        }

        String targetUrl = getSuccessfulRedirectUrl(request, authResult);

        onSuccessfulAuthentication(request, response, authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    /**
     * getSuccessfulRedirectUrl
     *
     * @param request HttpServletRequest
     * @return String
     */
    protected String getSuccessfulRedirectUrl(HttpServletRequest request, Authentication authResult) {
        DigiSSOAuthentication auth = (DigiSSOAuthentication) authResult;

        // If everything is OK, redirect to referrer
        String referrer = request.getParameter(Constants.REFERRER_PARAM);
        logger.debug("Referrer, passed by parameter is " + referrer);
        if (referrer != null) {
            // Get referrer URL
            // If user has logged out from secured site, redirect to
            // main layout page
            if (auth.loginInfo.getLoginResult() ==
                HttpLoginManager.LOGIN_RESULT_LOGOUT) {
                if (HttpLoginManager.isRedirectPossible(request)) {
                    referrer = DgSecurityManager.getReferrerSite(request, false);
                    logger.debug(
                        "Going back is possible because action is  not secure. Redirecting to " +
                        referrer);
                }
                else {
                    referrer = null;
                    logger.debug(
                        "Redirection to referrer is not possible because the action is secure");
                }
            }
            else {
                referrer = DgSecurityManager.getReferrerSite(request, false);
                logger.debug(
                    "Real referrer, determined from one, passed by URL is " +
                    referrer);
            }
        }
        if (referrer == null) {
            // Go to the site's root
            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            referrer = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                            request.getServerPort(),
                                            request.getContextPath());
            logger.debug(
                "Referrer was not determined. Going to the site's root " +
                referrer);
        }
        logger.debug("Redirecting to referrer: " + referrer);

        return referrer;
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);

        // Store session id
        String dgSessionId = getNewSessoionId(request);
        HttpLoginManager.saveSessionIdToCookie(request, response, dgSessionId);

        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        String failureUrl = getUnuccessfulRedirectUrl(request, failed);

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString());
        }

        try {
            request.getSession().setAttribute(ACEGI_SECURITY_LAST_EXCEPTION_KEY, failed);
        } catch (Exception ignored) {}

        onUnsuccessfulAuthentication(request, response, failed);

        sendRedirect(request, response, failureUrl);
    }
}

class NoCookieException
    extends AuthenticationException {
    public NoCookieException() {
        super("No session cookie found in request");
    }
}

class NoParameterException
    extends AuthenticationException {
    public NoParameterException() {
        super("Request must contain parameter: " +
              Constants.DG_SESSION_ID_PARAMETER);
    }
}

class GeneralAuthenticationException
    extends AuthenticationException {
    public GeneralAuthenticationException(Throwable cause) {
        super("General authentication error", cause);
    }
}

class NotLoggedInException
    extends AuthenticationException {
    int reason;
    String dgSessionId;
    public NotLoggedInException(String dgSessionId, int reason) {
        super("Session " + dgSessionId + " is not logged in. Error code: " +
              reason);
        this.reason = reason;
        this.dgSessionId = dgSessionId;
    }
}
