package org.digijava.module.message.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.message.triggers.PerformanceRuleAlertTrigger;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class PerformanceRulesAlertJob extends ConnectionCleaningJob implements StatefulJob {
    
    private static final String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    private static final String AMP_MODIFIER_FIRST_NAME = "AMP";
    private static final String AMP_MODIFIER_LAST_NAME = "Activities Modifier";
    
    private static Logger logger = Logger.getLogger(PerformanceRulesAlertJob.class);
    
    public static final String PERFORMANCE_RULE_FM_PATH = "Project Performance Alerts Manager";
    public static final String DEFAULT_LOCALE_LANGUAGE = "en";

    private User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // we populate mockrequest to be able to translate
        AmpJobsUtil.populateRequest();
        TLSUtils.forceLangCodeToSiteLangCode();
        logger.info("Running the performance rule alert job...");

        if (isPerformanceAlertIssuesEnabled()) {
            List<Long> actIds = org.digijava.module.aim.util.ActivityUtil.getValidatedActivityIds();
            
            Map<AmpActivityVersion, List<PerformanceIssue>> actsWithPerfIssues = processActivities(actIds);
            new PerformanceRuleAlertTrigger(actsWithPerfIssues);
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
    private Map<AmpActivityVersion, List<PerformanceIssue>> processActivities(List<Long> actIds) {
        
        Map<AmpActivityVersion, List<PerformanceIssue>> activitiesWithPerformanceIssues = new HashMap<>();
        PerformanceRuleManager ruleManager = PerformanceRuleManager.getInstance();
        
        boolean noMatcherFound = ruleManager.getPerformanceRuleMatchers().isEmpty();
        
        if (noMatcherFound) {
            logger.info("No performance rule matcher found.");
        }
        
        for (Long actId : actIds) {
            String lockKey = null;
            List<PerformanceIssue> failedIssues = new ArrayList<>();
            try {
                lockKey = ActivityGatekeeper.lockActivity(Long.toString(actId), 0L);
                if (lockKey != null) {
                    AmpActivityVersion a = org.digijava.module.aim.util.ActivityUtil.loadActivity(actId);
                    
                    Set<AmpPerformanceRule> activityRules = a.getPerformanceRules();
                    Set<AmpPerformanceRule> matchedRules = new HashSet<>();
                    
                    if (!noMatcherFound) {
                        failedIssues = ruleManager.findPerformanceIssues(a);
                        matchedRules = ruleManager.getPerformanceRulesFromIssues(failedIssues);
                    }
                   
                    if (!ruleManager.isEqualPerformanceRuleCollection(matchedRules, activityRules)) {
                        AmpActivityVersion updActivity = updateActivity(a);
                        a = updActivity;
                        
                        final StringJoiner actLabelJoiner = new StringJoiner(",");
                        activityRules.stream().forEach(s -> actLabelJoiner.add(s.getLevel().getLabel()));
                        
                        final StringJoiner matchedLabelJoiner = new StringJoiner(",");
                        matchedRules.stream().forEach(s -> matchedLabelJoiner.add(s.getLevel().getLabel()));
                        
                        logger.info(String.format("\tactivity %d, updated performance alert level from <%s> to <%s>...",
                                actId, activityRules.isEmpty() ? null : actLabelJoiner.toString(),
                                        matchedRules.isEmpty() ? null : matchedLabelJoiner.toString()));
                        
                        logger.info(String.format("... done, new amp_activity_id=%d\n", 
                                updActivity.getAmpActivityId()));
                    }
                    
                    if (!failedIssues.isEmpty()) {
                        activitiesWithPerformanceIssues.put(a, failedIssues);
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
        
        AmpTeamMember modifyingMember = AmpBackgroundActivitiesUtil
                .createActivityTeamMemberIfNeeded(oldActivity.getTeam(), user);
        updatedActivity = ActivityUtil.saveActivityNewVersion(oldActivity, null, modifyingMember,
                Boolean.TRUE.equals(oldActivity.getDraft()), session, false, false);
            
        Locale javaLocale = new Locale(DEFAULT_LOCALE_LANGUAGE);
        
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true, 
                SiteUtils.getDefaultSite(), javaLocale, 
                updatedActivity, oldActivity);
        
        return updatedActivity;
    }
}
