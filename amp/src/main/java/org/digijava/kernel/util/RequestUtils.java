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

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.ExceptionHelper;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;

/**
 * This class contains user-related utility functions. User must be
 * <b>always</b> identified by User object
 */
public class RequestUtils {
    private static Logger logger = Logger.getLogger(RequestUtils.class);
    /**
     * Get attribute from request/tiles context. At first, method searches
     * attribute by key <code>attribute</code> in the tiles context (if it
     * exists for the current request). If search is unsuccessful, then it
     * searches this attribute in the request scope
     * @param request HttpServletRequest
     * @param attribute key of the attribute
     * @return attribute from request/tiles context
     */
    public static Object getDigiContextAttribute(HttpServletRequest request, String attribute) {
        ComponentContext context = ComponentContext.getContext(request);
        Object contextObject = null;

        if (context != null) {
            contextObject = context.getAttribute(attribute);
        }
        if (contextObject == null) {
            contextObject = request.getAttribute(attribute);
        }
        return contextObject;
    }

    /**
     * Get attribute from request/tiles context. Same as above, but with generics. 
     * At first, method searches
     * attribute by key <code>attribute</code> in the tiles context (if it
     * exists for the current request). If search is unsuccessful, then it
     * searches this attribute in the request scope
     * @param <E>
     * @param request
     * @param attribute
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E getDigiContextAttributeEx(HttpServletRequest request, String attribute) {
        ComponentContext context = ComponentContext.getContext(request);
        E contextObject = null;

        if (context != null) {
            contextObject = (E)context.getAttribute(attribute);
        }
        if (contextObject == null) {
            contextObject = (E)request.getAttribute(attribute);
        }
        return contextObject;
    }

    /**
     * Sets attribute into request/tiles context. At first, method checks, does
     * tiles contexst exist or not for the current request. If yes, it puts
     * attribute there. If no, then attribute will be placed in request
     * @param request HttpServletRequest
     * @param attribute key of the attribute
     * @param value Object to place in the context
     */
    public static void setDigiContextAttribute(HttpServletRequest request, String attribute, Object value) {
        ComponentContext context = ComponentContext.getContext(request);
        if (context != null) {
            context.putAttribute(attribute, value);
        } else {
            request.setAttribute(attribute, value);
        }
    }

    /**
     * Sets translation attribute, which will be used by digi:trn tag to
     * assemble formatted translations. For example, if one executes this method
     * with attribute=name, value=Mikheil, then &lt;digi:trn key=".."&gt;Hello,
     * {name}&lt;/digi:trn&gt will be rendered as "Hello, Mikheil".
     * @param request HttpServletRequest
     * @param attribute name of the translation attribute
     * @param value value of the translation attribute
     */
    public static void setTranslationAttribute(HttpServletRequest request, String attribute, String value) {
        Map translationParams = (Map) getDigiContextAttribute(request,
            Constants.TRANSLATION_PARAMETERS);
        if (translationParams == null) {
            translationParams = new HashMap();
            setDigiContextAttribute(request, Constants.TRANSLATION_PARAMETERS, translationParams);
        }

        translationParams.put(attribute, value);
    }

    /**
     * Returns current site or null, if request does not contain site
     * information
     * @param request HttpServletRequest
     * @return current site
     */
    public static Site getSite(HttpServletRequest request) {
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);

