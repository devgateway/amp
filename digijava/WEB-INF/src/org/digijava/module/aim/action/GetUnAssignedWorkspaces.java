/*
 * GetUnAssignedWorkspaces.java @Author Priyajith C Created: 07-Apr-2005
 * Modified by Akashs 22-Feb-2006
 */

package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;

/**
 * The action class will retreive all workspaces which is not associated with
 * any parent team. It also filters the workspaces based on the 'Workspace Type' 
 * & 'Team Category'
 */
public class GetUnAssignedWorkspaces extends Action {
	
	private static Logger logger = Logger.getLogger(GetUnAssignedWorkspaces.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		if (null != form) {
			
			UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
			
			String dest = request.getParameter("dest");
			String workspaceType = request.getParameter("wType");
			String teamCategory  = request.getParameter("tCategory");
			String team = uwForm.getCategory();
			
			if ((workspaceType == null || workspaceType.trim().length() == 0) 
					&& (teamCategory == null || teamCategory.trim().length() == 0)) {
				uwForm.setChildWorkspaceType(null);
				uwForm.setChildTeamCategory(null);
			}
			
			Collection col = TeamUtil.getUnassignedWorkspaces(workspaceType, teamCategory, team);
			logger.debug("Unassigned workspaces retreived, size = " + col.size());
			
			uwForm.setAvailChildWorkspaces(col);
			uwForm.setReset(false);
			//uwForm.setActionEvent(dest);
			uwForm.setDest(dest);

			return mapping.findForward("forward");
		}
		
		return null;
	}
}
