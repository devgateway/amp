package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.message.triggers.PerformanceRuleAlertTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.*;

public class PerformanceRulesAlertJob extends ConnectionCleaningJob implements StatefulJob {
    
    private static Logger logger = Logger.getLogger(PerformanceRulesAlertJob.class);
    
    public static final String PERFORMANCE_RULE_FM_PATH = "Project Performance Alerts Manager";
    public static final String DEFAULT_LOCALE_LANGUAGE = "en";

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
        ruleManager.deleteAllActivityPerformanceRules(PersistenceManager.getSession());
        
        boolean noMatcherFound = ruleManager.getPerformanceRuleMatchers().isEmpty();
        
        if (noMatcherFound) {
            logger.info("No performance rule matcher found.");
        }
        
        for (Long actId : actIds) {
            List<PerformanceIssue> failedIssues = new ArrayList<>();
            try {
                Set<AmpPerformanceRule> matchedRules = new HashSet<>();
                AmpActivityVersion a = org.digijava.module.aim.util.ActivityUtil.loadActivity(actId);
                
                if (!noMatcherFound) {
                    failedIssues = ruleManager.findPerformanceIssues(a);
                    matchedRules = ruleManager.getPerformanceRulesFromIssues(failedIssues);
                }
                
                ruleManager.updateActivityPerformanceRules(actId, matchedRules);
               
                final StringJoiner matchedLabelJoiner = new StringJoiner(",");
                matchedRules.stream().forEach(r -> matchedLabelJoiner.add(
                        String.format("%s (%s)", 
                        PerformanceRuleManager.PERF_ALERT_TYPE_TO_DESCRIPTION.get(r.getTypeClassName()),
                        r.getLevel().getLabel())));
                
                logger.info(String.format("\tactivity %d, alert rules: <%s>...",
                        actId, matchedRules.isEmpty() ? null : matchedLabelJoiner.toString()));
                
                if (!failedIssues.isEmpty()) {
                    activitiesWithPerformanceIssues.put(a, failedIssues);
                }
            } catch (Exception e) {
                logger.error(String.format("\tactivity %d, error occured... %s", actId, e.getMessage()), e);
            }
        }
        ruleManager.updatePerformanceAlertETL(PersistenceManager.getSession());
        
        return activitiesWithPerformanceIssues;
    }

}
