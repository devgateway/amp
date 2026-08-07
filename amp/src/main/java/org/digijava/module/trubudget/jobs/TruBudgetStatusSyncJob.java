package org.digijava.module.trubudget.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.message.jobs.AmpJobsUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.trubudget.dbentity.AmpComponentFundingTruWF;
import org.digijava.module.trubudget.dbentity.AmpComponentTruSubProject;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.util.ProjectUtil;
import org.digijava.module.um.util.DbUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;

/**
 * Periodically checks the live status of TruBudget projects/subprojects/workflowitems already linked to
 * AMP and caches "closed" status locally (TruBudgetActivity.projectClosed / AmpComponentTruSubProject.subProjectClosed
 * / AmpComponentFundingTruWF.workflowItemClosed).
 * The activity form reads these cached flags instead of calling the TruBudget API on every page render.
 */
public class TruBudgetStatusSyncJob extends ConnectionCleaningJob implements StatefulJob {

    public static final String NAME = "Sync TruBudget project/subproject closed status";

    private static final Logger logger = LoggerFactory.getLogger(TruBudgetStatusSyncJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();

        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        if (!"true".equalsIgnoreCase(getSettingValue(settings, "isEnabled"))) {
            logger.info("Skipping TruBudget status sync because TruBudget integration is disabled.");
            return;
        }
        boolean manualRun = Scheduler.DEFAULT_MANUAL_TRIGGERS.equals(context.getTrigger().getGroup());
        if (!manualRun && !"true".equalsIgnoreCase(getSettingValue(settings, "statusSyncJobEnabled"))) {
            logger.info("Skipping scheduled TruBudget status sync because the statusSyncJobEnabled setting is off.");
            return;
        }

        String token = DbUtil.getRootToken(settings);
        if (token == null || token.isEmpty()) {
            logger.warn("Skipping TruBudget status sync because root login did not return a token.");
            return;
        }

        Session session = PersistenceManager.getRequestDBSession();

        int projectsClosed = 0;
        List<TruBudgetActivity> openProjects = session.createQuery(
                "FROM " + TruBudgetActivity.class.getName() + " ta "
                        + "WHERE ta.projectClosed = false OR ta.projectClosed IS NULL",
                TruBudgetActivity.class).list();
        for (TruBudgetActivity truBudgetActivity : openProjects) {
            try {
                if (ProjectUtil.isProjectClosedInTruBudget(truBudgetActivity.getTruBudgetId(), settings, token)) {
                    ProjectUtil.markProjectClosedInAmp(truBudgetActivity.getTruBudgetId());
                    projectsClosed++;
                }
            } catch (Exception e) {
                logger.error("Failed to sync TruBudget project status for projectId={}",
                        truBudgetActivity.getTruBudgetId(), e);
            }
        }

        int subProjectsClosed = 0;
        List<AmpComponentTruSubProject> openSubProjects = session.createQuery(
                "FROM " + AmpComponentTruSubProject.class.getName() + " sp "
                        + "WHERE sp.subProjectClosed = false OR sp.subProjectClosed IS NULL",
                AmpComponentTruSubProject.class).list();
        for (AmpComponentTruSubProject subProject : openSubProjects) {
            try {
                if (ProjectUtil.isSubProjectClosedInTruBudget(subProject.getTruProjectId(), subProject.getTruSubProjectId(), settings, token)) {
                    ProjectUtil.markSubProjectClosedInAmp(subProject.getTruSubProjectId());
                    subProjectsClosed++;
                }
            } catch (Exception e) {
                logger.error("Failed to sync TruBudget subproject status for projectId={}, subProjectId={}",
                        subProject.getTruProjectId(), subProject.getTruSubProjectId(), e);
            }
        }

        int workflowItemsClosed = 0;
        List<AmpComponentFundingTruWF> openWorkflowItems = session.createQuery(
                "FROM " + AmpComponentFundingTruWF.class.getName() + " wf "
                        + "WHERE wf.workflowItemClosed = false OR wf.workflowItemClosed IS NULL",
                AmpComponentFundingTruWF.class).list();
        for (AmpComponentFundingTruWF workflowItem : openWorkflowItems) {
            try {
                if (ProjectUtil.isWorkflowItemClosedInTruBudget(workflowItem, settings, token)) {
                    ProjectUtil.markWorkflowItemClosedInAmp(workflowItem.getTruWFId());
                    workflowItemsClosed++;
                }
            } catch (Exception e) {
                logger.error("Failed to sync TruBudget workflowitem status for truWFId={}",
                        workflowItem.getTruWFId(), e);
            }
        }

        logger.info("TruBudget status sync completed. projectsClosed={}, subProjectsClosed={}, workflowItemsClosed={}",
                projectsClosed, subProjectsClosed, workflowItemsClosed);
    }
}

