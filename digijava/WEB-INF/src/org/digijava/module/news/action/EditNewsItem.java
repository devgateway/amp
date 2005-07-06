/*
 *   EditNewsItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: EditNewsItem.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

import java.util.GregorianCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;

/**
 * Action edits an existing news item identified by activeNewsItem identity(if not null), updates it into database and redirects back to the action from which it was invoked
 */

public class EditNewsItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        NewsItemForm itemForm = (NewsItemForm) form;
        News news = null;

        if (itemForm.getActiveNewsItem() != null) {

            news = DbUtil.getNewsItem(itemForm.getActiveNewsItem());
            NewsItem newsItem = news.getFirstNewsItem();

            newsItem.setNews(news);
            newsItem.setTitle(itemForm.getTitle());
//          newsItem.setDescription(Hibernate.createClob(itemForm.getDescription()));
            newsItem.setDescription(itemForm.getDescription());
            newsItem.setLanguage(itemForm.getSelectedLanguage());

            if (itemForm.getCountry().equals(News.noneCountryIso)){
              news.setCountry(null);
            } else {
              news.setCountry(itemForm.getCountry());
            }
            news.setSourceName(itemForm.getSourceName());
            news.setSourceUrl(itemForm.getSourceUrl());

            news.setEnableHTML(itemForm.isEnableHTML());
            news.setEnableSmiles(itemForm.isEnableSmiles());

            GregorianCalendar releaseDate = new GregorianCalendar();
            releaseDate.set(java.util.Calendar.MINUTE, 0);
            releaseDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
            releaseDate.set(java.util.Calendar.SECOND, 0);
            releaseDate.set(java.util.Calendar.MONTH,
                            Integer.parseInt(itemForm.getReleaseMonth()) - 1);
            releaseDate.set(java.util.Calendar.DAY_OF_MONTH,
                            Integer.parseInt(itemForm.getReleaseDay()));
            releaseDate.set(java.util.Calendar.YEAR,
                            Integer.parseInt(itemForm.getReleaseYear()));

            news.setReleaseDate(releaseDate.getTime());

            if (itemForm.isNeverArchive()) {
                news.setArchiveDate(null);
            }
            else {
                GregorianCalendar archiveDate = new GregorianCalendar();
                archiveDate.set(java.util.Calendar.MINUTE,
                                archiveDate.
                                getActualMaximum(java.util.Calendar.MINUTE));
                archiveDate.set(java.util.Calendar.HOUR_OF_DAY,
                                archiveDate.
                                getActualMaximum(java.util.Calendar.HOUR_OF_DAY));
                archiveDate.set(java.util.Calendar.SECOND,
                                archiveDate.
                                getActualMaximum(java.util.Calendar.SECOND));
                archiveDate.set(java.util.Calendar.MONTH,
                                Integer.parseInt(itemForm.getArchiveMonth()) -
                                1);
                archiveDate.set(java.util.Calendar.DAY_OF_MONTH,
                                Integer.parseInt(itemForm.getArchiveDay()));
                archiveDate.set(java.util.Calendar.YEAR,
                                Integer.parseInt(itemForm.getArchiveYear()));

                news.setArchiveDate(archiveDate.getTime());
            }

            //update here
            DbUtil.updateNews(news);
        }
          return new ActionForward( itemForm.getReturnUrl(), true);
    }

}