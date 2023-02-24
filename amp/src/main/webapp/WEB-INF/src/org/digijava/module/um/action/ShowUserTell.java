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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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
              userForm.setSenderEmail(user.getEmailUsedForNotification());
              userForm.setContentLanguages(TrnUtil.getSortedUserLanguages(
                  request));
          }
          else {
              ActionMessages errors = new ActionMessages();
              errors.add(null, new ActionMessage("error.um.selectedUserEmpty"));
          }
      }
      else {
          ActionMessages errors = new ActionMessages();
          errors.add(null, new ActionMessage("error.um.userNotLoggedin"));
      }


      return mapping.findForward("forward");
    }
 }

