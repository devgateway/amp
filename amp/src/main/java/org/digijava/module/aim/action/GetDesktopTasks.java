package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;

public class GetDesktopTasks extends TilesAction {

    private static Logger logger = Logger.getLogger(GetDesktopTasks.class);

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        if (session.getAttribute(Constants.MY_TASKS) == null) {
            TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
            if (tm != null && tm.getTeamHead() == true) {
                Collection myTaskColl = DbUtil.getCreatedOrEditedActivities(tm.getTeamId());
                session.setAttribute(Constants.MY_TASKS,myTaskColl);                
            }
        }
        
        if(session.getAttribute(Constants.MY_MESSAGES) == null)
        {
            TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
            if(tm != null && tm.getTeamHead() == true)
            {
                Collection myMessagesCol = DesktopUtil.getActivitiesTobeClosed(tm.getTeamId());
                session.setAttribute(Constants.MY_MESSAGES,myMessagesCol);
            }
        }
        return null;
    }
}
