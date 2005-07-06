/*
 *   AddUsersToGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 16, 2003
 * 	 CVS-ID: $Id: AddUsersToGroup.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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