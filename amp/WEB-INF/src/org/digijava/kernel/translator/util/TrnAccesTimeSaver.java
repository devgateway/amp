package org.digijava.kernel.translator.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Thread to save translation access time stamps.
 * Reads messages from queue and saves then in db.
 * Usually this should be low priority thread.
 * Priority and queue are specified in constructor.
 * Note: note used since AMP-9402
 * @author Irakli Kobiashvili
 *
 */
public class TrnAccesTimeSaver implements Runnable {

	private static Logger logger = Logger.getLogger(TrnAccesTimeSaver.class);
	private TrnAccessUpdateQueue queue = TrnAccessUpdateQueue.getQueue();
	private int priority;
	private volatile boolean loop;
	
	/**
	 * Creates saver with 
	 */
	public TrnAccesTimeSaver(){
		this(Thread.MIN_PRIORITY);
		this.loop = true;
	}
	
	public TrnAccesTimeSaver(int priority){
		this.priority = priority;
	}
	
	@Override
	public void run() {
		while (this.loop) {
			Message message = null;
			try {
				message = queue.get();//will block here if there are no messages in queue
				if (message != null) {
					save(message);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private void save(Message message){
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.update(message);
			//logger.info("Saved timestamp for key:"+message.getKey()+" lang="+message.getLocale()+" Thread name="+Thread.currentThread().getName());
			
		} catch (Exception e) {
			logger.error(e);		
		}finally{
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception e3) {
				logger.error("cannot release db session in translation access time saver",e3);
			}
		}
	}
	
	
	/**
	 * Shuts down saver.
	 * usually this is called from other threads.
	 */
	public void shutdown(){
		logger.info("Shutting down translation access time saver thread...");
		this.loop=false;
		//queue.notify();//saver may be waiting on queue
	}
}
