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

package org.digijava.module.translation.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.form.TranslationAdminForm;

public class ShowEditPermissions
      extends Action {

    private static final Comparator groupInfo = new Comparator() {
    public int compare(Object o1, Object o2) {
        TranslationAdminForm.GroupInfo i1 = (TranslationAdminForm.GroupInfo)
          o1;
        TranslationAdminForm.GroupInfo i2 = (TranslationAdminForm.GroupInfo)
          o2;

        if (i1.getName() == null || i2.getName() == null) {
        return 0;
        }
        else {
        if (i1.getName() == null) {
            return 1;
        }
        else
        if (i2.getName() == null) {
            return -1;
        }
        }
        return i1.getName().compareTo(i2.getName());
    }
    };

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 javax.servlet.http.HttpServletRequest request,
                 javax.servlet.http.HttpServletResponse
                 response) throws java.lang.Exception {

    TranslationAdminForm formBean = (TranslationAdminForm) form;
    Site currentSite = RequestUtils.getSite(request);

    formBean.setGroups(new ArrayList());
    Iterator iter = currentSite.getGroups().iterator();
    while (iter.hasNext()) {
        Group group = (Group) iter.next();
        TranslationAdminForm.GroupInfo gi = new TranslationAdminForm.
          GroupInfo();
        gi.setId(group.getId());
        gi.setName(group.getName());

        formBean.getGroups().add(gi);
    }
    Collections.sort(formBean.getGroups(), groupInfo);

/*  if (request.getParameter("search") != null) {
        formBean.setUsers(new ArrayList());
        //get founded user list
        List dbUsers = org.digijava.module.um.util.DbUtil.searchUsers(
          formBean.getSearchUserInfo());
        iter = dbUsers.iterator();
        while (iter.hasNext()) {
        User user = (User) iter.next();
        TranslationAdminForm.UserInfo ui = new TranslationAdminForm.
              UserInfo();

        ui.setId(user.getId());
        ui.setFirstNames(user.getFirstNames());
        ui.setLastName(user.getLastName());
        ui.setEmail(user.getEmail());

        formBean.getUsers().add(ui);
        }
    }*/

    //site name translation
    RequestUtils.setTranslationAttribute(request, "siteName", currentSite.getName());
    formBean.setSiteName(currentSite.getName());

    return mapping.findForward("forward");
    }

}
