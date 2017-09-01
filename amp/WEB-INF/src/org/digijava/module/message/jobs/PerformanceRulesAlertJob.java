package org.digijava.module.message.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.message.triggers.PerformanceRuleAlertTrigger;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class PerformanceRulesAlertJob extends ConnectionCleaningJob implements StatefulJob {
    
    private static Logger logger = Logger.getLogger(PerformanceRulesAlertJob.class);
    
    public static final String PERFORMANCE_RULE_FM_PATH = "Project Performance Alerts Manager";
    public static final String DEFAULT_LOCALE_LANGUAGE = "en";

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Running the performance rule alert job...");
        
        if (isPerformanceAlertIssuesEnabled()) {
            List<Long> actIds = org.digijava.module.aim.util.ActivityUtil.getValidatedActivityIds();
            
            List<AmpActivityVersion> activitiesWithPerformanceIssues = processActivitiesWithPerformanceRules(actIds);
            if (activitiesWithPerformanceIssues != null) {
                new PerformanceRuleAlertTrigger(activitiesWithPerformanceIssues);
            }
        } else {
            logger.info("Performance rule module is not enabled...");
        }
        
        logger.info(String.format("Performance rule alert job completed.\n"));
    }
    
    private boolean isPerformanceAlertIssuesEnabled() {
        return FMUtil.isFmVisible(PERFORMANCE_RULE_FM_PATH, AmpFMTypes.MODULE);
    }

    /**
     * Checks and updates the performance alert level of the activity (if is needed). 
     * If the performance level is not changed, the activity will be skipped.
     * 
     * @param actIds
     */
    private List<AmpActivityVersion> processActivitiesWithPerformanceRules(List<Long> actIds) {
        List<AmpActivityVersion> activitiesWithPerformanceIssues = new ArrayList<>();
        PerformanceRuleManager ruleManager = PerformanceRuleManager.getInstance();
        
        boolean noMatcherFound = ruleManager.getPerformanceRuleMatchers().isEmpty();
        
        if (noMatcherFound) {
            logger.info("No performance rule matcher found.");
        }
        
        for (Long actId : actIds) {
            String lockKey = null;
            try {
                lockKey = ActivityGatekeeper.lockActivity(Long.toString(actId), 0L);
                if (lockKey != null) {
                    AmpActivityVersion a = org.digijava.module.aim.util.ActivityUtil.loadActivity(actId);
                    
                    AmpCategoryValue activityLevel = ruleManager.getPerformanceIssueFromActivity(a);
                    AmpCategoryValue higherLevel = null;
                    
                    if (!noMatcherFound) {
                        AmpCategoryValue matchedLevel = ruleManager.matchActivity(a);
                        higherLevel = ruleManager.getHigherLevel(matchedLevel, activityLevel);
                    }
                   
                    if (!Objects.equals(activityLevel, higherLevel)) {
                        AmpActivityVersion updActivity = updateActivity(a);
                        
                        logger.info(String.format("\tactivity %d, updated performance alert level from <%s> to <%s>...",
                                actId, activityLevel == null ? null : activityLevel.getLabel(),
                                higherLevel == null ? null : higherLevel.getLabel()));
                        
                        if (higherLevel != null) {
                            activitiesWithPerformanceIssues.add(updActivity);
                        }
                        
                        logger.info(String.format("... done, new amp_activity_id=%d\n", 
                                updActivity.getAmpActivityId()));
                    } else if (activityLevel != null) {
                        activitiesWithPerformanceIssues.add(a);
                    }
                } else {
                    logger.error(String.format("Activity is locked, amp_activity_id=%d", actId));
                }
            } catch (Exception e) {
                logger.error(String.format("\tactivity %d, error occured... %s", actId, e.getMessage()), e);
            } finally {
                if (lockKey != null) {
                    ActivityGatekeeper.unlockActivity(Long.toString(actId), lockKey);
                }
                PersistenceManager.endSessionLifecycle();
            }
        }
        
        return activitiesWithPerformanceIssues;
    }

    /**
     * Update existing activity by creating new version.
     * 
     * @param oldActivity
     * @return newActivity
     * @throws Exception
     */
    private AmpActivityVersion updateActivity(AmpActivityVersion oldActivity) throws Exception {
        Session session = PersistenceManager.getSession();
        AmpActivityVersion updatedActivity = null;
        
        AmpTeamMember modifyingMember = AmpBackgroundActivitiesCloser
                .createActivityCloserTeamMemberIfNeeded(oldActivity.getTeam());
        
        updatedActivity = ActivityUtil.saveActivityNewVersion(oldActivity, null, modifyingMember,
                oldActivity.getDraft(), session, false, false);
            
        Locale javaLocale = new Locale(DEFAULT_LOCALE_LANGUAGE);
        
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true, 
                SiteUtils.getDefaultSite(), javaLocale, 
                updatedActivity, oldActivity);
        
        return updatedActivity;
    }
}
