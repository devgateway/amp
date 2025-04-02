package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.SummaryChange;
import org.digijava.module.aim.helper.SummaryChangeData;
import org.digijava.module.aim.helper.SummaryChangeHtmlRenderer;
import org.digijava.module.aim.helper.SummaryChangesService;
import org.digijava.module.message.triggers.SummaryChangeNotificationTrigger;
import org.hibernate.jdbc.Work;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SummaryChangeNotificationJob extends ConnectionCleaningJob implements StatefulJob {

    private static Logger logger = Logger.getLogger(SummaryChangeNotificationJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        PersistenceManager.getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {

                Date date = new Date();
                LinkedHashMap<Long, Long> activitiesIds = SummaryChangesService.getActivities(date);

                if (activitiesIds.size() > 0) {

                    LinkedHashMap<Long, Collection<SummaryChange>> activityList = SummaryChangesService
                            .processActivity(activitiesIds);

                    Map<String, Set<AmpActivityVersion>> reminderUsers = SummaryChangesService
                            .getValidators(activityList);

                    try {
                        String bodyHeader = "The following activities, for which you are an approver, "
                                + "were either added or edited within the last 24 hours. The details are below.";
                        String subject = "Summary of changes in AMP";

                        for (String receiver : reminderUsers.keySet()) {
                            StringBuffer body = new StringBuffer();

                            User user = UserUtils.getUserByEmailAddress(receiver);

                            for (AmpActivityVersion activity : reminderUsers.get(receiver)) {

                                Collection<SummaryChange> changesList = activityList.get(activity.getAmpActivityId());
                                SummaryChangeHtmlRenderer renderer = new SummaryChangeHtmlRenderer(activity,
                                        changesList, user.getRegisterLanguage().getCode());
                                if (body.length() == 0) {
                                    body.append(renderer.renderWithLegend());
                                } else {
                                    body.append(renderer.render());
                                }
                            }
                            SummaryChangeData event = new SummaryChangeData();
                            event.setEmail(receiver);
                            event.setSubject(subject);
                            event.setBody(body.toString());
                            event.setDate(date);
                            event.setBodyHeader(bodyHeader);
                            new SummaryChangeNotificationTrigger(event);
                        }
                    } catch (Exception ex) {
                        logger.error("Error sending summary change notification" + ex);
                    }
                }
            }
        });


    }

}
