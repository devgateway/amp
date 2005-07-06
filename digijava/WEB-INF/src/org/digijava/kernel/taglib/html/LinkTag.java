/*
 *   LinkTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: LinkTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts.util.ResponseUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.DgUrlProcessor;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteConfigUtils;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import org.digijava.kernel.Constants;
import java.util.regex.Pattern;
import org.digijava.kernel.util.RequestUtils;
import java.net.URLDecoder;
import java.io.*;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Lasha Dolidze
 * @version 1.0
 */

public class LinkTag
    extends org.apache.struts.taglib.html.LinkTag {

    private static Logger logger = Logger.getLogger(LinkTag.class);

    private String context = "context/module/moduleinstance";
    private String site;
    private String friendlyUrl = "true";

    /** @todo this attribute must be remove you may use friendlyUrl instead
     of frandlyUrl, only backward compatability,
    NOTE: DO NOTE USE THIS ATTRIBUTE IN YOUR JSP FILE */
    private String frandlyUrl = null;
    // ------------------------------------------

    private boolean dgscp=false;

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

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public String getFrandlyUrl() {
        return frandlyUrl;
    }

    public void setFrandlyUrl(String frandlyUrl) {
        this.frandlyUrl = frandlyUrl;
    }

// --------------------------------------------------------- Public Methods

    /**
     * Process the start tag.
     *
     * @return value
     * @throws JspException
     */
    public int doStartTag() throws JspException {

        String contextPath = null;
        String action = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        SiteDomain siteDomain = (SiteDomain) request.getAttribute(org.digijava.
            kernel.Constants.CURRENT_SITE);
        String moduleName = DgUtil.getModuleName(request);
        StringBuffer context = new StringBuffer();

        if (getSite() != null) {

            SiteDomain domain = null;
            String siteURL = null;

            if (!SiteConfigUtils.getLogonSiteId().equalsIgnoreCase(getSite())) {

                Site site = PersistenceManager.getSite(getSite());
                Iterator iter = site.getSiteDomains().iterator();
                while (iter.hasNext()) {
                    domain = (SiteDomain) iter.next();
                }

                siteURL = request.getScheme() + "://" +
                    domain.getSiteDomain() + ":" +
                    new Long(request.getServerPort()).toString() +
                    SiteConfigUtils.buildDgURL(request, domain, false);
            }
            else {
                StringBuffer tmpSiteURL = new StringBuffer(SiteConfigUtils.
                    getLogonSite());
                if (tmpSiteURL.charAt(tmpSiteURL.length() - 1) == '/') {
                    tmpSiteURL.delete(tmpSiteURL.length() - 1,
                                      tmpSiteURL.length());
                }
                siteURL = tmpSiteURL.toString();
            }

            context.append(siteURL);
            context.append(TagUtil.generateContext(request, pageContext,
                getContextPath()));
            context.append(getHref());
/*            String uri = request.getScheme() + "://" +
                siteDomain.getSiteDomain() + ":" +
                new Long(request.getServerPort()).toString() +
                SiteConfigUtils.buildDgURL(request, siteDomain, false);
*/

           // org.digijava.kernel.Constants.REQUEST_URL is already encoded
           String url = (String) request.getAttribute(org.digijava.kernel.Constants.REQUEST_URL);
           if (url != null)
               context.append("?" + url);

        }
        else {

            context.append(TagUtil.generateContext(request, pageContext,
                getContextPath()));
            context.append(getHref());
        }

        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);
        if (moduleInstance != null && dgscp) {
            if (context.toString().indexOf("?") < 0) {
                context.append("?");
            }
            else {
                context.append("&");
            }
            context.append("dgscp=").append(moduleInstance.getModuleName()).
                append(":").append(moduleInstance.getInstanceName());
        }

        action = getHref();
        logger.debug("Current action for digi link tag: " + action );
        setHref(context.toString());
        logger.debug("Current context for digi link tag: " + context );

        // Special case for name anchors
        if (linkName != null) {
            StringBuffer results = new StringBuffer("<a name=\"");
            results.append(linkName);
            results.append("\">");
            ResponseUtils.write(pageContext, results.toString());
            return (EVAL_BODY_BUFFERED);
        }


        //Remove jsessionid from calculated URL
        String calculateUrl = DgUtil.removeJSessionId (calculateURL());

        logger.debug("Current calculate Url for digi link tag: " + calculateUrl );

        if( frandlyUrl != null )
            friendlyUrl = frandlyUrl;

        // modify google friendly if friendlyUrl = true
        if (friendlyUrl.equalsIgnoreCase("true")) {
            logger.debug("Google friendly true, module name: " + moduleName);
            int n = action.indexOf(".");
            if (n != -1 && getSite() == null) {
                    calculateUrl = DgUrlProcessor.formatUrl(calculateUrl.replaceAll("\\&amp;","&"),
                        moduleName, action.substring(0, n), request);
            }
        }

        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a href=\"");
        // * @since Struts 1.1
        results.append(calculateUrl);
        results.append("\"");
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(prepareStyles());
        results.append(prepareEventHandlers());
        results.append(">");

        // Print this element to our output writer
        ResponseUtils.write(pageContext, results.toString());

        // Evaluate the body of this tag
        this.text = null;

        return (EVAL_BODY_BUFFERED);
    }


    /**
     * Save the associated label from the body content.
     * @todo this attribute must be remove you may use friendlyUrl instead
     * of frandlyUrl, only backward compatability,
     * @exception JspException if a JSP exception has occurred
     */
/*    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
          if( frandlyUrl != null ) {
              HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              String servletPath = (String) request.getAttribute(Constants.INCLUDE_SERVLET_PATH);
              text = "<font color=red>digi link tag, You may use friendlyUrl attribute instead of frandlyUrl</font>";
              if( servletPath != null )
                  text += " (<b>File: " + servletPath + "</b>)";
          } else {
              return super.doAfterBody();
          }
       }
        return (SKIP_BODY);
    }*/

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

    public boolean isDgscp() {
        return dgscp;
    }

    public void setDgscp(boolean dgscp) {
        this.dgscp = dgscp;
    }

}
