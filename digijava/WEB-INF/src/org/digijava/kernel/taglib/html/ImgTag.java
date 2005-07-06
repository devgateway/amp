/*
 *   ImgTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Sept 27, 2003
 * 	 CVS-ID: $Id: ImgTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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

import org.digijava.kernel.taglib.util.RefLink;
import org.digijava.kernel.taglib.util.RefLinkManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.taglib.util.TagUtil;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import org.digijava.kernel.util.I18NHelper;
import org.apache.log4j.Logger;

/**
 *
 * Support inheritable images, giving <img src=""> an appropriate URL,
 * according to whether the actual file wase found in site's folder,
 * template's folder or the blank template folder.
 *
 * This means that an image may be in site tempalte folder and site does not
 * necessarily need to copy it into its own folder, it can be used from the
 * template (and from blank template by any site, therefore.).
 *
 * The search is performed in the following order: Site folder, Template folder
 * Blank Template folder. To boost the performance the result of this lookup is
 * cached. On the development server, you can turn off the cacheing from
 * digi.xml, to have smoother development process.
 *
 * The tag may also have cache="false" attribute setting which will turn off
 * cacheing, in case if the image is frequently-changing its source place and
 * cacheing does not make any sense.
 *
 */

public class ImgTag
    extends org.apache.struts.taglib.html.ImgTag {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ImgTag.class);

    private String cache = "false";
    private boolean skipBody = false;

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public boolean isSkipBody() {
        return skipBody;
    }

    public void setSkipBody(boolean skipBody) {
        this.skipBody = skipBody;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String src;

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        if ( (getCache() != null && getCache().trim().length() > 0 &&
              getCache().equalsIgnoreCase("false") && !DgUtil.isResourceCached()))
            src = TagUtil.calculateURL(getSrc(), false, pageContext, false);
        else
            src = TagUtil.calculateURL(getSrc(), true, pageContext, false);

        if( src != null ) {
            this.skipBody = false;
        }


        setSrc((src == null) ? getSrc() : src);

        return super.doStartTag();
    }


    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        if( this.skipBody ) {
            return (EVAL_PAGE);
        }

        return super.doEndTag();
    }

}