/*
 * AddTeamMember.java
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
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
import org.digijava.module.contentrepository.helper.CrConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Adds a member to a team
 * 
 * @author priyajith
 */
public class AddTeamMember extends Action {

    private static Logger logger = Logger.getLogger(AddTeamMember.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        TeamMemberForm upMemForm = (TeamMemberForm) form;
        ActionMessages errors = new ActionMessages();
        logger.debug("In add members");
        Collection<AmpTeamMemberRoles> roles=null;
        
        AmpTeam ampTeam = TeamUtil.getAmpTeam(upMemForm.getTeamId());
        User user = UserUtils.getUserByEmailAddress(upMemForm.getEmail());

        /* check if the user have entered an invalid user id */
        if (user == null) {
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT)) {
                roles=TeamMemberUtil.getAllTeamMemberRoles(false);
            } else {
                roles = TeamMemberUtil.getAllTeamMemberRoles();
            }
            upMemForm.setAmpRoles(roles);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.aim.addTeamMember.invalidUser"));
            saveErrors(request, errors);
            if (upMemForm.getFromPage() == 1) {
                return mapping.findForward("showAddFromAdmin"); 
            } else {
                return mapping.findForward("showAddFromTeam");  
            }
        }

        /* if user havent specified the role for the new member */
        if (upMemForm.getRole() == null) {
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT)) {
                roles=TeamMemberUtil.getAllTeamMemberRoles(false);
            } else {
                roles=TeamMemberUtil.getAllTeamMemberRoles();
            }
            upMemForm.setAmpRoles(roles);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.aim.addTeamMember.roleNotSelected"));
            saveErrors(request, errors);
            if (upMemForm.getFromPage() == 1) {
                return mapping.findForward("showAddFromAdmin"); 
            } else {
                return mapping.findForward("showAddFromTeam");  
            }           
        }
        
        /* check if user have selected role as Team Lead when a team lead 
         * already exist for the team */
        if (ampTeam.getTeamLead() != null &&
                ampTeam.getTeamLead().getAmpMemberRole().getAmpTeamMemRoleId().equals(upMemForm.getRole())) {
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT)){
                roles=TeamMemberUtil.getAllTeamMemberRoles(false);
            }
            else {
                roles=TeamMemberUtil.getAllTeamMemberRoles();
            }
            upMemForm.setAmpRoles(roles);
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(
                    "error.aim.addTeamMember.teamLeadAlreadyExist"));
            saveErrors(request, errors);
            if (upMemForm.getFromPage() == 1) {
                return mapping.findForward("showAddFromAdmin"); 
            } else {
                return mapping.findForward("showAddFromTeam");  
            }                       
        }
        
        /* check if user is already part of the selected team */
        if (TeamUtil.isMemberExisting(upMemForm.getTeamId(),upMemForm.getEmail())) {
            if(ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT)){
                roles=TeamMemberUtil.getAllTeamMemberRoles(false);
            }
            else{
                roles=TeamMemberUtil.getAllTeamMemberRoles();
            }
            upMemForm.setAmpRoles(roles);
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(
                    "error.aim.addTeamMember.teamMemberAlreadyExist"));
            saveErrors(request, errors);
            logger.debug("Member is already existing");
            if (upMemForm.getFromPage() == 1) {
                logger.debug("Forwarding to showAddFromAdmin");
                return mapping.findForward("showAddFromAdmin"); 
            } else {
                logger.debug("Forwarding to showAddFromTeam");
                return mapping.findForward("showAddFromTeam");  
            }               
        }
        /**
         *Incorrect checking, cos admin can have another emails !!!
         */ 
        /*check if user is not admin; as admin he can't be part of a workspace*/
//      if (upMemForm.getEmail().equalsIgnoreCase("admin@amp.org")) {
//          upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
//          errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.aim.addTeamMember.teamMemberIsAdmin"));
//          saveErrors(request, errors);
//          upMemForm.setSomeError(true);
//          logger.debug("Member is already existing");
//          if (upMemForm.getFromPage() == 1) {
//              logger.debug("Forwarding to showAddFromAdmin");
//              return mapping.findForward("showAddFromAdmin"); 
//          } else {
//              logger.debug("Forwarding to showAddFromTeam");
//              return mapping.findForward("showAddFromTeam");  
//          }
//      }
        Site site = RequestUtils.retreiveSiteDomain(request).getSite();        
        boolean siteAdmin = UserUtils.isAdmin(user, site);
        if(siteAdmin){ // should be impossible to ban admin
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamMemberIsAdmin"));
            saveErrors(request, errors);
            upMemForm.setSomeError(true);
            logger.debug("Member is Admin");
            if (upMemForm.getFromPage() == 1) {
                logger.debug("Forwarding to showAddFromAdmin");
                return mapping.findForward("showAddFromAdmin"); 
            } else {
                logger.debug("Forwarding to showAddFromTeam");
                return mapping.findForward("showAddFromTeam");  
            }
        }
        
        AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upMemForm.getRole());
        if (role != null) {
            AmpTeamMember newMember = new AmpTeamMember();
            newMember.setUser(user);
            newMember.setAmpTeam(ampTeam);
            newMember.setAmpMemberRole(role);
            
            // add the default application settings for the user  
            AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeam.getAmpTeamId());
            Integer docPublishingPermissions=ampAppSettings.getAllowPublishingResources();
            if(docPublishingPermissions!=null){
                if(docPublishingPermissions.equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM ) || 
                        (docPublishingPermissions.equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL) && (role.getTeamHead()!=null && role.getTeamHead()))){
                    newMember.setPublishDocPermission(true);
                }else{
                    newMember.setPublishDocPermission(false);
                }
            }           
            
//          AmpApplicationSettings newAppSettings = new AmpApplicationSettings();
//          //newAppSettings.setTeam(ampAppSettings.getTeam());
//          newAppSettings.setTeam(newMember.getAmpTeam());
//          newAppSettings.setMember(newMember);
//          newAppSettings.setDefaultRecordsPerPage(ampAppSettings.getDefaultRecordsPerPage());
//          newAppSettings.setCurrency(ampAppSettings.getCurrency());
//          newAppSettings.setFiscalCalendar(ampAppSettings.getFiscalCalendar());
//          newAppSettings.setLanguage(ampAppSettings.getLanguage());
//          newAppSettings.setUseDefault(new Boolean(true));
//          newAppSettings.setValidation(ampAppSettings.getValidation());
            try{
                TeamUtil.addTeamMember(newMember, site);
            }catch (Exception e){
                    e.printStackTrace();
                    logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "
                            + newMember.getAmpTeam().getName());
                }
            
            upMemForm.setEmail(null);
            upMemForm.setRole(null);
            upMemForm.setTeamName(null);
            upMemForm.setAmpRoles(null);
            upMemForm.setPermissions(null);
        }
        if (upMemForm.getFromPage() == 1) {
            return mapping.findForward("addedFromAdmin");
        } else {
            return mapping.findForward("addedFromTeam");    
        }
        
    }
}
