/*
 * UpdateWorkspace.java
 */

package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

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
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

public class UpdateWorkspace extends Action {

	private static Logger logger = Logger.getLogger(UpdateWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

            boolean permitted = false;
            HttpSession session = request.getSession();
            if(session.getAttribute("ampAdmin") != null) {
                String key = (String) session.getAttribute("ampAdmin");
                if(key.equalsIgnoreCase("yes")) {
                    permitted = true;
                } else {
                    if(session.getAttribute("teamLeadFlag") != null) {
                        key = (String) session.getAttribute("teamLeadFlag");
                        if(key.equalsIgnoreCase("true")) {
                            permitted = true;
                        }
                    }
                }
            }
            if(!permitted) {
                return mapping.findForward("index");
            }
            if(session.getAttribute("ampWorkspaces") != null) {
                session.removeAttribute("ampWorkspaces");
            }

            UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;

            String event = request.getParameter("event");
            String dest = request.getParameter("dest");
            String tId1 = request.getParameter("tId");
            //System.out.println("Am primit parametrul " + tId1 +" *****************************8");
            logger.debug("event : " + event + " dest : " + dest);

            AmpCategoryValue typeCategoryValue	= null;
            String typeString					= null;
            if (uwForm.getTypeId() != null && uwForm.getTypeId().longValue() > 0) {
            	typeCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getTypeId());
            	typeString			= typeCategoryValue.getValue();
            }

            // Mapping regular DONOR team with a regular MOFEDd team of same
            // type (bilat/multilat)
            if("yes".equalsIgnoreCase(uwForm.getRelatedTeamFlag())) {
                Collection col = TeamUtil.getAllRelatedTeams();
                if(col.size() < 1)
                    uwForm.setRelatedTeamFlag("nil");
                else {
                    Iterator itr = col.iterator();
                    while(itr.hasNext()) {
                        AmpTeam team 			= (AmpTeam) itr.next();
                        String teamTypeString	= null;

                        if (team.getType() != null) {
                        	teamTypeString	= team.getType().getValue();
                        }

                        if( Constants.TEAM_TYPE_BILATERAL.equals(teamTypeString) )
                            uwForm.getRelatedTeamBilatColl().add(team);
                        else if( Constants.TEAM_TYPE_MULTILATERAL.equals(teamTypeString) )
                            uwForm.getRelatedTeamMutilatColl().add(team);
                    }
                    uwForm.setRelatedTeamBilatCollSize(new Integer(uwForm
                        .getRelatedTeamBilatColl().size()));
                    uwForm.setRelatedTeamFlag("set");
                    return mapping.findForward("admin");
                }
            }

            ActionErrors errors = new ActionErrors();
            AmpTeam newTeam = null;

            // Checking for type mismatch between selected team-type & selected
            // related-team
            // e.g. if selected team is of bilateral type then related team
            // should be chosen
            // from bilateral teams only.
            if("set".equalsIgnoreCase(uwForm.getRelatedTeamFlag())) {
                if("DONOR".equalsIgnoreCase(uwForm.getCategory())
                   && "Donor".equalsIgnoreCase(uwForm.getWorkspaceType())) {
                    String type = typeString;
                    Iterator itr1 = uwForm.getRelatedTeamBilatColl().iterator();
                    Iterator itr2 = uwForm.getRelatedTeamMutilatColl()
                        .iterator();
                    while(itr1.hasNext()) {
                        newTeam = (AmpTeam) itr1.next();
                        if("Multilateral".equals(type)
                           && newTeam.getAmpTeamId().equals(
                               uwForm.getRelatedTeam())) {
                            errors
                                .add(
                                    ActionErrors.GLOBAL_ERROR,
                                    new ActionError(
                                        "error.aim.updateWorkspace.multilatTeamSelected"));
                            break;
                        }
                    } while(itr2.hasNext()) {
                        newTeam = (AmpTeam) itr2.next();
                        if("Bilateral".equals(type)
                           && newTeam.getAmpTeamId().equals(
                               uwForm.getRelatedTeam())) {
                            errors
                                .add(
                                    ActionErrors.GLOBAL_ERROR,
                                    new ActionError(
                                        "error.aim.updateWorkspace.bilatTeamSelected"));
                            break;
                        }
                    }
                    if(null != errors && errors.size() > 1) {
                        saveErrors(request, errors);
                        logger
                            .debug(
                            "Type mismatch between selected team-type & selected related-team");
                        return mapping.getInputForward();
                    }
                }
            }
            // end Checking for type mismatch

            newTeam = null;
            if(uwForm.getTeamName() != null) {
                newTeam = new AmpTeam();
                newTeam.setName(uwForm.getTeamName());
                newTeam.setTeamCategory(uwForm.getCategory());
                newTeam.setAccessType(uwForm.getWorkspaceType());
                newTeam.setType(typeCategoryValue);
                if(uwForm.getOrganizations()!=null) 
                	{
                	TreeSet s=new TreeSet();
                	s.addAll(uwForm.getOrganizations());
                	newTeam.setOrganizations(s);
                	}
                if(null == uwForm.getRelatedTeam()
                   || "-1".equals(uwForm.getRelatedTeam().toString()
                                  .trim()))
                    newTeam.setRelatedTeamId(null);
                else
                    newTeam.setRelatedTeamId(TeamUtil.getAmpTeam(uwForm
                        .getRelatedTeam()));
                if(uwForm.getDescription() != null
                   && uwForm.getDescription().trim().length() > 0) {
                    newTeam.setDescription(uwForm.getDescription());
                } else {
                    newTeam.setDescription(" ");
                }
            }

