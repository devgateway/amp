package org.digijava.module.message.jobs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class CloseExpiredActivitiesJob extends ConnectionCleaningJob implements StatefulJob {

    public final static String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    public final static String AMP_MODIFIER_FIRST_NAME = "AMP";
    public final static String AMP_MODIFIER_LAST_NAME = "Activities Modifier";

    private static Logger logger = Logger.getLogger(CloseExpiredActivitiesJob.class);
    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    /**
     * clones activity, sets modifying member, modification date, etc
     * @param session
     * @param member
     * @param oldActivity
     * @return
     * @throws CloneNotSupportedException
     */
    protected AmpActivityVersion cloneActivity(Session session, AmpTeamMember member, AmpActivityVersion oldActivity,
            ApprovalStatus newStatus, Long closedProjectStatusCategoryValue) throws CloneNotSupportedException
    {       
        AmpActivityVersion prevVersion = oldActivity.getAmpActivityGroup().getAmpActivityLastVersion();
        oldActivity.getAmpActivityGroup().setAutoClosedOnExpiration(true);

        oldActivity.setApprovalStatus(newStatus);
        oldActivity.getCategories().remove(CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME, oldActivity.getCategories()));
        oldActivity.getCategories().add(CategoryManagerUtil.getAmpCategoryValueFromDb(closedProjectStatusCategoryValue));

        EditorStore editorStore = new EditorStore();
        Site site = SiteUtils.getDefaultSite();

        AmpActivityVersion auxActivity = null;
        try {
            auxActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(oldActivity, null,
                    member, oldActivity.getDraft(), session, SaveContext.job(), editorStore, site);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        
        java.util.Locale javaLocale = new java.util.Locale("en");
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true, 
                SiteUtils.getDefaultSite(), javaLocale, 
                auxActivity, prevVersion);
        return auxActivity;
    }
    
    @Override public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            String val = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATICALLY_CLOSE_ACTIVITIES);
            boolean autoClosingEnabled = "true".equalsIgnoreCase(val);
            if (!autoClosingEnabled)
                return; // feature disabled => nothing to do;

            logger.info("Running the activity autocloser job...");
            
            //no longer available: TLSUtils.forceLocaleUpdate(org.digijava.module.um.util.DbUtil.getLanguageByCode("en"));
            TLSUtils.getThreadLocalInstance().site = SiteUtils.getDefaultSite();
            
            Session session = PersistenceManager.getSession();

            AmpBackgroundActivitiesUtil.createActivityUserIfNeeded(user);
            PersistenceManager.cleanupSession(session); // commit user in case it was created
            session = PersistenceManager.getSession();
        
            Long closedCategoryValue = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);
            
            String filterQuery = "SELECT amp_activity_last_version_id "
                    + "FROM amp_activity_group aag "
                    + "WHERE aag.autoclosedonexpiration = false "
                    + "AND aag.amp_activity_last_version_id IN ("
                    + "  SELECT amp_activity_id "
                    + "  FROM amp_activity "
                    + "  WHERE (draft IS NULL or draft=false) "
                    + "  AND (amp_team_id IS NOT NULL) "
                    + "  AND (deleted IS NULL OR deleted = false) "
                    + "  AND approval_status IN (" + Util.toCSString(AmpARFilter.VALIDATED_ACTIVITY_STATUS) + ") "
                    + "  AND actual_completion_date < now()"
                    + ") "
                    + "AND aag.amp_activity_last_version_id IN ("
                    + "  select amp_activity_id "
                    + "  FROM v_status "
                    + "  WHERE amp_status_id != " + closedCategoryValue
                    + ")";
        
            List<Long> eligibleIds = DbHelper.getInActivities(filterQuery);
            //java.util.Map<Long, Long> statusIdsByActivityIds = getStatuses(session, eligibleIds);                     
            
            List<AmpActivityVersion> closeableActivities;
            if (eligibleIds.isEmpty())
                closeableActivities = new ArrayList<AmpActivityVersion>();
            else {
                String queryString = "select aav from " + AmpActivityVersion.class.getName() +
                      " aav " + "where aav.ampActivityId IN (" + Util.toCSString(eligibleIds) + ")";
                closeableActivities = session.createQuery(queryString).list();
            }

            ApprovalStatus newStatus = ApprovalStatus.APPROVED;
            session.setFlushMode(FlushMode.MANUAL);
            for(AmpActivityVersion ver:closeableActivities) {
                if ("On".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION))) {
                     newStatus = ver.getApprovalStatus().equals(ApprovalStatus.STARTED_APPROVED)
                             ? ApprovalStatus.STARTED : ApprovalStatus.EDITED;
                }
                
                AmpCategoryValue oldActivityStatus = CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME, ver.getCategories());
                
                logger.info(String.format("\tautoclosing activity %d, changing status ID from %s to %d and approvalStatus from <%s> to <%s>...", 
                        ver.getAmpActivityId(), oldActivityStatus == null ?  "<null>" : Long.toString(oldActivityStatus.getId()), closedCategoryValue, 
                        ver.getApprovalStatus(), newStatus));
                
                AmpTeamMember ampClosingMember = AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(ver.getTeam(), user);
                                
                AmpActivityVersion newVer = cloneActivity(session, ampClosingMember, ver, newStatus/*, projectStatusCategoryClass.getId()*/, closedCategoryValue);

                logger.info(String.format("... done, new amp_activity_id=%d\n", newVer.getAmpActivityId()));
            }
            session.setFlushMode(FlushMode.AUTO);
        }
        catch(Exception e) {
            // not rethrowing, because we don't want the thread to die
            logger.error("error while closing expired activities: ", e);
        }
    }
}
