package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ViewActivityHistoryForm;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import clover.org.apache.commons.lang.StringUtils;

public class ViewActivityHistory extends Action {

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ViewActivityHistoryForm hForm = (ViewActivityHistoryForm) form;
		// Load current activity, get group and retrieve past versions.
		Session session = PersistenceManager.getRequestDBSession();
		AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, hForm.getActivityId());

		// AMP-7706: Filter last 5 versions.
		Query qry = session.createQuery("SELECT act FROM " + AmpActivityVersion.class.getName() 
				+ " act WHERE act.ampActivityGroup.ampActivityGroupId = ? ORDER BY act.ampActivityId DESC").setMaxResults(ActivityVersionUtil.numberOfVersions());
		qry.setParameter(0, currentActivity.getAmpActivityGroup().getAmpActivityGroupId());
		List<AmpActivityVersion> activities = new ArrayList<AmpActivityVersion>(qry.list());
		
		hForm.setActivities(getActivitiesHistory(activities));
			
		TeamMember currentMember = (TeamMember)request.getSession().getAttribute("currentMember");

		//it also can be accessed anonymously
		if (currentMember!=null) {
			AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
			
			boolean ispartofamanagetmentworkspace = ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);
			boolean iscurrentworkspacemanager = ampCurrentMember.getAmpMemberRole().getTeamHead();
			
			//If the current user is part of the management workspace or is not the workspace manager of a workspace that's not management then hide.
			hForm.setEnableadvanceoptions(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
		}
		
		return mapping.findForward("forward");
	}
	
	private List<ActivityHistory> getActivitiesHistory(List<AmpActivityVersion> activities) {
		List<ActivityHistory> activitiesHistory = new ArrayList<>();
		
		for (AmpActivityVersion activity : activities) {
			ActivityHistory auditHistory = getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
			
			ActivityHistory activityHistory = new ActivityHistory();
			activityHistory.setActivityId(activity.getAmpActivityId());
			activityHistory.setModifiedBy(getModifiedByUserName(activity, auditHistory));
			activityHistory.setModifiedDate(getModifiedByDate(activity, auditHistory));
			
			activitiesHistory.add(activityHistory);
		}
		
		return activitiesHistory;
	}

	/** Get the user first name and last name  who modified (created) the activity.
	 * @param actitivity
	 * @param modifiedByInfo
	 * @param auditHistory
	 * @return
	 */
	private String getModifiedByUserName(AmpActivityVersion actitivity, ActivityHistory auditHistory) {
		AmpTeamMember modifiedBy = actitivity.getModifiedBy();
		AmpTeamMember createdBy = actitivity.getModifiedBy();
		AmpTeamMember approvedBy = actitivity.getApprovedBy();
		
		if (modifiedBy != null) {
			return String.format("%s %s", modifiedBy.getUser().getFirstNames(), modifiedBy.getUser().getLastName());
		} else if(auditHistory != null) {
			return auditHistory.getModifiedBy();
		} else if (approvedBy != null) {
			return String.format("%s %s", approvedBy.getUser().getFirstNames(), approvedBy.getUser().getLastName());
		} else if (createdBy != null) {
			return String.format("%s %s", createdBy.getUser().getFirstNames(), createdBy.getUser().getLastName());
		}
		
		return "";
	}
	
	/** Get modified date
	 * @param activity
	 * @param auditHistory
	 * @param activityHistory
	 */
	private Date getModifiedByDate(AmpActivityVersion activity, ActivityHistory auditHistory) {
		if (activity.getModifiedDate() != null) {
			return activity.getModifiedDate();
		} else if (auditHistory != null) {
			return auditHistory.getModifiedDate();
		} else if (activity.getApprovalDate() != null) {
			return activity.getApprovalDate();
		} else if (activity.getCreatedDate() != null) {
			return activity.getCreatedDate();
		}
		
		return null;
	}
	
	/**
	 * Get audit info about the activity from amp_audit_logger table
	 * @param activityId
	 * @return
	 */
	private ActivityHistory getModifiedByInfoFromAuditLogger(Long activityId) {
		ActivityHistory logActivityHistory = new ActivityHistory();
		List<AmpAuditLogger> activityLogObjects = AuditLoggerUtil.getActivityLogObjects(activityId.toString());
		
		for(AmpAuditLogger aal : activityLogObjects) {
			if (StringUtils.isNotEmpty(aal.getEditorName())) {
				logActivityHistory.setModifiedBy(aal.getEditorName());
				logActivityHistory.setModifiedDate(aal.getLoggedDate());
				return logActivityHistory;
			} else if (StringUtils.isNotEmpty(aal.getEditorEmail())) {
				try {
					User u = UserUtils.getUserByEmail(aal.getEditorEmail());
					if (u != null) {
						logActivityHistory.setModifiedBy(String.format("%s %s", u.getFirstNames(), u.getLastName()));
						logActivityHistory.setModifiedDate(aal.getLoggedDate());
						return logActivityHistory;
					}
				} catch (DgException e) {
					logger.error(e);				
				}
			}
		}
		
		return null;
	}
}