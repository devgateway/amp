package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
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
        ActionMessages errors = new ActionMessages();

        UserDetailForm formBean = (UserDetailForm) form;
        HttpSession httpSession = request.getSession();
        
        User user = null;
        Long userId = null;
        String email = "";
        
        TeamMember teamMember = (TeamMember) httpSession.getAttribute("currentMember");       
        AmpTeamMember member = null;
        List<TeamMember> memberInformationn = new ArrayList<TeamMember>();
        
        if (StringUtils.isNotEmpty(request.getParameter("id"))) {
            userId = Long.parseLong(request.getParameter("id"));
        } else if (StringUtils.isNotEmpty(request.getParameter("email"))) {
            email = request.getParameter("email");
            user = UserUtils.getUserByEmailAddress(email);
            if (user == null || user.getId() == null) {
                 errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidUserIdLogs"));
                 saveErrors(request, errors);
                 return mapping.findForward("forward");
            } else {
                userId = user.getId();
            }
        }

        if (httpSession.getAttribute("ampAdmin") == null || httpSession.getAttribute("ampAdmin").equals("no")) {
            if (user != null) {
                member = TeamMemberUtil.getAmpTeamMember(user);
            } else {
                member = TeamMemberUtil.getAmpTeamMemberByUserId(userId);
            }
            
            if (member == null) {
                if(userId != null) {
                    if (userId.equals(teamMember.getMemberId())) {
                        user = DbUtil.getUser(teamMember.getMemberId());
                        memberInformationn.add(new TeamMember(teamMember.getTeamName(), teamMember.getRoleName()));
                    } else {
                        user = DbUtil.getUser(userId);
                        if (user == null) {
                            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidUserId"));
                            saveErrors(request, errors);
                            mapping.findForward("forward");
                        }
                    }
                }
            } else {
                user = DbUtil.getUser(member.getUser().getId());    
            }
        } else {
            user = DbUtil.getUser(userId);
        }
        

        if (user != null) {
            if (memberInformationn.size() == 0) {
                memberInformationn = TeamMemberUtil.getMemberInformation(user.getId());
            }
            
            formBean.setAddress(user.getAddress());
            formBean.setFirstNames(user.getFirstNames());
            formBean.setLastName(user.getLastName());
            formBean.setMailingAddress(user.getEmail());
            formBean.setOrganizationName(user.getOrganizationName());
            formBean.setTeamMemberTeamHelpers(memberInformationn);
        }

        return mapping.findForward("forward");
    }
}
