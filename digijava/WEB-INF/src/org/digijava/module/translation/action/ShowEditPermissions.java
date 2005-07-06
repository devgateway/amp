/*
 *   ShowTranslationAdministration.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: ShowEditPermissions.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.form.TranslationAdminForm;
import java.util.List;
import org.digijava.kernel.user.User;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;

public class ShowEditPermissions
      extends Action {

    private static final Comparator groupInfo = new Comparator() {
	public int compare(Object o1, Object o2) {
	    TranslationAdminForm.GroupInfo i1 = (TranslationAdminForm.GroupInfo)
		  o1;
	    TranslationAdminForm.GroupInfo i2 = (TranslationAdminForm.GroupInfo)
		  o2;

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

	TranslationAdminForm formBean = (TranslationAdminForm) form;
	Site currentSite = RequestUtils.getSite(request);

	formBean.setGroups(new ArrayList());
	Iterator iter = currentSite.getGroups().iterator();
	while (iter.hasNext()) {
	    Group group = (Group) iter.next();
	    TranslationAdminForm.GroupInfo gi = new TranslationAdminForm.
		  GroupInfo();
	    gi.setId(group.getId());
	    gi.setName(group.getName());

	    formBean.getGroups().add(gi);
	}
	Collections.sort(formBean.getGroups(), groupInfo);

/*	if (request.getParameter("search") != null) {
	    formBean.setUsers(new ArrayList());
	    //get founded user list
	    List dbUsers = org.digijava.module.um.util.DbUtil.searchUsers(
		  formBean.getSearchUserInfo());
	    iter = dbUsers.iterator();
	    while (iter.hasNext()) {
		User user = (User) iter.next();
		TranslationAdminForm.UserInfo ui = new TranslationAdminForm.
		      UserInfo();

		ui.setId(user.getId());
		ui.setFirstNames(user.getFirstNames());
		ui.setLastName(user.getLastName());
		ui.setEmail(user.getEmail());

		formBean.getUsers().add(ui);
	    }
	}*/

	//site name translation
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
	String siteName = moduleInstance.getSite().getName();
	RequestUtils.setTranslationAttribute(request, "siteName", siteName);
	formBean.setSiteName(siteName);

	return mapping.findForward("forward");
    }

}