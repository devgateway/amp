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

package org.digijava.kernel.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
/**
 * Custom tag that retrieves an internationalized messages string
 *
 * @author inadareishvili@worldbank.org
 * @version $Id: IsAdmin.java,v 1.2 2008-12-15 15:49:38 kobia Exp $
 */

public class IsAdmin extends BodyTagSupport {

    private static final long serialVersionUID = 1L;
    private Logger log =
        Logger.getLogger("org.developmentgateway.core.taglib.AuthorizeTag");
    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() {

        try {
            //set up the logger
            //log.setResourceBundle(I18NHelper.getAiDABundle());

            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();


//          boolean showData = false;


                if (request.getParameter("user") == null) {
                    return (SKIP_BODY);
                }else{

                    if(request.getParameter("user").equalsIgnoreCase("admin")){

                            return EVAL_BODY_INCLUDE;

                        }else{
                            return (SKIP_BODY);

                            }

                    }

        } catch (Exception exception) {

            //TODO: comment this
            log.error("Could not retrieve an internationalized messages",exception);

            //TODO: UnCOMMENT this
            //if (log.isEnabledFor(Level.ERROR)) {
            //  Object[] obj = { "AuthorizeTag - doStartTag()" };
            //  log.l7dlog(
                //  Level.ERROR,
                //  "TaglibClass.Exception.err",
                //  obj,
                //  exception);
            //}
        }

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
