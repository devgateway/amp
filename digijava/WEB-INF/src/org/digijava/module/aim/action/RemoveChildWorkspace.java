/*
 * RemoveChildWorkspace.java @Author Priyajith C Created: 07-Apr-2005
 */

package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.TreeSet;

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

public class RemoveChildWorkspace extends Action {
	
	private static Logger logger = Logger.getLogger(RemoveChildWorkspace.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
		
		String dest = request.getParameter("dest");
		logger.debug("In Remove child workspace");
		String id = request.getParameter("tId");
		if (id != null && id.trim().length() > 0) {
			long temp = Long.parseLong(id);
			Long teamId = new Long(temp);
			AmpTeam tempTeam = new AmpTeam();
			AmpTeam tempTeam1 = new AmpTeam();
			tempTeam.setAmpTeamId(teamId);
			tempTeam1=TeamUtil.getAmpTeam(teamId);
			if (uwForm.getChildWorkspaces() != null) {
				uwForm.getChildWorkspaces().remove(tempTeam);
				Collection col=new TreeSet();
				col.add(tempTeam1);
				uwForm.setAvailChildWorkspaces(col);
				logger.debug("Child workspace removed!");
			}
		}
		
		return mapping.findForward(dest);
	}
}
