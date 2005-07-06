/*
 *   DgSecurityManager.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Aug 17, 2003
 * 	 CVS-ID: $Id: DgSecurityManager.java,v 1.1 2005-07-06 10:34:22 rahul Exp $
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

import java.security.AccessControlException;
import java.security.Permission;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.PropertyPermission;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.digijava.kernel.util.ShaCrypt;
import javax.security.auth.login.LoginContext;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.exception.UMException;
import javax.security.auth.login.LoginException;
import java.io.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.dbentity.SignOn;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.Iterator;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.UserLangPreferences;
import java.util.*;
import org.digijava.module.um.util.UmUtil;
import javax.servlet.http.HttpSession;
import org.apache.struts.Globals;
import org.digijava.kernel.util.UserUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.SiteUtils;
import java.net.URLDecoder;
import org.digijava.kernel.util.RequestUtils;

public class DgSecurityManager {

    private static Logger logger = I18NHelper.getKernelLogger(DgSecurityManager.class);

    private static String REFERRER_PARAM = "rfr";

    /**
     * Gets Subject from request and returns it. If request does not contain
     * subject, creates new one
     * @param request HttpServletRequest object
     * @return Subject from request
     */
    static public Subject getSubject(HttpServletRequest request) {

        Subject subject = null;
        boolean login = false;
        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        if (user == null) {

            logger.l7dlog(Level.DEBUG,
                          "DgSecurityManager.noSubjectIntoSession", null, null);
            subject = new Subject();
        }
        else {

            subject = user.getSubject();
            logger.l7dlog(Level.DEBUG,
                          "DgSecurityManager.subjectFoundIntoSession", null, null);
        }

        return subject;
    }


    /**
     * Get referrer url from request, if root true return referrer root url
     * otherwise full url
     *
     * @param request
     * @param root
     * @return DECODED string
     * @throws java.io.IOException
     */
    public static String getReferrerSite(HttpServletRequest request,
                                         boolean root) {

        String refferer = DgUtil.decodeString(request.getParameter(Constants.
            REFERRER_PARAM));

        if (refferer != null && refferer.trim().length() > 0) {
            int index = refferer.indexOf("~");
            if (index != -1) {
                if (!root)
                    return refferer.substring(0, index) +
                        refferer.substring(index + 1, refferer.length());
                else
                    return refferer.substring(0, index);
            }
            else {
                return refferer;
            }
        }

        return null;
    }

    /**
     *
     * @param request
     * @param root
     * @return
     * @throws java.io.IOException
     */
    public static String getActionMappingFromReferrer(HttpServletRequest
        request) {

        String referrer = DgUtil.decodeString(request.getParameter(Constants.
            REFERRER_PARAM));

        if (referrer != null && referrer.trim().length() > 0) {
            int index = referrer.indexOf("~");
            if (index != -1) {
                String servletMapping = (String) request.getSession().
                    getServletContext().getAttribute(Globals.SERVLET_KEY);
                referrer = referrer.substring(index + 1, referrer.length());
                if (servletMapping.startsWith("*.")) {
                    int end = referrer.indexOf(servletMapping.substring(1));
                    if (end != -1) {
                        return referrer.substring(0, end);
                    }
                }
                return referrer;
            }
        }

        return null;
    }


    /**
     * Checks, is <code>action</code> is permitted on <code>site</site> for
     * the specified <code>subject</code>
     * @param subject Subject
     * @param site Site
     * @param action Action to perform: integer representation of READ, WRITE,
     * ADMIN, TRANSLATE
     * @return true, if action is performed, false - if not
     */
    public static boolean permitted(Subject subject, Site site, int action) {

        Object params[] = {
            subject.toString(), site.getId(), new Integer(action)};
        logger.l7dlog(Level.DEBUG, "DgSecurityManager.permittedCalled",
                      params, null);

        if ( (!site.isSecure()) && (action == SitePermission.INT_READ)) {
            // Everyone has read access to non-secure site
            logger.l7dlog(Level.DEBUG, "DgSecurityManager.unsecuredSite",
                          null, null);

            return true;
        }

        logger.l7dlog(Level.DEBUG, "DgSecurityManager.securedSite",
                      null, null);

        SitePermission permission = new SitePermission(site, action);

        return permitted(subject, permission);

    }

    public static boolean permitted(Subject subject, Site site,
                                    ModuleInstance moduleInstance,
                                    int action) {
        Object params[] = {
            subject.toString(), moduleInstance.getModuleName(),
            moduleInstance.getInstanceName(), new Integer(action)};
        logger.l7dlog(Level.DEBUG,
                      "DgSecurityManager.permittedCalledForModule", params, null);

        ModuleInstancePermission permission = new ModuleInstancePermission(
            moduleInstance, action);
        boolean isPermitted = permitted(subject, permission);

        if (!isPermitted) {
            SitePermission sitePermission;
            if (moduleInstance.getSite() == null) {
                sitePermission = new SitePermission(site, action);
                return permitted(subject, sitePermission);
            }
            else {
                if (site.getId().equals(moduleInstance.getSite().getId())) {
                    return permitted(subject, site, action);
                }
                else {
                    sitePermission = new SitePermission(moduleInstance.getSite(),
                        action);
                    return permitted(subject, sitePermission);
                }
            }
        }
        else {
            return true;
        }
    }

    /**
     * Checks, is specified <code>permission</code> permitted for the given
     * <code>subject</code>
     * @param subject Subject
     * @param permission Permission
     * @return true, if subject has permission. false - if not
     * @deprecated use DigiSecurityManager.checkPermission() instead
     */
    static public boolean permitted(
        Subject subject,
        final Permission permission) {

        return DigiSecurityManager.checkPermission(subject, permission);
    }
}