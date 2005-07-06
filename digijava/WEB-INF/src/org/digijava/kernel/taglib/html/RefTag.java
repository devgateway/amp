/*
 *   ImgTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Sept 27, 2003
 * 	 CVS-ID: $Id: RefTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
import org.apache.struts.util.ResponseUtils;

import org.digijava.kernel.taglib.util.RefLink;
import org.digijava.kernel.taglib.util.RefLinkManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.taglib.util.TagUtil;

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
        ResponseUtils.write(pageContext, results.toString());


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
        ResponseUtils.write(pageContext, results.toString());

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }


}