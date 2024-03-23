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
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.request.SiteDomain;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class, which provides some useful methods to simplify access to site
 * configuration
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class SiteConfigUtils {

    private static Logger logger = I18NHelper.getKernelLogger(SiteConfigUtils.class);

    public static final String MODULE_DIR = "module";
    public static final String LAYOUT_DIR = "layout";
    public static final String TEMPLATE_DIR = "TEMPLATE";
    public static final String SITE_DIR = "SITE";
    public static final String BLANK_TEMPLATE_NAME = "blank";


    /**
     * Generate Url relative or non relative url
     * @param request HttpServletRequest
     * @param relative boolean
     * @return builded URL
     * @deprecated don't use this method. There are better alternatives to
     * construct URLs in SiteUtils class
     */
    public static String buildDgURL(HttpServletRequest request,
                                    boolean relative) {

        // get site domain object from request
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);

        return buildDgURL(request, siteDomain, relative);
    }

    /**
     * Generate Url, relative or non relative. If non-relative, then URL has
     * form /<context>/<site-path>. If relative - <site-path> (without /)
     * @param request HttpServletRequest
     * @param siteDomain site domain for which URL must be calculated
     * @param relative boolean relative or not
     * @return Url, relative or non relative
     * @deprecated don't use this method. There are better alternatives to
     * construct URLs in SiteUtils class
     */
    public static String buildDgURL(HttpServletRequest request,
                                    SiteDomain siteDomain, boolean relative) {

        String loginUrl = null;

        if (!relative) {
            loginUrl = (request.getContextPath() == null ? "" :
                        request.getContextPath()) +
                (siteDomain.getSitePath() == null ? "" :
                 siteDomain.getSitePath());
        }
        else {
            loginUrl = (siteDomain.getSitePath() == null ? "" :
                        siteDomain.getSitePath());
        }

        return loginUrl;
    }

    /**
     * Get current module name with instance
     * for example: /news/sport
     * @param request HttpServletRequest
     * @return current module/instance path
     * @deprecated for most cases RequestUtils.getFullModuleUrl() is what you
     * may need
     */
    public static String getCurrentModuleURL(HttpServletRequest request) {

        String url = null;
        /**
         * @todo this module must be moved away in ModuleUtils and RequestUtils
         * and be default module/instance aware
         */
        ComponentContext context = ComponentContext.getContext(request);

        if (context == null) {
            url = "/" + (String) request.getAttribute(Constants.MODULE_NAME) +
                "/" + (String) request.getAttribute(Constants.MODULE_INSTANCE);
        }
        else {
            url = "/" + (String) context.getAttribute(Constants.MODULE_NAME) +
                "/" + (String) context.getAttribute(Constants.MODULE_INSTANCE);
        }

        return url;
    }

    /**
     * Get logon site id
     * @return login site id
     */

    public static String getLogonSiteId() {

        DigiConfig congig = null;

        try {
            congig = DigiConfigManager.getConfig();
        }
        catch (Exception ex) {
            logger.error("Could not get Logon SiteId",ex);
        }

        return congig == null ? null : congig.getLogonSite().getId();

    }

    /**
     * check is logon site
     * @param request HttpServletRequest
     * @return true if current site is a login site
     */
    public static boolean isLogonSite(HttpServletRequest request) {

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);
        String logonSiteId = getLogonSiteId();

        if (siteDomain != null) {
            return siteDomain.getSite().getSiteId().equals(logonSiteId);
        }

        return false;

    }


    /**
     * Check param in request URL
     * @param name name of the parameter
     * @param request HttpServletRequest
     * @return true if this parameter exists in request
     * @todo this "strange" method needs to be placed somewere, not here
     */
    public static boolean isParam( String name, HttpServletRequest request ) {

        String paramValue = request.getParameter(name);

        return (paramValue != null && paramValue.length() > 0);
    }

 }

