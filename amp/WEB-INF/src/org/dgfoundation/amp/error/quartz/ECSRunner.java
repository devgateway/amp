package org.dgfoundation.amp.error.quartz;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * @author Arty
 * 
 */
public class ECSRunner extends Thread {

	private Class classJob = ECSJob.class;
	private final static long ONE_DAY = 1000 * 60 * 60 * 24;
	private final static long TEAN_DAYS = 1000 * 60 * 60 * 24 * 10;
	private final static long FIVE_MIN = 1000 * 60 * 5;

	private static Logger logger = Logger.getLogger(ECSRunner.class);
	public final  static long DELAY_MIN_TIME = FIVE_MIN;
	public final  static long DELAY_MAX_TIME = ONE_DAY; // servers should report at least once a day
	public static long DELAY_TIME = FIVE_MIN;

	private Long ctime = System.currentTimeMillis();
	private boolean stopped = false;

	public void stopEcs() {
		try {
			stopped = true;
			interrupt();
			logger.info("Sync ECS Stoped.....................");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ECSJob ej = new ECSJob();
		logger.error("ECS Started DELAY is:" + DELAY_TIME/1000 + "s");
		while (!stopped ){
			try {
				sleep(DELAY_TIME);
				//launch periodic ECSJob
				ej.execute();
			} catch (Exception ignored) {
				logger.error("ECS Periodical Job error:", ignored);
			}
		}
	}
}
