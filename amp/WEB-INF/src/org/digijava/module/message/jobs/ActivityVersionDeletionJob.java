package org.digijava.module.message.jobs;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityVersionDeletionJob implements StatefulJob {

	private static final Logger logger = Logger
			.getLogger(ActivityVersionDeletionJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Collection<AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
		String name;
		String value;
		int period = 0;
		int quequeSize = 0;
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			AmpGlobalSettings ampGls = (AmpGlobalSettings) iterator.next();
			name = ampGls.getGlobalSettingsName();
			value = ampGls.getGlobalSettingsValue();
			if (name.equalsIgnoreCase(GlobalSettingsConstants.ACTIVITY_LIFE)) {
				period = Integer.parseInt(value);
			} else if (name
					.equalsIgnoreCase(GlobalSettingsConstants.VERSION_QUEUE_SIZE)) {
				quequeSize = Integer.parseInt(value);
			}
		}
		Session hibernateSession = null;

		try {
			hibernateSession = PersistenceManager.openNewSession();
			hibernateSession.setFlushMode(FlushMode.MANUAL);
			hibernateSession.beginTransaction();
			Date compareDate = AmpDateUtils.getDateBeforeDays(new Date(),
					period);
			Collection<AmpActivityVersion> activities = ActivityUtil
					.getOldActivities(hibernateSession, quequeSize, compareDate);
			if (activities != null && activities.size() > 0) {
				for (AmpActivityVersion act : activities) {
					ActivityUtil.deleteAllActivityContent(act, hibernateSession);
					hibernateSession.flush();
					hibernateSession.delete(act);
					hibernateSession.flush();

				}
			}
			hibernateSession.getTransaction().commit();
			hibernateSession.flush();
			hibernateSession.setFlushMode(FlushMode.AUTO);
		} catch (Throwable t) {
			try {
				logger.error("Error while flushing session:", t);
				if (hibernateSession.getTransaction().isActive()) {
					logger.info("Trying to rollback database transaction after exception");
					hibernateSession.getTransaction().rollback();
				} else
					logger.error("Can't rollback transaction because transaction not active");
			} catch (Throwable rbEx) {
				logger.error("Could not rollback transaction after exception!",
						rbEx);
			}
		} finally {
			hibernateSession.close();
		}

	}

}
