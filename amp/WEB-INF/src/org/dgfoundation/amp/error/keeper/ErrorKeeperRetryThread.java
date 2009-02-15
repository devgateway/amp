package org.dgfoundation.amp.error.keeper;

import java.util.LinkedList;

import org.dgfoundation.amp.ecs.client.ECSClient;
import org.dgfoundation.amp.ecs.common.ErrorKeeperItem;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * 
 * @author Arty
 *
 */
public class ErrorKeeperRetryThread extends Thread {
	public static final long EKR_MIN_RETRY_TIME = 15000; //15 seconds
	public static long EKR_RETRY_TIME = 15000; //15 seconds -- this will auto-adjust
	public static final long EKR_MAX_RETRY_TIME = 60*60*1000; //1 hour
	
	private LinkedList<ErrorKeeperItem> queue;
	private boolean stop = false;
	public ErrorKeeperRetryThread() {
		queue = new LinkedList<ErrorKeeperItem>();
	}
	
	@Override
	public void run() {
		boolean refailFlag = false;
		ErrorKeeperItem eki = null;
		ECSClient ecs = null;
		int count;
		boolean firstWait = true;
		
		String ecsEnabled = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED);
		if ("false".equalsIgnoreCase(ecsEnabled))
			return;
		
		while (!stop){
			try{
				eki = null;
				if (firstWait){
					firstWait = false;
					synchronized (queue) {
						queue.wait();
					}
				}
				try{
					do{
						synchronized (queue) {
							count = queue.size();
							if (count > 0)
								eki = queue.removeFirst();
						}
						if (count > 0){
							if (ecs == null){ //try connecting to the server
								//if this fails we wait again
								ecs = new ECSClient();
								
								//if we're here communication with the server succeeded
								refailFlag = false;
								if (EKR_MIN_RETRY_TIME < EKR_RETRY_TIME){ // the time had been adjusted
									EKR_RETRY_TIME /= 2;
								}
							}
							
							ecs.sendError(eki);
							//transmission succeeded
							eki = null; //watch for thread getting interrupted
						}
					}while (count > 0);
					
					
					
					//sent all errors ... reduce retry_time and wait on queue 
					if (EKR_MIN_RETRY_TIME < EKR_RETRY_TIME){ // let's adjust the time after a successful set of transmissions
						EKR_RETRY_TIME /= 2;
					}
					synchronized (queue) {
						queue.wait();
					}
				} catch (AMPException ex){
					//communication to the server failed we wait
					ecs = null; //any communication failure will invalidate the client object
					//we must put back the error in the queue
					if (eki != null){
						synchronized (queue) {
							queue.addFirst(eki);
						}
						eki = null; //we assure if thread get's interrupted this error doesn't get reinserted
					}
					
					if (refailFlag){ //repetitive failure?
						if (EKR_RETRY_TIME < EKR_MAX_RETRY_TIME){
							EKR_RETRY_TIME *= 2;
						}
					}
					refailFlag = true;
					this.sleep(EKR_RETRY_TIME);
				}

			} catch (InterruptedException ignored){
				//we must put back the error in the queue
				if (eki != null){
					synchronized (queue) {
						queue.addFirst(eki);
					}
					eki = null; //we assure if thread get's interrupted this error doesn't get reinserted
				}
			}
		}
	}
	
	/**
	 * Add an error to be resent ASAP
	 * Method is Thread Safe
	 *  
	 * @param eki
	 */
	public void add(ErrorKeeperItem eki){
		//access to queue is synchronized
		synchronized (queue) {
			queue.addLast(eki);
			queue.notify();
		}
	}
	
	public int getQueueSize(){
		int ret;
		synchronized (queue){
			ret = queue.size();
		}
		return ret;
	}
	
	public void stopRetryThread(){
		stop = true;
		this.interrupt();
	}
}
