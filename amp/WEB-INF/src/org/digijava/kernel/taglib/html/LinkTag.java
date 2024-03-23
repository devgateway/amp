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

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.DgUrlProcessor;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class LinkTag
    extends org.apache.struts.taglib.html.LinkTag {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(LinkTag.class);

    private String context = "context/module/moduleinstance";
    private String site;
    private String friendlyUrl = "true";
    private String moduleName = null;
    private String instanceName = null;

    /** @todo this attribute must be remove you may use friendlyUrl instead
     of frandlyUrl, only backward compatability,
       NOTE: DO NOTE USE THIS ATTRIBUTE IN YOUR JSP FILE */
    private String frandlyUrl = null;
    // ------------------------------------------

    private boolean dgscp = false;

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
    public int doEndTag() throws JspException {
        //Moved from doStartTag to doEndTag for changes in struts LinkTag class in struts 1.3.10

//        String contextPath = null;
        String action = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        String moduleName = DgUtil.getModuleName(request);
        StringBuffer context = new StringBuffer();

        if (getSite() != null) {

            String siteURL = null;
            String requestUrl = null;
            if (!SiteConfigUtils.getLogonSiteId().equalsIgnoreCase(getSite())) {

                Site site = SiteCache.lookupByName(getSite());
                SiteDomain domain = SiteUtils.getDefaultSiteDomain(site);

                siteURL = SiteUtils.getSiteURL(domain, request.getScheme(),
                                               request.getServerPort(),
                                               request.getContextPath());
            }
            else {
                // Special case, link to the login site
                siteURL = HttpLoginManager.getLoginSiteURL(request);
                if (siteURL.endsWith("/")) {
                    siteURL = siteURL.substring(0, siteURL.length() - 1);
}

                // org.digijava.kernel.Constants.REQUEST_URL is already encoded
                requestUrl = (String) request.getAttribute(org.digijava.kernel.
                    Constants.REQUEST_URL);
            }

            context.append(siteURL);
            context.append(TagUtil.generateContext(request, pageContext,
                getContextRenderPath(true)));
            context.append(getHref());

            if (requestUrl != null) {
                context.append("?" + requestUrl);
            }
        }
        else {

            context.append(TagUtil.generateContext(request, pageContext,
                getContextRenderPath(false)));
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
        logger.debug("Current action for digi link tag: " + action);
        setHref(context.toString());
        logger.debug("Current context for digi link tag: " + context);

        // Special case for name anchors
        if (linkName != null) {
            StringBuffer results = new StringBuffer("<a name=\"");
            results.append(linkName);
            results.append("\">");
            TagUtils.getInstance().write(pageContext, results.toString());
            return (EVAL_BODY_BUFFERED);
        }

        //Remove jsessionid from calculated URL
        String calculateUrl = DgUtil.removeJSessionId(calculateURL());

        logger.debug("Current calculate Url for digi link tag: " + calculateUrl);

        if (frandlyUrl != null)
            friendlyUrl = frandlyUrl;

        // modify google friendly if friendlyUrl = true
        if (friendlyUrl.equalsIgnoreCase("true")) {
            logger.debug("Google friendly true, module name: " + moduleName);
            int n = action.indexOf(".");
            if (n != -1 && getSite() == null) {
                calculateUrl = DgUrlProcessor.formatUrl(calculateUrl.replaceAll(
                    "\\&amp;", "&"),
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
        // Prepare the textual content and ending element of this hyperlink
        if (text != null) {
            results.append(text);
        }
        results.append("</a>");

        // Print this element to our output writer
        TagUtils.getInstance().write(pageContext, results.toString());

        // Evaluate the body of this tag
        this.text = null;

        return (EVAL_BODY_BUFFERED);
    }

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

    /**
     * Returns context render path: If module attribute is set, it returns
     * context/<module> string. If instance is set too,
     * it returns context/<module>/<instance>. Else it returns
     * context/module/moduleinstance
     * @return context render path
     */
    private String getContextRenderPath(boolean isSiteSpecified) {
        if (moduleName == null) {
            if (isSiteSpecified && context != null && context.indexOf("context/") >= 0) {
                return context.replaceAll("context/", "/");
            } else {
                return context;
            }
        }
        else {
            String result;
            if (instanceName == null) {
                result = moduleName;
            }
            else {
                result = moduleName + "/" + instanceName;
            }
            if (isSiteSpecified) {
                return result;
            } else {
                return "context/" + result;
            }
        }
    }

    public String getInstance() {
        return instanceName;
    }

    public void setInstance(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getModule() {
        return moduleName;
    }

    public void setModule(String moduleName) {
        this.moduleName = moduleName;
    }

}
