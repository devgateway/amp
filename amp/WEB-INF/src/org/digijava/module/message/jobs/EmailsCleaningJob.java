package org.digijava.module.message.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.dbentity.AmpEmailReceiver;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.Date;

public class EmailsCleaningJob extends ConnectionCleaningJob implements StatefulJob {
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer datesToKeep =
                FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.TIME_TO_KEEP_UNSENT_EMAILS);
        DateTime dateTime = new DateTime(new Date());

        dateTime = dateTime.minusDays(datesToKeep );

        String deleteEmailReceivers = String.format("delete from %s er where er.creationDate < :creationDate",
                AmpEmailReceiver.class
                        .getName());

        PersistenceManager.getSession()
                .createQuery(deleteEmailReceivers).setParameter("creationDate",dateTime.toDate()).executeUpdate();

    }
}
