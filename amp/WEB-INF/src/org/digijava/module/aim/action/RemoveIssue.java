/*
 * RemoveIssue.java
 * Created : 05-Sep-2005
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

public class RemoveIssue extends Action {
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (eaForm.getIssues().getSelIssues() != null && 
				eaForm.getIssues().getSelIssues().length > 0) {
			Long issues[] = eaForm.getIssues().getSelIssues();
			Issues issue = new Issues();
			for (int i = 0;i < issues.length; i++) {
				issue.setId(issues[i]);
				eaForm.getIssues().getIssues().remove(issue);
			}
			eaForm.getIssues().setSelIssues(null);
		}
		return mapping.findForward("forward");
	}
}