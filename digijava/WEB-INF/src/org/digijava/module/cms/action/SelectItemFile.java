package org.digijava.module.cms.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.form.CMSContentItemForm;


public class SelectItemFile extends Action {
  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
      CMSContentItemForm contItemForm = (CMSContentItemForm) form;
      ActionErrors errors = new ActionErrors();

      String forward = null;
      if (contItemForm.getProcessingMode() == CMSContentItemForm.MODE_NEW) {
        forward = "showCreate";
      } else {
        forward = "showEdit";
      }

      return mapping.findForward(forward);
  }
}