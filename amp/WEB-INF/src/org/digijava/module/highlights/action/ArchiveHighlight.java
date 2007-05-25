/*
 *   ArchiveHighlight.java
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action archives active Highlight
 */

public class ArchiveHighlight
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    HighlightItemForm formBean = (HighlightItemForm) form;

    User user = RequestUtils.getUser(request);

    if (user != null) {

      Highlight highlight = null;

      if ( (formBean.getActiveHighlightId() != null) &&
          (!formBean.getActiveHighlightId().equals(new Long(0)))) {

        highlight = DbUtil.getHighlight(formBean.getActiveHighlightId());
      }

      if (highlight == null) {

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);

        highlight = DbUtil.getHighlight(moduleInstance.getSite().
                                        getSiteId(),
                                        moduleInstance.getInstanceName());
      }

      if (highlight != null) {
        highlight.setActive(false);
        highlight.setUpdaterUserId(user.getId());
        GregorianCalendar date = new GregorianCalendar();
        highlight.setUpdationDate(date.getTime());

        DbUtil.updateHighlight(highlight);
      }
      formBean.setFormReset(true);
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