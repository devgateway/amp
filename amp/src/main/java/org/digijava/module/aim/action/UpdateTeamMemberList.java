package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateTeamMemberList extends Action {

    private static Logger logger = Logger.getLogger(UpdateTeamMemberList.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In update teammember list");
        TeamMemberForm tmForm = (TeamMemberForm) form;

        HttpSession session = request.getSession();
        if (session.getAttribute("currentMember") != null) {
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            if (tmForm.getAddMember() != null) {
                tmForm.setTeamId(tm.getTeamId());
                tmForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                tmForm.setPermissions("default");
                tmForm.setAction("teamWorkspaceSetup");
                return mapping.findForward("showAdd");
            } else if (tmForm.getRemoveMember() != null) {
                logger.debug("in remove members");
                Long selMembers[] = tmForm.getSelMembers();
                TeamMemberUtil.removeTeamMembers(selMembers);
                return mapping.findForward("forward");
            } else {
                return mapping.findForward(null);
            }
        } else {
            return mapping.findForward(null);
        }
    }
}
