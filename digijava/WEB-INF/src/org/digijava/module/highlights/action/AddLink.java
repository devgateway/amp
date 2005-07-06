/*
 *   AddLink.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2003
 * 	 CVS-ID: $Id: AddLink.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.highlights.form.HighlightItemForm;

/**
 * Action adds new link to Highlight links Set
 */
public class AddLink
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    HighlightItemForm formBean = (HighlightItemForm) form;
    HighlightItemForm.LinkInfo link = new HighlightItemForm.LinkInfo();

    int index = Integer.parseInt(request.getParameter("offset")) + 1;

    ArrayList links = formBean.getLinks();

    //reindex links
    for (int i = index; i < links.size(); i++) {
      HighlightItemForm.LinkInfo item = (HighlightItemForm.LinkInfo) links.get(
          i);
      item.setIndex(item.getIndex()+1);
    }
    //add new link
    link.setIndex(index);
    link.setUrl(request.getScheme() + "://");

    links.add(index, link);

    formBean.setLinks(links);

    String param = request.getParameter("CreateOrEdit");

    formBean.setFormReset(false);
    if (param.equals("editHighlight")) {
      return mapping.findForward("forwardEdit");
    }
    else {
      return mapping.findForward("forwardCreate");
    }

  }

}