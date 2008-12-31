/*
 * RemoveMeasures.java
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

public class RemoveMeasures extends Action {
	
	private static Logger logger = Logger.getLogger(RemoveMeasures.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		logger.debug("In remove measures");
		
		if (eaForm.getIssues().getSelMeasures() != null && 
				eaForm.getIssues().getSelMeasures().length > 0) {
			Long measures[] = eaForm.getIssues().getSelMeasures();
			
			logger.debug("Issue Id = " + eaForm.getIssues().getIssueId());
			if (eaForm.getIssues().getIssueId() != null) {
				Issues issue = new Issues();
				issue.setId(eaForm.getIssues().getIssueId());
				int index = eaForm.getIssues().getIssues().indexOf(issue);
				issue = (Issues) eaForm.getIssues().getIssues().get(index);
				
				for (int i = 0;i < measures.length;i ++) {
					Measures measure = new Measures();
					measure.setId(measures[i]);
					issue.getMeasures().remove(measure);
				}
				eaForm.getIssues().getIssues().set(index,issue);
			}
			eaForm.getIssues().setSelMeasures(null);
		}
		return mapping.findForward("forward");
	}
}