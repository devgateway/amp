/*
 * RemoveMeasures.java
 * Created : 06-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

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
		
		if (eaForm.getIssues().getIssueId() != null && eaForm.getIssues().getMeasureId() != null) {
			
			Long issueId = eaForm.getIssues().getIssueId();
			Long measureId = eaForm.getIssues().getMeasureId();
			
			ArrayList<Issues> issues = eaForm.getIssues().getIssues();
			
			for (Issues issue : issues) {
				if(issue.getId().equals(issueId)){
					ArrayList<Measures> measures = issue.getMeasures();
					for (Measures measure : measures) {
						if(measure.getId().equals(measureId)){
							measures.remove(measure);
							break;
						}
					}
					break;
				}
			}
			eaForm.getIssues().setIssueId(null);
			eaForm.getIssues().setMeasureId(null);
			
		}
		
		return mapping.findForward("forward");
	}
}