/*
 *   ViewHighlight.java
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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action displayes Highlight
 */

public class ViewHighlight
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    HighlightItemForm formBean = (HighlightItemForm) form;

    if (formBean.getActiveHighlightId() != null) {
      if (DbUtil.isHighlightActive(formBean.getActiveHighlightId())) {
        formBean.setPreviewItem(null);
        return new ActionForward("/highlights/showMore.do", true);
      }

      Highlight highlight = DbUtil.getHighlight(formBean.
                                                getActiveHighlightId());

      HighlightItemForm.HighlightInfo previewItem = new HighlightItemForm.
          HighlightInfo();

      previewItem.setTitle(highlight.getTitle());
      previewItem.setTopic(highlight.getTopicText());
      previewItem.setDescription(highlight.getDescription());

      switch (highlight.getLayout()) {
        case 1: {

          formBean.setLayout1(true);
          formBean.setLayout2(false);
          formBean.setLayout3(false);

          if (highlight.getImage() != null) {
            previewItem.setHaveImage(true);
            previewItem.setImageHeight(Integer.toString(highlight.
                getImageHeight()));
            previewItem.setImageWidth(Integer.toString(highlight.
                getImageWidth()));
            previewItem.setShowImage(highlight.isShowImage());
          }

          break;
        }
        case 2: {
          formBean.setLayout2(true);
          formBean.setLayout1(false);
          formBean.setLayout3(false);

          if (highlight.getImage() != null) {
            previewItem.setHaveImage(true);
            previewItem.setImageHeight(Integer.toString(highlight.
                getImageHeight()));
            previewItem.setImageWidth(Integer.toString(highlight.
                getImageWidth()));
            previewItem.setShowImage(highlight.isShowImage());
          }
          break;
        }
        case 3: {
          formBean.setLayout1(false);
          formBean.setLayout2(false);
          formBean.setLayout3(true);

          previewItem.setHaveImage(false);
          break;
        }
        default: {
          break;
        }
      }
      if (!previewItem.isShowImage()) {
        formBean.setLayout1(false);
        formBean.setLayout2(false);
        formBean.setLayout3(true);
      }

      if (highlight.getCreationDate() != null) {
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(highlight.getCreationDate());

        String creationDate = DgUtil.formatDate(date.getTime());
        previewItem.setCreationDate(creationDate);
      }

      previewItem.setLinks(new ArrayList());
      Iterator iterator = highlight.getLinks().iterator();
      while (iterator.hasNext()) {
        HighlightItemForm.LinkInfo li = new HighlightItemForm.LinkInfo();
        HighlightLinks currentLink = (HighlightLinks) iterator.next();

        li.setName(currentLink.getName());
        li.setUrl(currentLink.getUrl());
        li.setIndex(currentLink.getOffset());

        previewItem.getLinks().add(li);
      }
      previewItem.setId(highlight.getId());

      formBean.setPreviewItem(previewItem);
    }

    return mapping.findForward("forward");
  }

}