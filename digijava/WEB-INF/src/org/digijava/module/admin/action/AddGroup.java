/*
 *   AddGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: AddGroup.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

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
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.admin.groupNameMustBeUnique"));
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