        if (siteDomain == null) {
            return null;
        } else {
            return siteDomain.getSite();
        }
    }

    /**
     * Returns site domain, which was used to initiate request
     * @param request HttpServletRequest
     * @return current SiteDomain object
     */
    public static SiteDomain getSiteDomain(HttpServletRequest request) {
        if (request == null)
            return null;
        return (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
    }

    /**
     * Sets current site domain to request. This method should used
     * <b>internally</b> by DiGi kernel.
     * @param request HttpServletRequest
     * @param siteDomain SiteDomain object
     */
    public static void setSiteDomain(HttpServletRequest request, SiteDomain siteDomain) {
        request.setAttribute(Constants.CURRENT_SITE, siteDomain);
    }

    /**
     * Returns ModuleInstance object, for which request was caled.
     * @param request HttpServletRequest
     * @return ModuleInstance object
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
        return  moduleInstance;
    }

    /**
     * Returns ModuleInstance object, for which request was caled. Return value
     * is the REAL ModuleInstance object, for mapped instances it returns
     * original module instance, not the link.
     * @param request request
     * @return ModuleInstance object
     */
    public static ModuleInstance getRealModuleInstance(HttpServletRequest
     request) {
    ModuleInstance moduleInstance = getModuleInstance(request);
    if ( (moduleInstance != null) && (moduleInstance.getRealInstance() != null)) {
        moduleInstance = moduleInstance.getRealInstance();
    }

    return moduleInstance;
    }

    /**
     * Returns user, currently logged in into system or null, if user is not
     * logged in
     * @param request HttpServletRequest object
     * @return logged user, currently logged in into system
     */
    public static User getUser(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.REQUEST_USER);
        return user;
    }

    /**
     * Returns navigation language for current request.
     * @param request HttpServletRequest object
     * @return navigation language for current request
     */
    public static Locale getNavigationLanguage(HttpServletRequest request) {
        Locale res = (Locale) request.getAttribute(Constants.NAVIGATION_LANGUAGE);
        if (res == null)
        {
            res = new Locale("en", "");
            String errMsg = "request does not have a locale, returned dummy 'en'. FIX THIS ERROR!";
            logger.error(errMsg);
            ExceptionHelper.printReducedStacktrace(logger);
        }
        return res;
    }

    /**
     * Get full URL for the current request where "referring back" must be
     * performed. This URL is needed for example, for language switch dropdown,
     * which executed language change action and then redirects back to source
     * URL
     * @todo We probably should define more "smart" algoritm for "referring
     * back" rather than simply calculate the original URL. Mikheil
     * @param request HttpServletRequest object
     * @return full URL for the current request where "referring back" must be
     * performed
     */
    public static String getSourceURL(HttpServletRequest request) {
        return (String)request.getAttribute(Constants.REQUEST_ALREADY_PROCESSED);
    }

    /**
     * Get relative URL for the current request where "referring back" must be
     * performed. Always starts with "/"
     * @param request HttpServletRequest object
     * @return relative URL for the current request
     */
    public static String getRelativeSourceURL(HttpServletRequest request) {
        String fullUrl = getSourceURL(request);

        SiteDomain siteDomain = getSiteDomain(request);

        String siteUrl = SiteUtils.getSiteURL(siteDomain, request.getScheme(), request.getServerPort(), request.getContextPath());

        if (fullUrl.startsWith(siteUrl)) {
            String relativeUrl;
            relativeUrl = fullUrl.substring(siteUrl.length());
            if (relativeUrl.trim().length() == 0) {
                relativeUrl = "/";
            }

            return relativeUrl;
        } else {
            throw new IllegalStateException(
                "Unable to determine relative URL. Full URL is: " + fullUrl +
                ". Site URL is: " + siteUrl + ".");
        }
    }


    /**
     * Returns remote host's IP address. If X-Forwarded-For header presents,
     * the first part of it is valid IP and not local, returns it. Otherwise
     * returns IP of the last client (it may be a client's real IP, if there
     * are no proxies, or address of the last proxy in the chain)
     * @param request HttpServletRequest object
     * @return remote host's IP address
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String forwarded = getForwardAddresses(request);

        String result = getForwarderAddress(forwarded, false);
        if (result == null) {
            return request.getRemoteAddr();
        } else {
            return result;
        }
    }

    /**
     * Returns X-Forwarded-For header data for the request
     * @param request X-Forwarded-For header
     * @return X-Forwarded-For header data for the request
     */
    public static String getForwardAddresses(HttpServletRequest request) {
        return request.getHeader("X-Forwarded-For");
    }

    /**
     * Analyzes forwardParam parameter, which contains list of IP addresses,
     * got from HTTP header's X-Forwarded-For parameter and returns first of
     * them, if this is not a local IP address (the last check may be disabled
     * using includeLocal parameter). Local is the address with the following
     * pattern: 0.*.*.*, 127.0.0.1, 192.168.*.*, 172.16.*.*, 10.*.*.*,
     * 224.*.*.*, 240.*.*.*
     * @param forwardParam list of IP addresses
     * @param includeLocal if true - returns IP even if it is the local one
     * @return IP address or null, if it can not be determined
     */
    public static String getForwarderAddress(String forwardParam, boolean includeLocal) {
        if (forwardParam == null) {
            return null;
        }
        int i = 0;
        // Skip spaces
        for(; forwardParam.charAt(i) == ' ' && i < forwardParam.length() ; i++);
        int start = i;
//        boolean waitForDigit = true;
        int[] digitsLength = new int[4];
        int partIndex = 0;
        // initialize
        digitsLength[0] = 0;
        for(; i<forwardParam.length(); i++) {
            char c = forwardParam.charAt(i);
            if (c >= '0' && c <= '9') {
                digitsLength[partIndex]++;
            } else {
                if (c == '.' && partIndex<3) {
                    partIndex ++;
                    digitsLength[partIndex] = 0;
                } else {
                    break;
                }
            }
        }
        int end = i;
        // "Suspected" fragment is between start and end now. Re-check, is
        // there a normal ip or not
        if (partIndex != 3) {
            // There were less than 4 parts
            return null;
        }
        for (i=0; i<4; i++) {
            if (digitsLength[i] == 0) {
                return null;
            }
        }

        String ipAddress = forwardParam.substring(start, end);

        if (includeLocal) {
            return ipAddress;
        } else {
            /**
             * @todo verify for local address
             */
        }

        return ipAddress;
    }

    /**
     * Returns root url for the current site with current module name and
     * instance
     * @param request HttpServletRequest
     * @return root url for the current site with current module name and
     * instance
     */
    public static String getFullModuleUrl(HttpServletRequest request) {

        StringBuffer url = new StringBuffer(1024);

        ModuleInstance moduleInstance = getModuleInstance(request);
        String rootUrl = DgUtil.getCurrRootUrl(request);

        url.append(rootUrl);

        if( !rootUrl.endsWith("/") ) {
            url.append("/");
        }

        url.append(moduleInstance.getModuleName());
        if (!moduleInstance.getInstanceName().equalsIgnoreCase(ModuleUtils.
            getModuleDefaultInstance(moduleInstance.getModuleName()))) {
            url.append(DgUtil.getParamSeparator());
            url.append(moduleInstance.getInstanceName());
        }
        url.append("/");

        return url.toString();
    }

    /**
     * Gets Subject from request and returns it. If request does not contain
     * subject, creates new one
     * @param request HttpServletRequest object
     * @return Subject from request
     */
    static public Subject getSubject(HttpServletRequest request) {

        Subject subject = null;
        User user = getUser(request);

        if (user == null) {
            subject = new Subject();
        }
        else {

            subject = UserUtils.getUserSubject(user);
            if (subject == null) {
                subject = new Subject();
                logger.warn("User, found in request, does not contain subject information");
            }
        }

        return subject;
    }

    /**
     * Switch to "development mode" - all teasers and layouts will have borders
     * and descriptions
     * @param request HttpServletRequest
     */
    public static void switchToDevelopmentMode(HttpServletRequest request) {
        request.getSession().setAttribute(Constants.DEVELPOMENT_MODE, Boolean.TRUE);
    }

    /**
     * Switch to "user mode" - normal page look & fill
     * @param request HttpServletRequest
     */
    public static void switchToUserMode(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().setAttribute(Constants.DEVELPOMENT_MODE,
                                              Boolean.FALSE);
        }
    }

    /**
     * Returns true if development mode is activated. false - if not
     * @param request HttpServletRequest
     * @return boolean development mode activation status
     */
    public static boolean isDevelopmentModeActive(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            Boolean mode = (Boolean) request.getSession().getAttribute(
                Constants.DEVELPOMENT_MODE);
            if (mode == null) {
                return false;
            }
            else {
                return mode.booleanValue();
            }
        }
        else {
            return false;
        }
    }

    /**
     * Returns current site's identity based on configuration setting. If
     * siteIdentityService service is defined in the system, it is asked to
     * calculate identity. If it is not - site's string identity is returned
     * @param request HttpServletRequest
     * @param service String name of the service for which identity should be
     * determined. For example - "google", "omniture",
     * @return site identity
     */
    public static String getSiteIdentity(HttpServletRequest request, String service) {
        Site site = getSite(request);

        return SiteUtils.getSiteIdentity(site, service);
    }

    /**
     * Returns information (URI, query, cookies, headers, attributes, etc )
     * about the request, given as parameter
     * @param request HttpServletRequest
     * @return String information about the request
     */
    public static String getRequestInfo(HttpServletRequest request) {
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

        buffer.append("Attributes:").append(newLine);
        Enumeration attrEnumer = request.getAttributeNames();
        while (attrEnumer.hasMoreElements()) {
            String attrName = (String) attrEnumer.nextElement();
            Object attribute = request.getAttribute(attrName);
            if (attribute instanceof String) {
                buffer.append("   ").append(attrName).append(" : ").append(attribute);
            } else {
                buffer.append("   ").append(attrName).append(" of class ").append(attribute.getClass());
            }
            buffer.append(newLine);
        }
        buffer.append("End of attributes").append(newLine);

        return buffer.toString();
    }

    /**
     * Retreive site domain from request based on calculation. This is usually
     * used in filters when current site is not determined yet.
     * @param request HttpServletRequest
     * @return SiteDomain
     */
    public static SiteDomain retreiveSiteDomain(HttpServletRequest request) {
        SiteDomain siteDomain =
            SiteCache.getInstance().getSiteDomain(request.getServerName(),
                                                  request.getRequestURI());
        return siteDomain;

    }
    
    /**
     * Check if the current user is an Admin, if not then the response is
     * redirected to the index page and the method returns false.
     * 
     * @param response
     * @param session
     * @param request
     * @return
     * @throws IOException
     */
    public static boolean isAdmin(HttpServletResponse response, HttpSession session, HttpServletRequest request)
            throws IOException {
        boolean ret = true;
        String str = (String) session.getAttribute("ampAdmin");
        if (str == null || str.equalsIgnoreCase("no")) {
            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(), request.getServerPort(), request
                    .getContextPath());
            url += "/aim/index.do";
            response.sendRedirect(url);
            ret = false;
        }
        return ret;
    }
    
    public static boolean isLoggued(HttpServletResponse response, HttpSession session, HttpServletRequest request)
            throws IOException {
        boolean ret = true;
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm == null) {
            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(), request.getServerPort(), request
                    .getContextPath());
            url += "/aim/index.do";
            response.sendRedirect(url);
            ret = false;
        }
        return ret;
    }
}
