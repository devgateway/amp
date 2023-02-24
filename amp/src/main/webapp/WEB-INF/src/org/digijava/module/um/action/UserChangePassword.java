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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserChangePasswordForm;
import org.digijava.module.um.util.DbUtil;

public class UserChangePassword
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(UserChangePassword.class);

    public UserChangePassword() {
    }

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserChangePasswordForm userUpdateForm = (UserChangePasswordForm) form;

        User user = RequestUtils.getUser(request);

        if( user != null ) {
            if (DbUtil.isCorrectPassword(user.getEmail(), userUpdateForm.getCurrentpassword())) {

                // Update Password
                DbUtil.updatePassword(user.getEmail(), userUpdateForm.getCurrentpassword(), userUpdateForm.getNewpassword());
            }
            else {

                if (logger.isDebugEnabled()) {
                    logger.debug("Invalid password");
                }

                // current password don't match
                ActionMessages errors = new ActionMessages();
                errors.add(null, new ActionMessage("error.password.invalid"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

        }

        return mapping.findForward("success");
    }

}
