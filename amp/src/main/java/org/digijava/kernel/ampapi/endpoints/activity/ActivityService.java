package org.digijava.kernel.ampapi.endpoints.activity;

import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.util.List;

public interface ActivityService {
    
    boolean isActivityStale(Long ampActivityId, Long activityGroupVersion);
    
    boolean addActivityAllowed(AmpTeamMember ampTeamMember);
    
    boolean isEditableActivity(AmpTeamMember ampTeamMember, Long activityId);
    
    AmpActivityVersion getActivity(Long activityId) throws DgException;
    
    AmpActivityVersion saveActivity(AmpActivityVersion newActivity, List<AmpContentTranslation> translations,
            List<AmpContentTranslation> cumulativeTranslations,
            AmpTeamMember modifiedBy, boolean draftChange, SaveContext saveContext,
            EditorStore editorStore, Site site) throws Exception;
    
    void updateLuceneIndex(AmpActivityVersion newActivity, AmpActivityVersion oldActivity, boolean update,
                           TranslationSettings trnSettings, List<AmpContentTranslation> translations, Site site);
    
}
