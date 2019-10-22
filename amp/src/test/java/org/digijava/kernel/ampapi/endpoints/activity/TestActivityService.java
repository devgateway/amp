package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.ampapi.exception.ActivityLockNotGrantedException;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.InMemoryActivityManager;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class TestActivityService implements ActivityService {
    
    public TestTeamMemberContext tmContext;
    
    public InMemoryActivityManager activityManager = InMemoryActivityManager.getInstance();
    
    public TestActivityService(TestTeamMemberContext tmContext) {
        this.tmContext = tmContext;
    }
    
    @Override
    public boolean isActivityStale(Long ampActivityId, Long activityGroupVersion) {
        if (!activityManager.activityExists(ampActivityId)) {
            return false;
        }
        
        return !activityManager.isActivityLastVersion(ampActivityId, activityGroupVersion);
    }
    
    @Override
    public boolean addActivityAllowed(AmpTeamMember ampTeamMember) {
        return tmContext.isAddActivity();
    }
    
    @Override
    public boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId) {
        return tmContext.isEditActivity();
    }
    
    @Override
    public AmpActivityVersion getActivity(Long activityId) {
        return activityManager.getActivity(activityId);
    }
    
    @Override
    public AmpActivityVersion saveActivity(AmpActivityVersion newActivity, List<AmpContentTranslation> translations,
                                           AmpTeamMember modifiedBy, boolean draftChange, SaveContext saveContext,
                                           EditorStore editorStore, Site site) {
        return newActivity;
    }
    
    @Override
    public void updateLuceneIndex(AmpActivityVersion newActivity, AmpActivityVersion oldActivity, boolean update,
                                  TranslationSettings trnSettings, List<AmpContentTranslation> translations, Site site) {
        
    }
    
}
