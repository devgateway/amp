package org.dgfoundation.amp.mondrian;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * the Quartz Job which runs the ETL
 *
 */
public class MondrianETLQuartzJob extends ConnectionCleaningJob implements StatefulJob {
    
    protected static Logger logger = Logger.getLogger(MondrianETLQuartzJob.class);
    
    @Override public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.warn("Starting Mondrian ETL...");
        EtlResult res = MondrianETL.runETL(false);
        logger.warn("the ETL result is: " + res);
        
        if (res.cacheInvalidated) {
            logger.warn("invalidating Mondrian cache...");
            MondrianReportUtils.flushCache();
        }
    }
    
    /**
     * a somewhat hacky way of making sure a quartz job is added & configured to run hourly. Did not risk doing it via an XML patch writing directly to qrtz_triggers, because there is a "job_data" binary column there.
     * so, the job is added via Java calls to Quartz classes. This is the only way I have found to "run this Java code once and only once". A side-effect of this is that you won't ever be able to disable this job, but it is ok - it does nothing when the corresponding feature is disabled from the GS, so you won't save resources by disabling it
     */
    public static void enableQuartzJob() {
        
        AmpQuartzJobClass jobClass = QuartzJobClassUtils.getJobClassesByClassfullName("org.dgfoundation.amp.mondrian.MondrianETLQuartzJob");
        
        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setDayOfMonth(1);
        jobForm.setDayOfWeek(1);
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
//      jobForm.setTriggerGroupName(triggerGroupName);
//      jobForm.setTriggerName(triggerName);
        jobForm.setTriggerType(QuartzJobForm.MONTHLY);
        jobForm.setExeTimeH("1");
        jobForm.setExeTimeM("1");
        jobForm.setExeTimeS("1");
        jobForm.setStartDateTime("05/05/2016");
        jobForm.setStartH("00");
        jobForm.setStartM("00");
        try {
            QuartzJobUtils.addJob(jobForm);
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
        
    }
}

