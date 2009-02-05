package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Collection;

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
import org.digijava.module.aim.form.MainProjectDetailsForm;
import org.digijava.module.aim.helper.COverSubString;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;

public class ViewMainProjectDetails extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewMainProjectDetails.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		MainProjectDetailsForm formBean = (MainProjectDetailsForm) form;
		HttpSession session = request.getSession();
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		try {
		
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		} else {
			formBean.setSessionExpired(false);
			Long id = new Long(formBean.getAmpActivityId());
			if(request.getAttribute("costs")==null){
				Collection euActs = EUActivityUtil.getEUActivities(id);
			    request.setAttribute("costs", euActs);
			}			
			int tabIndex = formBean.getTabIndex();
			String channelOverviewTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String financialProgressTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String physicalProgressTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String documentsTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			formBean.setAmpActivityId(id.longValue());
			formBean.setTabIndex(tabIndex);
			AmpActivity ampActivity = ActivityUtil.getProjectChannelOverview(id);
			if(ampActivity!=null){
				if (ampActivity.getProjectComments() == null)
					formBean.setProjectComments("");
				else {
					formBean.setProjectComments(ampActivity.getProjectComments());
				}
				if (ampActivity.getDescription() == null)
					formBean.setDescription("");
				else {
					formBean.setDescription(ampActivity.getDescription());
				}
				if (ampActivity.getObjective() == null)
					formBean.setObjectives("");
				else
					formBean.setObjectives(ampActivity.getObjective());
				if (ampActivity.getPurpose() == null)
					formBean.setPurpose("");
				else
					formBean.setPurpose(ampActivity.getPurpose());
				if (ampActivity.getResults() == null)
					formBean.setResults("");
				else
					formBean.setResults(ampActivity.getResults());
				if (ampActivity.getDescription() != null)
					formBean.setFlag(COverSubString.getCOverSubStringLength(ampActivity
							.getDescription(), 'D'));
				formBean.setName(ampActivity.getName());
				formBean.setAmpId(ampActivity.getAmpId());
			}
			
			if (tabIndex == 0) {
				channelOverviewTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			}				
			else if (tabIndex == 1) {
				financialProgressTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			}				
			else if (tabIndex == 2) {
				physicalProgressTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			}			
			else if (tabIndex == 3) {
				documentsTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			}				
			formBean.setChannelOverviewTabColor(channelOverviewTabColor);
			formBean.setFinancialProgressTabColor(financialProgressTabColor);
			formBean.setPhysicalProgressTabColor(physicalProgressTabColor);
			formBean.setDocumentsTabColor(documentsTabColor);
			
			//Long id = new Long(request.getParameter("ampActivityId"));
			AmpActivity activity = ActivityUtil.getAmpActivity(id);
			//AmpActivity ampact = ActivityUtil.getAmpActivity(id);
			String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
			TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
			Long ampTeamId = teamMember.getTeamId();
			boolean teamLeadFlag    = teamMember.getTeamHead();
			boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
			if (workingTeamFlag) {
				formBean.setButtonText("edit");	// In case of regular working teams
				if (!(activity.getDraft()!=null && activity.getDraft()) && ( actApprovalStatus != null && Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(actApprovalStatus.toLowerCase())))
			 	{
			 		//burkina
			 		// if an user save an activity he could edit it even it is not approved by team leader
			 		//if(workingTeamFlag && !teamLeadFlag && teamMember.getMemberId().equals(activity.getCreatedBy().getAmpTeamMemId()))
			 		if (workingTeamFlag && teamLeadFlag && teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId())) {
			 			formBean.setButtonText("validate");
			 		}/*else {
			 			formBean.setButtonText("approvalAwaited");
			 		}*/		 		
			 	}
			} else {
				formBean.setButtonText("none");	// In case of management-workspace
			}
		}
		} catch (Exception e) {
			logger.debug("Exception " + e.getMessage());
			e.printStackTrace(System.out);
		}
		return null;
	}
}