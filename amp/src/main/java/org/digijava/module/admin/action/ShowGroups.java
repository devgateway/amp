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
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.*;

public class ShowGroups
    extends Action {

    private static final Comparator groupInfo = new Comparator() {
        public int compare(Object o1, Object o2) {
            GroupsForm.GroupInfo i1 = (GroupsForm.GroupInfo) o1;
            GroupsForm.GroupInfo i2 = (GroupsForm.GroupInfo) o2;

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
        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());

        GroupsForm groupForm = (GroupsForm) form;
        groupForm.setGroups(new ArrayList());
        Iterator iter = realSite.getGroups().iterator();
        while (iter.hasNext()) {
            Group group = (Group) iter.next();
            GroupsForm.GroupInfo info = new GroupsForm.GroupInfo();
            info.setId(group.getId());
            info.setName(group.getName());
            info.setDefaultGroup(group.isDefaultGroup());
            List users = DbUtil.getGroupUsers(group.getId());
            info.setEmpty(users == null || users.isEmpty());
            groupForm.getGroups().add(info);
        }

        Collections.sort(groupForm.getGroups(), groupInfo);

        groupForm.setOtherGroups(new ArrayList(DbUtil.getReferencedGroups(currentSite.getId())));

        return mapping.findForward("forward");
    }

}
