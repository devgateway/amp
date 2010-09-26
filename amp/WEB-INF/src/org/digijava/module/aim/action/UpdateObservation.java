/*
 * UpdateIssue.java
 * Created : 05-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;

public class UpdateObservation extends Action {
	private static Logger logger = Logger.getLogger(UpdateObservation.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;	
		if (eaForm.getObservations().getIssue() != null &&
				eaForm.getObservations().getIssue().trim().length() > 0) {
			Issues issue = new Issues();
			if (eaForm.getObservations().getIssueId() == null || 
					eaForm.getObservations().getIssueId().longValue() < 0) {
				logger.debug("Setting the id from currentTimeMills()");
				issue.setId(new Long(System.currentTimeMillis()));					
			} else {
				logger.debug("Setting the id from the previous");
				issue.setId(eaForm.getObservations().getIssueId());
			}
			if (eaForm.getObservations().getIssues() == null) {
				issue.setName(eaForm.getObservations().getIssue());
				issue.setIssueDate(eaForm.getObservations().getIssueDate());
				if (eaForm.getObservations().getIssues() == null) {
					eaForm.getObservations().setIssues(new ArrayList());
				}
				eaForm.getObservations().getIssues().add(issue);								
			} else {
				int index = eaForm.getObservations().getIssues().indexOf(issue);
				if (index < 0) {
					issue.setName(eaForm.getObservations().getIssue());
					issue.setIssueDate(eaForm.getObservations().getIssueDate());
					if (eaForm.getObservations().getIssues() == null) {
						eaForm.getObservations().setIssues(new ArrayList());
					}
					eaForm.getObservations().getIssues().add(issue);				
				} else {
					issue = (Issues) eaForm.getObservations().getIssues().get(index);
					issue.setName(eaForm.getObservations().getIssue());
					issue.setIssueDate(eaForm.getObservations().getIssueDate());
					
				}				
			}
			eaForm.getObservations().setIssue(null);
			eaForm.getObservations().setIssueDate(null);
			logger.debug("returning observationsAdded");
		}
		return mapping.findForward("forward");
	}
}