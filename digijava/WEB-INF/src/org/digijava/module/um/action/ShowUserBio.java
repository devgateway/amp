/*
 *   ShowUserBio.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 19, 2003
 * 	 CVS-ID: $Id: ShowUserBio.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserBioForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.entity.UserPreferences;
import java.sql.Clob;

public class ShowUserBio
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserBioForm bioForm = (UserBioForm) form;

        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        String bio = user.getUserPreference().getBiography();
        if (bio != null) {
            bioForm.setBioText(bio);
        }

        bioForm.setHavePhoto( (user.getPhoto() == null) ? false : true);
        bioForm.setName(user.getName());

        return mapping.findForward("forward");
    }

}