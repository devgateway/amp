package org.digijava.module.currencyrates;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Marcelo Sotero
 *
 */
public class CurrencyRatesRunner {
    private static Logger logger = Logger
            .getLogger(CurrencyRatesRunner.class);

    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;
    private JobDetail jobDetail;
    private SimpleTrigger simpleTrigger;
    private final String jobName = "jobDetail-s1";
    private final String jobGroupName = "jobDetailGroup-s1";
    private final String trgName = "simpleTrigger";
    private final String trgGroupName = "triggerGroup-s1";
    private int hour_update;
    private int min_update;
    private int hour_delay;
    private int min_delay;
    private final static long fONCE_PER_DAY = 1000 * 60 * 60 * 24;
    private final static int fONE_DAY = 1;
    private final static int fFOUR_AM = 4;
    private final static int fZERO_MINUTES = 0;
    private Class classJob;

    public CurrencyRatesRunner(int hour_init, int min_init, int hour_delay,
                               int min_delay) {
        this.hour_update = hour_init;
        this.min_update = min_init;
        this.hour_delay = hour_delay;
        this.min_delay = min_delay;
        this.classJob = CurrencyRatesQuartzJob.class;
    }

    public void restart(int hour, int min, int hour_repeat, int min_repeat) {
        this.hour_update = hour;
        this.min_update = min;
        this.hour_delay = hour_repeat;
        this.min_delay = min_repeat;

        simpleTrigger = new SimpleTrigger(trgName, trgGroupName, jobName,
                jobGroupName, initDate(), null, // end never
                SimpleTrigger.REPEAT_INDEFINITELY, getDelay());
        try {
            scheduler.rescheduleJob(trgName, trgGroupName, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            scheduler.shutdown(false);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            jobDetail = new JobDetail(jobName, jobGroupName, classJob);
            simpleTrigger = new SimpleTrigger(trgName, trgGroupName);
            simpleTrigger.setStartTime(this.initDate());
            simpleTrigger.setRepeatInterval(this.getDelay());
            simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            scheduler.scheduleJob(jobDetail, simpleTrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            //e.printStackTrace();
            logger.info("currency job already registered");
        }
    }

    private long getDelay() {
        if (hour_delay < 0 || hour_delay > 24 || min_delay < 0
                || min_delay > 60 || (hour_delay <= 0 && min_delay <= 0)) {
            return fONCE_PER_DAY;
        }
        return 1000 * 60 * (60 * hour_delay + min_delay);
    }

    private static Date getTomorrowMorning4am() {
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, fONE_DAY);
        Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR),
                tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE),
                fFOUR_AM, fZERO_MINUTES);
        return result.getTime();
    }

    private Date getTomorrow(int hour, int min) {
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, fONE_DAY);
        Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR),
                tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE),
                hour, min);
        return result.getTime();

    }

    private Date getToday(int hour, int min) {
        Calendar today = new GregorianCalendar();
        // tomorrow.add(Calendar.DATE);
        Calendar result = new GregorianCalendar(today.get(Calendar.YEAR), today
                .get(Calendar.MONTH), today.get(Calendar.DATE), hour, min);
        return result.getTime();

    }

    private Date initDate() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMin = cal.get(Calendar.MINUTE);
        GregorianCalendar gc1 = new GregorianCalendar(Calendar.YEAR,
                Calendar.MONTH, Calendar.DAY_OF_MONTH, hour_update, min_update,
                0);
        GregorianCalendar gc2 = new GregorianCalendar(Calendar.YEAR,
                Calendar.MONTH, Calendar.DAY_OF_MONTH, currentHour, currentMin,
                Calendar.SECOND);

        Date d1 = gc1.getTime();
        Date d2 = gc2.getTime();
        long l1 = d1.getTime();
        long l2 = d2.getTime();
        long difference = l1 - l2;
        if (difference <= 0) {
            return getTomorrow(hour_update, min_update);
        }
        return getToday(hour_update, min_update);
    }

    public int getHour_update() {
        return hour_update;
    }

    public int getMin_update() {
        return min_update;
    }

    public static void main(String args[]) {
        try {
            CurrencyRatesRunner qRunner = new CurrencyRatesRunner(18, 47, 0, 1);
            qRunner.launch();
            Thread.sleep(5000);
            qRunner.restart(18, 55, 0, 1);
            Thread.sleep(15000);
            qRunner.stop();
            Thread.sleep(5000);
            qRunner.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}