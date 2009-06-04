/*
 * RemoveIssue.java
 * Created : 05-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

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
		if (request.getParameter("issues.selIssue") != null) {
			Long idIssue = Long.parseLong(request.getParameter("issues.selIssue"));
			
			ArrayList<Issues> issues = eaForm.getIssues().getIssues();
	
			for (Issues issue : issues) {
				if(issue.getId().equals(idIssue)){
					issues.remove(issue);
					break;
				}
			}
			
			eaForm.getIssues().setIssueId(null);;
		}
		return mapping.findForward("forward");
	}
}