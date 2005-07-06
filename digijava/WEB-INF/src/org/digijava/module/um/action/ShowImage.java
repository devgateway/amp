/*
 *   ShowImage.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowImage.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.module.um.action;

import javax.servlet.ServletException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import javax.servlet.ServletOutputStream;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import java.io.InputStream;
import org.digijava.kernel.entity.Image;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.I18NHelper;
import java.util.regex.Pattern;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowImage
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowImage.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        long id = 76;

        User user = (User)request.getSession(true).getAttribute(Constants.USER);

        if( user != null ) {
            if( user.getPhoto() != null )
                id = user.getPhoto().getId().longValue();
        }

        id = ( request.getParameter("id") == null ||
               (!Pattern.matches("\\p{Digit}*\\p{Digit}", request.getParameter("id")) )
               ) ? id : Long.parseLong(request.getParameter("id"));

        Image image = DbUtil.getImage(id);

        if( image != null && image.getImage() != null) {
            response.setContentType(image.getContentType());
            ServletOutputStream output = response.getOutputStream();
                output.write(image.getImage());
                output.flush();
        }
        else {
            // if set debug mode then print out
            logger.debug("No image for given identifier #" + id);
        }

        return null;
    }
}