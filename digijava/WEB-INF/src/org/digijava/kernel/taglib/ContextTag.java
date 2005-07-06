/*
 *   ContextTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ContextTag.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.kernel.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.digijava.kernel.taglib.util.TagUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ContextTag
    extends TagSupport {

    /**
     * Name of the property to be accessed on the specified context.
     */
    private String property = "context/module/moduleinstance";

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * context name definition
     */
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

        /**
         * Generate full context path
         */
        String context = TagUtil.generateContext(request, pageContext,
                                                 getProperty());

        pageContext.setAttribute(getName(), context);

        // Continue processing this page
        return (SKIP_BODY);
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

}