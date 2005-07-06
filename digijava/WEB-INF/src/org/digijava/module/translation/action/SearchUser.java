/*
 *   SearchUser.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: SearchUser.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.digijava.kernel.user.User;
import org.digijava.module.translation.form.TranslationAdminForm;

public class SearchUser
      extends Action {
    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationAdminForm formBean = (TranslationAdminForm) form;

	formBean.setUsers(new ArrayList());
	//get founded user list
	List dbUsers = org.digijava.module.um.util.DbUtil.searchUsers(
	      formBean.getSearchUserInfo());
	Iterator iter = dbUsers.iterator();
	while (iter.hasNext()) {
	    User user = (User) iter.next();
	    TranslationAdminForm.UserInfo ui = new
		  TranslationAdminForm.
		  UserInfo();

	    ui.setId(user.getId());
	    ui.setFirstNames(user.getFirstNames());
	    ui.setLastName(user.getLastName());
	    ui.setEmail(user.getEmail());

	    formBean.getUsers().add(ui);
	}

	return mapping.findForward("forward");

    }

}