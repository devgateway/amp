package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;

public class GetStatusDescription extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		EditActivityForm eaForm=(EditActivityForm)form;
		HttpSession session=request.getSession();
		session.setAttribute("activityName", eaForm.getIdentification().getTitle());
        session.setAttribute("activityFieldName", "Status Description");
		return mapping.findForward("forward");
	}
}
