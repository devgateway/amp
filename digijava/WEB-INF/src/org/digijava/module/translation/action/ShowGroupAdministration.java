/*
 *   ShowGroupAdministration.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: ShowGroupAdministration.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;

public class ShowGroupAdministration
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationPermissionsForm formBean = (TranslationPermissionsForm) form;
	formBean.setUserMode(false);

	Site currentSite = RequestUtils.getSite(request);

	Group group = null;
	if (formBean.getGroupId() != null) {
	    group = org.digijava.module.admin.util.DbUtil.getGroup(
		  formBean.
		  getGroupId());
	}
	if (group != null) {
	    formBean.setSites(new ArrayList());
	    if (RequestUtils.getUser(request).isGlobalAdmin()) {
		formBean.getSites().add(new TranslationPermissionsForm.
					SiteInfo(new
						 Long(0),
						 TranslateObject.SITE_NAME_GLOBAL));
	    }
	    if (group.getSite().getId().equals(currentSite.getId())) {
		TranslationPermissionsForm.SiteInfo si = new
		      TranslationPermissionsForm.
		      SiteInfo();

		si.setId(currentSite.getId());
		si.setName(currentSite.getName());
		formBean.getSites().add(si);
	    }
	    //get translation languages for Site
	    Set languages = SiteUtils.getTransLanguages(currentSite);
	    HashMap translations = new HashMap();
	    Iterator iterator = TrnUtil.getLanguages(RequestUtils.
		  getNavigationLanguage(request).
		  getCode()).iterator();
	    while (iterator.hasNext()) {
		TrnLocale item = (TrnLocale) iterator.next();
		translations.put(item.getCode(), item);
	    }
	    //sort languages
	    List sortedLanguages = new ArrayList();
	    iterator = languages.iterator();
	    while (iterator.hasNext()) {
		Locale item = (Locale) iterator.next();
		sortedLanguages.add(translations.get(item.getCode()));
	    }
	    Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);
	    sortedLanguages.add(0, new Locale(TranslateObject.LOCALE_CODE_ALL, "all"));
	    formBean.setLanguages(sortedLanguages);
	    //

	    formBean.setPermissions(new ArrayList());
	    GroupPrincipal groupPrincipal = new GroupPrincipal(group.getId().
		  longValue());
	    PermissionCollection permissions = DigiSecurityManager.
		  getPermissions(
		  groupPrincipal);
	    if (permissions != null) {
		Enumeration permissionEnum = permissions.elements();
		while (permissionEnum.hasMoreElements()) {
		    Permission permission = (Permission) permissionEnum.
			  nextElement();
		    if (permission instanceof TranslatePermission) {
			TranslatePermission tp = (TranslatePermission)
			      permission;
			TranslateObject to = tp.getTranslateId();

			if (to != null) {
			    TranslationPermissionsForm.PermissionInfo
				  pi = new
				  TranslationPermissionsForm.
				  PermissionInfo();

			    pi.setId(0);
			    if (to.getSiteId() != null) {
				pi.setSiteId(to.getSiteId());
			    }
			    else {
				pi.setSiteId(new Long(0));
			    }
			    if (to.getLocaleId() != null) {
				pi.setLocaleId(to.getLocaleId());
			    }
			    else {
				pi.setLocaleId(TranslateObject.LOCALE_CODE_ALL);
			    }

			    formBean.getPermissions().add(pi);
			}
		    }
		}
	    }

	    formBean.setSiteName(group.getSite().getName());
	    formBean.setGroupName(group.getName());
	}

	return mapping.findForward("forward");

    }

}