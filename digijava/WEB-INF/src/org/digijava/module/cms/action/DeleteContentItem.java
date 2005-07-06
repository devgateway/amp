/*
 *   DeleteContentItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 12,2004
 * 	 CVS-ID: $Id: DeleteContentItem.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.cms.util.CMSManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DeleteContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    if (contItemForm.getItemId() != null) {
      CMSContentItem contentItem = DbUtil.getCMSContentItem(contItemForm.getItemId());

      if (contentItem != null) {
        DbUtil.deleteCMSContentItem(contentItem);
      }
    }

    return mapping.findForward("deleteContentItem");
  }

}