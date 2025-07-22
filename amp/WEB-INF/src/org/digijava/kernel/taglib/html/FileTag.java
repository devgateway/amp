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
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

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

    private static final long serialVersionUID = 1L;

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
