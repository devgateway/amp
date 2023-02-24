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

package org.digijava.kernel.taglib.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.ModuleUtils;

/**
 * @todo COMMENT HERE
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TagUtil {

    private static Logger logger = Logger.getLogger(TagUtil.class);


    /**
     *
     * @param pageContext
     * @param name
     * @return
     * @throws JspException
     */

    public static Object getForm( PageContext pageContext, String name ) throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return getForm( request, name );
    }

    /**
     *
     * @param pageContext
     * @param name
     * @return
     * @throws JspException
     */
    public static Object getForm( HttpServletRequest request, String name ) throws JspException 
    {
        String formName = getCanonicalFormName(request, name, null);
        
        Object objectForm = request.getAttribute(formName);
        if (objectForm != null)
            return objectForm;
        objectForm = request.getSession().getAttribute(formName);
        if (objectForm != null)
            return objectForm;

        logger.debug("Action form not found in any scope (request,session)  " + formName);
        formName = getCanonicalFormName(request, name, name);
        objectForm = request.getAttribute(formName);
        if (objectForm != null)
            return objectForm;
        objectForm = request.getSession().getAttribute(formName);
        if (objectForm != null)
            return objectForm;
        
        return objectForm; // e.g. null
    }

    /**
     * builds an unique name for a form
     * @param request
     * @param name
     * @return
     * @throws JspException
     */
    public static String getCanonicalFormName(HttpServletRequest request, String name, String forcedModuleInstanceName) throws JspException
    {
        ComponentContext context = ComponentContext.getContext(request);
              
        // Get Teaser(module) name from tiles context
        String moduleInstanceName = context != null ? (String) context.getAttribute(Constants.MODULE_INSTANCE) : null;
        if (forcedModuleInstanceName != null)
            moduleInstanceName = forcedModuleInstanceName;
        
        // If Teaser(module) name not set in tiles context then throw exception
        if (moduleInstanceName == null) {

            moduleInstanceName = (String) request.getAttribute(
                Constants.MODULE_INSTANCE);
            if (moduleInstanceName == null) {
                throw new JspException("TagUtil: Teaser name " +
                                       moduleInstanceName +
                                       " not found in tiles context");
            }
        }
        
        //Get current site information from request
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
        if (siteDomain == null) {
            throw new JspException("TagUtil: unknown site");
        }
        
        // Generate full form name from teaser and form
        String formName = "site" + siteDomain.getSite().getSiteId() + moduleInstanceName + name;
        return formName;
    }
    
    /**
     * 
     * @param request
     * @param name
     * @param formObj
     * @param inSession
     * @throws JspException
     */
    public static void setForm( HttpServletRequest request, String name , ActionForm formObj, boolean inSession) throws JspException
    {
        String formName = getCanonicalFormName(request, name, null);
        logger.debug("Action form name is: " + formName);
        if ( inSession ) {
            request.getSession().setAttribute(formName, formObj);
        }
        else
            request.setAttribute(formName, formObj);            
    }

    
    /**
     *
     * @param request
     * @param pageContext
     * @param property
     * @return
     * @throws JspException
     */
    public static String generateContext(HttpServletRequest request,
                                         PageContext pageContext,
                                         String property) throws JspException {

        String contextPath = null;
        String moduleInstanceName = Constants.PROPERTY_MODULEINSTANCE;
        String moduleName = Constants.PROPERTY_MODULE;

        ComponentContext context = ComponentContext.getContext(request);

        /**
         * Get module context path
         */
        String contextPathName = (String) request.getAttribute(
            Constants.DIGI_CONTEXT);
        if (contextPathName == null) {
            throw new JspException(
                "Custom tag context: Digi context path is null");
        }

        /**
         * Get Teaser(module) instance name from tiles context
         */
        if( context != null ) {
            moduleInstanceName = (String) context.getAttribute(
                Constants.MODULE_INSTANCE);
        }

        /**
             * If Teaser(module) instance name not set in tiles context then throw exception
         */
        if (moduleInstanceName == null) {

            moduleInstanceName = (String) request.getAttribute(
                Constants.MODULE_INSTANCE);
        }

        /**
         * Get Teaser(module) name from tiles context
         */
        if( context != null ) {
            moduleName = (String) context.getAttribute(
                Constants.MODULE_NAME);
            if (moduleName == null) {
                moduleName = (String) request.getAttribute(
                    Constants.MODULE_NAME);
            }
        }


        StringBuffer tmp = new StringBuffer();
        String[] properties = property.split("\\/");

        if (properties.length == 0) {
            properties[0] = property;
        }

        for (int i = 0; i < properties.length; i++) {

            if (properties[i].equals(Constants.PROPERTY_CONTEXT)) {
                tmp.append(contextPathName);
            }
            else
            if (properties[i].equals(Constants.PROPERTY_MODULE)) {
                if (moduleName == null) {
                    throw new JspException("Custom tag context: Digi module name is null");
                }
                tmp.append(moduleName);
                if( !moduleInstanceName.equalsIgnoreCase(ModuleUtils.getModuleDefaultInstance(moduleName)) )
                    tmp.append(DgUtil.getParamSeparator());
                continue;
            }
            else
            if (properties[i].equals(Constants.PROPERTY_MODULEINSTANCE)) {
                if (moduleInstanceName == null) {
                    throw new JspException(
                        "Custom tag context: Teaser name " +
                        moduleInstanceName +
                        " not found in tiles context");
                }
                if( !moduleInstanceName.equalsIgnoreCase(ModuleUtils.getModuleDefaultInstance(moduleName)) ) {
                    tmp.append(moduleInstanceName);
                }
            }
            else {
                Object contextdata = pageContext.getAttribute(
                    properties[i], PageContext.PAGE_SCOPE);
                if (contextdata == null) {
                    tmp.append(properties[i]);
                }
                else {
                    tmp.append(contextdata.toString());
                }

            }

            tmp.append('/');
        }

        if( tmp.toString().endsWith("/") || tmp.toString().endsWith(DgUtil.getParamSeparator()) )
            tmp.delete(tmp.length() - 1, tmp.length());

        return tmp.toString();
    }


    public static String calculateURL(String url, boolean cache, PageContext pageContext ) {
        return calculateURL(url, cache, pageContext, true);
    }


    /**
     *
     * @return
     */
    public static String calculateURL(String url, boolean cache, PageContext pageContext, boolean returnUrl) {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        RefLinkManager refLinkManager = null;
        RefLink refLink = null;
        String calcUrl = null;

        // get file link instance if is first call then new
        refLinkManager = RefLinkManager.getInstance();

        refLink = refLinkManager.getRefLink(url, cache , request);

        if (refLink != null) {
            calcUrl = (request.getContextPath() != null) ?
                request.getContextPath() + refLink.getPath() :
                refLink.getPath();
        }

        return (returnUrl && calcUrl == null ) ? url : calcUrl;
    }

    /**
     *
     * @return
     */
    public static String calculateURL(String url, boolean cache,HttpServletRequest request) {

        RefLinkManager refLinkManager = null;
        RefLink refLink = null;
        String calcUrl = null;

        // get file link instance if is first call then new
        refLinkManager = RefLinkManager.getInstance();

        refLink = refLinkManager.getRefLink(url, cache, request);

        if (refLink != null) {
            calcUrl = DgUtil.getSiteUrl(RequestUtils.getSite(request), request) +
                refLink.getPath();
        }

        return calcUrl;
    }



}
