/*
 *   UpdateConfirmNewsItems.java
 *   @Author Lasha Dolidze lasha@digijava.org
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
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.common.util.ModuleEmailManager;

/**
 * Action updates selected news items statuses into database, sends appropriate notification email alerts to news item's authors and redirects back to the action from which it was invoked
 */

public class UpdateConfirmNewsItems
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    NewsItemForm itemForm = (NewsItemForm) form;
    List newsToReject = new ArrayList();
    String status = "";

    List selectedNews = itemForm.getNewsList();

    if (selectedNews != null && selectedNews.size() != 0) {

      Iterator iter = selectedNews.iterator();
      if (iter.hasNext()) {
        while (iter.hasNext()) {
          NewsItemForm.NewsInfo item = (NewsItemForm.NewsInfo) iter.
              next();

          if (item.isSelected()) {

            // get news by id
            News news = DbUtil.getNewsItem(item.getId());

            if (itemForm.getSelectedStatus() != null) {
              if (itemForm.getSelectedStatus().equalsIgnoreCase(
                  ItemStatus.REVOKE))
                status = ItemStatus.PUBLISHED;
              else
                status = itemForm.getSelectedStatus();
            }
            else {
              status = ItemStatus.REJECTED;
            }

            // update news status
            DbUtil.updateStatus(news, status);

            NewsItem newsItem = news.getFirstNewsItem();

            User author = UserUtils.getUser(newsItem.
                                            getUserId());

            if (author != null && itemForm.isSendMessage()) {

              ModuleEmailManager.sendUserEmail(DgUtil.getRealModuleInstance(
                  request), author, newsItem.getTitle(), news.getSourceUrl(),
                                               status);

            }
          }
        }
      }
    }

    return new ActionForward(itemForm.getReturnUrl(), true);
  }

}