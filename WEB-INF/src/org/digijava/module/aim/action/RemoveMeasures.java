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
		
		if (eaForm.getSelMeasures() != null && 
				eaForm.getSelMeasures().length > 0) {
			Long measures[] = eaForm.getSelMeasures();
			
			logger.debug("Issue Id = " + eaForm.getIssueId());
			if (eaForm.getIssueId() != null) {
				Issues issue = new Issues();
				issue.setId(eaForm.getIssueId());
				int index = eaForm.getIssues().indexOf(issue);
				issue = (Issues) eaForm.getIssues().get(index);
				
				for (int i = 0;i < measures.length;i ++) {
					Measures measure = new Measures();
					measure.setId(measures[i]);
					issue.getMeasures().remove(measure);
				}
				eaForm.getIssues().set(index,issue);
			}
			eaForm.setSelMeasures(null);
		}
		return mapping.findForward("forward");
	}
}