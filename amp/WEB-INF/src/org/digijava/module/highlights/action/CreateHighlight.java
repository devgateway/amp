/*
 *   CreateHighlight.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 11, 2003
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

package org.digijava.module.highlights.action;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;
import net.sf.hibernate.Hibernate;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action creates new active Highlight and archives other active Highlights(if any)
 */

public class CreateHighlight
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    HighlightItemForm formBean = (HighlightItemForm) form;

    User user = RequestUtils.getUser(request);

    if (user != null) {

      formBean.setPreview(false);

      Highlight highlight = new Highlight();

      ModuleInstance instance = RequestUtils.getModuleInstance(request);
      highlight.setSiteId(instance.getSite().getSiteId());
      highlight.setInstanceId(instance.getInstanceName());

      highlight.setTitle(formBean.getTitle());
      highlight.setDescription(formBean.getDescription());

      if (formBean.getTopic() != null) {
        highlight.setTopic(formBean.getTopic());
      }

      if (formBean.getLayout().equals("1")) {
        highlight.setLayout(1);
        if (formBean.getImageHeight1().trim().length() == 0 ||
            formBean.getImageWidth1().trim().length() == 0) {
          highlight.setImageHeight(0);
          highlight.setImageWidth(0);
        }
        else {
          highlight.setImageHeight(Integer.parseInt(formBean.
              getImageHeight1()));
          highlight.setImageWidth(Integer.parseInt(formBean.
              getImageWidth1()));
        }

      }
      if (formBean.getLayout().equals("2")) {
        highlight.setLayout(2);
        if (formBean.getImageHeight2().trim().length() == 0 ||
            formBean.getImageWidth2().trim().length() == 0) {
          highlight.setImageHeight(0);
          highlight.setImageWidth(0);
        }
        else {
          highlight.setImageHeight(Integer.parseInt(formBean.
              getImageHeight2()));
          highlight.setImageWidth(Integer.parseInt(formBean.
              getImageWidth2()));
        }

      }
      //
      //Image -------------
      FormFile formFile = formBean.getPhotoFile();
      if (formFile != null) {
        if (formFile.getFileSize() != 0) {
          byte[] image = formFile.getFileData();
          highlight.setImage(image);
          highlight.setContentType(formFile.getContentType());
        }
        else {
          if (formBean.getImage() != null) {
            highlight.setImage(formBean.getImage());
            if (formBean.getContentType() != null) {
              highlight.setContentType(formBean.getContentType());
            }
          }
          else {
            highlight.setImage(null);
            highlight.setContentType(null);
          }
        }
      }

      if (formBean.getLayout().equals("3")) {
        highlight.setLayout(3);
        highlight.setImage(null);
        highlight.setImageHeight(0);
        highlight.setImageWidth(0);
        //
        formBean.setImage(null);
        formBean.setImageSize(0);
      }

      highlight.setShortTopicLength(formBean.getShortTopicLength());

      highlight.setAuthorUserId(user.getId());
      highlight.setUpdaterUserId(null);

      GregorianCalendar date = new GregorianCalendar();
      highlight.setCreationDate(date.getTime());
      highlight.setUpdationDate(null);

      highlight.setActive(true);
      highlight.setShowImage(true);
      highlight.setIsPublic(true);

      //Assign links to highlight
      highlight.setLinks(new HashSet());
      Iterator iterator = formBean.getLinks().iterator();
      while (iterator.hasNext()) {
        HighlightItemForm.LinkInfo linkItem = (HighlightItemForm.
                                               LinkInfo)
            iterator.next();

        HighlightLinks link = new HighlightLinks();

        link.setName(linkItem.getName());
        link.setUrl(linkItem.getUrl());
        link.setOffset(linkItem.getIndex());
        link.setHighlight(highlight);

        highlight.getLinks().add(link);
      }
      //   --------------------
      // Archive old highlights if any
      ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
          request);
      DbUtil.ArchiveHighlights(moduleInstance.getSite().
                               getSiteId(),
                               moduleInstance.getInstanceName());

      DbUtil.createHighlight(highlight);

    }
    else {
      ActionErrors errors = new ActionErrors();
      errors.add(null,
                 new ActionError("error.highlights.userEmpty"));
      saveErrors(request, errors);
    }

    return mapping.findForward("forward");
  }

}