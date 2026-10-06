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

import org.apache.struts.action.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.SearchUserForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.
        Exception {

        SearchUserForm searchForm = (SearchUserForm) form;

        if ( (searchForm.getSearchUserInfo() == null) ||
            (searchForm.getSearchUserInfo().trim().length() == 0)) {
            ActionMessages errors = new ActionMessages();
            errors.add(null, new ActionMessage("error.admin.userNameEmpty"));
            saveErrors(request, errors);
        }
        else {
            List userList = null;

            List tmpUserList = org.digijava.module.um.util.DbUtil.searchUsers(
                searchForm.
                getSearchUserInfo());

            //Filter search results for non globaladmin users
            User curUser = RequestUtils.getUser(request);
            if (!curUser.isGlobalAdmin()) {
                Site curSite = RequestUtils.getSite(request);
                ArrayList filteredUsers = new ArrayList();
                Iterator userIter = tmpUserList.iterator();
                while (userIter.hasNext()) {
                    User tmpUser = (User) userIter.next();
                    if (isSuitableUser(tmpUser, searchForm)) {
                        Site regSite = tmpUser.getRegisteredThrough();
                        if ( (regSite != null) &&
                            (regSite.getSiteId().equals(curSite.getSiteId()))) {
                            filteredUsers.add(tmpUser);
                        }
                    }
                }
                userList = filteredUsers;
            }
            else {
                ArrayList filteredUsers = new ArrayList();
                Iterator userIter = tmpUserList.iterator();
                while (userIter.hasNext()) {
                    User tmpUser = (User) userIter.next();
                    if (isSuitableUser(tmpUser, searchForm)) {
                        filteredUsers.add(tmpUser);
                    }
                }
                userList = filteredUsers;
            }

            searchForm.setUserList(userList);
        }

        if (searchForm.getTargetAction() == null) {
            return mapping.findForward("searchUser");
        }
        else {
            return mapping.findForward("searchUsers");
        }
    }

    private boolean isSuitableUser(User user, SearchUserForm searchForm) {
        if (searchForm.isHideBanned() && user.isBanned()) {
            return false;
        }

        return true;
    }
}
