/*
 *   ConfirmNewsItems.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ConfirmNewsItems.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.news.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

/**
 * Action displayes confirmation page for selected news items, status of which is being changed
 */

public class ConfirmNewsItems
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	NewsItemForm itemForm = (NewsItemForm) form;
	List newsToReject = new ArrayList();

	//translation
	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

	String moduleName = moduleInstance.getModuleName();
	String siteName = moduleInstance.getSite().getName();
	String instanceName = moduleInstance.getInstanceName();

	RequestUtils.setTranslationAttribute(request, "moduleName", moduleName);
	RequestUtils.setTranslationAttribute(request, "siteName", siteName);

	itemForm.setModuleName(moduleName);
	itemForm.setSiteName(siteName);
	itemForm.setInstanceName(instanceName);
	//end translation

	// get default settings
	NewsSettings setting = DbUtil.getNewsSettings(request);

	if (itemForm.getSelectedStatus() != null) {
	    if (itemForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
		  PUBLISHED)) {
		itemForm.setSendMessage(setting.isSendApproveMessage());
		itemForm.setDefaultMessage(setting.getApproveMessage());
		itemForm.setStatusTitle("Publish");
	    }
	    else
	    if (itemForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
		  REJECTED)) {
		itemForm.setSendMessage(setting.isSendRejectMessage());
		itemForm.setDefaultMessage(setting.getRejectMessage());
		itemForm.setStatusTitle("Reject");
	    }
	    else
	    if (itemForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
		  ARCHIVED)) {
		itemForm.setSendMessage(setting.isSendArchiveMessage());
		itemForm.setDefaultMessage(setting.getArchiveMessage());
		itemForm.setStatusTitle("Archive");
	    }
	    else
	    if (itemForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.REVOKE)) {
		itemForm.setSendMessage(setting.isSendRevokeMessage());
		itemForm.setDefaultMessage(setting.getRevokeMessage());
		itemForm.setStatusTitle("Re-publish");
	    }
	}
	else {
	    itemForm.setSelectedStatus(ItemStatus.REJECTED);
	    itemForm.setSendMessage(setting.isSendRejectMessage());
	    itemForm.setDefaultMessage(setting.getRejectMessage());
	    itemForm.setStatusTitle("Reject");
	}

	if (itemForm.getActiveNewsItem() != null) {

	    itemForm.setSendMessage(setting.isSendRejectMessage());
	    itemForm.setDefaultMessage(setting.getRejectMessage());
	    itemForm.setStatusTitle("Reject");

	    News news = DbUtil.getNewsItem(itemForm.getActiveNewsItem());
	    NewsItem newsItem = news.getFirstNewsItem();

	    NewsItemForm.NewsInfo ni = new NewsItemForm.NewsInfo();

	    ni.setTitle(newsItem.getTitle());
	    ni.setReleaseDate(DgUtil.formatDate(news.getReleaseDate()));

	    UserInfo author = DgUtil.getUserInfo(newsItem.getUserId());
	    ni.setAuthorUserId(newsItem.getUserId());
	    ni.setAuthorFirstNames(author.getFirstNames());
	    ni.setAuthorLastName(author.getLastName());
	    ni.setSelected(true);

	    ni.setId(news.getId());

	    newsToReject.add(ni);

	    itemForm.setNewsList(newsToReject);
	}

	itemForm.setSelected(false);
	if (itemForm.getNewsList() != null) {
	    Iterator iter = itemForm.getNewsList().iterator();
	    while (iter.hasNext()) {
		NewsItemForm.NewsInfo item = (NewsItemForm.NewsInfo) iter.next();
		if (item.isSelected()) {
		    itemForm.setSelected(true);
		    break;
		}
	    }
	}
	//
	if (itemForm.isSelected()) {
	    itemForm.setSubmitted(false);
/*
	    int countAutoArchived = 0;
	    Iterator iter = itemForm.getNewsList().iterator();
	    while (iter.hasNext()) {
		NewsItemForm.NewsInfo item = (NewsItemForm.NewsInfo) iter.
		      next();
		if (item.isSelected() && item.isAutoArchived()) {
		    ++countAutoArchived;
		}
	    }

	    if (countAutoArchived > 0) {
		itemForm.setCountAutoArchived(countAutoArchived);
		ActionErrors errors = new ActionErrors();
		errors.add(null,
			   new ActionError("error.news.autoArchivedItemsFound",new Integer(countAutoArchived)));
		saveErrors(request, errors);

		return mapping.findForward("error");
	    }

	    itemForm.setCountAutoArchived(0);*/
	    return mapping.findForward("forward");
	}
	else {
	    itemForm.setSubmitted(true);
	    return new ActionForward(itemForm.getReturnUrl(), true);
	}

    }

}