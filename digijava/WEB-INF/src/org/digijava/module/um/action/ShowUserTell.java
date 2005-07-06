/*
*   ShowUserTell.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ShowUserTell.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.form.UserContactForm;
import org.digijava.module.um.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowUserTell extends Action {

     public ActionForward execute(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
      IOException, UMException, DgException {

      UserContactForm userForm = (UserContactForm) form;
      User   user = RequestUtils.getUser(request);
      User   contactUser = null;

      if( userForm.getActiveUserId() != null ) {
          contactUser = DbUtil.getSelectedUser(userForm.getActiveUserId(), request);
      } else {
          contactUser = user;
      }

      if (user != null) {

          if (contactUser != null) {
              userForm.setSenderName(user.getName());
              userForm.setSenderEmail(user.getEmail());
              userForm.setContentLanguages(TrnUtil.getSortedUserLanguages(
                  request));
          }
          else {
              ActionErrors errors = new ActionErrors();
              errors.add(null, new ActionError("error.um.selectedUserEmpty"));
          }
      }
      else {
          ActionErrors errors = new ActionErrors();
          errors.add(null, new ActionError("error.um.userNotLoggedin"));
      }


      return mapping.findForward("forward");
    }
 }

