package org.digijava.module.cms.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.form.CMSContentItemForm;

public class ShowSelectItemFile extends Action {
  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
      String forward = "showSelectFile";
      CMSContentItemForm contentItemForm = (CMSContentItemForm) form;
      contentItemForm.setNoReset(true);
      return mapping.findForward(forward);
    }

}