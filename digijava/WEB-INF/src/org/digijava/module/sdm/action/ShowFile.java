/*
 *   ShowFile.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: ShowFile.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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


package org.digijava.module.sdm.action;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowFile
    extends Action {

  // log4J class initialize String
  private static Logger logger = I18NHelper.getKernelLogger(ShowFile.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws
      java.lang.Exception {

    SdmForm formBean = (SdmForm) form;
    Sdm sdmDoc = null;
    SdmItem sdmItem = null;

    Long paramDocId = new Long(Long.parseLong(request.getParameter("documentId")));

    if (paramDocId != null) {
      sdmDoc = DbUtil.getDocument(paramDocId);
    }
    else {
      sdmDoc = formBean.getSdmDocument();
    }
    if (sdmDoc != null) {
      Long paramParagId = new Long(Long.parseLong(request.getParameter("activeParagraphOrder")));

      if (paramParagId != null) {

        sdmItem = sdmDoc.getItemByIndex(paramParagId);

        if (sdmItem != null) {
          byte[] file = sdmItem.getContent();

          if (file != null) {
            ResponseUtil.writeFile(response, sdmItem.getContentType(),
                                   sdmItem.getContentText(), file);

          }
        }
      }
    }
    return null;
  }

}