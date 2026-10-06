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


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.Image;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserBioForm;
import org.digijava.module.um.util.DbUtil;

import java.sql.Blob;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserBio
    extends Action {

    private static Logger logger = Logger.getLogger(UserBio.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserBioForm bioForm = (UserBioForm) form;

        User user = RequestUtils.getUser(request);
        Blob imagetmp = null;

           FormFile formFile = bioForm.getPhotoFile();
           Image tmp = user.getPhoto();
           if( formFile != null ) {
               if( formFile.getFileSize() != 0 ) {
                   if (tmp == null) {
                       logger.debug("%%%%% NO IMAGE CREATE ONE");
                       tmp = new Image();
                       user.setPhoto(tmp);
                   }

                       logger.debug("%%%%% SET IMAGE");
                       tmp.setContentType(formFile.getContentType());
                       tmp.setImage(formFile.getFileData());
               }
           }
           if( request.getParameter("clear") != null ) {
               logger.debug("%%%%% EMPTY SUBMIT");
               if (tmp != null) {
                   logger.debug("%%%%% IMAGE");
                   tmp.setContentType(null);
                   tmp.setImage(null);
               }
           }

//        user.getUserPreference().setBiography(Hibernate.createClob(bioForm.getBioText()));
         if( bioForm.getBioText() != null )
             user.getUserPreference().setBiography(bioForm.getBioText());
         else
             bioForm.setBioText(user.getUserPreference().getBiography());


        DbUtil.updateUserBio(user);
        logger.debug("End update bio");

        return mapping.findForward("forward");

    }
}
