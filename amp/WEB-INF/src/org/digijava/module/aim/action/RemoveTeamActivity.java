package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RemoveTeamActivity extends Action {

    private static Logger logger = Logger.getLogger(AssignActivity.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;
        
        if (taForm.getSelActivities() != null) {
            TeamUtil.removeActivitiesFromTeam(taForm.getSelActivities(),taForm.getTeamId());
            Long selActivities[] = taForm.getSelActivities();
            for(Long selActivityId:selActivities){
                AmpActivityVersion activity=ActivityUtil.loadAmpActivity(selActivityId);
                String detail="unassigned from team";
                List<String> details=new ArrayList<String>();
                details.add(detail);
                AuditLoggerUtil.logActivityUpdate(request, activity, details);
            }
        }
        
        return mapping.findForward("forward");
    }
}
