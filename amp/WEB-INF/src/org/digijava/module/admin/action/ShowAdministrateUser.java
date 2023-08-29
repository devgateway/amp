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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.form.AdministrateUserForm;
import org.digijava.module.um.exception.UMException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Action forwarding user Administration page

public class ShowAdministrateUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException, UMException {

        AdministrateUserForm userForm = (AdministrateUserForm) form;

        //get selected user by selectedUserId
        User selectedUser = org.digijava.module.um.util.DbUtil.getSelectedUser(
            userForm.getSelectedUserId(), request);

        //set first name of user
        userForm.setFirstNames(selectedUser.getFirstNames());
        //set last name of user
        userForm.setLastName(selectedUser.getLastName());

        userForm.setBan(selectedUser.isBanned());

        userForm.setGlobalAdmin(selectedUser.isGlobalAdmin());
        
        userForm.setEmailVerified(selectedUser.isEmailVerified());

        return mapping.findForward("forward");
    }
}
