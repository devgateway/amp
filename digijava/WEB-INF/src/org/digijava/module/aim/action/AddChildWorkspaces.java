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
		
		if (uwForm.getSelChildWorkspaces() != null && uwForm.getSelChildWorkspaces().length > 0) {
			logger.info("Selchildworkspaces.length :" + uwForm.getSelChildWorkspaces().length);
			Collection teams = TeamUtil.getAllTeams(uwForm.getSelChildWorkspaces());
			if (uwForm.getChildWorkspaces() == null) {
				uwForm.setChildWorkspaces(new ArrayList());
			}
			
			ArrayList temp = new ArrayList(teams);
			for (int i= 0;i < temp.size();i ++) {
				Iterator itr1 = uwForm.getChildWorkspaces().iterator();
				boolean flag = false;
				AmpTeam	childTeam = (AmpTeam) temp.get(i);			
				while (itr1.hasNext()) {
					AmpTeam team = (AmpTeam) itr1.next();
					logger.info("team.getAmpTeamId() = " + team.getAmpTeamId());
					logger.info("childTeam.getAmpTeamId() = " + childTeam.getAmpTeamId());
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
		logger.info("Action :" + uwForm.getActionEvent());
		return mapping.findForward("forward");
	}
}
