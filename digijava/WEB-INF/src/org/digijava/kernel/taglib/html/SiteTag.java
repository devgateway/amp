/*
 *   SiteTag.java
 * 	 @Author Irakli Nadareishvili inadareishvili@worldbank.org
 * 	 Created: Sept 27, 2003
 * 	 CVS-ID: $Id: SiteTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.exception.DgException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SiteTag
    extends TagSupport {

    private static Logger logger = Logger.getLogger(SiteTag.class);

    String property;
    String siteId;

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        String property = this.property;

        Site site = null;
        String siteTag = "";


        if (getSiteId() != null && getSiteId().trim().length() > 0) {
            try {
                site = SiteUtils.getSite(getSiteId());
                if( site != null ) {
                    siteTag = DgUtil.getSiteUrl(site, request);
                } else {
                    siteTag = "Could not determine site";
                }
            }
            catch (DgException ex) {
                logger.warn("Could not get Site from Database",ex);
            }
        } else {
            //-- Get current Site object from request
            site = RequestUtils.getSite(request);

            if ( site != null )  {
                if (property.equals("name")) {
                    siteTag = site.getName();
                }
                else if (property.equals("url")) {
                    siteTag = DgUtil.getCurrRootUrl(request);
                }
                else {
                    siteTag =
                        "Valid values for the parameter 'property' are only: name, url.";
                }
            } else { siteTag = "Could not determine current site."; }
        }

        if ( siteTag == null ) { //getName or getUrl or some other returned null
            siteTag = "";
        }

        try {
            JspWriter out = pageContext.getOut();
            out.write(siteTag);
        }
        catch (IOException e) {
            logger.warn(" SiteTag failed to render ",e);
        }

        return EVAL_BODY_INCLUDE;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}