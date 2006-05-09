package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;

public class SaveIndicatorValues extends Action 
{
	private static Logger logger = Logger.getLogger(SaveIndicatorValues.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		eaForm.setCurrentVal(eaForm.getCurrentVal());
		eaForm.setIndicatorValId(eaForm.getIndicatorValId());
		eaForm.setCurrentValDate(eaForm.getCurrentValDate());
		eaForm.setIndicatorRisk(eaForm.getIndicatorRisk());
		eaForm.setComments(eaForm.getComments());
		
		return mapping.findForward("forward");
	}
}