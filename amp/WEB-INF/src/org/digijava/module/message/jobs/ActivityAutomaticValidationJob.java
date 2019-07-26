package org.digijava.module.message.jobs;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.ActivityUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityAutomaticValidationJob extends ConnectionCleaningJob implements StatefulJob {

    public final static String AMP_VALIDATOR_USER_EMAIL = "amp_validator@amp.org";
    public final static String AMP_VALIDATOR_USER_FIRST_NAME = "Automatic";
    public final static String AMP_VALIDATOR_USER_LAST_NAME = "Validation";

    private static Logger logger = Logger.getLogger(ActivityAutomaticValidationJob.class);
    private static User user = new User(AMP_VALIDATOR_USER_EMAIL, AMP_VALIDATOR_USER_FIRST_NAME, AMP_VALIDATOR_USER_LAST_NAME);

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

        oldActivity.setApprovalStatus(ApprovalStatus.APPROVED);
        oldActivity.setApprovedBy(member);
        oldActivity.setApprovalDate(Calendar.getInstance().getTime());

        AmpActivityVersion auxActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivity(oldActivity, null,
                member, SiteUtils.getDefaultSite(), new java.util.Locale("en"),
                AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, oldActivity.getDraft(), SaveContext.job());

        return auxActivity;
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        logger.info("\tRunning the activity auto validation job...");

        try {
            Session session = PersistenceManager.getSession();
            AmpBackgroundActivitiesUtil.createActivityUserIfNeeded(user);
            PersistenceManager.cleanupSession(session); // commit user in case it was created
            session = PersistenceManager.getSession();
            session.setFlushMode(FlushMode.MANUAL);

            List<AmpActivityVersion> activityList = ActivityUtil.getActivitiesPendingValidation();

            if (activityList.size() > 0) {

                for (AmpActivityVersion activity : activityList) {

                    logger.info(String.format("\tAutovalidating activity %d, approvalStatus from <%s> to APPROVED...",
                            activity.getAmpActivityId(), activity.getApprovalStatus()));

                    AmpTeamMember ampValidatingMember = AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(activity.getTeam(), user);

                    AmpActivityVersion newActivityVersion = validateActivity(session, ampValidatingMember, activity);

                    logger.info(String.format("\t... done, new amp_activity_id = %d", newActivityVersion.getAmpActivityId()));
                }
                session.setFlushMode(FlushMode.AUTO);
            }
        } catch (Exception e) {
            // not rethrowing, because we don't want the thread to die
            logger.error("error while validating activities: ", e);
        }

        logger.info("\tActivity auto validation job finished");
    }

}
