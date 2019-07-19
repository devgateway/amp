package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.dbentity.AmpTeamMember;

public interface ActivityService {
    
    boolean isActivityStale(Long ampActivityId, Long activityGroupVersion);
    
    void doWithLock(Long ampActivityId, Long ampTeamMemId, Runnable runnable);
    
    boolean addActivityAllowed(AmpTeamMember ampTeamMember);
    
    boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId);
    
}
