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

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

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

    private static final long serialVersionUID = 1L;

    // log4J class initialize String
//    private static Logger logger = I18NHelper.getKernelLogger(ImgTag.class);

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

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        boolean cacheImages = (getCache() != null &&
                               getCache().trim().length() > 0 &&
                               getCache().equalsIgnoreCase("false") &&
                               !DgUtil.isResourceCached());

        Locale navLocale = RequestUtils.getNavigationLanguage(request);

        String sourceURL = localizeImageUrl(getSrc(), navLocale);

        String newSrc = TagUtil.calculateURL(sourceURL, cacheImages,
                                             pageContext, false);

        if (newSrc == null) {
            newSrc = TagUtil.calculateURL(getSrc(), cacheImages,
                                          pageContext, false);
        }

        if (newSrc != null) {
            this.skipBody = false;
        }

        setSrc( (newSrc == null) ? getSrc() : newSrc);

        return super.doStartTag();
    }

    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        if (this.skipBody) {
            return (EVAL_PAGE);
        }

        return super.doEndTag();
    }

    public static String localizeImageUrl(String url, Locale locale) {
        int pointPos = url.lastIndexOf('.');
        if (pointPos < 0) {
            return url + "_" + locale.getCode();
        }
        else {
            String first = url.substring(0, pointPos);
            String last = url.substring(pointPos);
            return first + "_" + locale.getCode() + last;
        }
    }
}
