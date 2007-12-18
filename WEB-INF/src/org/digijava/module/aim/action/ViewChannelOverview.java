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
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.aim.util.ProgramUtil;

public class ViewChannelOverview extends TilesAction {

	private static Logger logger = Logger.getLogger(ViewChannelOverview.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		PermissionUtil.resetScope(session);
		PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
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
			PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY,activity);
			ArrayList colAux=new ArrayList();
			Collection ampFields=DbUtil.getAmpFields();
			HashMap allComments=new HashMap();
			for(Iterator itAux=ampFields.iterator(); itAux.hasNext();)
			{
				AmpField field = (AmpField) itAux.next();
				//System.out.println(field.getFieldName());
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

		 	if ("approved".equals(actApprovalStatus) || "started".equals(actApprovalStatus)) {
		 		if (workingTeamFlag && teamMember.getWrite())
		 			formBean.setButtonText("edit");	// In case of regular working teams
		 		else
		 			formBean.setButtonText("none");	// In case of management-workspace
		 	}
		 	else if (workingTeamFlag && teamLeadFlag)
		 			formBean.setButtonText("validate");
		 	     else
		 			formBean.setButtonText("approvalAwaited");
		 	// end

			String perspective = null;
			String currCode = null;

			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getPerspective() != null) {
					perspective = appSettings.getPerspective();
				} else {
					perspective = "MOFED";
				}
				if (appSettings.getCurrencyId() != null) {
					currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
				} else {
					currCode = Constants.DEFAULT_CURRENCY;
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

				/* please do not remove these commented lines, I am sure we will need them again */
				//String totalsSetting=FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_INCLUDE_PLANNED);
				//boolean includePlannedInTotals=(totalsSetting!=null) && (totalsSetting.trim().equals("On"));


				if (activity.getStatus().equalsIgnoreCase("Planned")) {
					logger.debug("Planned");
					formBean.setGrandTotal(mf.format(DbUtil.getAmpFundingAmount(activity.getActivityId(),
							new Integer(0),new Integer(0),perspective,currCode)));
				} else {
					logger.debug("Not planned");

					//if (includePlannedInTotals){
						// again this is for Bolivia. But may be usefull for other countries too. AMP-1774
						double actual = DbUtil.getAmpFundingAmount(activity.getActivityId(),new Integer(0),new Integer(1),perspective,currCode);
						double planned = DbUtil.getAmpFundingAmount(activity.getActivityId(),new Integer(0),new Integer(0),perspective,currCode);
						formBean.setGrandTotal(mf.format(actual+planned));
					//}else{
					//	formBean.setGrandTotal(mf.format(DbUtil.getAmpFundingAmount(activity.getActivityId(),
					//			new Integer(0),new Integer(1),perspective,currCode)));
					//}
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
}
