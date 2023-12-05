/*
 * ShowAddTeamMember.java 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.util.AmpUserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class ShowAddTeamMember extends Action {
    
    private static Logger logger = Logger.getLogger(ShowAddTeamMember.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
        
        TeamMemberForm upMemForm = (TeamMemberForm) form;
        String reset = request.getParameter("reset");
        if(reset!=null){
            upMemForm.setFullname(null);
        }

        String teamId = request.getParameter("teamId");
        String fromPage = request.getParameter("fromPage");
        
        if ((teamId != null || upMemForm.getTeamId()!=null) && fromPage != null) {
            try {
                Long id = upMemForm.getTeamId()!=null? upMemForm.getTeamId(): new Long(Long.parseLong(teamId)); 
                int frmPage = Integer.parseInt(fromPage);
                AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
                upMemForm.setTeamId(id);
                upMemForm.setTeamName(ampTeam.getName());
                upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                upMemForm.setPermissions("default");
                upMemForm.setFromPage(frmPage);
                Collection<User> allUsers= AmpUserUtil.getAllUsersNotBelongingToTeam(id,upMemForm.getFullname()); //get only the users not belonging to current team
                upMemForm.setallUser(allUsers);
                
                AmpTeamMember teamLeader=TeamMemberUtil.getTeamHead(id);
                upMemForm.setTeamHead(teamLeader);
                //workspace Manager allowed or not
                if(teamLeader==null){
                    upMemForm.setTeamLeaderExists("not exists");
                }else{
                    upMemForm.setTeamLeaderExists("exists");
                }
                //team head role ID
                for (AmpTeamMemberRoles role : (Collection<AmpTeamMemberRoles>)upMemForm.getAmpRoles()) {
                    if(role.getTeamHead()!=null && role.getTeamHead()){
                        upMemForm.setWorkspaceManagerRoleId(role.getAmpTeamMemRoleId());
                        break;
                    }
                }
                
                if (upMemForm.getSelectedRow() != null) {   
                    String redirectWhere=(String)request.getSession().getAttribute("redirectTo");
                    if(redirectWhere!=null){
                        request.getSession().removeAttribute(redirectWhere);
                        return mapping.findForward(redirectWhere);
                    }else{
                        return mapping.findForward("showAddFromAdmin");
                    }   
                } else {
                    return mapping.findForward("showAddFromTeam");
                }
            } catch (NumberFormatException nfe) {
                logger.error("NumberFormatException from ShowAddTeamMember action");
                logger.error("Trying to parse " + teamId + " to Long and " +fromPage + "to int");
            }
        }
        return mapping.findForward("index");
    }
}
