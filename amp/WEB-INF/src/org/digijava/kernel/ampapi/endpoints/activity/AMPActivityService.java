package org.digijava.kernel.ampapi.endpoints.activity;

import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AMPActivityService implements ActivityService {
    
    private FMService fmService = new AMPFMService();
    
    /**
     * Checks if the activity is stale. Used only for the case when new activity versions are created.
     */
    @Override
    public boolean isActivityStale(Long ampActivityId, Long activityGroupVersion) {
        Number activityCount = (Number) PersistenceManager.getSession().createCriteria(AmpActivityVersion.class)
                .add(Restrictions.eq("ampActivityId", ampActivityId))
                .setProjection(Projections.count("ampActivityId"))
                .uniqueResult();
        if (activityCount.longValue() == 0) {
            return false;
        }
        
        Number latestActivityCount = (Number) PersistenceManager.getSession().createCriteria(AmpActivityGroup.class)
                .createAlias("ampActivityLastVersion", "a")
                .add(Restrictions.and(
                        Restrictions.eq("a.ampActivityId", ampActivityId),
                        Restrictions.eq("version", activityGroupVersion)))
                .setProjection(Projections.count("a.ampActivityId"))
                .uniqueResult();
        
        return latestActivityCount.longValue() == 0;
    }
    
    @Override
    public void doWithLock(Long activityId, Long ampTeamMemId, Runnable runnable) {
        ActivityGatekeeper.doWithLock(activityId, ampTeamMemId, runnable);
    }
    
    /**
     * @param ampTeamMember    team member
     * @return true if add activity is allowed
     */
    @Override
    public boolean addActivityAllowed(AmpTeamMember ampTeamMember) {
        TeamMember tm = new TeamMember(ampTeamMember);
        return tm != null && Boolean.TRUE.equals(tm.getAddActivity())
                && (FeaturesUtil.isVisibleField("Add Activity Button")
                || FeaturesUtil.isVisibleField("Add SSC Button"));
    }
    
    /**
     * @param ampTeamMember    team member
     * @param activityId    activity id
     * @return true if team member can edit the activity
     */
    @Override
    public boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId) {
        TeamMember tm = new TeamMember(ampTeamMember);
        return activityId != null && ActivityUtil.getEditableActivityIdsNoSession(tm).contains(activityId);
    }
}
