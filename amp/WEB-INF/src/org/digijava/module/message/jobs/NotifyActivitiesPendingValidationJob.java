package org.digijava.module.message.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.triggers.ActivityValidationWorkflowTrigger;
import org.hibernate.jdbc.Work;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.mockrunner.mock.web.MockRequestDispatcher;

/**
 * Job to pull for activities that have been submitted but not yet validate
 * after GlobalSettingsConstants.DAYS_NOTIFY_ACTIVITY_SUBMITED_VALIDATION days
 * 
 * @author JulianEduardo
 *
 */
public class NotifyActivitiesPendingValidationJob extends ConnectionCleaningJob implements StatefulJob {

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		final List<AmpActivityVersion> activitiesToNotifiy = new ArrayList<>();
		PersistenceManager.getSession().doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				String daysToNotify = FeaturesUtil
						.getGlobalSettingValue(GlobalSettingsConstants.DAYS_NOTIFY_ACTIVITY_SUBMITED_VALIDATION);
				String condition = " where date_updated::date =(current_date - " + daysToNotify + ") "
						+ " and approval_status in (" + Constants.ACTIVITY_NEEDS_APPROVAL_STATUS + ") and draft=false";

				ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_activity", condition,
						TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(),
						connection, "*");
				RsInfo activities = v.fetch(null);
				while (activities.rs.next()) {
					AmpActivityVersion activity = new AmpActivityVersion();

					activity.setAmpActivityId(activities.rs.getLong("amp_activity_id"));
					activity.setName(activities.rs.getString("name"));
					activitiesToNotifiy.add(activity);
				}
				activities.close();
			}
		});
		for (AmpActivityVersion ampActivityVersion : activitiesToNotifiy) {
			new ActivityValidationWorkflowTrigger(ampActivityVersion);

		}

	}

}
