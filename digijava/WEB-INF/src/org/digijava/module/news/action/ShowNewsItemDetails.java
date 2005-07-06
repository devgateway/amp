/*
 *   ShowNewsItemDetails.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ShowNewsItemDetails.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.util.ModuleUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.ModuleInstance;

/**
 * Action displayes details of news item identified by activeNewsItem identity(if not null)
 */
public class ShowNewsItemDetails
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        NewsItemForm itemForm = (NewsItemForm) form;
        User user = RequestUtils.getUser(request);
	Site currentSite = RequestUtils.getSite(request);
	ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);

        News news = null;

        if (itemForm.getActiveNewsItem() != null) {

            //ser return Url
            String currentUrl = DgUtil.getFullURL(request);
            String sitetUrl = DgUtil.getSiteUrl(RequestUtils.getSiteDomain(request),request);
            String returnUrl = currentUrl.substring(sitetUrl.length(), currentUrl.length());
            int index = returnUrl.indexOf("/default");
            if (index >= 0) {
                returnUrl = returnUrl.substring(0,index)+returnUrl.substring(index+((String)("/default")).length(),returnUrl.length());
            }
            itemForm.setReturnUrl(returnUrl);
            //

            news = DbUtil.getNewsItem(itemForm.getActiveNewsItem());
            NewsItem newsItem = news.getFirstNewsItem();

            itemForm.setTitle(newsItem.getTitle());

	    //get delimiter character
	    String delimiter = DbUtil.getShortVersionDelimiter(currentSite.
		  getSiteId(), moduleInstance.getInstanceName());

	    newsItem.setDescription(ModuleUtil.removeDelimiters(newsItem.
		  getDescription(), delimiter));
	    itemForm.setDescription(BBCodeParser.parse(newsItem.getDescription(),
		  news.isEnableSmiles(), news.isEnableHTML(), request));

            itemForm.setSourceName(news.getSourceName());
            itemForm.setSourceUrl(news.getSourceUrl());

            if (news.getCountry() != null) {
              itemForm.setCountry(news.getCountry());
              itemForm.setCountryName(news.getCountry());
              itemForm.setCountryKey((String)("cn:"+news.getCountry()));
            } else {
              itemForm.setCountry(News.noneCountryIso);
              itemForm.setCountryName(News.noneCountryName);
              itemForm.setCountryKey((String)("cn:"+News.noneCountryIso));
            }

            itemForm.setSelectedLanguage(newsItem.getLanguage());
            itemForm.setLanguageKey((String)("ln:"+newsItem.getLanguage()));

            itemForm.setReleaseDate(DgUtil.formatDate(news.getReleaseDate()));
            if (news.getArchiveDate() == null) {
                itemForm.setArchiveDate("never");
            }
            else {
                itemForm.setArchiveDate(DgUtil.formatDate(news.
                    getArchiveDate()));
            }

            UserInfo author = DgUtil.getUserInfo(newsItem.getUserId());

            itemForm.setAuthorUserId(newsItem.getUserId());
	    if (author != null) {
        	itemForm.setAuthorFirstNames(author.getFirstNames());
        	itemForm.setAuthorLastName(author.getLastName());
	    } else {
        	itemForm.setAuthorFirstNames(null);
        	itemForm.setAuthorLastName(null);
	    }
            itemForm.setEditable(false);

            if( user != null ) {
                if ( ((author != null && author.getEmail() != null &&
                    (author.getEmail().equals(user.getEmail()))) ||
                     DgUtil.isModuleInstanceAdministrator(request))) {
                    itemForm.setEditable(true);
                }
            }

            //reset status
            itemForm.setSelectedStatus(null);
        }

        return mapping.findForward("forward");
    }
}
