package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ViewIndicatorsForm;
import org.digijava.module.aim.util.IndicatorUtil;

public class RemoveIndicator extends Action {
	 public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {		 
		 ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
		 String id = request.getParameter("indicatorId");
		 if (id != null){ 
			 IndicatorUtil.deleteIndicator(new Long(id));
	     }
		 return mapping.findForward("viewAll");
	 }
}
