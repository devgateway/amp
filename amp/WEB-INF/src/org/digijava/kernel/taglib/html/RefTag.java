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

import org.apache.struts.taglib.TagUtils;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Lasha Dolidze
 * @version 1.0
 */

public class RefTag
    extends org.apache.struts.taglib.html.BaseHandlerTag {

    private static final long serialVersionUID = 1L;

    /**
     * The body content of this tag (if any).
     */
    protected String text = null;

    private String cache = "false";
    private String href;
    private String hrefLang;
    private String charset;
    private String type;
    private String rel;
    private String rev;
    private String media;
    private String title;

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getCharset() {
        return charset;
    }

    public String getHref() {
        return href;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public String getMedia() {
        return media;
    }

    public String getRel() {
        return rel;
    }

    public String getRev() {
        return rev;
    }

    public String getType() {
        return type;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<link href=\"");
        if ( (getCache() != null && getCache().trim().length() > 0 &&
              getCache().equalsIgnoreCase("false") && !DgUtil.isResourceCached()))
          results.append(TagUtil.calculateURL(href, false, pageContext ));
        else
          results.append(TagUtil.calculateURL(href, true, pageContext ));

        results.append("\"");
        if (title != null) {
            results.append(" title=\"");
            results.append(title);
            results.append("\"");
        }
        if (rel != null) {
            results.append(" rel=\"");
            results.append(rel);
            results.append("\"");
        }
        if (type != null) {
            results.append(" type=\"");
            results.append(type);
            results.append("\"");
        }
        if (rev != null) {
            results.append(" rev=\"");
            results.append(rev);
            results.append("\"");
        }
        if (media != null) {
            results.append(" media=\"");
            results.append(media);
            results.append("\"");
        }
        if (hrefLang != null) {
            results.append(" hreflang=\"");
            results.append(hrefLang);
            results.append("\"");
        }
        if (charset != null) {
            results.append(" charset=\"");
            results.append(charset);
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


        return (EVAL_BODY_BUFFERED);
    }

    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return (SKIP_BODY);

    }


    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Prepare the textual content and ending element of this hyperlink
        StringBuffer results = new StringBuffer();
        if (text != null)
            results.append(text);
        results.append("</link>");

        // Render the remainder to the output stream
        TagUtils.getInstance().write(pageContext, results.toString());

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }


}
