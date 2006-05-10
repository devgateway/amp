package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;

public class SaveIndicatorValues extends Action 
{
	private static Logger logger = Logger.getLogger(SaveIndicatorValues.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (eaForm.getIndicatorsME() != null && 
				eaForm.getIndicatorsME().size() > 0) {
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorId(eaForm.getIndicatorId());
			Iterator itr = eaForm.getIndicatorsME().iterator();
			while (itr.hasNext()) {
				ActivityIndicator temp = (ActivityIndicator) itr.next();
				if (temp.equals(actInd)) {
					temp.setCurrentVal(eaForm.getCurrentVal());
					temp.setCurrentValDate(eaForm.getCurrentValDate());
					temp.setComments(eaForm.getComments());
					temp.setRisk(eaForm.getIndicatorRisk());
					break;
				}
			}
		}
		
		return mapping.findForward("forward");
	}
}