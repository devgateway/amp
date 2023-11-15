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

package org.digijava.module.um.action;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public final class NewSession
    extends Action {

    private static Logger logger = Logger.getLogger(NewSession.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        // Authenticate by session id
        if (request.getParameter(Constants.DG_SESSION_ID_PARAMETER) != null) {
            String dgSessionId = DgUtil.decodeString(request.getParameter(Constants.DG_SESSION_ID_PARAMETER));
            logger.debug("Starting new session with id " + dgSessionId);

            String oldSessionId = HttpLoginManager.getDigiSessionId(request);
            if (oldSessionId == null) {
                logger.warn("Unable to authenticate session " + dgSessionId +
                            ", seems like browser does not accept cookies");
                logger.debug("============================= Request Info =============================\n" +
                            getRequestInfo(request));
                return new ActionForward("/noSessionCookie.jsp?cookieName=" +
                                         HttpLoginManager.getSessionIdCookieName());
            }
            HttpLoginManager.LoginInfo loginInfo = HttpLoginManager.loginBySessionId(request, response, dgSessionId, true);
            if (logger.isDebugEnabled()) {
                logger.debug("Login information, passed to the action is " + loginInfo);
            }
            if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_BANNED ||
                loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_INVALID) {

                ActionMessages errors = new ActionMessages();
                ActionForward failureForward = HttpLoginManager.getFailureForward(request, getServlet().getServletContext());
                    switch (loginInfo.getLoginResult()) {
                        case HttpLoginManager.LOGIN_RESULT_INVALID:
                            errors.add(HttpLoginManager.LOGIN_ERROR_KEY, new ActionMessage("error.logon.invalid"));
                            break;
                        case HttpLoginManager.LOGIN_RESULT_BANNED:
                            errors.add(HttpLoginManager.LOGIN_ERROR_KEY, new ActionMessage("error.logon.banned"));
                            break;
                    }

                    ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
                    Long moduleId = null;
                    if( moduleInstance != null ) {
                        moduleId = moduleInstance.getModuleInstanceId();
                    }
                    HttpLoginManager.saveErrors(request, dgSessionId, moduleId, errors);
                logger.debug("Login failed. Forwarding to failure info " + failureForward.getPath() + " redirect" + failureForward.getRedirect());
                return failureForward;
            }
            // If everything is OK, redirect to referrer
            String referrer = request.getParameter(Constants.REFERRER_PARAM);
            logger.debug("Referrer, passed by parameter is " + referrer);
            if (referrer != null) {
                // Get referrer URL
                // If user has logged out from secured site, redirect to
                // main layout page
                if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_LOGOUT) {
                    if (HttpLoginManager.isRedirectPossible(request)) {
                        referrer = DgSecurityManager.getReferrerSite(request, false);
                        logger.debug("Going back is possible because action is  not secure. Redirecting to " + referrer);
                    } else {
                        referrer = null;
                        logger.debug("Redirection to referrer is not possible because the action is secure");
                    }
                } else {
                    referrer = DgSecurityManager.getReferrerSite(request, false);
                    logger.debug("Real referrer, determined from one, passed by URL is " + referrer);
                }
            }
            if (referrer == null) {
                // Go to the site's root
                SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
                referrer = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                                request.getServerPort(),
                                                request.getContextPath());
                logger.debug("Referrer was not determined. Going to the site's root " + referrer);
            }
            logger.debug("Redirecting to referrer: " + referrer);
            response.sendRedirect(referrer);
        } else {
            throw new IllegalArgumentException("Request must contain parameter: " + Constants.DG_SESSION_ID_PARAMETER);
        }


        return null;
    }

    private String getRequestInfo(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer(512);
        String newLine = "\n";
        buffer.append("URL: ").append(request.getRequestURL()).append(newLine);
        buffer.append("URI: ").append(request.getRequestURI()).append(newLine);
        buffer.append("Query: ").append(request.getQueryString()).append(newLine);
        buffer.append("Cookies:").append(newLine);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                buffer.append("   ").append(i + 1).append(": ");
                buffer.append(ReflectionToStringBuilder.toString(cookies[i]));
                buffer.append(newLine);
            }
        }
        buffer.append("End of cookies").append(newLine);
        buffer.append("Headers:").append(newLine);
        Enumeration headerNameenum = request.getHeaderNames();
        while (headerNameenum.hasMoreElements()) {
            String headerName = (String) headerNameenum.nextElement();
            buffer.append("   ").append(headerName).append(newLine);
            buffer.append("     Value: ").append(request.getHeader(headerName)).append(newLine);
            buffer.append("     All headers with this name:").append(newLine);
            Enumeration headerValEnum = request.getHeaders(headerName);
            while (headerValEnum.hasMoreElements()) {
                String item = (String) headerValEnum.nextElement();
                buffer.append("         ").append(item).append(newLine);
            }
        }
        buffer.append("End of headers").append(newLine);

        return buffer.toString();
    }
}
