/*
 *   RemoveMemberUser.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: RemoveMemberUser.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
