/*
 *   NewSession.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Feb 23, 2004
 * 	 CVS-ID: $Id: NewSession.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.digijava.kernel.entity.ModuleInstance;

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

            HttpLoginManager.LoginInfo loginInfo = HttpLoginManager.loginBySessionId(request, response, dgSessionId, true);
            if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_BANNED ||
                loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_INVALID) {

                ActionErrors errors = new ActionErrors();
                ActionForward failureForward = HttpLoginManager.getFailureForward(request, getServlet().getServletContext());
                    switch (loginInfo.getLoginResult()) {
                        case HttpLoginManager.LOGIN_RESULT_INVALID:
                            errors.add(HttpLoginManager.LOGIN_ERROR_KEY, new ActionError("error.logon.invalid"));
                            break;
                        case HttpLoginManager.LOGIN_RESULT_BANNED:
                            errors.add(HttpLoginManager.LOGIN_ERROR_KEY, new ActionError("error.logon.banned"));
                            break;
                    }

                    loginInfo.setActionErrors(errors);
                    ModuleInstance moduleInstance = (ModuleInstance) request.
                        getAttribute(Constants.MODULE_INSTANCE_OBJECT);
                    if( moduleInstance != null ) {
                        loginInfo.setModuleId(moduleInstance.getModuleInstanceId());
                        logger.debug("Set UM module id " + moduleInstance.getModuleInstanceId());
                        logger.debug("Set UM module name " + moduleInstance.getModuleName());
                    }
                logger.debug("Login failed");
                return failureForward;
            }
            // If everything is OK, redirect to referrer
            String referrer = request.getParameter(Constants.REFERRER_PARAM);
            if (referrer != null) {
                // Get referrer URL
                // If user has logged out from secured site, redirect to
                // main layout page
                if (loginInfo.getLoginResult() == HttpLoginManager.LOGIN_RESULT_LOGOUT) {
                    if (HttpLoginManager.isRedirectPossible(request)) {
                        referrer = DgSecurityManager.getReferrerSite(request, false);
                    } else {
                        referrer = null;
                    }
                } else {
                    referrer = DgSecurityManager.getReferrerSite(request, false);
                }
            }
            if (referrer == null) {
                // Go to the site's root
                SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
                referrer = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                                request.getServerPort(),
                                                request.getContextPath());
            }
            logger.debug("Redirecting to referrer: " + referrer);
            response.sendRedirect(referrer);
        } else {
            throw new IllegalArgumentException("Request must contain parameter: " + Constants.DG_SESSION_ID_PARAMETER);
        }


        return null;
    }
}
