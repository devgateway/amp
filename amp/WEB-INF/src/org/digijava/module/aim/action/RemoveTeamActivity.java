package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.util.TeamUtil;

public class RemoveTeamActivity extends Action {

    private static Logger logger = Logger.getLogger(AssignActivity.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;
        
        if (taForm.getSelActivities() != null) {
            TeamUtil.removeActivitiesFromTeam(taForm.getSelActivities(),taForm.getTeamId());
        }
        
        return mapping.findForward("forward");
    }
}
