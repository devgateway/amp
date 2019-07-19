package org.digijava.kernel.ampapi.endpoints.activity;

import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.ampapi.exception.ActivityLockNotGrantedException;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class TestActivityService implements ActivityService {
    
    public boolean stale;
    
    public TestTeamMemberContext tmContext;
    
    public TestActivityService(boolean stale, TestTeamMemberContext tmContext) {
        this.stale = stale;
        this.tmContext = tmContext;
    }
    
    @Override
    public boolean isActivityStale(Long ampActivityId, Long activityGroupVersion) {
        return stale;
    }
    
    @Override
    public void doWithLock(Long ampActivityId, Long ampTeamMemId, Runnable runnable) {
        String activityId = ampActivityId == null ? null : ampActivityId.toString();
        String key = null;
        try {
            if (ampActivityId != null) {
                key = ActivityGatekeeper.lockActivity(activityId, ampTeamMemId);
                if (key == null) {
                    Long editingUserId = ActivityGatekeeper.getUserEditing(activityId);
                    throw new ActivityLockNotGrantedException(editingUserId);
                }
            }
        } finally {
            if (key != null) {
                ActivityGatekeeper.unlockActivity(activityId, key);
            }
        }
    }
    
    @Override
    public boolean addActivityAllowed(AmpTeamMember ampTeamMember) {
        return tmContext.isAddActivity();
    }
    
    @Override
    public boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId) {
        return tmContext.isEditActivity();
    }
}
