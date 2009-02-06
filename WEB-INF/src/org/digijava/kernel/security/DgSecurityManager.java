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

import java.security.Permission;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;

public class DgSecurityManager {

    private static Logger logger = I18NHelper.getKernelLogger(DgSecurityManager.class);


    /**
     * Gets Subject from request and returns it. If request does not contain
     * subject, creates new one
     * @param request HttpServletRequest object
     * @return Subject from request
     * @deprecated use RequestUtils.getSubject() instead
     */
    static public Subject getSubject(HttpServletRequest request) {
        return RequestUtils.getSubject(request);
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
                if (!root) {
                    String url = refferer.substring(index + 1, refferer.length());
                    if( url.startsWith("http") ) {
                        return url;
                    } else {
                        return refferer.substring(0, index) + url;
                    }
                }
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
    	//logger.info("### checking permissions for"+subject.toString() + " and permission "+permission.toString());
        return DigiSecurityManager.checkPermission(subject, permission);
    }
}
