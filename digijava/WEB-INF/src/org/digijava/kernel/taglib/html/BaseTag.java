/*
 *   BaseTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: BaseTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
import javax.servlet.jsp.PageContext;
import org.apache.struts.Globals;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BaseTag
    extends org.apache.struts.taglib.html.BaseTag {

    private static Logger logger = Logger.getLogger(BaseTag.class);

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String filePath = (String)request.getAttribute(Constants.INCLUDE_SERVLET_PATH);
        if (filePath == null) {
           filePath = request.getServletPath();
        }
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(filePath, "/");
        String path = null;
        String buffer = tokenizer.nextToken();
        if (buffer.equalsIgnoreCase("TEMPLATE")) {
           path = "/" + buffer + "/" + tokenizer.nextToken() + "/";
        } else {
           path = "/" + buffer + "/";
        }


        String serverName = (this.server == null) ? request.getServerName() : this.server;

        // render base tag
        String baseTag =
            renderBaseElement(
            request.getScheme(),
            serverName,
            request.getServerPort(),
            ( (request.getContextPath() == null) ? "" :
             request.getContextPath()) + path
            );


        JspWriter out = pageContext.getOut();
        try {
            out.write(baseTag);
        }
        catch (IOException e) {
            logger.debug("doStartTag() failed ",e);

            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                                     PageContext.REQUEST_SCOPE);
            throw new JspException(messages.getMessage("common.io", e.toString()));
        }

        return EVAL_BODY_INCLUDE;
    }

}