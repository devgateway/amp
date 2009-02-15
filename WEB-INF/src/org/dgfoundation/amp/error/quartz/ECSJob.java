package org.dgfoundation.amp.error.quartz;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ecs.xhtml.param;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.client.ECSClient;
import org.dgfoundation.amp.ecs.common.ECSCustom;
import org.dgfoundation.amp.ecs.common.ECSParameters;
import org.dgfoundation.amp.ecs.common.ErrorKeeperItem;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.keeper.ErrorKeeper;
import org.dgfoundation.amp.error.keeper.ErrorKeeperRAM;
import org.dgfoundation.amp.error.keeper.ErrorKeeperRetryThread;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Arty
 * 
 */
public class ECSJob implements Job {
	private static Logger logger = Logger.getLogger(ECSJob.class);
	/**
	 * 
	 */
	public void execute(JobExecutionContext contex)
			throws JobExecutionException {

		ErrorKeeperRAM ek = new ErrorKeeperRAM();
		ConcurrentHashMap<String, ErrorKeeperItem> errors = ek.getErrors();
		ConcurrentHashMap<String, Date> loggedErrors = ek.getLoggedErrors();

		logger.info("Starting to synchronize with ECS Server ...");
		try {
			Queue<ErrorKeeperItem> queue = new LinkedList<ErrorKeeperItem>();
			
			ECSClient ecs;
			try { //try connecting to the server first
				logger.info("Contacting ECS Server ...");
				ecs = new ECSClient();
			} catch (AMPException e) {
				//will be resent on next quartz trigger
				logger.error("Couldn't contact ECS server", e);
				return;
			}
			logger.info("Connected to ECS Server!");
			
			Iterator<String> it = errors.keySet().iterator(); //weekly-consistent iterator => reading is thread safe
			while (it.hasNext()) {//remove in a thread-safe way items from the errors list
				String key = (String) it.next();
				ErrorKeeperItem eki = errors.remove(key);
				queue.add(eki);
			}
			
			logger.info("Sending update on errors ...");
			try { //try connecting to the server first
				ecs.sendErrorList(queue);
			} catch (AMPException e) {
				logger.error("Couldn't send data", e);
				
				logger.info("Putting the errors back in the list");
				try {
					for (ErrorKeeperItem eki: queue){
						ek.reinsert(eki);
					}
					logger.info("Done");
				} catch (Exception e2) {
					logger.error("error while adding errors back to the list", e);
				}
				return;
			}
			logger.info("Updates sent!");
			
			logger.info("Get custom parameters ...");
			try {
				
				ECSParameters param = new ECSParameters();
				param.setRunOnceCustom(false);
				String report = "";
				
				report += "Errors Queue Size = " + errors.size() + "\n";
				report += "Logged Errors Size = " + loggedErrors.size() + "\n";
				report += "Retry Thread Retry Time = " + ek.getRetryThread().EKR_RETRY_TIME + "\n";
				report += "Retry Thread Queue Size = " + ek.getRetryThread().getQueueSize() + "\n";
				
				do{
					param = ecs.getParameters(report);
					report = null;
					if (param.isRunOnceCustom()){
						ECSCustom custom = ecs.runCustom();
						custom.run();
					}
				} while (param.isRunOnceCustom());
			} catch (Exception e) {
				logger.error("Couldn't get parameters", e);
				return;
			}

			logger.info("Synchronize with ECS Server finished...................................................");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
