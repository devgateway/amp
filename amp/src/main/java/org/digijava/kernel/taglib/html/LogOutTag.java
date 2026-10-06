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
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LogOutTag
    extends org.apache.struts.taglib.html.LinkTag {

    private static final long serialVersionUID = 1L;

    private String returnUrl;

    private static Logger logger = Logger.getLogger(LogOutTag.class);


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

        String calculateUrl = "";
        String referrer;

        if( getReturnUrl() == null ) {
            referrer = (String) request.getAttribute(org.digijava.kernel.Constants.REQUEST_URL);
        } else {
            SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
            String url = SiteUtils.getSiteURL(siteDomain, request.getScheme(),
                                              request.getServerPort(),
                                              request.getContextPath());

            referrer = Constants.REFERRER_PARAM + "=" + url + "~" +
                DgUtil.encodeString(getReturnUrl());
        }

        calculateUrl = HttpLoginManager.getLogoutURL(request, referrer);
        logger.debug("Current calculate Url for digi logout tag: " + calculateUrl );


        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a href=\"");
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

}
