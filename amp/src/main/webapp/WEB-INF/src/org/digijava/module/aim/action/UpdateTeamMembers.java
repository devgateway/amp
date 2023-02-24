package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.entity.geocoding.GeoCodingProcess;
import org.digijava.kernel.geocoding.service.GeoCodingService;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.CrConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UpdateTeamMembers extends Action {

    private static Logger logger = Logger.getLogger(UpdateTeamMembers.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        logger.debug("In update teammembers");
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }

        TeamMemberForm upForm = (TeamMemberForm) form;
        ActionMessages errors = new ActionMessages();
        AmpTeam ampTeam = TeamUtil.getAmpTeam(upForm.getTeamId());
        if (upForm.getAction() != null
            && upForm.getAction().trim().equals("edit")) {
            logger.debug("In edit team member");
            AmpTeamMember ampMember = null;

            if (upForm.getTeamMemberId() != null) {
                ampMember = TeamUtil.getAmpTeamMember(upForm.getTeamMemberId());
            } else {
                ampMember = new AmpTeamMember();
            }

            //ampMember.setAmpTeamMemId(upForm.getTeamMemberId());
            AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upForm.getRole());
            AmpTeamMemberRoles teamLead = TeamMemberUtil.getAmpTeamHeadRole();
            if (role.getRole().equals(teamLead.getRole())) {
                ActionMessages error = new ActionMessages();
                error.add(ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage(
                              "error.aim.addTeamMember.teamLeadRole"));
            }
            if (role.getRole().equals(teamLead.getRole())) {
                logger.info("team name = " + ampTeam.getName());
                logger.info(" team role = " + upForm.getRole());
                logger.info(" this is the team Id = " + upForm.getTeamId() + " this is the member id = " + upForm.getTeamMemberId());
                if ( (ampTeam.getTeamLead() != null) && (!ampTeam.getTeamLead().getAmpTeamMemId().equals(upForm.getTeamMemberId()))) {
                    upForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());

                    logger.info(" here");
                    String trnKey = "aim:teamLeadAlreadyExist";
                    String msg = CategoryManagerUtil.translate(trnKey, Constants.TEAM_LEAD_ALREADY_EXISTS);
                    errors.add(
                        ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage(
                            "error.aim.addTeamMember.teamLeadAlreadyExist", msg));
                    saveErrors(request, errors);

                    return mapping.findForward("forward");
                    //return mapping.getInputForward();

                }

            }
            logger.info(" this is the role.... " + role.getRole());
            ampMember.setAmpMemberRole(role);
            //publishing permissions
            AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeam.getAmpTeamId());
            Integer docPublishingPermissions=ampAppSettings.getAllowPublishingResources();
            if(docPublishingPermissions!=null){
                if(docPublishingPermissions.equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM ) || 
                        (docPublishingPermissions.equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL) && (role.getTeamHead()!=null && role.getTeamHead())) ||
                        (docPublishingPermissions.equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS) && ampMember.getPublishDocPermission()!=null && ampMember.getPublishDocPermission())){
                    ampMember.setPublishDocPermission(true);
                }else{
                    ampMember.setPublishDocPermission(false);
                }
            }           
            
            
            ampMember.setUser(UserUtils.getUser(upForm.getUserId()));
            ampMember.setAmpTeam(ampTeam);
            Collection col = TeamMemberUtil.getAllMemberAmpActivities(upForm.getTeamMemberId());
            Set temp = new HashSet();
            temp.addAll(col);
            ampMember.setActivities(temp);
            logger.info(" updating the team member now....");
            DbUtil.update(ampMember);
            AmpTeamMember ampTeamHead = TeamMemberUtil.getTeamHead(ampTeam.getAmpTeamId());
            logger.info(" here finally");

            if (ampTeam == null) {
                logger.debug("ampTeam is null");
            }

            if (ampTeamHead != null) {
                ampTeam.setTeamLead(ampTeamHead);
            } else {
                ampTeam.setTeamLead(null);
            }
            DbUtil.update(ampTeam);

            if (ampTeam != null) {
                request.setAttribute("teamId", ampTeam.getAmpTeamId());
                return mapping.findForward("forward");
            }
        } else if (upForm.getAction() != null
                   && upForm.getAction().trim().equals("delete")) {

            logger.debug("In delete team member");

            GeoCodingProcess geoCodingProcess = new GeoCodingService().getCurrentGeoCoding();
            if (geoCodingProcess != null
                    && geoCodingProcess.getTeamMember().getAmpTeamMemId().equals(upForm.getTeamMemberId())) {
                upForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                upForm.setTeamName(ampTeam.getName());
                errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.geocodingExistForTeam",
                        geoCodingProcess.getTeamMember().getAmpTeam().getName()));
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }

            Long selMembers[] = new Long[1];
            selMembers[0] = upForm.getTeamMemberId();
            TeamMemberUtil.removeTeamMembers(selMembers);

            if (ampTeam != null) {
                request.setAttribute("teamId", ampTeam.getAmpTeamId());
                return mapping.findForward("forward");
            }
        }
        return mapping.findForward("forward");
    }
}
