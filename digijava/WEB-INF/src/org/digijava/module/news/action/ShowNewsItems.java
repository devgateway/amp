/*
 *   ShowNewsItems.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ShowNewsItems.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;

/**
 * Action displayes news according to the selected status defined by FormBean property - status, action redirects back to the action, which invoked it
 */

public class ShowNewsItems
      extends PaginationAction {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	NewsItemForm itemForm = (NewsItemForm) form;

	List dbNewsList = null;
	List newsList = new ArrayList();
	List statusList = new ArrayList();
	Collection itemStatus = null;

	if (!itemForm.isSubmitted()) {
	    itemForm.setSelected(true);
	}
	itemForm.setSubmitted(false);
//	itemForm.setCountAutoArchived(0);

	ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
	String siteId = moduleInstance.getSite().getSiteId();
	String instanceName = moduleInstance.getInstanceName();

	// set status item
	statusList.add(new NewsItemForm.NewsInfo("Published",
						 ItemStatus.PUBLISHED, false));
	statusList.add(new NewsItemForm.NewsInfo("Pending", ItemStatus.PENDING, false));
	statusList.add(new NewsItemForm.NewsInfo("Rejected",
						 ItemStatus.REJECTED, false));
	statusList.add(new NewsItemForm.NewsInfo("Archived",
						 ItemStatus.ARCHIVED, false));
	statusList.add(new NewsItemForm.NewsInfo("All", "all", false));

	itemForm.setStatusList(statusList);
	// ---------------

	//set return Url
	String currentUrl = DgUtil.getFullURL(request);
	String sitetUrl = DgUtil.getSiteUrl(DgUtil.getSiteDomain(request),
					    request);
	String returnUrl = currentUrl.substring(sitetUrl.length(),
						currentUrl.length());
	int index = returnUrl.indexOf("/default");
	if (index >= 0) {
	    returnUrl = returnUrl.substring(0, index) +
		  returnUrl.substring(index + ( (String) ("/default")).length(),
				      returnUrl.length());
	}
	itemForm.setReturnUrl(returnUrl);
	//-------------
	String param = itemForm.getStatus();
	//
	// Pagination
	NewsSettings setting = DbUtil.getNewsSettings(request);
	// set number of items per page
	int numberOfItemsPerPage;

	Long nOfItems = setting.getNumberOfItemsPerPage();
	if (nOfItems != null && nOfItems.longValue() != 0) {
	  numberOfItemsPerPage = nOfItems.intValue();
	}
	else { //set default value
	  numberOfItemsPerPage = 5;
	}

	int numberOfItems = 0;
	doStartPagination(itemForm, numberOfItemsPerPage);

/*	L.D. removed
     if (itemForm.getNav() != null &&
	    (Integer.parseInt(itemForm.getNav()) == -1)) {
	    this.offset = numberOfItemsPerPage;
	}*/

	//Initialize NewsList from Datebase Here
	if ( (param != null) && param.equalsIgnoreCase(ItemStatus.PENDING)) {

	    // Get Pending items from database
	    dbNewsList = DbUtil.getNewsItems(siteId, instanceName,
					     ItemStatus.PENDING,
					     getOffset(),
					     numberOfItemsPerPage + 1);
	    numberOfItems = DbUtil.getNumOfNewsItems(siteId,instanceName,ItemStatus.PENDING);
	    //
	    NewsItemForm.NewsInfo newsInfo = (NewsItemForm.NewsInfo) statusList.
		  get(1);
	    newsInfo.setSelected(true);

	    itemStatus = new ArrayList();
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.PUBLISHED,
		  "Publish"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REJECTED,
		  "Reject"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.ARCHIVED,
		  "Archive"));

	    itemForm.setItemStatus(itemStatus);
	}
	else
	if ( (param != null) && param.equalsIgnoreCase(ItemStatus.PUBLISHED)) {

	    // Get Published items from database
	    dbNewsList = DbUtil.getNewsItems(siteId, instanceName,
					     ItemStatus.PUBLISHED,
					     getOffset(),
					     numberOfItemsPerPage + 1);
	    numberOfItems = DbUtil.getNumOfNewsItems(siteId,instanceName,ItemStatus.PUBLISHED);

	    //
	    NewsItemForm.NewsInfo newsInfo = (NewsItemForm.NewsInfo) statusList.
		  get(0);
	    newsInfo.setSelected(true);

	    itemStatus = new ArrayList();
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.ARCHIVED,
		  "Archive"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REJECTED,
		  "Reject"));

	    itemForm.setItemStatus(itemStatus);
	}
	else
	if ( (param != null) && param.equalsIgnoreCase(ItemStatus.REJECTED)) {

	    // Get Rejected items from database
	    dbNewsList = DbUtil.getNewsItems(siteId, instanceName,
					     ItemStatus.REJECTED,
					     getOffset(),
					     numberOfItemsPerPage + 1);
	    numberOfItems = DbUtil.getNumOfNewsItems(siteId,instanceName,ItemStatus.REJECTED);

	    //
	    NewsItemForm.NewsInfo newsInfo = (NewsItemForm.NewsInfo) statusList.
		  get(2);
	    newsInfo.setSelected(true);

	    itemStatus = new ArrayList();
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REVOKE,
		  "Revoke"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.ARCHIVED,
		  "Archive"));

	    itemForm.setItemStatus(itemStatus);

	}
	else
	if ( (param != null) && param.equalsIgnoreCase(ItemStatus.ARCHIVED)) {

	    // Get Archived items from database
	    dbNewsList = DbUtil.getNewsItems(siteId, instanceName,
					     ItemStatus.ARCHIVED,
					     getOffset(),
					     numberOfItemsPerPage + 1);
	    numberOfItems = DbUtil.getNumOfNewsItems(siteId,instanceName,ItemStatus.ARCHIVED);

	    //
	    NewsItemForm.NewsInfo newsInfo = (NewsItemForm.NewsInfo) statusList.
		  get(3);
	    newsInfo.setSelected(true);

	    itemStatus = new ArrayList();
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REVOKE,
		  "Revoke"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REJECTED,
		  "Reject"));

	    itemForm.setItemStatus(itemStatus);
	}
	else {
	    ModuleInstance instance = DgUtil.getModuleInstance(request);

	    // Get all items from database
	    dbNewsList = DbUtil.getNewsItems(siteId, instanceName,
					     null,
					     getOffset(),
					     numberOfItemsPerPage + 1);
	    numberOfItems = DbUtil.getNumOfNewsItems(siteId,instanceName,null);

	    //
	    NewsItemForm.NewsInfo newsInfo = (NewsItemForm.NewsInfo) statusList.
		  get(4);
	    newsInfo.setSelected(true);

	    itemStatus = new ArrayList();
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.PUBLISHED,
		  "Publish"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.REJECTED,
		  "Reject"));
	    itemStatus.add(new NewsItemForm.NewsStatusInfo(ItemStatus.ARCHIVED,
		  "Archive"));

	    itemForm.setItemStatus(itemStatus);
	}
	//
	endPagination( ( (dbNewsList != null) ? dbNewsList.size() : 0));

	/*if (getOffset() == 0) {
	    itemForm.setNext(null);
	}
	if ( getOffset() >= numberOfItems || (getOffset() + numberOfItemsPerPage)>= numberOfItems) {
	    itemForm.setPrev(null);
	}*/
	//
	if (dbNewsList != null && dbNewsList.size() > 0) {

	    int n;
	    if ( (numberOfItemsPerPage + 1) == dbNewsList.size())
		n = dbNewsList.size() - 1;
	    else {
		n = dbNewsList.size();
	    }
	    ///

	    for (int i = 0; i < n; i++) {
		News news = (News) dbNewsList.get(i);
		NewsItem newsItem = news.getFirstNewsItem();
		NewsItemForm.NewsInfo ni = new NewsItemForm.NewsInfo();

		if (newsItem != null) {
		    if (newsItem.getTitle() != null) {
			if (newsItem.getTitle().length() > 8)
			    ni.setTitle(newsItem.getTitle().substring(0, 8) +
					"...");
			else
			    ni.setTitle(newsItem.getTitle());
		    }

		    UserInfo author = DgUtil.getUserInfo(newsItem.getUserId());

		    ni.setId(news.getId());
		    ni.setAuthorFirstNames(author.getFirstNames());
		    ni.setAuthorLastName(author.getLastName());
		    ni.setReleaseDate(DgUtil.formatDate(news.getReleaseDate()));

		    ItemStatus newsStatus = news.getStatus();
		    if (newsStatus != null) {
			ni.setStatus(newsStatus.getName());
		    }
/*		    Date archiveDate = news.getArchiveDate();
		    if (archiveDate!= null && archiveDate.compareTo(new Date()) <= 0) {
			ni.setAutoArchived(true);
		    }*/
		    newsList.add(ni);
		}
	    }
	}
	if (newsList.size() != 0) {
	    itemForm.setNewsList(newsList);
	}
	else {
	    itemForm.setNewsList(null);
	}

	return mapping.findForward("forward");
    }

}
