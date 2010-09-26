/*
 * ShowUpdateMeasures.java
 * Created : 06-Sep-2005
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
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;

public class ShowUpdateRegionalObservationMeasures extends Action {
	
	private static Logger logger = Logger.getLogger(ShowUpdateRegionalObservationMeasures.class);
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		logger.debug("In showUpdateMeasures");
		if (eaForm.getObservations().getMeasureId() != null && 
				eaForm.getObservations().getIssueId() != null && 
				eaForm.getObservations().getIssueId().longValue() > 0 && 
				eaForm.getObservations().getMeasureId().longValue() > 0) {
			
			logger.debug("In here");
			Issues issue = new Issues();
			issue.setId(eaForm.getObservations().getIssueId());
			int index = eaForm.getObservations().getIssues().indexOf(issue);
			issue = (Issues) eaForm.getObservations().getIssues().get(index);
			
			Measures measures = new Measures();
			measures.setId(eaForm.getObservations().getMeasureId());
			index = issue.getMeasures().indexOf(measures);
			measures = (Measures) issue.getMeasures().get(index);
			eaForm.getObservations().setMeasure(measures.getName());
		} else {
			eaForm.getObservations().setMeasure(null);
		}
		return mapping.findForward("forward");
	}
}