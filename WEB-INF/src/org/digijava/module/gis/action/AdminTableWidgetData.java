package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.gis.form.AdminTableWidgetDataForm;

public class AdminTableWidgetData extends DispatchAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return super.unspecified(mapping, form, request, response);
	}


	protected ActionForward showEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		
		return mapping.findForward("forward");
	}
	
	
}
