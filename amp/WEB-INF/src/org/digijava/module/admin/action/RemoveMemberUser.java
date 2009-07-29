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
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.form.GroupMembersForm;
import org.digijava.module.admin.util.DbUtil;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.RequestUtils;

public class RemoveMemberUser
    extends Action {

    private static Logger logger = Logger.getLogger(RemoveMemberUser.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        Site currentSite = RequestUtils.getSite(request);
        GroupMembersForm membersForm = (GroupMembersForm) form;

        Group group = null;
        try {
            group = DbUtil.getGroup(membersForm.getGroupId());
            if (group.getSite().getId().equals(currentSite.getId())) {
                DbUtil.removeUserFromGroup(group.getId(), membersForm.getUserId());
            }
        }
        catch (AdminException ex) {
            logger.warn("Unabel to remove User from Group ",ex);
        }
        return mapping.findForward("forward");
    }

}
