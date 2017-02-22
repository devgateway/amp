package org.digijava.module.message.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesValidator;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityAutomaticValidationJob extends ConnectionCleaningJob implements StatefulJob {

    private static Logger logger = Logger.getLogger(ActivityAutomaticValidationJob.class);

    /**
     * validate activity, sets modifying member, modification date, approval status, etc
     *
     * @param session
     * @param member
     * @param oldActivity
     * @return
     * @throws CloneNotSupportedException
     */
    protected AmpActivityVersion validateActivity(Session session, AmpTeamMember member, AmpActivityVersion oldActivity) throws CloneNotSupportedException {
        AmpActivityVersion prevVersion = oldActivity.getAmpActivityGroup().getAmpActivityLastVersion();
        oldActivity.setModifiedDate(Calendar.getInstance().getTime());
        oldActivity.setModifiedBy(member);

        oldActivity.setApprovalStatus(Constants.APPROVED_STATUS);
        oldActivity.setApprovedBy(member);
        oldActivity.setApprovalDate(Calendar.getInstance().getTime());

        AmpActivityVersion auxActivity = null;
        try {
            auxActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(oldActivity, null,
                    member, oldActivity.getDraft(), session, false, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        session.flush();

        java.util.Locale javaLocale = new java.util.Locale("en");
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true,
                SiteUtils.getDefaultSite(), javaLocale,
                auxActivity, prevVersion);
        return auxActivity;
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        final List<Long> activityIdsList = new ArrayList<>();
        PersistenceManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                logger.info("\tRunning the activity auto validation job...");

                String daysToValidation = FeaturesUtil
                        .getGlobalSettingValue(GlobalSettingsConstants.NUMBER_OF_DAYS_BEFORE_AUTOMATIC_VALIDATION);

                String queryCondition = String.format(
                        " WHERE date_updated <= ( current_date - %s ) "
                                + " and approval_status in ( %s ) and draft=false and not amp_team_id is null ",
                        daysToValidation, Constants.ACTIVITY_NEEDS_APPROVAL_STATUS);

                ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_activity", queryCondition,
                        TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(),
                        connection, "*");
                RsInfo activities = v.fetch(null);
                while (activities.rs.next()) {
                    activityIdsList.add(activities.rs.getLong("amp_activity_id"));
                }
                activities.close();

            }
        });

        try {

            if (activityIdsList.size() > 0) {

                Session session = PersistenceManager.getSession();
                AmpBackgroundActivitiesValidator.createActivityValidatorUserIfNeeded();
                PersistenceManager.cleanupSession(session); // commit user in case it was created
                session = PersistenceManager.getSession();
                session.setFlushMode(FlushMode.MANUAL);

                List<AmpActivityVersion> activityList = ActivityUtil.getActivitiesPendingValidation(activityIdsList);

                for (AmpActivityVersion activity : activityList) {

                    logger.info(String.format("\tAutovalidating activity %d, approvalStatus from <%s> to APPROVED...",
                            activity.getAmpActivityId(), activity.getApprovalStatus()));

                    AmpTeamMember ampValidatingMember = AmpBackgroundActivitiesValidator.createActivityValidatorTeamMemberIfNeeded(activity.getTeam());

                    AmpActivityVersion newActivityVersion = validateActivity(session, ampValidatingMember, activity);

                    logger.info(String.format("\t... done, new amp_activity_id = %d\n", newActivityVersion.getAmpActivityId()));
                }
                session.setFlushMode(FlushMode.AUTO);
            }
        } catch (Exception e) {
            // not rethrowing, because we don't want the thread to die
            logger.error("error while validating activities: ", e);
        }

    }

}
