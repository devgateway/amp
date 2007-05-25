/*
 *   PreviewContentItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 06,2004
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

package org.digijava.module.cms.action;

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
import org.digijava.module.cms.form.CMSContentItemForm;
import org.apache.struts.upload.FormFile;

/**
 * Action Previews the news before publcation
 */

public class PreviewContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    String param = request.getParameter("CreateOrEdit");

    contItemForm.setPreview(true);

    CMSContentItemForm.CMSContentItemInfo previewItem = new CMSContentItemForm.
        CMSContentItemInfo();

    previewItem.setTitle(contItemForm.getTitle());
    previewItem.setDescription(contItemForm.getDescription());

    if (contItemForm.getUrl() != null && contItemForm.getUrl().trim().length() != 0 &&
        !contItemForm.getUrl().equals(request.getScheme()+"://")) {
      previewItem.setUrl(contItemForm.getUrl());
    }
    //
    FormFile formFile = contItemForm.getFormFile();
    if (formFile != null) {
      if (formFile.getFileSize() != 0) {

        previewItem.setFileName(formFile.getFileName());
        //save image into form
        contItemForm.setFile(formFile.getFileData());
        contItemForm.setFileName(formFile.getFileName());
        contItemForm.setContentType(formFile.getContentType());
      }
      else {
        if (contItemForm.getFile() != null) {
          previewItem.setFileName(contItemForm.getFileName());
        }
        else {
          previewItem.setFileName(null);

          contItemForm.setFile(null);
          contItemForm.setFileName(null);
          contItemForm.setContentType(null);
        }
      }
    }

   //
    previewItem.setLanguage(contItemForm.getLanguage());
    previewItem.setLanguageKey( (String) ("ln:" + contItemForm.getLanguage()));
    //
    previewItem.setCountry(contItemForm.getCountry());
    previewItem.setCountryName(contItemForm.getCountry());
    previewItem.setCountryKey( (String) ("cn:" + contItemForm.getCountry()));
    //
    contItemForm.setItemPreview(previewItem);

    ActionErrors errors = contItemForm.validate(mapping, request);
    if (errors != null && errors.size() != 0) {
      saveErrors(request, errors);
      contItemForm.setPreview(false);
    }

    contItemForm.setNoReset(true);

    return mapping.findForward("previewContentItem");
  }

}