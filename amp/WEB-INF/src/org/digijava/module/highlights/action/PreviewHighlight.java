/*
 *   PreviewHighlight.java
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action Previews the Highlight before publishment
 */
public class PreviewHighlight
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    HighlightItemForm formBean = (HighlightItemForm) form;
    User user = RequestUtils.getUser(request);

    String param = request.getParameter("CreateOrEdit");

    if (user != null) {
      formBean.setPreview(true);

      HighlightItemForm.HighlightInfo previewItem = new HighlightItemForm.
          HighlightInfo();

      previewItem.setTitle(formBean.getTitle());
      previewItem.setTopic(formBean.getTopic());
      previewItem.setDescription(formBean.getDescription());

      if (formBean.getLayout().equals("1")) {

        if (formBean.getImageHeight1().trim().length() == 0 ||
            formBean.getImageWidth1().trim().length() == 0) {
          previewItem.setHaveImageSizes(false);
        }
        else {
          previewItem.setImageHeight(formBean.getImageHeight1());
          previewItem.setImageWidth(formBean.getImageWidth1());
          previewItem.setHaveImageSizes(true);
        }

        formBean.setLayout1(true);
        formBean.setLayout2(false);
        formBean.setLayout3(false);
      }
      if (formBean.getLayout().equals("2")) {

        if (formBean.getImageHeight2().trim().length() == 0 ||
            formBean.getImageWidth2().trim().length() == 0) {
          previewItem.setHaveImageSizes(false);
        }
        else {
          previewItem.setImageHeight(formBean.getImageHeight2());
          previewItem.setImageWidth(formBean.getImageWidth2());
          previewItem.setHaveImageSizes(true);
        }

        formBean.setLayout2(true);
        formBean.setLayout1(false);
        formBean.setLayout3(false);

      }
      if (formBean.getLayout().equals("3")) {

        formBean.setImage(null);
        formBean.setImageSize(0);

        formBean.setLayout3(true);
        formBean.setLayout1(false);
        formBean.setLayout2(false);

      }
      previewItem.setLinks(formBean.getLinks());

      //Image -------------

      FormFile formFile = formBean.getPhotoFile();
      if (formFile != null) {
        if (formFile.getFileSize() != 0) {

          //save image into form
          formBean.setImage(formFile.getFileData());
          formBean.setImageSize(formFile.getFileSize());
          formBean.setContentType(formFile.getContentType());
          formBean.setHaveImage(true);
        }
        else {
          if (formBean.getImage() != null) {
            formBean.setHaveImage(true);
          }
          else {
            formBean.setImage(null);
            formBean.setImageSize(0);
            formBean.setHaveImage(false);
            formBean.setContentType(null);
          }
        }
      }
      formBean.setPreviewItem(previewItem);

      ActionErrors errors = formBean.validate(mapping, request);
      if (errors != null && errors.size() != 0) {
        saveErrors(request, errors);
        formBean.setPreview(false);
      }
    }

    //--------------------

    formBean.setFormReset(false);

    if (param.equals("editHighlight")) {
      return mapping.findForward("forwardEdit");
    }
    else {
      return mapping.findForward("forwardCreate");
    }

  }

}