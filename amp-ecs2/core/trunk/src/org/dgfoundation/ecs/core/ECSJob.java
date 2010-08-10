package org.dgfoundation.ecs.core;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dgfoundation.ecs.exceptions.ECSException;
import org.dgfoundation.ecs.exceptions.ECSIgnoreException;
import org.dgfoundation.ecs.keeper.ErrorKeeper;
import org.dgfoundation.ecs.keeper.ErrorKeeperItem;

/**
 * 
 * @author Arty
 * 
 */
public class ECSJob{
	private static Logger logger = Logger.getLogger(ECSJob.class);

	public static void execute(String serverName, ErrorKeeper ek){
		ConcurrentHashMap<String, ErrorKeeperItem> errors = ek.getErrors();

		logger.info("Starting to synchronize with ECS Server ...");
		try {
			Queue<ErrorKeeperItem> queue = new LinkedList<ErrorKeeperItem>();
			
			HttpClient ecs;
			try { //try connecting to the server first
				//logger.info("Contacting ECS Server ...");
				ecs = ECSClientManager.getClient();
			} catch (ECSException e) {
				//will be resent on next trigger
				logger.error("Couldn't contact ECS server", new ECSIgnoreException(e));
				return;
			}
			//logger.info("Connected to ECS Server!");
			
			Iterator<String> it = errors.keySet().iterator(); //weekly-consistent iterator => reading is thread safe
			while (it.hasNext()) {//remove in a thread-safe way items from the errors list
				String key = (String) it.next();
				ErrorKeeperItem eki = errors.remove(key);
				queue.add(eki);
			}
			
			//logger.info("Sending update on errors ...");
			try { //try connecting to the server first
				ecs.sendErrorList(serverName, queue);
				logger.info("Updates sent to server!");
			} catch (ECSException e) {
				logger.error("Couldn't send data to server", new ECSIgnoreException(e));
				
				logger.info("Putting the errors back in the list");
				try {
					for (ErrorKeeperItem eki: queue){
						ek.reinsert(eki);
					}
					logger.info("Done");
				} catch (Exception e2) {
					logger.error("error while adding errors back to the list", new ECSIgnoreException(e2));
				}
				return;
			}
			
			
			//logger.info("Get custom parameters ... ");
			String[] res = ecs.getParameters(serverName, "nothing-to-report");
			int updateDelay = -1;
			try {
				updateDelay = Integer.parseInt(res[1]);
				if (updateDelay < ECSRunner.ONE_MIN/2) //wrong value, invalidating
					updateDelay = -1;
			} catch (Exception e) {
				updateDelay = -1;
			}
			if (updateDelay != -1 && updateDelay != ECSRunner.DELAY_TIME){
				//logger.info(".... updating report interval from " + ECSRunner.DELAY_TIME/1000 + "s to " + updateDelay/1000 + "s");
				ECSRunner.DELAY_TIME = updateDelay;
			}
			else{
				//logger.info(".... report interval is: " + ECSRunner.DELAY_TIME/1000 + "s");
			}
			

			//logger.info("Synchronize with ECS Server finished...................................................");
		} catch (Exception e) {
			logger.error(new ECSIgnoreException(e));
			//e.printStackTrace();
		}
	}

}
