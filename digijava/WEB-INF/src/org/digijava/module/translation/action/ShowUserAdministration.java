/*
 *   ShowUserAdministration.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: ShowUserAdministration.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import java.util.Set;
import org.digijava.kernel.util.SiteUtils;
import java.util.HashMap;
import java.util.Iterator;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.translator.util.TrnLocale;
import java.util.List;
import java.util.ArrayList;
import org.digijava.kernel.entity.Locale;
import java.util.Collections;
import org.digijava.kernel.security.principal.UserPrincipal;
import java.security.PermissionCollection;
import org.digijava.kernel.security.DigiSecurityManager;
import java.util.Enumeration;
import java.security.Permission;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.module.translation.security.TranslateObject;

public class ShowUserAdministration
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationPermissionsForm formBean = (
	      TranslationPermissionsForm) form;

	formBean.setUserMode(true);
	Site currentSite = RequestUtils.getSite(request);

	User user = null;
	if (formBean.getUserId() != null) {
	    user = UserUtils.getUser(formBean.getUserId());
	}
	if (user != null) {
	    formBean.setSites(new ArrayList());
	    if (RequestUtils.getUser(request).isGlobalAdmin()) {
		formBean.getSites().add(new TranslationPermissionsForm.
					SiteInfo(new
						 Long(0),
						 TranslateObject.
						 SITE_NAME_GLOBAL));
	    }
	    TranslationPermissionsForm.SiteInfo si = new
		  TranslationPermissionsForm.
		  SiteInfo();

	    si.setId(currentSite.getId());
	    si.setName(currentSite.getName());
	    formBean.getSites().add(si);

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
	    sortedLanguages.add(0,
				new Locale(TranslateObject.LOCALE_CODE_ALL, "all"));
	    formBean.setLanguages(sortedLanguages);
	    //

	    formBean.setPermissions(new ArrayList());
	    UserPrincipal userPrincipal = new UserPrincipal(user.getId().
		  longValue());
	    PermissionCollection permissions = DigiSecurityManager.
		  getPermissions(
		  userPrincipal);
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
	    formBean.setSiteName(currentSite.getName());
	    formBean.setFirstNames(user.getFirstNames());
	    formBean.setLastName(user.getLastName());
	}

	return mapping.findForward("forward");
    }
}