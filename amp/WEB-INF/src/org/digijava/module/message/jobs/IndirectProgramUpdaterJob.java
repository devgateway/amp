package org.digijava.module.message.jobs;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.mutable.MutableInt;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Ciubotaru
 */
public class IndirectProgramUpdaterJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(IndirectProgramUpdaterJob.class);

    private final User user = new User(CloseExpiredActivitiesJob.AMP_MODIFIER_USER_EMAIL,
            CloseExpiredActivitiesJob.AMP_MODIFIER_FIRST_NAME,
            CloseExpiredActivitiesJob.AMP_MODIFIER_LAST_NAME);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            List<Long> activityIds = PersistenceManager.supplyInTransaction(this::loadActivityIds);

            logger.info("Activities to process: " + activityIds.size());
            MutableInt errors = new MutableInt();

            activityIds.forEach(id -> {
                try {
                    PersistenceManager.inTransaction(() -> loadAndSaveActivity(id));
                } catch (Exception e) {
                    errors.increment();
                    logger.error("Failed to save activity " + id, e);
                }
            });

            logger.info("Finished updating activities with {} errors.", errors);
        } catch (Exception e) {
            logger.error("Job failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Long> loadActivityIds() {
        return PersistenceManager.getSession()
                .createCriteria(AmpActivity.class)
                .createAlias("actPrograms", "ap")
                .createAlias("ap.programSetting", "ps")
                .add(Restrictions.isNotNull("team"))
                .add(Restrictions.eq("ps.name", "Primary Program"))
                .setProjection(Projections.distinct(Projections.property("ampActivityId")))
                .list();
    }

    private void loadAndSaveActivity(Long id) {
        AmpActivityVersion activity = (AmpActivityVersion)
                PersistenceManager.getSession().load(AmpActivityVersion.class, id);

        AmpTeamMember member = getTeamMember(activity.getTeam(), user);

        saveActivity(activity, member);
    }

    private AmpTeamMember getTeamMember(AmpTeam team, User user) {
        try {
            return AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(team, user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain or create team member.", e);
        }
    }

    private void saveActivity(AmpActivityVersion activity, AmpTeamMember member) {
        Site site = SiteUtils.getDefaultSite();
        Locale locale = new Locale("en");
        String path = AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH;
        Boolean draft = activity.getDraft();
        SaveContext context = SaveContext.job();
        ActivityUtil.saveActivity(activity, null, member, site, locale, path, draft, context);
    }
}
