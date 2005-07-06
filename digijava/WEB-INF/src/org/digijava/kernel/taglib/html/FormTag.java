/*
 *   FormTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: FormTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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

package org.digijava.kernel.taglib.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.digijava.kernel.taglib.util.TagUtil;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.PageContext;
import org.apache.struts.taglib.html.Constants;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.RequestUtils;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import java.util.Iterator;
import org.digijava.kernel.util.SiteConfigManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.security.DgSecurityManager;

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

    private String oldAction;
    private String site;
    private String referrer;

    private String context = "context/module/moduleinstance";

    public String getContextPath() {
        return context;
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

    public void setReferrer(String referrer) {
        this.referrer = referrer;
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


        String context = TagUtil.generateContext(request, pageContext, "module");

        oldAction = action;
        action = context + action;

        // Look up the form bean name, scope, and type if necessary
        lookup();

        // Create an appropriate "form" element based on our parameters
        StringBuffer results = new StringBuffer();

        results.append(this.renderFormStart());

        results.append(renderToken());

        ResponseUtils.write(pageContext, results.toString());

        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);

        Object objectForm = TagUtil.getForm( pageContext, beanName );

        if (objectForm == null) {
            throw new JspException("Custom tag Form: Form " + beanName +
                                   " not found in any scope");
        }

        pageContext.setAttribute(beanName, objectForm);

        pageContext.setAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY, objectForm);

        initFormBean();

        return (EVAL_BODY_INCLUDE);
    }

    protected String renderFormStart() throws JspException {

        String contextPath = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(org.digijava.kernel.Constants.CURRENT_SITE);

        StringBuffer context = new StringBuffer();

        if( getSite() != null ) {

            SiteDomain domain = null;
            String siteURL = null;

            if( !SiteConfigUtils.getLogonSiteId().equalsIgnoreCase(getSite()) ) {

                Site site = PersistenceManager.getSite(getSite());
                Iterator iter = site.getSiteDomains().iterator();
                while (iter.hasNext()) {
                    domain = (SiteDomain) iter.next();
                }

                siteURL = request.getScheme() + "://" +
                    domain.getSiteDomain() + ":" +
                    new Long(request.getServerPort()).toString() +
                    SiteConfigUtils.buildDgURL(request, domain, false);
            } else {
                StringBuffer tmpSiteURL =  new StringBuffer( SiteConfigUtils.getLogonSite() );
                if( tmpSiteURL.charAt(tmpSiteURL.length() - 1) == '/') {
                    tmpSiteURL.delete(tmpSiteURL.length() - 1, tmpSiteURL.length());
                }
                siteURL = tmpSiteURL.toString();
            }

            context.append(siteURL);
            context.append(TagUtil.generateContext(request, pageContext,getContextPath()));
            context.append(this.oldAction);

            /**
             * @todo this "formula" is standartized and developer must use corresponding method
             */
/*            String uri = request.getScheme() + "://" +
                siteDomain.getSiteDomain() + ":" +
                new Long(request.getServerPort()).toString() +
                SiteConfigUtils.buildDgURL(request, siteDomain, false);*/

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
        results.append(beanName);
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