            if(event != null && event.trim().equalsIgnoreCase("reset")) {
        		uwForm.setPopupReset(false);
                uwForm.setTeamName("");
                uwForm.setCategory("");
                uwForm.setTypeId(new Long(0));
                uwForm.setDescription("");
                uwForm.setWorkspaceType("");
                uwForm.setRelatedTeamFlag("no");
                uwForm.setRelatedTeamName("");
                if (uwForm.getChildWorkspaces() != null)
                	uwForm.getChildWorkspaces().clear();
                return mapping.findForward("admin");
            }
            else
            if(event != null && event.trim().equalsIgnoreCase("add")) {
                uwForm.setActionEvent("add");
                if(newTeam != null) {
                	if(uwForm.getChildWorkspaces()==null && uwForm.getWorkspaceType().compareTo("Management")==0)
                    {
                 	   errors
                        .add(
                            ActionErrors.GLOBAL_ERROR,
                            new ActionError(
                                "error.aim.updateWorkspace.noManagementChildSelected"));
                    saveErrors(request, errors);
                    logger
                        .debug(
                        "error.aim.updateWorkspace.noManagementChildSelected !!!!!");
                    return mapping.getInputForward();
                    }

                    boolean teamExist = TeamUtil.createTeam(newTeam, uwForm
                        .getChildWorkspaces());
                    if(teamExist) {
                        errors
                            .add(
                                ActionErrors.GLOBAL_ERROR,
                                new ActionError(
                                    "error.aim.updateWorkspace.teamNameAlreadyExist"));
                        saveErrors(request, errors);
                        logger
                            .debug(
                            "Team name already exist. Error message saved to request");
                        return mapping.getInputForward();
                    }
                }
            } else if(event != null && event.trim().equalsIgnoreCase("edit")) {
                uwForm.setActionEvent("edit");
                if(newTeam != null) {
                	if(uwForm.getChildWorkspaces().size()==0 && uwForm.getWorkspaceType().compareTo("Management")==0)
                    {
                 		errors
                        .add(
                            ActionErrors.GLOBAL_ERROR,
                            new ActionError(
                                "error.aim.updateWorkspace.noManagementChildSelected"));
                    saveErrors(request, errors);
                    logger
                        .debug(
                        "error.aim.updateWorkspace.noManagementChildSelected !!!!!");
                    return mapping.getInputForward();
                    }
                	if (tId1==null)	newTeam.setAmpTeamId(uwForm.getTeamId());
                	else newTeam.setAmpTeamId(new Long(Long.parseLong(tId1)));
                    if((newTeam.getAccessType().equalsIgnoreCase("Team")||newTeam.getAccessType().equalsIgnoreCase("Donor"))
                       && (uwForm.getChildWorkspaces() != null && uwForm
                           .getChildWorkspaces().size() > 0)) {
                        errors
                            .add(
                                ActionErrors.GLOBAL_ERROR,
                                new ActionError(
                                    "error.aim.updateWorkspace.childTeamsExistForTeam"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                   
                    
                    if(uwForm.getOrganizations()!=null) 
                	{
                	TreeSet s=new TreeSet();
                	s.addAll(uwForm.getOrganizations());
                	newTeam.setOrganizations(s);
                	}
        
                    boolean teamExist = TeamUtil.updateTeam(newTeam, uwForm
                        .getChildWorkspaces());
                    if(teamExist) {
                        errors
                            .add(
                                ActionErrors.GLOBAL_ERROR,
                                new ActionError(
                                    "error.aim.updateWorkspace.teamNameAlreadyExist"));
                        saveErrors(request, errors);
                        logger
                            .debug(
                            "Team name already exist. Error message saved to request");
                        return mapping.getInputForward();
                    } else {
                        uwForm.setUpdateFlag(true);
                    }
                    TeamMember tm = (TeamMember) session
                        .getAttribute("currentMember");
                    if(tm != null) {
                        if(tm.getTeamId() != null) {
                            session.removeAttribute("currentMember");
                            tm.setTeamName(newTeam.getName());
                            session.setAttribute("currentMember", tm);
                            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
                        }
                    }return mapping.findForward("forward");
                }
            } else if(event != null && event.trim().equalsIgnoreCase("delete")) {
                String tId = request.getParameter("tId");
                Long teamId = new Long(Long.parseLong(tId));
                boolean memExist = TeamUtil.membersExist(teamId);
                if(!memExist) {
                    TeamUtil.removeTeam(teamId);
                } else {
                    errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                        "error.aim.membersExistForTeam"));
                    saveErrors(request, errors);
                }
                return mapping.findForward("forward");
            }


            uwForm.setReset(true);
            uwForm.reset(mapping, request);

            return mapping.findForward("forward");
        }
}
