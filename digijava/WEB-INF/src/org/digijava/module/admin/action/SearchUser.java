/*
 *   SearchUser.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 18, 2003
 * 	 CVS-ID: $Id: SearchUser.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.form.SearchUserForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class SearchUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.
        Exception {

        SearchUserForm searchForm = (SearchUserForm) form;


        if ( (searchForm.getSearchUserInfo() == null) || (searchForm.getSearchUserInfo().trim().length() == 0)) {
            ActionErrors errors = new ActionErrors();
            errors.add(null, new ActionError("error.admin.userNameEmpty"));
            saveErrors(request, errors);
        } else {
            List userList = null;

            //get founded user list
            userList = org.digijava.module.um.util.DbUtil.searchUsers(
                searchForm.
                getSearchUserInfo());

            //set founded user list
            searchForm.setUserList(userList);
        }

        if (searchForm.getTargetAction() == null) {
            return mapping.findForward("searchUser");
        } else {
            return mapping.findForward("searchUsers");
        }
    }

}