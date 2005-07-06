/*
 * CompDateRevised.java 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;

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
		
		if (eaForm.getRevisedCompDate().trim().length() > 0) {
			if (eaForm.getActivityCloseDates() != null) {
				eaForm.getActivityCloseDates().add(eaForm.getCurrentCompDate());
				eaForm.setCurrentCompDate(eaForm.getRevisedCompDate());
				eaForm.setRevisedCompDate("");
			}
		}
		return mapping.findForward("forward");
	}
}