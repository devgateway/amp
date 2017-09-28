package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
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
            ActivityHistory auditHistory = null;
            
            if (activity.getModifiedBy() == null || (activity.getUpdatedDate() == null && activity.getModifiedDate() == null)) {
                auditHistory = ActivityUtil.getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
            }
            
            ActivityHistory activityHistory = new ActivityHistory();
            activityHistory.setActivityId(activity.getAmpActivityId());
            activityHistory.setModifiedBy(ActivityUtil.getModifiedByUserName(activity, auditHistory));
            activityHistory.setModifiedDate(FormatHelper.formatDate(ActivityUtil.getModifiedByDate(activity, auditHistory)));
            
            activitiesHistory.add(activityHistory);
        }
        
        return activitiesHistory;
    }
}
