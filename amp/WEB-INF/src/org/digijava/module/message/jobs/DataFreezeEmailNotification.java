package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeUtil;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.util.AmpDateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class DataFreezeEmailNotification extends ConnectionCleaningJob implements StatefulJob {
private static Logger logger = Logger.getLogger(DataFreezeEmailNotification.class);
    
    private static final String MAIL_SENDER = "ampsite@dg.org";
    private static final String MAIL_SUBJECT = "Data Freeze Notification";
    private static final String MAIL_BODY = "The AMP will be locked for edits in %d days, please make any necessary changes before (date).";
    public static final Integer DATA_FREEZE_NOTIFICATION_DAYS = 10;
    
    @Override
    public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
      
        
        List<AmpDataFreezeSettings> events =  DataFreezeUtil.getEnabledDataFreezeEvents(null);
        try {            
            List<User> users = DataFreezeUtil.getUsers();
            for(AmpDataFreezeSettings event : events) {
                Integer numberOfDaysToFreezing = AmpDateUtils.daysBetween(new Date(), event.getFreezingDate());
                if (numberOfDaysToFreezing == DATA_FREEZE_NOTIFICATION_DAYS) {
                    sendEmail(users);  
                }
            }              
           
        } catch (Exception ex) {
            logger.error("Error sending data freeze notification" + ex);
        } 
    }
    
    private void sendEmail(List<User> users) throws Exception{
        boolean asHtml = true;
        boolean log = true;
        boolean rtl = false;
        String from;
        String subject;
        String body;
        for(User user : users){                
            InternetAddress[] emailAddrs =  new InternetAddress[]{new InternetAddress(user.getEmail())};                
            from = TranslatorWorker.translateText(MAIL_SENDER, user.getRegisterLanguage().getCode(), 3L);
            body = TranslatorWorker.translateText(MAIL_BODY, user.getRegisterLanguage().getCode(), 3L);
            subject = TranslatorWorker.translateText(MAIL_SUBJECT, user.getRegisterLanguage().getCode(), 3L);
            body = String.format(body, getDataFreezeNotificationDays());
            DgEmailManager.sendMail(emailAddrs, from, null, null, subject, body, "UTF8", asHtml, log, rtl);
            
            //AuditLoggerUtil.logSentReminderEmails(hibernateSession,user);
        }
    }
    
    private int getDataFreezeNotificationDays() {
        return DATA_FREEZE_NOTIFICATION_DAYS;        
    }

}
