package org.digijava.module.trubudget.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.message.jobs.AmpJobsUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.util.TruBudgetAuthUtil;
import org.digijava.module.trubudget.util.ProjectUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;

/**
 * Bulk-imports AMP projects into TruBudget.
 *
 * This job is intended for explicit synchronization runs and is separate from
 * the DataImporter flow, which does not auto-sync with TruBudget.
 */
public class BulkTruBudgetProjectImportJob extends ConnectionCleaningJob implements StatefulJob {

    public static final String NAME = "Bulk import AMP projects into TruBudget";
    private static final int JOB_FIRST_START_DELAY_IN_MIN = 5;

    private static final Logger logger = LoggerFactory.getLogger(BulkTruBudgetProjectImportJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();

        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        if (!"true".equalsIgnoreCase(getSettingValue(settings, "isEnabled"))) {
            logger.info("Skipping bulk TruBudget project import because TruBudget integration is disabled.");
            return;
        }

        User bulkImportUser = getBulkImportUser(settings);
        if (bulkImportUser == null) {
            logger.warn("Skipping bulk TruBudget project import because bulk import user is not configured properly.");
            return;
        }

        try {
            TruBudgetAuthUtil.doActualTruBudgetLogin(bulkImportUser);
        } catch (Exception e) {
            logger.warn("Unable to authenticate configured bulk import user.", e);
        }

        String token = ProjectUtil.getTrubudgetToken();
        if (token == null || token.isEmpty()) {
            logger.warn("Skipping bulk TruBudget project import because TruBudget token is not available.");
            return;
        }

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpActivityVersion> latestActivities = session.createQuery(
                "SELECT av FROM " + AmpActivityVersion.class.getName() + " av " +
                        "WHERE av.ampId IS NOT NULL " +
                        "AND av.draft = false " +
                        "AND (av.archived IS NULL OR av.archived = false) " +
                        "AND av.ampActivityId = (" +
                        "  SELECT MAX(av2.ampActivityId) FROM " + AmpActivityVersion.class.getName() + " av2 " +
                        "  WHERE av2.ampId = av.ampId" +
                        ")",
                AmpActivityVersion.class)
                .list();

        int created = 0;
        int updated = 0;
        int failed = 0;

        for (AmpActivityVersion activity : latestActivities) {
            try {
                Query<AmpComponent> query = session.createQuery(
                                "FROM " + AmpComponent.class.getName()
                                        + " ac WHERE ac.activity = :activity AND ac.activity IS NOT NULL",
                                AmpComponent.class)
                        .setCacheable(true);
                query.setParameter("activity", activity.getAmpActivityId(), LongType.INSTANCE);

                TruBudgetActivity existingTruBudgetActivity = ProjectUtil.activityAlreadyInTrubudget(activity.getAmpId());
                if (existingTruBudgetActivity == null) {
                    ProjectUtil.createProject(activity, query.list(), activity.getName());
                    created++;
                } else {
                    ProjectUtil.updateProject(existingTruBudgetActivity.getTruBudgetId(), activity, query.list(),
                            activity.getName());
                    updated++;
                }
            } catch (URISyntaxException e) {
                failed++;
                logger.error("TruBudget URL error while importing activity {}", activity.getAmpActivityId(), e);
            } catch (Exception e) {
                failed++;
                logger.error("Unexpected error while importing activity {} into TruBudget",
                        activity.getAmpActivityId(), e);
            }
        }

        logger.info("Bulk TruBudget import completed. created={}, updated={}, failed={}", created, updated, failed);
    }

    private User getBulkImportUser(List<AmpGlobalSettings> settings) {
        try {
            String userId = getSettingValue(settings, GlobalSettingsConstants.TRUBUDGET_BULK_IMPORT_USER);
            if (userId == null || userId.trim().isEmpty()) {
                logger.warn("Bulk import user global setting is empty.");
                return null;
            }

            User user = UserUtils.getUser(Long.parseLong(userId));
            if (user == null) {
                logger.warn("Configured bulk import user was not found. userId={}", userId);
                return null;
            }

            if (!Boolean.TRUE.equals(user.getTruBudgetEnabled()) || user.getTruBudgetPassword() == null) {
                logger.warn("Configured bulk import user is not TruBudget-ready. userId={}", userId);
                return null;
            }

            return user;
        } catch (RuntimeException e) {
            logger.warn("Unable to resolve TruBudget bulk import user from global settings.", e);
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(BulkTruBudgetProjectImportJob.class.getName());
        jobClass.setName(NAME);
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(true);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(QuartzJobForm.DAILY);
        jobForm.setExeTimeH("1");
        jobForm.setExeTimeM("0");

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, JOB_FIRST_START_DELAY_IN_MIN);
        Date startDate = instance.getTime();

        jobForm.setStartDateTime(new SimpleDateFormat("dd/MM/yyyy").format(startDate));
        jobForm.setStartH(new SimpleDateFormat("HH").format(startDate));
        jobForm.setStartM(new SimpleDateFormat("mm").format(startDate));

        QuartzJobUtils.addJob(jobForm);
    }
}
