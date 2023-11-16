package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.activity.ActivityCloser;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.ArrayList;
import java.util.List;

public class CloseExpiredActivitiesJob extends ConnectionCleaningJob implements StatefulJob {

    public final static String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    public final static String AMP_MODIFIER_FIRST_NAME = "AMP";
    public final static String AMP_MODIFIER_LAST_NAME = "Activities Modifier";

    private static Logger logger = Logger.getLogger(CloseExpiredActivitiesJob.class);
    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            String val = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATICALLY_CLOSE_ACTIVITIES);
            boolean autoClosingEnabled = "true".equalsIgnoreCase(val);
            if (!autoClosingEnabled)
                return; // feature disabled => nothing to do;

            logger.info("Running the activity autocloser job...");

            TLSUtils.getThreadLocalInstance().site = SiteUtils.getDefaultSite();

            Session session = PersistenceManager.getSession();

            AmpBackgroundActivitiesUtil.createActivityUserIfNeeded(user);
            PersistenceManager.cleanupSession(session); // commit user in case it was created
            session = PersistenceManager.getSession();

            Long closedCategoryValue =
                    FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);

            String filterQuery = "SELECT amp_activity_last_version_id "
                    + "FROM amp_activity_group aag "
                    + "WHERE aag.autoclosedonexpiration = false "
                    + "AND aag.amp_activity_last_version_id IN ("
                    + "  SELECT amp_activity_id "
                    + "  FROM amp_activity "
                    + "  WHERE (draft IS NULL or draft=false) "
                    + "  AND (amp_team_id IS NOT NULL) "
                    + "  AND (deleted IS NULL OR deleted = false) "
                    + "  AND approval_status IN (" + Util.toCSString(AmpARFilter.VALIDATED_ACTIVITY_STATUS) + ") "
                    + "  AND actual_completion_date < now()"
                    + ") "
                    + "AND aag.amp_activity_last_version_id IN ("
                    + "  select amp_activity_id "
                    + "  FROM v_status "
                    + "  WHERE amp_status_id != " + closedCategoryValue
                    + ")";

            List<Long> eligibleIds = DbHelper.getInActivities(filterQuery);

            List<AmpActivityVersion> closeableActivities;
            if (eligibleIds.isEmpty())
                closeableActivities = new ArrayList<>();
            else {
                String queryString = "select aav from " + AmpActivityVersion.class.getName() +
                        " aav " + "where aav.ampActivityId IN (" + Util.toCSString(eligibleIds) + ")";
                closeableActivities = session.createQuery(queryString).list();
            }

            session.setFlushMode(FlushMode.MANUAL);

            (new ActivityCloser()).closeActivities(closeableActivities, closedCategoryValue, SaveContext.job());

            session.setFlushMode(FlushMode.AUTO);
        } catch (Exception e) {
            // not rethrowing, because we don't want the thread to die
            logger.error("error while closing expired activities: ", e);
        }
    }

}
