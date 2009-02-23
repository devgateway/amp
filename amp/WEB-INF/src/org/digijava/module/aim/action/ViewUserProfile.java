package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UserDetailForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class ViewUserProfile
        extends Action {

    private static Logger logger = Logger.getLogger(ViewUserProfile.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        ActionErrors errors = new ActionErrors();

        UserDetailForm formBean = (UserDetailForm) form;
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute(
                "currentMember");
        User user = null;
        Long memId = null;
        String email = "";
        AmpTeamMember member = null;
        if (request.getParameter("id") != null) {
            long id = Long.parseLong(request.getParameter("id"));
            memId = new Long(id);
        }
        if (request.getParameter("email") != null && request.getParameter("email") != "") {
            email = request.getParameter("email");
            memId = DbUtil.getUser(email).getId();
        }

        String[] memberInformationn = null;
        member = TeamMemberUtil.getAmpTeamMember(memId);
        if (member == null && request.getParameter("id") != null) {
            if (memId.equals(teamMember.getMemberId())) {
                user = DbUtil.getUser(teamMember.getMemberId());
                memberInformationn = new String[]{teamMember.getTeamName(), teamMember.getRoleName()};
            } else {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.invalidUserId"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        } else if (memId != null) {
        	user = DbUtil.getUser(memId); 
            if(user!=null) 
            	memberInformationn = TeamMemberUtil.getMemberInformation(user.getId());
        }


        formBean.setAddress(user.getAddress());
        formBean.setFirstNames(user.getFirstNames());
        formBean.setLastName(user.getLastName());
        formBean.setMailingAddress(user.getEmail());
        formBean.setOrganizationName(user.getOrganizationName());
        formBean.setInfo(memberInformationn);

        return mapping.findForward("forward");
    }
}
