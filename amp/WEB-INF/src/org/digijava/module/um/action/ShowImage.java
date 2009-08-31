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
import org.digijava.kernel.util.RequestUtils;

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

        long id = -1;

        User user = RequestUtils.getUser(request);

        if( user != null ) {
            if( user.getPhoto() != null )
                id = user.getPhoto().getId().longValue();
        }

        id = ( request.getParameter("id") == null ||
               (!Pattern.matches("\\p{Digit}*\\p{Digit}", request.getParameter("id")) )
               ) ? id : Long.parseLong(request.getParameter("id"));

        Image image = null;
       if (id != -1) {
           image = DbUtil.getImage(id);
       }

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
