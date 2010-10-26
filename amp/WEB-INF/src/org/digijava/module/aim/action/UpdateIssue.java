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

public class UpdateIssue extends Action {
	private static Logger logger = Logger.getLogger(UpdateIssue.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (eaForm.getIssues().getIssue() != null &&
				eaForm.getIssues().getIssue().trim().length() > 0) {
			Issues issue = new Issues();
			if (eaForm.getIssues().getIssueId() == null || 
					eaForm.getIssues().getIssueId().longValue() < 0) {
				logger.debug("Setting the id from currentTimeMills()");
				issue.setId(new Long(System.currentTimeMillis()));					
			} else {
				logger.debug("Setting the id from the previous");
				issue.setId(eaForm.getIssues().getIssueId());
			}
			if (eaForm.getIssues().getIssues() == null) {
				issue.setName(eaForm.getIssues().getIssue());
				issue.setIssueDate(eaForm.getIssues().getIssueDate());
				if (eaForm.getIssues().getIssues() == null) {
					eaForm.getIssues().setIssues(new ArrayList());
				}
				eaForm.getIssues().getIssues().add(issue);								
			} else {
				int index = eaForm.getIssues().getIssues().indexOf(issue);
				if (index < 0) {
					issue.setName(eaForm.getIssues().getIssue());
					issue.setIssueDate(eaForm.getIssues().getIssueDate());
					if (eaForm.getIssues().getIssues() == null) {
						eaForm.getIssues().setIssues(new ArrayList());
					}
					eaForm.getIssues().getIssues().add(issue);				
				} else {
					issue = (Issues) eaForm.getIssues().getIssues().get(index);
					issue.setName(eaForm.getIssues().getIssue());
					issue.setIssueDate(eaForm.getIssues().getIssueDate());
					
				}				
			}
			eaForm.getIssues().setIssue(null);
			eaForm.getIssues().setIssueDate(null);
			logger.debug("returning issueAdded");
		}
		return mapping.findForward("forward");
	}
}