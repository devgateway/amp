package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.form.AddUserForm;
import org.digijava.module.um.util.UmUtil;

import java.util.ArrayList;
import java.util.Collection;

public class addWorkSpaceUser extends Action {
    private static Logger logger = Logger.getLogger(addWorkSpaceUser.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
        
        AddUserForm upMemForm = (AddUserForm) form;
        ActionMessages errors = new ActionMessages();
        logger.debug("In add members");
        
        String actionFlag = request.getParameter("actionFlag");
        logger.debug("actionFlag: " + actionFlag);
        if ("deleteWS".equals(actionFlag)) {
            logger.debug("In delete team member");
            Long selMembers[] = new Long[1];
            Long id = upMemForm.getTeamMemberId();
            selMembers[0] = id;
            TeamMemberUtil.removeTeamMembers(selMembers);
            Collection asWS = upMemForm.getAssignedWorkspaces();
            for (Object asW : asWS) {
                AmpTeamMember newMember = (AmpTeamMember) asW;
                if (newMember.getAmpTeamMemId().compareTo(id) == 0) {
                    asWS.remove(newMember);
                    break;
                }
            }
        } else {
            AmpTeam ampTeam = TeamUtil.getAmpTeam(upMemForm.getTeamId());
            User user = UserUtils.getUserByEmailAddress(upMemForm.getEmail());

            /* check if the user have entered an invalid user id */
            if (user == null) {
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.addTeamMember.invalidUser"));
                saveErrors(request, errors);
    
                return mapping.findForward("forward");  
            }
    
            /* if user havent specified the role for the new member */
            if (upMemForm.getRole() == null) {
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "error.aim.addTeamMember.roleNotSelected"));
                saveErrors(request, errors);
                return mapping.findForward("forward");          
            }
            
            /* check if user have selected role as Team Lead when a team lead 
             * already exist for the team */
            if (ampTeam.getTeamLead() != null &&
                    ampTeam.getTeamLead().getAmpMemberRole().getAmpTeamMemRoleId().equals(upMemForm.getRole())) {
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamLeadAlreadyExist"));
                saveErrors(request, errors);
                return mapping.findForward("forward");          
            }
            
            /* check if user is already part of the selected team */
            if (TeamUtil.isMemberExisting(upMemForm.getTeamId(),upMemForm.getEmail())) {
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamMemberAlreadyExist"));
                saveErrors(request, errors);
                logger.debug("Member is already existing");
                return mapping.findForward("forward");
            }
            /*check if user is not admin; as admin he can't be part of a workspace*/
            if (upMemForm.getEmail().equalsIgnoreCase("admin@amp.org")) {
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamMemberIsAdmin"));
                saveErrors(request, errors);
                logger.debug("Member is already existing");
                return mapping.findForward("forward");
            }
            
            addWorkSpace(UmUtil.assignWorkspaceToUser(request, upMemForm.getRole(), user, ampTeam), upMemForm);
            DgUtil.saveWorkspaceLanguagePreferences(request, ampTeam, user);
        }
        return mapping.findForward("forward");
    }

    private void addWorkSpace(AmpTeamMember newMember, AddUserForm upMemForm) {
        Collection assignedWS = upMemForm.getAssignedWorkspaces();
        if(assignedWS==null){
            ArrayList assWS = new ArrayList();
            upMemForm.setAssignedWorskpaces(assWS);
        }
        upMemForm.getAssignedWorkspaces().add(newMember);
        upMemForm.setTeamId(-1L);
        upMemForm.setRole(-1L);
    }   
}
