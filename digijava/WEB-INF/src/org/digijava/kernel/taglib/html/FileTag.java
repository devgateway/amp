/*
 *   FileTag.java
 * 	 @Author Irakli Nadareishvili inadareishvili@worldbank.org
 * 	 Created: Oct 29, 2003
 * 	 CVS-ID: $Id: FileTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;

/**
 * Support inheritable resources like: images, css, javascript. giving
 * appropriate URL to them, according to whether they were found in site's
 * folder, template's folder or blank folder.
 *
 * This means that an image may be in site tempalte folder and site does not
 * necessarily need to copy it into its own folder, it can be used from the
 * template (and from blank template by any site, therefore.).
 *
 * The search is performed in the following order: Site folder, Template folder
 * Blank Template folder. To boost the performance the result of this lookup is
 * cached. On the development server, you can turn off the cacheing from digi.xml, though.
 *
 * The tag may also have cache="false" attribute setting which will turn off
 * cacheing, in case if the resource is frequently-changing its place and cacheing
 * it does not make any sense.
 *
 */
public class FileTag
    extends org.apache.struts.taglib.html.ImgTag {

    private static Logger logger = Logger.getLogger(FileTag.class);

    private String cache = "false";

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }


    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
      String src;

      if ( (getCache() != null && getCache().trim().length() > 0 &&
            getCache().equalsIgnoreCase("false") && !DgUtil.isResourceCached()))
        src = TagUtil.calculateURL(getSrc(), false, pageContext );
      else
        src = TagUtil.calculateURL(getSrc(), true, pageContext );

          try {
              JspWriter out = pageContext.getOut();
              out.write(src);
          }
          catch (IOException e) {
              logger.warn(" img tag failed to render ",e);
          }

          return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
          return EVAL_PAGE;
    }
}