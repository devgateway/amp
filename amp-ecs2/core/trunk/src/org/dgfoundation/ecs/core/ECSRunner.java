package org.dgfoundation.ecs.core;


import org.apache.log4j.Logger;
import org.dgfoundation.ecs.core.ECSConstants;
import org.dgfoundation.ecs.exceptions.ECSIgnoreException;
import org.dgfoundation.ecs.keeper.ErrorKeeper;

/**
 * 
 * @author Arty
 * 
 */
public class ECSRunner extends Thread {

	private final static long ONE_DAY = 1000 * 60 * 60 * 24;
	private final static long FIVE_MIN = 1000 * 60 * 5;
	public final static long ONE_MIN = 1000 * 60;
	

	private static Logger logger = Logger.getLogger(ECSRunner.class);
	public final  static long DELAY_MIN_TIME = FIVE_MIN;
	public final  static long DELAY_MAX_TIME = ONE_DAY; // servers should report at least once a day
	public static long DELAY_TIME = FIVE_MIN/5;

	private boolean stopped = false;
	private String serverName;
	public String getServerName() {
		return serverName;
	}

	private ErrorKeeper ekeeper;
	
	public ECSRunner(String serverName, ErrorKeeper ekeeper) {
		this.serverName = serverName;
		this.ekeeper = ekeeper;
	}

	public void stopEcs() {
		try {
			String[] res = ECSClientManager.getClient().getParameters(serverName, ECSConstants.SERVER_SHUTDOWN);
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
		
		try {
			logger.info("Informing of server startup:");
			String[] res = ECSClientManager.getClientBypassStarted().getParameters(serverName, ECSConstants.SERVER_STARTING_UP);
			logger.info(".... sent");
			if (serverName.compareTo(res[0]) != 0){
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
				
				ECSClientManager.setStartedOk(true);
			}
		} catch (Exception e) {
			logger.error("Error while trying to get parameters", new ECSIgnoreException(e));
		}

		while (!stopped ){
			try {
				sleep(DELAY_TIME);
				//launch periodic ECSJob
				ECSJob.execute(serverName, ekeeper);
			}catch (InterruptedException ignored) {
				// stopping mechanism
			}catch (Exception ignored) {
				logger.error("ECS Periodical Job error:", new ECSIgnoreException(ignored));
			}
		}
	}
}
