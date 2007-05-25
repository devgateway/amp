/*
 *   PreviewNewsItem.java
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
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.common.util.ModuleUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.ModuleInstance;

/**
 * Action Previews the news before publcation
 */

public class PreviewNewsItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        NewsItemForm itemForm = (NewsItemForm) form;
	Site currentSite = RequestUtils.getSite(request);
	ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);


        String param = request.getParameter("CreateOrEdit");

        itemForm.setPreview(true);
        NewsItemForm.NewsInfo previewItem = new NewsItemForm.NewsInfo();

        previewItem.setTitle(itemForm.getTitle());

        GregorianCalendar releaseDate = new GregorianCalendar();
        releaseDate.set(java.util.Calendar.MONTH,
                        Integer.parseInt(itemForm.getReleaseMonth()) - 1);
        releaseDate.set(java.util.Calendar.DAY_OF_MONTH,
                        Integer.parseInt(itemForm.getReleaseDay()));
        releaseDate.set(java.util.Calendar.YEAR,
                        Integer.parseInt(itemForm.getReleaseYear()));

        previewItem.setReleaseDate(DgUtil.formatDate(releaseDate.getTime()));

        if (itemForm.isNeverArchive()) {
            previewItem.setArchiveDate("never");
        }
        else {
            GregorianCalendar archiveDate = new GregorianCalendar();
            archiveDate.set(java.util.Calendar.MONTH,
                            Integer.parseInt(itemForm.getArchiveMonth()) - 1);
            archiveDate.set(java.util.Calendar.DAY_OF_MONTH,
                            Integer.parseInt(itemForm.getArchiveDay()));
            archiveDate.set(java.util.Calendar.YEAR,
                            Integer.parseInt(itemForm.getArchiveYear()));

            previewItem.setArchiveDate(DgUtil.formatDate(archiveDate.getTime()));
        }
	//get delimiter character
	String delimiter = DbUtil.getShortVersionDelimiter(currentSite.
	      getSiteId(), moduleInstance.getInstanceName());

	previewItem.setDescription(ModuleUtil.removeDelimiters(itemForm.
	      getDescription(), delimiter));
	previewItem.setDescription(BBCodeParser.parse(previewItem.
	      getDescription(),
	      itemForm.isEnableSmiles(), itemForm.isEnableHTML(), request));

        if (param.equals("editNewsItem")) {

            if (itemForm.getActiveNewsItem() != null) {

                News news = DbUtil.getNewsItem(itemForm.getActiveNewsItem());
                UserInfo author = DgUtil.getUserInfo(news.getFirstNewsItem().
                    getUserId());

                previewItem.setAuthorUserId(news.getFirstNewsItem().getUserId());
                previewItem.setAuthorFirstNames(author.getFirstNames());
                previewItem.setAuthorLastName(author.getLastName());
            }

        }
        else {
            User user = RequestUtils.getUser(request);
            if (user != null) {
                previewItem.setAuthorUserId(user.getId());
                previewItem.setAuthorFirstNames(user.getFirstNames());
                previewItem.setAuthorLastName(user.getLastName());
            }

        }
        previewItem.setSourceName(itemForm.getSourceName());
        previewItem.setSourceUrl(itemForm.getSourceUrl());

        Collection languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
        Iterator iter = languages.iterator();
        while (iter.hasNext()) {
            Locale item = (Locale) iter.next();
            if (item.getCode().equals(itemForm.getSelectedLanguage())) {
                previewItem.setLanguage(item.getCode());
                previewItem.setLanguageKey( (String) ("ln:" + item.getCode()));
                break;
            }
        }

        itemForm.setLanguages(languages);

        if (itemForm.getCountry().equals(News.noneCountryIso)) {
          previewItem.setCountry(News.noneCountryIso);
          previewItem.setCountryName(News.noneCountryName);
          previewItem.setCountryKey( (String) ("cn:" + News.noneCountryIso));
        }
        else {
              previewItem.setCountry(itemForm.getCountry());
              previewItem.setCountryName(itemForm.getCountry());
              previewItem.setCountryKey( (String) ("cn:" + itemForm.getCountry()));
          }
    //        itemForm.setCountryResidence(sortedCountries);

        itemForm.setPreviewItem(previewItem);

        ActionErrors errors = itemForm.validate(mapping, request);
        if (errors != null && errors.size() != 0) {
            saveErrors(request, errors);
            itemForm.setPreview(false);
            itemForm.loadCalendar();
        }

        if (param.equals("editNewsItem")) {
            return mapping.findForward("forwardEdit");
        }
        else {
            return mapping.findForward("forwardCreate");
        }
    }

}