package org.digijava.kernel.jobs;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.message.jobs.AmpJobsUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Clean resources created in IATI and not referenced by activities
 *
 * @author Viorel Chihai
 */
public class CleanIATIResourceJob extends ConnectionCleaningJob {

    public static final String NAME = "Clean orphan resources created in IATI";

    private static final int JOB_FIRST_START_DELAY_IN_MIN = 5;

    private final Logger logger = LoggerFactory.getLogger(CleanIATIResourceJob.class);

    private final ResourceService resourceService = new ResourceService();

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();

        List<String> activityDocumentUUIDs = getActivityDocuments();
        List<String> iatiResourcesUUIDs = resourceService.getPrivateUuidsCreatedInIATI();

        iatiResourcesUUIDs.removeAll(activityDocumentUUIDs);
        for (String uuidToDelete : iatiResourcesUUIDs) {
            DocumentManagerUtil.deleteNode(null, uuidToDelete);
            logger.info(String.format("Resource %s is not referenced by any activity and was removed from the system",
                    uuidToDelete));
        }
    }

    public List<String> getActivityDocuments() {
        return PersistenceManager.getSession().doReturningWork(conn -> {
            String query = "SELECT DISTINCT uuid FROM amp_activity_document";
            return SQLUtils.fetchAsList(conn, query, 1);
        });
    }

    @SuppressWarnings("unused")
    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(CleanIATIResourceJob.class.getName());
        jobClass.setName(NAME);
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
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
