/*
 * AddChildWorkspaces.java @Author Priyajith C Created: 07-Apr-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;

public class AddChildWorkspaces extends Action {
	
	private static Logger logger = Logger.getLogger(AddChildWorkspaces.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
		uwForm.setReset(false);
		
		String dest = request.getParameter("dest");
		
		if (uwForm.getSelChildWorkspaces() != null && uwForm.getSelChildWorkspaces().length > 0) {
			logger.info("Selchildworkspaces.length :" + uwForm.getSelChildWorkspaces().length);
			Collection teams = TeamUtil.getAllTeams(uwForm.getSelChildWorkspaces());
			if (uwForm.getChildWorkspaces() == null) {
				logger.debug("childWorkspace is null");
				uwForm.setChildWorkspaces(new ArrayList());
			} else {
				logger.debug("Child workspaces size = " + uwForm.getChildWorkspaces().size());
			}
			
			ArrayList temp = new ArrayList(teams);
			for (int i= 0;i < temp.size();i ++) {
				Iterator itr1 = uwForm.getChildWorkspaces().iterator();
				boolean flag = false;
				AmpTeam	childTeam = (AmpTeam) temp.get(i);			
				while (itr1.hasNext()) {
					AmpTeam team = (AmpTeam) itr1.next();
					if (team.getAmpTeamId().equals(childTeam.getAmpTeamId())) {
						flag = true;
						break;
					}
				}
				if (!flag && childTeam != null) {
					uwForm.getChildWorkspaces().add(childTeam);
				}
			}		
		}
		uwForm.setSelChildWorkspaces(null);
		return mapping.findForward(dest);
	}
}
