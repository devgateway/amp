/*
 * ShowUpdateIssue.java
 * Created : 06-Sep-2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;

public class ShowUpdateIssue extends Action {

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		if (eaForm.getIssues().getIssueId() != null 
				&& eaForm.getIssues().getIssueId().longValue() > 0) {
			Issues issue = new Issues();
			issue.setId(eaForm.getIssues().getIssueId());
			int index = eaForm.getIssues().getIssues().indexOf(issue);
			issue = (Issues) eaForm.getIssues().getIssues().get(index);
			eaForm.getIssues().setIssue(issue.getName());
			eaForm.getIssues().setIssueDate(issue.getIssueDate());
		}  else {
			eaForm.getIssues().setIssue(null);
		}
		return mapping.findForward("forward");
	}
}