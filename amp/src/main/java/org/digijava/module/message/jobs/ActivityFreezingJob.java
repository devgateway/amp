package org.digijava.module.message.jobs;

import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeService;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityFreezingJob extends ConnectionCleaningJob implements StatefulJob {

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();
        Long ampTeamId = FeaturesUtil
                .getGlobalSettingValueLong(GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FROM_JOB);
        AmpJobsUtil.setTeamForNonRequestReport(ampTeamId);
        DataFreezeService.processFreezingEvent();

    }

}
