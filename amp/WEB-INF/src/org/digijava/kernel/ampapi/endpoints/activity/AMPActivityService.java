package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;
import java.util.Locale;

import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import static org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion;

public class AMPActivityService implements ActivityService {
    
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
    
    @Override
    public AmpActivityVersion getActivity(Long activityId) throws DgException {
        return ActivityUtil.loadActivity(activityId);
    }
    
    @Override
    public AmpActivityVersion saveActivity(AmpActivityVersion newActivity, List<AmpContentTranslation> translations,
                                           AmpTeamMember modifiedBy, boolean draftChange, SaveContext saveContext,
                                           EditorStore editorStore, Site site) throws Exception {
        
        Session session = PersistenceManager.getSession();
        return saveActivityNewVersion(newActivity, translations, modifiedBy,
                Boolean.TRUE.equals(newActivity.getDraft()), draftChange,
                session, saveContext, editorStore, site);
    }
    
    @Override
    public void updateLuceneIndex(AmpActivityVersion newActivity, AmpActivityVersion oldActivity, boolean update,
                                  TranslationSettings trnSettings, List<AmpContentTranslation> translations,
                                  Site site) {
        String rootPath = TLSUtils.getRequest().getServletContext().getRealPath("/");
        Locale lang = Locale.forLanguageTag(trnSettings.getDefaultLangCode());
        LuceneUtil.addUpdateActivity(rootPath, update, site, lang, newActivity, oldActivity, translations);
    }
    
    
}
