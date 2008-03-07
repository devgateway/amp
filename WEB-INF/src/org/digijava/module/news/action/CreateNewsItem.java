/*
 *   CreateNewsItem.java
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

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.module.news.dbentity.NewsItem;
import org.digijava.module.common.util.ModuleEmailManager;

/**
 * Action creates new News item and saves it into database
 */

public class CreateNewsItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    NewsItemForm itemForm = (NewsItemForm) form;
    User user = RequestUtils.getUser(request);
    String status = ItemStatus.PUBLISHED;

    // check if user loged
    if (user != null) {

      // set preview false
      itemForm.setPreview(false);

      // get default settings
      NewsSettings setting = DbUtil.getNewsSettings(request);

      // if moderated news status set Pending
      if (setting.isModerated() &&
          (!DgUtil.isModuleInstanceAdministrator(request)))
        status = ItemStatus.PENDING;

        // create news item
      News news = DbUtil.createNews(user.getId(),
                                    status,
                                    itemForm.getSelectedLanguage(),
                                    itemForm.getTitle(),
                                    itemForm.getDescription(),
                                    request);

      if (itemForm.getCountry().equals(News.noneCountryIso)){
        news.setCountry(null);
      } else {
        news.setCountry(itemForm.getCountry());
      }
      //
      news.setSourceName(itemForm.getSourceName());
      news.setSourceUrl(itemForm.getSourceUrl());

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
                        archiveDate.getActualMaximum(java.util.Calendar.MINUTE));
        archiveDate.set(java.util.Calendar.HOUR_OF_DAY,
                        archiveDate.
                        getActualMaximum(java.util.Calendar.HOUR_OF_DAY));
        archiveDate.set(java.util.Calendar.SECOND,
                        archiveDate.getActualMaximum(java.util.Calendar.SECOND));
        archiveDate.set(java.util.Calendar.MONTH,
                        Integer.parseInt(itemForm.getArchiveMonth()) - 1);
        archiveDate.set(java.util.Calendar.DAY_OF_MONTH,
                        Integer.parseInt(itemForm.getArchiveDay()));
        archiveDate.set(java.util.Calendar.YEAR,
                        Integer.parseInt(itemForm.getArchiveYear()));

        news.setArchiveDate(archiveDate.getTime());
      }
      news.setEnableHTML(itemForm.isEnableHTML());
      news.setEnableSmiles(itemForm.isEnableSmiles());

      // update into database
      DbUtil.updateNews(news);

      if (setting.isModerated() &&
          (!DgUtil.isModuleInstanceAdministrator(request))) {
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);

        String link = request.getScheme() + "://" +
            request.getServerName() + ":" +
            new Long(request.getServerPort()).toString() +
            SiteConfigUtils.buildDgURL(request, false) +
            SiteConfigUtils.getCurrentModuleURL(request) +
            "/showNewsItems.do?status=pe";

        NewsItem newsItem = news.getFirstNewsItem();
        ModuleEmailManager.sendAdminEmail(moduleInstance,
                                          newsItem.getTitle(),
                                          news.getSourceName(),
                                          news.getSourceUrl(),
                                          newsItem.getDescription(),
                                          link);

      }

    }
    else {
      ActionErrors errors = new ActionErrors();
      errors.add(null,
                 new ActionError("error.:news.userEmpty"));
      saveErrors(request, errors);

    }

    return mapping.findForward("forward");
  }

}