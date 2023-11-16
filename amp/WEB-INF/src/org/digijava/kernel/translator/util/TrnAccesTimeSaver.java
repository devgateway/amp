package org.digijava.kernel.translator.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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

    public static boolean SKIP_ALL_UPDATES = false;
    
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
                    if (!SKIP_ALL_UPDATES)
                        save(message);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void save(Message message){
        Session session = null;
        Transaction tx = null;
        try {
            
            //since we are not interested in precious time: we are interesting if this  message was accessed
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date currentDate = cal.getTime();
            synchronized (this) {
                if (message.getLastAccessed() == null
                        || message.getLastAccessed().before(currentDate)) {
                    session = PersistenceManager.openNewSession();
                    Message msg = (Message) session.get(Message.class, message);
                    // for newly created messages the last access time is set in
                    // the save method
                    if (msg != null) {
                        tx = session.beginTransaction();
                        msg.setLastAccessed(new Timestamp(currentDate.getTime()));
                        session.update(msg);
                        tx.commit();
                        TranslatorWorker.getInstance("").refresh(msg);
                    }
                }
            }
            //logger.info("Saved timestamp for key:"+message.getKey()+" lang="+message.getLocale()+" Thread name="+Thread.currentThread().getName());
        }  catch (Exception e) {
            logger.error("Could not save timestamp for key: " + message.getKey() + " lang=" + message.getLocale()); 
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
        } finally {
            PersistenceManager.closeSession(session);
        }
    }
    
    
    /**
     * Shuts down saver.
     * usually this is called from other threads.
     */
    public void shutdown(){
        logger.info("Shutting down translation access time saver thread...");
        this.loop=false;
        

        synchronized(queue) {
            queue.notify();//saver may be waiting on queue
        }
    }
}
