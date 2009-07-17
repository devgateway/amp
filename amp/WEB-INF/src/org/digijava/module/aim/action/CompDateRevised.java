/*
 * CompDateRevised.java 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.DateConversion;

/**
 * The CompDateRevised action class will revise the current completion date for
 * an activity. This will make the current comp date to previous completion date
 * and make the newly obtained revised completion date to current
 * 
 * @author priyajith
 */
public class CompDateRevised extends Action {

	private static Logger logger = Logger.getLogger(CompDateRevised.class);
	

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		logger.info("Revising the completion date");
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		
		if (eaForm.getPlanning().getRevisedCompDate().trim().length() > 0) {
			if (eaForm.getPlanning().getActivityCloseDates() != null) {
				eaForm.getPlanning().getActivityCloseDates().add(eaForm.getPlanning().getCurrentCompDate());
				eaForm.getPlanning().setCurrentCompDate(eaForm.getPlanning().getRevisedCompDate());
				eaForm.getPlanning().setRevisedCompDate("");
			}
		}
		List list = new ArrayList(eaForm.getPlanning().getActivityCloseDates()); 
		Collections.sort(list,DateConversion.dtComp);
		eaForm.getPlanning().setActivityCloseDates(list);
			
		return mapping.findForward("forward");
	}
}