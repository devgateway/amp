/*
 *   DownloadFile.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: DownloadFile.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DownloadFile
    extends Action {

  // log4J class initialize String
  private static Logger logger = I18NHelper.getKernelLogger(DownloadFile.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws
      java.lang.Exception {

      CMSContentItemForm contItemForm = (CMSContentItemForm) form;
      CMSContentItem cmsItem = null;

       if (contItemForm.getItemId() != null) {
           cmsItem = DbUtil.getCMSContentItem(contItemForm.getItemId());
           if (cmsItem != null) {
             if (cmsItem.getFile() != null) {
                 ResponseUtil.writeFile(response, cmsItem.getContentType(),
                                        cmsItem.getFileName(), cmsItem.getFile());
             }
           }
        }

    return null;
  }

}
