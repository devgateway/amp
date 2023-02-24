package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.AssignActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class AssignActivity extends Action {

    private static Logger logger = Logger.getLogger(AssignActivity.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        AssignActivityForm aForm = (AssignActivityForm) form;

        if (request.getParameter("id") != null) {
            /* load the values */
            Long id = new Long(Long.parseLong(request.getParameter("id")));
            AmpTeam ampTeam = TeamUtil.getAmpTeam(id);

            Collection col = TeamUtil.getAllUnassignedActivities();
            aForm.setActivities(col);
            aForm.setTeamName(ampTeam.getName());
            aForm.setTeamId(id);
            return mapping.findForward("showAssignActivity");
        } else if (aForm.getTeamId() != null) {
            /* assign the values selected */
            Long selActivities[] = aForm.getSelectedActivities();
            for (int i = 0; i < selActivities.length; i++) {
                if (selActivities[i] != null) {
                    Long actId = selActivities[i];
                    AmpActivityVersion activity = ActivityUtil.loadAmpActivity(actId);
                    AmpTeam ampTeam = TeamUtil.getAmpTeam(aForm.getTeamId());
                    activity.setTeam(ampTeam);
                    DbUtil.update(activity);
                    String detail="assigned to team";
                    List<String> details=new ArrayList<String>();
                    details.add(detail);
                    AuditLoggerUtil.logActivityUpdate(request, activity, details);
                //UpdateDB.updateReportCache(actId);
                }
            }
            request.setAttribute("teamId", aForm.getTeamId());
            
            return mapping.findForward("forward");
        } else {
            return mapping.findForward(null);
        }

    }
}

