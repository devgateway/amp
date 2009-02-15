package org.dgfoundation.amp.error.quartz;

import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.currencyrates.DailyCurrencyRateSingleton;

/**
 * 
 * @author Arty
 * 
 */
public final class ECS {
	private static Logger logger = Logger.getLogger(ECS.class);
	private final static long ONE_DAY = 1000 * 60 * 60 * 24;
	private static volatile ECS instance;
	private boolean status = false;
	private Date lastcleanup;
	private Date nextcleanup;
	private ECSRunner ecsrunner;
	private Long ctime = System.currentTimeMillis();

	public static synchronized ECS getInstance() {
		if (instance == null)
			synchronized (ECS.class) {
				if (instance == null)
					instance = new ECS();
			}
		return instance;
	}

	public void start() {
		ecsrunner = new ECSRunner();
		ecsrunner.launch();
		status = true;
	}

	public void stop() {
		if (ecsrunner == null)
			ecsrunner = new ECSRunner();
			
		ecsrunner.stop();
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
