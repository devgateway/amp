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
import org.apache.struts.Globals;
import org.digijava.kernel.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

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

    private static final long serialVersionUID = 1L;
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
