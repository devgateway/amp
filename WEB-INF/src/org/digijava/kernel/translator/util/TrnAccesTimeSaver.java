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
	}
	
	public TrnAccesTimeSaver(int priority){
		this.priority = priority;
	}
	
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
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.update(message);
			//logger.info("Saved timestamp for key:"+message.getKey()+" lang="+message.getLocale()+" Thread name="+Thread.currentThread().getName());
			tx.commit();
		} catch (Exception e) {
			logger.error("Cannot update translation is access time saver",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e2) {
					logger.error("Cannot rallback translation update in access time saver",e2);
				}
			}
		}finally{
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception e3) {
				logger.error("cannot release db session in translation access time saver",e3);
			}
		}
	}
	
	/**
	 * Starts up saver.
	 */
	public void startup(){
		logger.info("Starting up translation access time saver thread...");
		Thread thread = new Thread(this);
		thread.setPriority(this.priority);
		thread.setName(TrnAccessUpdateQueue.ALLOWED_THREAD_NAME);
		this.loop = true;
		thread.start();
		
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
