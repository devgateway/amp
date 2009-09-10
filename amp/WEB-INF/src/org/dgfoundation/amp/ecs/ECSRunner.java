package org.dgfoundation.amp.ecs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.webservice.ECSConstants;
import org.dgfoundation.amp.error.AMPException;
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

	private final static long ONE_DAY = 1000 * 60 * 60 * 24;
	private final static long FIVE_MIN = 1000 * 60 * 5;
	private final static long ONE_MIN = 1000 * 60;
	

	private static Logger logger = Logger.getLogger(ECSRunner.class);
	public final  static long DELAY_MIN_TIME = FIVE_MIN;
	public final  static long DELAY_MAX_TIME = ONE_DAY; // servers should report at least once a day
	public static long DELAY_TIME = FIVE_MIN/5;

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
		ECSClient cl;
		
		try {
			logger.info("Informing of server startup:");
			cl = new ECSClient();
			String[] res = cl.getParameters(ECSConstants.SERVER_STARTING_UP);
			String currentAMPServerName = cl.getCurrentAMPServerName();
			logger.info(".... sent");
			if (currentAMPServerName.compareTo(res[0]) != 0){
				logger.info(".... simple check fail");
			}
			else{
				logger.info(".... check ok");	
				int updateDelay = -1;
				try {
					updateDelay = Integer.parseInt(res[1]);
					if (updateDelay < ONE_MIN/2) //wrong value, invalidating
						updateDelay = -1;
				} catch (Exception e) {
					updateDelay = -1;
				}
				if (updateDelay != -1 && updateDelay != DELAY_TIME){
					logger.info(".... updating report interval from " + DELAY_TIME/1000 + "s to " + updateDelay/1000 + "s");
					DELAY_TIME = updateDelay;
				}
				else{
					logger.info(".... report interval is: " + DELAY_TIME/1000 + "s");
				}
			}
		} catch (Exception e) {
			logger.error("Error while trying to get parameters", e);
		}

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
