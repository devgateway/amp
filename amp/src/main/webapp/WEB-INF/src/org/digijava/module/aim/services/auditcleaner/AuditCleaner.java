package org.digijava.module.aim.services.auditcleaner;

import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.currencyrates.DailyCurrencyRateSingleton;

/**
 * 
 * @Diego Dimunzio
 * 
 */
public final class AuditCleaner {
    private static Logger logger = Logger.getLogger(AuditCleaner.class);
    private final static long ONE_DAY = 1000 * 60 * 60 * 24;
    private static volatile AuditCleaner instance;
    private boolean status = false;
    private Date lastcleanup;
    private Date nextcleanup;
    private AuditCleanerRunner auditclenarrunner;
    private Long ctime = System.currentTimeMillis();

    public static synchronized AuditCleaner getInstance() {
        if (instance == null)
            synchronized (DailyCurrencyRateSingleton.class) {
                if (instance == null)
                    instance = new AuditCleaner();
            }
        return instance;
    }

    public void start() {
        auditclenarrunner = new AuditCleanerRunner();
        auditclenarrunner.launch();
        status = true;
    }

    public void stop() {
        auditclenarrunner.stop();
        status = false;
    }

    public boolean isRunning() {
        return status;
    }

    public Date getLastcleanup() {
        return lastcleanup;
    }

    public void setLastcleanup(Date lastcleanup) {
        this.lastcleanup = lastcleanup;
    }

    public Date getNextcleanup() {
        return nextcleanup;
    }

    public void setNextcleanup(Date nextcleanup) {
        this.nextcleanup = nextcleanup;
    }

    public String getRemainingdays() {
        if (getNextcleanup()!=null){
            Long diff = (getNextcleanup().getTime() - ctime) / ONE_DAY;
            return diff.toString();
        }
        return null;
    }
}
