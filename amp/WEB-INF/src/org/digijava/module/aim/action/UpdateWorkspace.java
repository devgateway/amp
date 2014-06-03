/*
 * UpdateWorkspace.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

public class UpdateWorkspace extends Action {

	private static Logger logger = Logger.getLogger(UpdateWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		ReportContextData.createWithId(request.getSession(), ReportContextData.REPORT_ID_WORKSPACE_EDITOR, false);
		request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ReportContextData.REPORT_ID_WORKSPACE_EDITOR);
		
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
		if (session.getAttribute("ampWorkspaces") != null) {
			session.removeAttribute("ampWorkspaces");
		}

		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;

		String event = request.getParameter("event");
//		String dest = request.getParameter("dest");
		String tId1 = request.getParameter("tId");
		ActionMessages errors = new ActionMessages();
		if (uwForm.getWorkspaceType() != null
				&& "Team".compareTo(uwForm.getWorkspaceType()) == 0) {
			/*
			 * Since we added Filters in the workspace you can have no child orgs
			if ((uwForm.getComputation() != null && uwForm.getComputation() == true)
					&& (uwForm.getOrganizations() == null || uwForm
							.getOrganizations().size() == 0)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.aim.updateWorkspace.noChildOrg"));
				saveErrors(request, errors);
				return mapping.getInputForward();
			}
			*/
			uwForm.setChildWorkspaces(new ArrayList());
			if (uwForm.getComputation() == null
					|| uwForm.getComputation() == false) {
				uwForm.setAddActivity(true);
				uwForm.setOrganizations(null);
			}
		}
		if (uwForm.getWorkspaceType() != null
				&& "Management".compareTo(uwForm.getWorkspaceType()) == 0) {
			uwForm.setOrganizations(new ArrayList());
			uwForm.setComputation(null);
			uwForm.setUseFilter(null);
			uwForm.setAddActivity(null);
		}

		AmpTeam newTeam = null;
		newTeam = null;
		if (uwForm.getTeamName() != null) {
			newTeam = new AmpTeam();
			newTeam.setName(uwForm.getTeamName());
			newTeam.setTeamCategory(uwForm.getCategory());
			newTeam.setAccessType(uwForm.getWorkspaceType());
			newTeam.setAddActivity(uwForm.getAddActivity());
			newTeam.setComputation(uwForm.getComputation());
			newTeam.setUseFilter(uwForm.getUseFilter());
			newTeam.setHideDraftActivities(uwForm.getHideDraftActivities());
                newTeam.setWorkspaceGroup(CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getWorkspaceGroup()));
            
            if(uwForm.getUseFilter()==null || !uwForm.getUseFilter()){
				if (uwForm.getOrganizations() != null) {
					TreeSet s = new TreeSet();
					s.addAll(uwForm.getOrganizations());
					newTeam.setOrganizations(s);
				}
            }else{
            	newTeam.setOrganizations(null);
            }
			if (null == uwForm.getRelatedTeam()
					|| "-1".equals(uwForm.getRelatedTeam().toString().trim()))
				newTeam.setRelatedTeamId(null);
			else
				newTeam.setRelatedTeamId(TeamUtil.getAmpTeam(uwForm
						.getRelatedTeam()));
			if (uwForm.getDescription() != null
					&& uwForm.getDescription().trim().length() > 0) {
				newTeam.setDescription(uwForm.getDescription());
			} else {
				newTeam.setDescription(" ");
			}
			if(uwForm.getFmTemplate()!=null && !uwForm.getFmTemplate().equals(-1L))
				newTeam.setFmTemplate(FeaturesUtil.getTemplateById(uwForm.getFmTemplate()));
			else
				newTeam.setFmTemplate(null);
            if (uwForm.getWorkspacePrefix() != null && !uwForm.getWorkspacePrefix().equals(-1L))
                newTeam.setWorkspacePrefix(CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getWorkspacePrefix()));
            else
                newTeam.setWorkspacePrefix(null);
		}

		if (event != null && event.trim().equalsIgnoreCase("reset")) {
			uwForm.setPopupReset(false);
			uwForm.setTeamName("");
			uwForm.setCategory("");
			uwForm.setTypeId(new Long(0));
			uwForm.setDescription("");
			uwForm.setWorkspaceType("");
                uwForm.setWorkspaceGroup(new Long(0));
			uwForm.setRelatedTeamFlag("no");
			uwForm.setRelatedTeamName("");
			uwForm.setAddActivity(null);
			uwForm.setHideDraftActivities(false);
			uwForm.setComputation(null);
			uwForm.setUseFilter(null);
			if (uwForm.getChildWorkspaces() != null)
				uwForm.getChildWorkspaces().clear();
			if(uwForm.getDeletedChildWorkspaces() != null){
				uwForm.getDeletedChildWorkspaces().clear();
			}
            uwForm.setFmTemplate(null);
			
			return mapping.findForward("admin");
		} else if (event != null && event.trim().equalsIgnoreCase("add")) {
			uwForm.setActionEvent("add");
			if (newTeam != null) {
				if (uwForm.getChildWorkspaces() == null
						&& uwForm.getWorkspaceType().compareTo("Management") == 0) {
					errors.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.aim.updateWorkspace.noManagementChildSelected"));
					//here we must reload the child workspaces
					Workspace workspace = TeamUtil.getWorkspace(uwForm.getTeamId());
					uwForm.setChildWorkspaces(workspace.getChildWorkspaces());					
					saveErrors(request, errors);
					logger.debug("error.aim.updateWorkspace.noManagementChildSelected !!!!!");
					return mapping.getInputForward();
				}

                if(uwForm.getUseFilter()!=null && uwForm.getUseFilter()){
                    AmpARFilter filter = ReportContextData.getFromRequest().getFilter();
                    if ( filter != null) {
                        if ( newTeam.getAmpTeamId()!=null )
                            AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
                        Set fdSet	= AmpFilterData.createFilterDataSet(newTeam, filter);
                        if ( newTeam.getFilterDataSet() == null )
                            newTeam.setFilterDataSet(fdSet);
                        else {
                            newTeam.getFilterDataSet().clear();
                            newTeam.getFilterDataSet().addAll(fdSet);
                        }
					}else{
						if (newTeam.getAmpTeamId()!=null ){
							AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
						}
					}
				}else{//don't use filter
						if (newTeam.getAmpTeamId()!=null ){
							AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
						}
					}
				

				boolean teamExist = TeamUtil.createTeam(newTeam,
						uwForm.getChildWorkspaces());
				if (teamExist) {
					errors.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					logger.debug("Team name already exist. Error message saved to request");
					return mapping.getInputForward();
				}
			}
		} else if (event != null && event.trim().equalsIgnoreCase("edit")) {
			uwForm.setActionEvent("edit");
			if (newTeam != null) {
				if (uwForm.getChildWorkspaces().size() == 0
						&& uwForm.getWorkspaceType().compareTo("Management") == 0) {
					errors.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.aim.updateWorkspace.noManagementChildSelected"));
					saveErrors(request, errors);
					logger.debug("error.aim.updateWorkspace.noManagementChildSelected !!!!!");
					return mapping.getInputForward();
				}
				if (tId1 == null)
					newTeam.setAmpTeamId(uwForm.getTeamId());
				else
					newTeam.setAmpTeamId(new Long(Long.parseLong(tId1)));

				if (newTeam.getAccessType().equalsIgnoreCase("Team"))
				{
					uwForm.setChildWorkspaces(new ArrayList());
				}
				if (newTeam.getAccessType().equalsIgnoreCase("Management") && uwForm.getDeletedChildWorkspaces() != null)
				{
					Iterator<Long> it = uwForm.getDeletedChildWorkspaces().iterator();
					while(it.hasNext()){
						Long ampTeamId = it.next();
						TeamUtil.unlinkParentWorkspace(ampTeamId);
					}
					uwForm.getDeletedChildWorkspaces().clear();
				}

				if( uwForm.getUseFilter() == null || !uwForm.getUseFilter() ){
					if (uwForm.getOrganizations() != null) {
						TreeSet s = new TreeSet();
						s.addAll(uwForm.getOrganizations());
						newTeam.setOrganizations(s);
					}
					if (newTeam.getAmpTeamId()!=null )
						AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
					
				}else{//uses filter
					newTeam.setOrganizations(null);

                    AmpARFilter filter = ReportContextData.getFromRequest().getFilter();
					if ( filter != null) {
						if ( newTeam.getAmpTeamId()!=null )
							AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
						Set fdSet	= AmpFilterData.createFilterDataSet(newTeam, filter);
						if ( newTeam.getFilterDataSet() == null )
							newTeam.setFilterDataSet(fdSet);
						else {
							newTeam.getFilterDataSet().clear();
							newTeam.getFilterDataSet().addAll(fdSet);
						}
					}
					else
						if (newTeam.getAmpTeamId()!=null )
							AmpTeamFilterData.deleteOldFilterData(newTeam.getAmpTeamId());
					
				}
	                
					if(uwForm.getWorkspaceGroup() != null){
	                	newTeam.setWorkspaceGroup(CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getWorkspaceGroup()));
	                }
	            
                    
				boolean teamExist = TeamUtil.updateTeam(newTeam,
						uwForm.getChildWorkspaces());
				if (teamExist) {
					errors.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					logger.debug("Team name already exist. Error message saved to request");
					return mapping.getInputForward();
				} else {
					uwForm.setUpdateFlag(true);
				}
				TeamMember tm = (TeamMember) session
						.getAttribute("currentMember");
				if (tm != null) {
					if (tm.getTeamId() != null) {
						session.removeAttribute("currentMember");
						tm.setTeamName(newTeam.getName());
						tm.setTeamId(newTeam.getAmpTeamId());
						session.setAttribute("currentMember", tm);
						PermissionUtil.putInScope(session,
								GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
					}
				}
				return mapping.findForward("forward");
			}
		} else if (event != null && event.trim().equalsIgnoreCase("delete")) {
			String tId = request.getParameter("tId");
			Long teamId = new Long(Long.parseLong(tId));
			boolean memExist = TeamUtil.membersExist(teamId);
			boolean actExist = TeamUtil.teamHasActivities(teamId);

			if (memExist) {
				errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.aim.membersExistForTeam"));
				saveErrors(request, errors);

				return mapping.findForward("forward");
			}
			if (actExist) {
				errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.aim.activitiesExistForTeam"));
				saveErrors(request, errors);

				return mapping.findForward("forward");
			}
			
			TeamUtil.removeTeam(teamId);
		}
		uwForm.setReset(true);
		uwForm.reset(mapping, request);

		return mapping.findForward("forward");
	}
}
