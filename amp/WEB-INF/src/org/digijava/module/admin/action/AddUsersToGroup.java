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

import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.admin.form.GroupMembersForm;
import org.digijava.module.admin.exception.AdminException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.RequestUtils;

public class AddUsersToGroup
    extends Action {

    private static Logger logger = Logger.getLogger(AddUsersToGroup.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        GroupMembersForm membersForm = (GroupMembersForm) form;
        
        if (!RequestUtils.isAdmin(response, request.getSession(), request)) {
            return null;
        }

        Group group = null;
        try {
            group = DbUtil.getGroup(membersForm.getGroupId());
        }
        catch (AdminException ex) {
            logger.error("Unable to get Group from database ",ex);
            return mapping.findForward("forward");
        }
        if (!group.getSite().getId().equals(currentSite.getId())) {
            return mapping.findForward("forward");
        }


        StringTokenizer tokenizer = new StringTokenizer(membersForm.getUsersToAdd(), ",");
        Long[] userIds = new Long[tokenizer.countTokens()];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = new Long( tokenizer.nextToken());
        }
        DbUtil.addUsersToGroup(group.getId(), userIds);

        membersForm.setUserId(null);
        membersForm.setUsersToAdd(null);
        membersForm.setGroupName(group.getName());

        return mapping.findForward("forward");
    }
    }
