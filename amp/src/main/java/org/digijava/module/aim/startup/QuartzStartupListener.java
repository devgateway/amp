package org.digijava.module.aim.startup;

import org.digijava.kernel.jobs.RegisterWithAmpRegistryJob;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.trubudget.jobs.TruBudgetStatusSyncJob;
import org.digijava.module.um.util.DbUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class QuartzStartupListener extends QuartzInitializerListener {

    private Logger logger = LoggerFactory.getLogger(QuartzStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);

        initializeQuartz(sce);

        QuartzJobUtils.runJobIfNotPaused(RegisterWithAmpRegistryJob.NAME);
    }

    /**
     * Initialize Quartz using some customized AMP settings - set the amp
     * servlet context as a metadata of the scheduler, to be available later
     * inside Jobs - set the quartz datasource the same as the Hibernate
     * datasource to reduce configuration redundancy
     *
     * @param sce
     *            the servlet context event received from the initialization
     */
    private void initializeQuartz(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        try {
            SchedulerFactory factory = (SchedulerFactory) sc.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);

            Scheduler scheduler = factory.getScheduler();

            scheduler.getContext().put(Constants.AMP_SERVLET_CONTEXT, sc);

            enableActivityCloserIfNeeded();
//            enableTruBudgetStatusSyncJobIfNeeded();

        } catch (SchedulerException e) {
            logger.error("Failed to configure the scheduler.", e);
        }
    }

    /**
     * a somewhat hacky way of making sure a quartz job is added & configured to run hourly. Did not risk doing it
     * via an XML patch writing directly to qrtz_triggers, because there is a "job_data" binary column there.
     * so, the job is added via Java calls to Quartz classes. This is the only way I have found to "run this Java
     * code once and only once". A side-effect of this is that you won't ever be able to disable this job, but it
     * is ok - it does nothing when the corresponding feature is disabled from the GS, so you won't save resources
     * by disabling it
     */
    private void enableActivityCloserIfNeeded() {
        if (isActivityCloserEnabled()) {
            return; //nothing to do
        }

        AmpQuartzJobClass jobClass = QuartzJobClassUtils.getJobClassesByClassfullName(
                "org.digijava.module.message.jobs.CloseExpiredActivitiesJob");

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setDayOfMonth(1);
        jobForm.setDayOfWeek(1);
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
//      jobForm.setTriggerGroupName(triggerGroupName);
//      jobForm.setTriggerName(triggerName);
        jobForm.setTriggerType(QuartzJobForm.DAILY);
        jobForm.setExeTimeH("1");
        jobForm.setExeTimeM("1");
        jobForm.setExeTimeS("1");
        jobForm.setStartDateTime("01/01/2013");
        jobForm.setStartH("00");
        jobForm.setStartM("00");

        try {
            QuartzJobUtils.addJob(jobForm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isActivityCloserEnabled() {
        Connection connection = null;
        try {
            connection = PersistenceManager.getJdbcConnection();
            String statement = String.format("SELECT job_name from qrtz_job_details where job_class_name='%s'",
                    "org.digijava.module.message.jobs.CloseExpiredActivitiesJob");
            ResultSet resultSet = connection.createStatement().executeQuery(statement);
            return resultSet.next();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            PersistenceManager.closeQuietly(connection);
        }
    }

    /**
     * Auto-schedules TruBudgetStatusSyncJob to run daily, once, the first time the setting is enabled.
     * Unlike enableActivityCloserIfNeeded, this respects the trubudget.statusSyncJobEnabled global
     * setting so deployments can opt out of the automatic schedule.
     */
    private void enableTruBudgetStatusSyncJobIfNeeded() {
        if (isJobScheduled(TruBudgetStatusSyncJob.class.getName())) {
            return; //already scheduled
        }

        List<AmpGlobalSettings> settings = DbUtil.getGlobalSettingsBySection("trubudget");
        if (!"true".equalsIgnoreCase(DbUtil.getSettingValue(settings, "statusSyncJobEnabled"))) {
            return; //auto-run disabled via global setting
        }

        AmpQuartzJobClass jobClass = QuartzJobClassUtils.getJobClassesByClassfullName(
                TruBudgetStatusSyncJob.class.getName());
        if (jobClass == null) {
            logger.warn("TruBudget status sync job class is not registered yet; skipping auto-schedule.");
            return;
        }

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(QuartzJobForm.DAILY);
        jobForm.setExeTimeH("2");
        jobForm.setExeTimeM("0");
        jobForm.setExeTimeS("0");
        jobForm.setStartDateTime("01/01/2013");
        jobForm.setStartH("00");
        jobForm.setStartM("00");

        try {
            QuartzJobUtils.addJob(jobForm);
        } catch (Exception e) {
            logger.error("Failed to auto-schedule TruBudget status sync job.", e);
        }
    }

    private boolean isJobScheduled(String jobClassName) {
        Connection connection = null;
        try {
            connection = PersistenceManager.getJdbcConnection();
            String statement = String.format("SELECT job_name from qrtz_job_details where job_class_name='%s'",
                    jobClassName);
            ResultSet resultSet = connection.createStatement().executeQuery(statement);
            return resultSet.next();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            PersistenceManager.closeQuietly(connection);
        }
    }
}
