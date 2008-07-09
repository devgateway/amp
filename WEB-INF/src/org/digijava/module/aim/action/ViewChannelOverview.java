/*
 * ViewChannelOverview.java
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
public class ViewChannelOverview extends TilesAction {

	private static Logger logger = Logger.getLogger(ViewChannelOverview.class);


	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		PermissionUtil.resetScope(session);
		PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
		
		ChannelOverviewForm formBean = (ChannelOverviewForm) form;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		String debugFM=request.getParameter("debugFM");
		
		if(debugFM!=null && "true".compareTo(debugFM)==0)
			{
				formBean.setDebugFM("true");
				session.setAttribute("debugFM", "true");
			}
		else {
				formBean.setDebugFM("false");
				session.setAttribute("debugFM", "false");
		}
		boolean debug = (request.getParameter("debug")!=null)?true:false;
		
		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			Long id = null;
			if (request.getParameter("ampActivityId") != null) {
				id = new Long(request.getParameter("ampActivityId"));
                formBean.setId(id);
			}
			else {
				id = formBean.getId();
			}

			Collection<AmpCategoryValue> implLocationLevels	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);

			if ( implLocationLevels != null)
					formBean.setNumImplLocationLevels( implLocationLevels.size() );

			try{
			    if(formBean.getClassificationConfigs()==null){
			    	formBean.setClassificationConfigs(SectorUtil.getAllClassificationConfigs());
			    }
			}
			catch(Exception e)
			{
			    logger.debug("Classification Config Not Found.");
			}
			
			Activity activity = ActivityUtil.getChannelOverview(id);

			formBean.clearMessages();
			AmpTeam ampTeam=TeamUtil.getAmpTeam(teamMember.getTeamId());
			boolean hasTeamLead=true;
			if(ampTeam.getTeamLead()==null) hasTeamLead=false;
			createWarnings(activity,teamMember.getTeamHead(), formBean,hasTeamLead);

			AmpActivity ampact = ActivityUtil.getAmpActivity(id);
			
			PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY, ampact);

			Set orgProjIdsSet = ampact.getInternalIds();
	          if (orgProjIdsSet != null) {
	            Iterator projIdItr = orgProjIdsSet.iterator();
	            Collection temp = new ArrayList();
	            while (projIdItr.hasNext()) {
	              AmpActivityInternalId actIntId = (
	                  AmpActivityInternalId) projIdItr
	                  .next();
	              OrgProjectId projId = new OrgProjectId();
	              projId.setAmpOrgId(actIntId.getOrganisation()
	                                 .getAmpOrgId());
	              projId
	                  .setName(actIntId.getOrganisation()
	                           .getName());
	              projId.setOrganisation(actIntId.getOrganisation());
	              projId.setProjectId(actIntId.getInternalId());
	              temp.add(projId);
	            }
	            if (temp != null && temp.size() > 0) {
	              OrgProjectId orgProjectIds[] = new OrgProjectId[
	                  temp
	                  .size()];
	              Object arr[] = temp.toArray();
	              for (int i = 0; i < arr.length; i++) {
	                orgProjectIds[i] = (OrgProjectId) arr[i];
	              }
	              formBean.setSelectedOrganizations(orgProjectIds);
	            }
	          }

	          AmpCategoryValue ampCategoryValue = CategoryManagerUtil
					.getAmpCategoryValueFromListByKey(
							CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
							ampact.getCategories());

			if (ampCategoryValue != null)
				formBean.setImplemLocationLevel(ampCategoryValue.getValue());


			//PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY,activity);
			ArrayList colAux=new ArrayList();
			Collection ampFields=DbUtil.getAmpFields();
			HashMap allComments=new HashMap();
			for(Iterator itAux=ampFields.iterator(); itAux.hasNext();)
			{
				AmpField field = (AmpField) itAux.next();
				////System.out.println(field.getFieldName());
				colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),id);
				allComments.put(field.getFieldName(),colAux);
			}
			formBean.setAllComments(allComments);

			// added by Akash
			// desc: approval status check
			// start
			String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
			Long ampTeamId = teamMember.getTeamId();
			boolean teamLeadFlag    = teamMember.getTeamHead();
			boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);

		 	if (!(activity.getDraft()!=null && activity.getDraft()) && ( actApprovalStatus != null &&
		 			Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(actApprovalStatus.toLowerCase())  ))
		 	{
		 		if (workingTeamFlag && teamLeadFlag && teamMember.getTeamId().equals(ampact.getTeam().getAmpTeamId()))
		 			formBean.setButtonText("validate");
		 		else
		 			formBean.setButtonText("approvalAwaited");
		 		
		 	}
		 	else {
		 		if (workingTeamFlag && teamMember.getWrite())
		 			formBean.setButtonText("edit");	// In case of regular working teams
		 		else
		 			formBean.setButtonText("none");	// In case of management-workspace
		 	}
		 	// end

			String currCode = null;

			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getCurrencyId() != null) {
					currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
				} else {
					currCode = Constants.DEFAULT_CURRENCY;
				}
				formBean.setWrite(teamMember.getWrite());
				formBean.setDelete(teamMember.getDelete());
				formBean.setCurrCode(currCode);

    			// call the logic instance to perform the caculations, so it will depend of each implementancion how we will calculate the total inlcuding or not planned 
    			DecimalWraper total = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(activity.getActivityId(), currCode);
            		if (!debug) {
            		    formBean.setGrandTotal(total.toString());
            		} else {
            		    formBean.setGrandTotal(total.getCalculations());
            		}
				
			}

			AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
			if(team.getAccessType().equals("Team"))
				formBean.setWrite(true);
			else
				formBean.setWrite(false);

			formBean.setActivity(activity);
			formBean.setCanView(true);

                        //set programs by setting name

                        formBean.setNationalPlanObjectivePrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getActivityId(),
                             ProgramUtil.NATIONAL_PLAN_OBJECTIVE));

                        formBean.setPrimaryPrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getActivityId(),
                             ProgramUtil.PRIMARY_PROGRAM));

                        formBean.setSecondaryPrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getActivityId(),
                             ProgramUtil.SECONDARY_PROGRAM));



			// end $1


			/*
			boolean canView = ActivityUtil.canViewActivity(id,teamMember);
			if (canView) {
			} else {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.illegalActivityAccess"));
				saveErrors(request, errors);
				formBean.setCanView(false);
			}*/
		}
		return null;
	}

	private void createWarnings (Activity activity, boolean isTeamHead, ChannelOverviewForm formBean, boolean hasTeamLead) {
		if (activity.getDraft()!=null && activity.getDraft()) {
			formBean.addError("error.aim.draftActivity", 
					"This is a draft activity");

		}
		else //we are not checking for TL because of AMP-2705
		{
			////System.out.println("the team member is not the TEAM LEADER!!!!!!!!");
			if ( Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(activity.getApprovalStatus()) ) {
				if(hasTeamLead)	formBean.addError("error.aim.activityAwaitingApproval",	"The activity is awaiting approval.");
					else formBean.addError("error.aim.activityAwaitingApprovalNoWorkspaceManager",	"This activity cannot be validated because there is no Workspace Manager.");
			}
		}
	}
}
