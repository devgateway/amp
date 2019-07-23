package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public interface ActivityService {
    
    boolean isActivityStale(Long ampActivityId, Long activityGroupVersion);
    
    boolean addActivityAllowed(AmpTeamMember ampTeamMember);
    
    boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId);
    
    AmpActivityVersion saveActivity(AmpActivityVersion newActivity, List<AmpContentTranslation> translations,
                                              AmpTeamMember modifiedBy, boolean draftChange, SaveContext saveContext,
                                              EditorStore editorStore, Site site) throws Exception;
    
    void updateLuceneIndex(AmpActivityVersion newActivity, AmpActivityVersion oldActivity, boolean update,
                           TranslationSettings trnSettings, List<AmpContentTranslation> translations, Site site);
    
}
