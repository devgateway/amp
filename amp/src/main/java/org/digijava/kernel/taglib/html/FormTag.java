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

package org.digijava.kernel.taglib.html;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.util.SiteUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FormTag
    extends org.apache.struts.taglib.html.FormTag {

    private static final long serialVersionUID = 1L;
    private String oldAction;
    private String site;
    private String referrer;
    private String moduleName;
    private String instanceName;
    private String name;

    public String getName() {
        if(name==null){
            this.name=beanName;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return beanType;
    }

    public void setType(String type) {
        this.beanType = type;
    }

    private String context = "context/module/moduleinstance";

    public String getContextPath() {
        if (moduleName == null) {
            return context;
        }
        else {
            if (instanceName == null) {
                return "context/" + moduleName;
            }
            else {
                return "context/" + moduleName + "/" + instanceName;
            }
        }
    }

    public void setContextPath(String context) {
        this.context = context;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getInstance() {
        return instanceName;
    }

    public String getModule() {

        if( moduleName == null ) {
            return "module";
        }

        return moduleName;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setInstance(String instance) {
        this.instanceName = instance;
    }

    public void setModule(String module) {
        this.moduleName = module;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Process the start tag.
     *
     * @return value
     * @throws JspException
     */
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();


        String context = TagUtil.generateContext(request, pageContext, getModule());

        oldAction = action;
        action = context + action;

        // Look up the form bean name, scope, and type if necessary
       lookup();

        // Create an appropriate "form" element based on our parameters
        StringBuffer results = new StringBuffer();

        results.append(this.renderFormStart());

        results.append(renderToken());

        TagUtils.getInstance().write(pageContext, results.toString());

        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);

        Object objectForm = TagUtil.getForm( pageContext, beanName );

        if (objectForm != null) {
            // throw new JspException("Custom tag Form: Form " + beanName +
            //                        " not found in any scope");


            pageContext.setAttribute(beanName, objectForm);

            pageContext.setAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY,
                                     objectForm);

            initFormBean();
        }

        return (EVAL_BODY_INCLUDE);
    }

    protected String renderFormStart() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
//        SiteDomain siteDomain = (SiteDomain) request.getAttribute(org.digijava.kernel.Constants.CURRENT_SITE);

        StringBuffer context = new StringBuffer();

        if( getSite() != null ) {

//            SiteDomain domain = null;
            String siteURL = null;

            if( !SiteConfigUtils.getLogonSiteId().equalsIgnoreCase(getSite()) ) {

                Site site = SiteCache.lookupByName(getSite());
                siteURL = SiteUtils.getSiteURL(SiteUtils.getDefaultSiteDomain(site), request.getScheme(), request.getServerPort(), request.getContextPath());
            } else {
                StringBuffer tmpSiteURL =  new StringBuffer( HttpLoginManager.getLoginSiteURL(request) );
                if( tmpSiteURL.charAt(tmpSiteURL.length() - 1) == '/') {
                    tmpSiteURL.delete(tmpSiteURL.length() - 1, tmpSiteURL.length());
                }
                siteURL = tmpSiteURL.toString();
            }

            context.append(siteURL);
            context.append(TagUtil.generateContext(request, pageContext,getContextPath()));
            context.append(this.oldAction);

            if( referrer != null ) {
                context.append("?" + org.digijava.kernel.Constants.REFERRER_PARAM + "=" + DgUtil.encodeString(referrer));
            }
            else {
                if (!SiteConfigUtils.isParam(org.digijava.kernel.Constants.REFERRER_PARAM, request)) {
                    //org.digijava.kernel.Constants.REQUEST_URL is already encoded
                    String url = (String) request.getAttribute(org.digijava.kernel.Constants.REQUEST_URL);
                    if (url != null)
                        context.append("?" + url);
                }
                else {
                    context.append("?" +
                                   DgUtil.generateReferrer(request,
                        request.
                        getParameter(org.digijava.kernel.Constants.REFERRER_PARAM)));
                }
            }
        } else {
            context.append(TagUtil.generateContext(request, pageContext,getContextPath()));
            context.append(this.oldAction);
        }

        StringBuffer results = new StringBuffer("<form");
        results.append(" name=\"");
        results.append(getName());
        results.append("\"");
        results.append(" method=\"");
        results.append(method == null ? "post" : method);
        results.append("\" action=\"");
        results.append( response.encodeURL(context.toString()) );

        results.append("\"");

        if (styleClass != null) {
            results.append(" class=\"");
            results.append(styleClass);
            results.append("\"");
        }
        if (enctype != null) {
            results.append(" enctype=\"");
            results.append(enctype);
            results.append("\"");
        }
        if (onreset != null) {
            results.append(" onreset=\"");
            results.append(onreset);
            results.append("\"");
        }
        if (onsubmit != null) {
            results.append(" onsubmit=\"");
            results.append(onsubmit);
            results.append("\"");
        }
        if (style != null) {
            results.append(" style=\"");
            results.append(style);
            results.append("\"");
        }
        if (styleId != null) {
            results.append(" id=\"");
            results.append(styleId);
            results.append("\"");
        }
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }
        results.append(">");
        return results.toString();
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

}


