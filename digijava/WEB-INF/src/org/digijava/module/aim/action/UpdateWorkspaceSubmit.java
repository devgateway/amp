/*
 * UpdateWorkspaceSubmit.java
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateWorkspaceForm;

public class UpdateWorkspaceSubmit extends Action {
	
	private static Logger logger = Logger.getLogger(UpdateWorkspaceSubmit.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
	
		uwForm.setReset(false);

		logger.info("Main Action :" + uwForm.getMainAction());
		if (uwForm.getMainAction().equalsIgnoreCase("addChild")) {
			return mapping.findForward("addChild");		  
		} else if (uwForm.getMainAction().equalsIgnoreCase("removeChild")) {
			return mapping.findForward("removeChild");
		} else {
			return mapping.findForward("updateWorkspace");		  
		}
	}
}

