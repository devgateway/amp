/*
 * ViewChannelOverview.java
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.text.DecimalFormat;

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
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ViewChannelOverview extends TilesAction {

	private static Logger logger = Logger.getLogger(ViewChannelOverview.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		ChannelOverviewForm formBean = (ChannelOverviewForm) form;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
			
		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			Long id = null;
			if (request.getParameter("ampActivityId") != null) {
				id = new Long(request.getParameter("ampActivityId"));
			}
			else {
				id = formBean.getId();
			}
			
			Activity activity = ActivityUtil.getChannelOverview(id);
			
			// added by Akash
			// desc: approval status check
			// start
			String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
			Long ampTeamId = teamMember.getTeamId();
			boolean teamLeadFlag    = teamMember.getTeamHead();
			boolean workingTeamFlag = DbUtil.checkForParentTeam(ampTeamId);
			
		 	if ("approved".equals(actApprovalStatus) || "started".equals(actApprovalStatus))
		 		formBean.setButtonText("edit");
		 	else if (workingTeamFlag && teamLeadFlag)
		 			formBean.setButtonText("validate");
		 	     else
		 			formBean.setButtonText("approvalAwaited");
		 	// end
			
			String perspective = null;
			String currCode = null;
			Long fiscalCalId = null;
			
			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getPerspective() != null) {
					perspective = appSettings.getPerspective();
				} else {
					perspective = "MOFED";
				}
				if (appSettings.getCurrencyId() != null) {
					currCode = DbUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
				} else {
					currCode = Constants.DEFAULT_CURRENCY;
				}
				if (appSettings.getFisCalId() != null) {
					fiscalCalId = appSettings.getFisCalId();
				} else {
					fiscalCalId = Constants.GREGORIAN;
				}
				formBean.setPerspective(perspective);
				formBean.setWrite(teamMember.getWrite());
				formBean.setDelete(teamMember.getDelete());
				formBean.setCurrCode(currCode);
				if (perspective.equalsIgnoreCase("MOFED")) {
					perspective = Constants.MOFED;
				} else {
					perspective = Constants.DONOR;
				}
				
				if (activity.getStatus().equalsIgnoreCase("Planned")) {
					logger.info("Planned");
					formBean.setGrandTotal(mf.format(DbUtil.getAmpFundingAmount(activity.getActivityId(),new Integer(0),new Integer(0),perspective,currCode)));					
				} else {
					logger.info("Not planned");
					formBean.setGrandTotal(mf.format(DbUtil.getAmpFundingAmount(activity.getActivityId(),new Integer(0),new Integer(1),perspective,currCode)));					
				}				
			}
			
			AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
			if(team.getAccessType().equals("Team"))
				formBean.setWrite(true);
			else
				formBean.setWrite(false);
			
			formBean.setActivity(activity);
			// end $1
		}
		return null;
	}
}
