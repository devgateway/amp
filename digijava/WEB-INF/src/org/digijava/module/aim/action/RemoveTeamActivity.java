package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.TeamActivitiesForm;
import javax.servlet.http.*;

public class RemoveTeamActivity extends Action {

		  private static Logger logger = Logger.getLogger(AssignActivity.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 TeamActivitiesForm taForm = (TeamActivitiesForm) form;

					 if (request.getParameter("id") != null) {
								/* load the current activity values to form bean */
								Long id = new Long(Long.parseLong(request.getParameter("id")));
								AmpActivity activity = DbUtil.getProjectChannelOverview(id);
								taForm.setActivityId(activity.getAmpActivityId());
								AmpTeam team = activity.getTeam();
								taForm.setTeamName(team.getName());
								taForm.setTeamId(team.getAmpTeamId());
								taForm.setActivityName(activity.getName());
								return mapping.findForward("showRemoveActivity");
					 } else if (taForm.getActivityId() != null){
								/* remove the association between team and activity */
								AmpActivity activity = DbUtil.getProjectChannelOverview(taForm.getActivityId());
								activity.setTeam(null);
								DbUtil.update(activity);
								request.setAttribute("teamId",taForm.getTeamId());
								return mapping.findForward("forward");
					 } else {
								return mapping.findForward(null);
					 }
					 	
		  }
}


