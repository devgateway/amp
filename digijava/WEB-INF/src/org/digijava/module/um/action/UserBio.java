/*
 *   UserBioAction.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserBio.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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


import java.sql.Blob;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Image;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserBioForm;
import org.digijava.module.um.util.DbUtil;


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

        User user = (User) request.getSession(true).getAttribute(Constants.USER);
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