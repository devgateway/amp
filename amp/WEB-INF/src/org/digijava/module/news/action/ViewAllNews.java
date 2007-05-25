/*
 *   ViewAllNews.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action displayes list of all news
 */

public class ViewAllNews
    extends PaginationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        NewsItemForm newsForm = (NewsItemForm) form;

        ArrayList newsList = new ArrayList();
        List dbNewsList = null;

        User user = RequestUtils.getUser(request);

        if (request.getParameter("all") != null ){
            newsForm.setStatus("all");
        }
        if (newsForm.getStatus() == null || newsForm.getStatus().equals("all")) {
            // Get Current news
            dbNewsList = DbUtil.getNews(ItemStatus.PUBLISHED, request,true);
        }
        else {
            //Get user events
            if (newsForm.getStatus() == null ||
                newsForm.getStatus().equals("mpe")) {
                dbNewsList = DbUtil.getNews(user.getId(), ItemStatus.PENDING);
            }
            else if (newsForm.getStatus().equals("mall")) {
                //user's all news List
                dbNewsList = DbUtil.getNews(user.getId(), null);
            }
            else {
                // Get Current news
                dbNewsList = DbUtil.getNews(ItemStatus.PUBLISHED, request, false);
            }

        }

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

        doStartPagination(newsForm, numberOfItemsPerPage);

        if (dbNewsList != null) {
            if (dbNewsList.size() <= numberOfItemsPerPage) {
                newsForm.setNext(null);
                newsForm.setPrev(null);
            }
            else {
//                genPagesList(itemForm, dbNewsList.size());

                dbNewsList = dbNewsList.subList(getOffset(),
                                                dbNewsList.size());

                if (dbNewsList.size() >= (numberOfItemsPerPage + 1)) {
                    dbNewsList = dbNewsList.subList(0,
                        (numberOfItemsPerPage + 1));
                }
                endPagination(dbNewsList.size());
            }
        }
        //end pegination

        if (dbNewsList != null) {
            int n;
            if ( (numberOfItemsPerPage + 1) == dbNewsList.size())
                n = dbNewsList.size() - 1;
            else {
                n = dbNewsList.size();
            }

            for (int i = 0; i < n; i++) {

                News item = (News) dbNewsList.get(i);
                NewsItem newsItem = item.getFirstNewsItem();

                NewsItemForm.NewsInfo ni = new NewsItemForm.NewsInfo();

                ni.setTitle(newsItem.getTitle());
                ni.setReleaseDate(DgUtil.formatDate(item.getReleaseDate()));

                ni.setSourceName(item.getSourceName());
                ni.setSourceUrl(item.getSourceUrl());

                if (item.getCountry() != null) {
                  ni.setCountry(item.getCountry());
                  ni.setCountryName(item.getCountry());
                  ni.setCountryKey( (String) ("cn:" + item.getCountry()));
                } else {
                  ni.setCountry(News.noneCountryIso);
                  ni.setCountryName(News.noneCountryName);
                  ni.setCountryKey( (String) ("cn:" + News.noneCountryIso));
                }

                UserInfo author = DgUtil.getUserInfo(newsItem.getUserId());
                ni.setAuthorUserId(newsItem.getUserId());
                ni.setAuthorFirstNames(author.getFirstNames());
                ni.setAuthorLastName(author.getLastName());

                ni.setId(item.getId());

                newsList.add(ni);
            }

            newsForm.setNewsList(newsList);

        }
        else {
            newsForm.setNewsList(null);
        }

        return mapping.findForward("forward");
    }

}
