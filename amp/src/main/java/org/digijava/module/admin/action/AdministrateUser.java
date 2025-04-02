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

package org.digijava.module.admin.action;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.admin.form.AdministrateUserForm;
import org.digijava.module.admin.util.DbUtil;

//Action for user administration

public class AdministrateUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception {

        AdministrateUserForm userForm = (AdministrateUserForm) form;
        Site currentSite = RequestUtils.getSite(request);

        //take selected user
        User selectedUser = UserUtils.getUser(userForm.getSelectedUserId());
        //get new password for selected user
        String password = userForm.getNewPassword();
        String newPassword = userForm.getConfirmnewPassword();

        boolean changed = false;

        Subject subject = RequestUtils.getSubject(request);
        boolean isGlobalAdmin = false;
        if (DigiSecurityManager.isGlobalAdminSubject(subject)) {
            isGlobalAdmin = true;
        }

        if (isGlobalAdmin ||
            (selectedUser.getRegisteredThrough() != null &&
             selectedUser.getRegisteredThrough().getId().equals(currentSite.getId()) &&
             !selectedUser.isGlobalAdmin())) {
            // Change password
            /** @todo this verification is already done in action form, it needs to be removed  */
            if ( (password != null) && (newPassword != null) && (password.trim().length() != 0)) {
                UserUtils.setPassword(selectedUser, newPassword);
                changed = true;
            }
        }

        // Only global admins can make global admin
        if (isGlobalAdmin) {
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
            
            // Set/clear email verified
            if (selectedUser.isEmailVerified() != userForm.isEmailVerified()){
                selectedUser.setEmailVerified(userForm.isEmailVerified());
                changed = true;
            }
        }

        if (changed) {
            DbUtil.updateUser(selectedUser);
        }
        return mapping.findForward("forward");
    }

}
