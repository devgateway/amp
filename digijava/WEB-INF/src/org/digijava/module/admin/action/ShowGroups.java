/*
 *   ShowGroups.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: ShowGroups.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

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