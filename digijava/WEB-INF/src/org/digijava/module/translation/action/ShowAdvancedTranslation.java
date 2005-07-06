/*
 *   ShowAdvancedTranslation.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sept 28, 2004
 * 	 CVS-ID: $Id: ShowAdvancedTranslation.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.form.TranslationInfo;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;

public class ShowAdvancedTranslation
      extends PaginationAction {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	AdvancedTranslationForm formBean = (AdvancedTranslationForm) form;
	Site currentSite = RequestUtils.getSite(request);
	List messagesList = new ArrayList();
	ArrayList keyPrefixesList = new ArrayList();

	//set site identity
	if (request.getParameter("global") != null) {
	    formBean.setSelectedPrefix(null);
	    formBean.setSearchKey(null);
	    formBean.setSearchSource(null);
	    formBean.setSearchTarget(null);

	    if (request.getParameter("global").equals("true")) {
		formBean.setGlobalTranslation(true);
		formBean.setSiteId(new Long(0));
	    }
	    else {
		formBean.setGlobalTranslation(false);
		formBean.setSiteId(currentSite.getId());
	    }
	}
	if (formBean.getSiteId() == null) {
	    formBean.setSiteId(currentSite.getId());
	}
	//
	String param = request.getParameter("prefix");
	if (param != null) {
	    String encoded = DgUtil.encodeString(param);
	    if (encoded.indexOf("%") >= 0) {
		encoded = DgUtil.decodeString(encoded);
	    }
	    formBean.setSelectedPrefix(encoded);
	}
	param = request.getParameter("slang");
	if (param != null) {
	    formBean.setSelectedLangSource(param);
	}
	param = request.getParameter("tlang");
	if (param != null) {
	    formBean.setSelectedLangTarget(param);
	}
	//
	if (formBean.getSelectedPrefix() != null &&
	    formBean.getSelectedPrefix().equals("all")) {
	    formBean.setSelectedPrefix(null);
	}
	if (formBean.getSelectedLangSource() == null) {
	    formBean.setSelectedLangSource(SiteUtils.getDefaultLanguages(currentSite).getCode());
	}
	if (formBean.getSelectedLangTarget() == null) {
        formBean.setSelectedLangSource(SiteUtils.getDefaultLanguages(currentSite).getCode());
	}
	//site name translation
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
	String siteName = moduleInstance.getSite().getName();
	RequestUtils.setTranslationAttribute(request, "siteName", siteName);
	formBean.setSiteName(siteName);

	//get key prefixes
	List dbKeyPrefixesList = DbUtil.getKeyPrefixesList(formBean.
	      getSiteId(), formBean.getSelectedLangSource());
	if (dbKeyPrefixesList != null) {
	    TreeSet keyPrefixes = new TreeSet(dbKeyPrefixesList);
	    if (keyPrefixes != null) {
		Iterator iter = keyPrefixes.iterator();
		while (iter.hasNext()) {
		    AdvancedTranslationForm.PrefixInfo pi = new
			  AdvancedTranslationForm.PrefixInfo();
		    String item = (String) iter.next();
		    pi.setValue(item);

		    keyPrefixesList.add(pi);
		}
	    }
	}
	keyPrefixesList.add(0, new AdvancedTranslationForm.PrefixInfo("all"));
	formBean.setKeyPrefixes(keyPrefixesList);
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
	formBean.setLanguages(sortedLanguages);

	//pagination
	doStartPagination(formBean, formBean.NUM_OF_MESSAGES_PER_PAGE);
	//get messages List
	List sourceMessages = null;
	List targetMessages = null;

	boolean permitted = true;

	if (!DgUtil.isLocalTranslatorForSite(request)) {
	    permitted = TranslateSecurityManager.getInstance().
		  checkPermission(
		  DgSecurityManager.getSubject(request), TranslateObject.class,
		  new TranslateObject(formBean.getSiteId(),
				      formBean.getSelectedLangTarget()),
		  TranslatePermission.INT_TRANSLATE);
	}

	String searchParam = request.getParameter("search");
	if (searchParam != null) {
	    if (searchParam.equals("key")) {
		sourceMessages = DbUtil.searchMessagesByKey(formBean.
		      getSearchKey(),
		      formBean.
		      getSelectedLangSource(),
		      formBean.getSiteId(), getOffset(),
		      formBean.NUM_OF_MESSAGES_PER_PAGE + 1);

		if (permitted &&
		    !formBean.getSelectedLangSource().equals(formBean.
		      getSelectedLangTarget())) {
		    targetMessages = DbUtil.searchMessagesByKey(formBean.
			  getSearchKey(),
			  formBean.getSelectedLangTarget(),
			  formBean.getSiteId(), getOffset(),
			  formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
		}
	    }
	    else if (searchParam.equals("source")) {
		sourceMessages = DbUtil.searchMessagesBySourceOrTarget(formBean.
		      getSearchSource(),
		      formBean.
		      getSelectedLangSource(),
		      formBean.getSiteId(), getOffset(),
		      formBean.NUM_OF_MESSAGES_PER_PAGE + 1);

		if (permitted &&
		    !formBean.getSelectedLangSource().equals(formBean.
		      getSelectedLangTarget())) {
		    targetMessages = DbUtil.searchMessagesBySourceOrTarget(formBean.
			  getSearchSource(),
			  formBean.getSelectedLangTarget(),
			  formBean.getSiteId(), getOffset(),
			  formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
		}
	    }
	    else if (searchParam.equals("target")) {
		sourceMessages = DbUtil.searchMessagesBySourceOrTarget(formBean.
		      getSearchTarget(),
		      formBean.
		      getSelectedLangSource(),
		      formBean.getSiteId(), getOffset(),
		      formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
		if (permitted &&
		    !formBean.getSelectedLangSource().equals(formBean.
		      getSelectedLangTarget())) {
		    targetMessages = DbUtil.searchMessagesBySourceOrTarget(formBean.
			  getSearchTarget(),
			  formBean.getSelectedLangTarget(),
			  formBean.getSiteId(), getOffset(),
			  formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
		}
	    }
	}
	else {
	    sourceMessages = DbUtil.getMessagesList(formBean.
		  getSelectedPrefix(),
		  formBean.
		  getSelectedLangSource(),
		  formBean.getSiteId(), getOffset(),
		  formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
	    if (permitted && !formBean.getSelectedLangSource().equals(formBean.
		  getSelectedLangTarget())) {
		targetMessages = DbUtil.getMessagesList(formBean.
		      getSelectedPrefix(),
		      formBean.getSelectedLangTarget(),
		      formBean.getSiteId(), getOffset(),
		      formBean.NUM_OF_MESSAGES_PER_PAGE + 1);
	    }

	}
	endPagination( (sourceMessages != null) ? sourceMessages.size() : 0);
	//
	if (sourceMessages != null && sourceMessages.size() != 0) {
	    int n;
	    if ( (formBean.NUM_OF_MESSAGES_PER_PAGE + 1) == sourceMessages.size())
		n = sourceMessages.size() - 1;
	    else {
		n = sourceMessages.size();
	    }
	    sourceMessages = sourceMessages.subList(0, n);
	}
	if (targetMessages != null && targetMessages.size() != 0) {
	    int n;
	    if ( (formBean.NUM_OF_MESSAGES_PER_PAGE + 1) == targetMessages.size())
		n = targetMessages.size() - 1;
	    else {
		n = targetMessages.size();
	    }
	    targetMessages = targetMessages.subList(0, n);
	}
	//
	if (sourceMessages != null && sourceMessages.size() != 0) {
	    Iterator iterSource = sourceMessages.iterator();
	    if (targetMessages != null && targetMessages.size() != 0) {
		HashMap targetMessagesMap = new HashMap();
		Iterator iter = targetMessages.iterator();
		while (iter.hasNext()) {
		    TranslationInfo ti = (TranslationInfo) iter.next();
		    targetMessagesMap.put(ti.getKey(), ti);
		}
		//
		while (iterSource.hasNext()) {
		    AdvancedTranslationForm.MessageInfo mi = new
			  AdvancedTranslationForm.MessageInfo();

		    TranslationInfo itemSource = (TranslationInfo)
			  iterSource.
			  next();

		    mi.setKey(itemSource.getKey());

		    String encodedKey = DgUtil.encodeString(mi.
			  getKey());
		    mi.setEncodedKey(encodedKey);
		    mi.setSourceValue(itemSource.getMessage());

		    TranslationInfo itemTarget = (TranslationInfo)
			  targetMessagesMap.get(itemSource.getKey());
		    if (itemTarget != null) {
			mi.setTranslation(true);
			mi.setTargetValue(itemTarget.getMessage());
		    }
		    else {
			mi.setTranslation(false);
			mi.setTargetValue("");
		    }

		    messagesList.add(mi);
		}
		while (iterSource.hasNext()) {
		    TranslationInfo itemSource = (TranslationInfo)
			  iterSource.
			  next();
		    AdvancedTranslationForm.MessageInfo mi = new
			  AdvancedTranslationForm.MessageInfo();
		    mi.setKey(itemSource.getKey());

		    String encodedKey = DgUtil.encodeString(mi.
			  getKey());
		    mi.setEncodedKey(encodedKey);

		    mi.setSourceValue(itemSource.getMessage());
		    mi.setTranslation(false);
		    mi.setTargetValue("");

		    messagesList.add(mi);
		}
	    }
	    else {
		while (iterSource.hasNext()) {
		    TranslationInfo itemSource = (TranslationInfo)
			  iterSource.
			  next();
		    AdvancedTranslationForm.MessageInfo mi = new
			  AdvancedTranslationForm.MessageInfo();
		    mi.setKey(itemSource.getKey());

		    String encodedKey = DgUtil.encodeString(mi.
			  getKey());
		    mi.setEncodedKey(encodedKey);

		    mi.setSourceValue(itemSource.getMessage());
		    if (!permitted ||
			!formBean.getSelectedLangSource().equals(formBean.
			  getSelectedLangTarget())) {
			mi.setTranslation(false);
			mi.setTargetValue("");
		    }
		    else {
			mi.setTranslation(true);
			mi.setTargetValue(itemSource.getMessage());
		    }
		    messagesList.add(mi);
		}
	    }
	}

	if (messagesList.size() != 0) {
	    formBean.setMessages(messagesList);
	}
	else {
	    formBean.setMessages(null);
	}

	return mapping.findForward("forward");
    }

}
