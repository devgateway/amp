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
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.Iterator;

public class AddGroup
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());

        GroupsForm groupForm = (GroupsForm) form;
        Iterator iter = realSite.getGroups().iterator();
        boolean equals = false;
        while (iter.hasNext()) {
            Group oneGroup = (Group) iter.next();
            if (oneGroup.getName().toLowerCase().equals(groupForm.getGroupName().toLowerCase())) {
                equals = true;
                break;
            }
        }

        if (equals) {
            ActionMessages errors = new ActionMessages();
            errors.add(null,
                       new ActionMessage("error.admin.groupNameMustBeUnique"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        else {
            Group group = new Group(realSite, groupForm.getGroupName(), null);
            realSite.getGroups().add(group);

            DbUtil.editSite(realSite);
            groupForm.setGroupName(null);
            return mapping.findForward("forward");
        }
    }

}
