package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.Collection;
import java.util.Date;

public class ActivityVersionDeletionJob extends ConnectionCleaningJob implements StatefulJob { 
    
    private static final Logger logger = Logger.getLogger(ActivityVersionDeletionJob.class);
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException{      
        
        int period = 0;     
        int queueSize = 0;
        
        String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ACTIVITY_LIFE);
        if (value != null)
            period = Integer.parseInt(value);
        
        value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.VERSION_QUEUE_SIZE);
        if (value != null)
            queueSize = Integer.parseInt(value);
        
        Session hibernateSession = PersistenceManager.openNewSession();

        try {
            hibernateSession.setFlushMode(FlushMode.MANUAL);
            hibernateSession.beginTransaction();
            Date compareDate = AmpDateUtils.getDateBeforeDays(new Date(), period);
            Collection<AmpActivityVersion> activities = ActivityUtil.getOldActivities(hibernateSession, queueSize, compareDate);
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
            logger.error("Error while flushing session:", t);
            if (hibernateSession.getTransaction().isActive()) {
                logger.info("Trying to rollback database transaction after exception");
                hibernateSession.getTransaction().rollback();
            }
        } finally {
            PersistenceManager.closeSession(hibernateSession);
        }
    }
}
