/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.um.util.AmpUserUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SendReminderEmailJob extends ConnectionCleaningJob implements StatefulJob{
    private static Logger logger = Logger.getLogger(SendReminderEmailJob.class);
    
    private static final String MAIL_SENDER="ampsite@dg.org";
    private static final String MAIL_SUBJECT="Reminder to enter to amp site";
    private static final String MAIL_BODY="You have a period of innactivity on ampsite, dont forget to use it";
    
    @Override
    public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        Collection<AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
        String name;
        String value;
        int period = 0;
        for (AmpGlobalSettings ampGls: col) {
            name = ampGls.getGlobalSettingsName();
            value = ampGls.getGlobalSettingsValue();
            if (name.equalsIgnoreCase(GlobalSettingsConstants.REMINDER_TIME)) {
                period = Integer.parseInt(value);
            } 
        }
        Session hibernateSession = null;

        try {
            hibernateSession = PersistenceManager.openNewSession();
            hibernateSession.setFlushMode(FlushMode.MANUAL);
            hibernateSession.beginTransaction();
            
            Date compareDate = AmpDateUtils.getDateBeforeDays(new Date(),
                    period);
            List<String> reminderUsers = AmpUserUtil.getUserReminder(compareDate);
            boolean asHtml = true;
            boolean log = true;
            boolean rtl = false;
            String from;
            String subject;
            String text;
            if (reminderUsers != null && reminderUsers.size() > 0) {
                
                logger.info("Enter DG Reminder Email manager");
                for(int i=0;i<reminderUsers.size();i++){
                    InternetAddress[] emailAddrs =  new InternetAddress[]{new InternetAddress(reminderUsers.get(i))};
                    User user = UserUtils.getUserByEmailAddress(reminderUsers.get(i));
                    from = TranslatorWorker.translateText(MAIL_SENDER, user.getRegisterLanguage().getCode(), 3L);
                    text = TranslatorWorker.translateText(MAIL_SUBJECT, user.getRegisterLanguage().getCode(), 3L);
                    subject = TranslatorWorker.translateText(MAIL_BODY, user.getRegisterLanguage().getCode(), 3L);
                    DgEmailManager.sendMail(emailAddrs, from, null, null, subject, text, "UTF8", asHtml, log, rtl);
                    AuditLoggerUtil.logSentReminderEmails(hibernateSession,user);
                }
                
                logger.info("Sent reminder emails");
                
            }
            hibernateSession.getTransaction().commit();
            hibernateSession.flush();
            hibernateSession.setFlushMode(FlushMode.AUTO);
        } catch (Throwable t) {
            if (hibernateSession.getTransaction().isActive()) {
                logger.info("Trying to rollback database transaction after exception");
                hibernateSession.getTransaction().rollback();
            }
        } finally {
            PersistenceManager.closeSession(hibernateSession);
        }
    }

}
