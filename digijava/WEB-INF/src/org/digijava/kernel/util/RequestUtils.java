/*
 *   RequestUtils.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Feb 04, 2004
 * 	 CVS-ID: $Id: RequestUtils.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
package org.digijava.kernel.util;

import org.apache.struts.tiles.ComponentContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.digijava.kernel.Constants;
import java.util.HashMap;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.entity.Locale;
import net.sf.hibernate.Session;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.exception.DgException;

/**
 * This class containts user-related utillity functions. User must be
 * <b>always</b> identified by User object
 */
public class RequestUtils {

    /**
     * Get attribute from request/tiles context. At first, method searches
     * attribute by key <code>attribute</code> in the tiles context (if it
     * exists for the currrent request). If search is unsuccessfull, then it
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
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);

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
        return (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);
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
     * @param session HttpSession object
     * @return logged user, currently logged in into system
     */
    public static User getUser(HttpServletRequest request) {
        User user = (User) request.getSession(true).getAttribute(Constants.USER);
        return user;
    }

    /**
     * Returns navigation language for current request.
     * @param request HttpServletRequest object
     * @return navigation language for current request
     */
    public static Locale getNavigationLanguage(HttpServletRequest request) {
        return (Locale) request.getAttribute(Constants.NAVIGATION_LANGUAGE);
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
        boolean waitForDigit = true;
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
     * Returns root url for the current site with current module name and instance
     *
     * @param request
     * @return
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
}