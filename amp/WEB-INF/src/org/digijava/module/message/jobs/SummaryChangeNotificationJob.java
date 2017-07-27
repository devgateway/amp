package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.SummaryChangeData;
import org.digijava.module.aim.helper.SummaryChangeHtmlRenderer;
import org.digijava.module.aim.helper.SummaryChangesService;
import org.digijava.module.message.triggers.SummaryChangeNotificationTrigger;
import org.hibernate.Session;
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
import java.util.stream.Collectors;

public class SummaryChangeNotificationJob extends ConnectionCleaningJob implements StatefulJob {

    private static Logger logger = Logger.getLogger(SummaryChangeNotificationJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        PersistenceManager.getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {

                Date date = new Date();
                Map<String, Collection<AmpActivityVersion>> reminderUsers = SummaryChangesService.getValidators
                        (SummaryChangesService
                                .getActivitiesChanged(date));

                try {

                    for (String receiver : reminderUsers.keySet()) {
                        StringBuffer body = new StringBuffer();

                        LinkedHashMap<String, Object> activityList = SummaryChangesService.getSummaryChanges
                                (reminderUsers.get(receiver).stream().collect(Collectors.toList()));

                        User user = UserUtils.getUserByEmail(receiver);
                        for (String activity : activityList.keySet()) {
                            Session session = PersistenceManager.getRequestDBSession();
                            AmpActivityVersion activityVersion = (AmpActivityVersion) session.load(AmpActivityVersion
                                            .class,
                                    Long.parseLong(activity));

                            LinkedHashMap<String, Object> changesList = (LinkedHashMap) activityList.get(activity);
                            SummaryChangeHtmlRenderer renderer = new SummaryChangeHtmlRenderer(activityVersion,
                                    changesList, user.getRegisterLanguage().getCode());
                            body.append(renderer.render());
                        }
                        SummaryChangeData event = new SummaryChangeData();
                        event.setEmail(receiver);
                        event.setBody(body.toString());
                        event.setDate(date);
                        new SummaryChangeNotificationTrigger(event);
                    }
                } catch (Exception ex) {
                    logger.error("Error sending summary change notification" + ex);
                }
            }
        });


    }

}
