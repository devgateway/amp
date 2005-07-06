/*
 *   AdministrateUser.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 18, 2003
 * 	 CVS-ID: $Id: AdministrateUser.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

package org.digijava.module.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.form.AdministrateUserForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

//Action for user administration

public class AdministrateUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception {

        AdministrateUserForm userForm = (AdministrateUserForm) form;
        User currentUser = RequestUtils.getUser(request);

        //take selected user
        User selectedUser = DgUtil.getUser(userForm.getSelectedUserId());
        //get new password for selected user
        String password = userForm.getNewPassword();
        String newPassword = userForm.getConfirmnewPassword();

        boolean changed = false;


        if (selectedUser.isActivate() != userForm.isAlertable()) {
            selectedUser.setActive(userForm.isAlertable());
            changed = true;
        }

        // Only global admins can change password, ban user or make global admin
        if (currentUser.isGlobalAdmin()) {

            // Change password
            /** @todo this verification is already done in action form, it needs to be removed  */
            if ( (password != null) && (newPassword != null)) {
                org.digijava.module.um.util.DbUtil.ResetPassword(selectedUser.
                    getEmail(), newPassword);
            }

            // Make user global admin
            if (selectedUser.isGlobalAdmin() != userForm.isGlobalAdmin()) {
                selectedUser.setGlobalAdmin(userForm.isGlobalAdmin());
                changed = true;
            }

            // Ban/unban user
            if (selectedUser.isBanned() != userForm.isBan() ) {
                selectedUser.setBanned(userForm.isBan());
                changed = true;
            }
        }

        if (changed) {
            DbUtil.updateUser(selectedUser);
        }
        return mapping.findForward("forward");
    }

}