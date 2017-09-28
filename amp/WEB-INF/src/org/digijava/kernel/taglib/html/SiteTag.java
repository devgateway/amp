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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;

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

    private static final long serialVersionUID = 1L;

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
             site = SiteCache.lookupByName(getSiteId());
             if( site != null ) {
                  siteTag = DgUtil.getSiteUrl(site, request);
              } else {
                  siteTag = "Could not determine site";
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
