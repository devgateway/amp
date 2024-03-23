package org.digijava.kernel.translator.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue for updating message records when they are accessed.
 * Uses map to get single latest record per key.
 * @author Irakli Kobiashvili
 *
 */
public class TrnAccessUpdateQueue {
    private static TrnAccessUpdateQueue _instance = null;
    private HashMap<Message,Timestamp> map = new HashMap<Message,Timestamp>();
    private BlockingQueue<DelayedMessage> queue = new LinkedBlockingQueue<>();
    private static Logger logger = Logger.getLogger(TrnAccessUpdateQueue.class);
    public static final String ALLOWED_THREAD_NAME = "pool-1-thread-1";

    private static final long DELAY = 1000;

    /**
     * A delayed message that can be retrieved only after a specified delay.
     */
    private static class DelayedMessage {

        private long delayUntil;
        private Message message;

        DelayedMessage(Message message) {
            this.message = message;
            this.delayUntil = System.currentTimeMillis() + DELAY;
        }

        public Message getMessage() throws InterruptedException {
            long millisToWait = delayUntil - System.currentTimeMillis();
            if (millisToWait > 0) {
                Thread.sleep(millisToWait);
            }
            return message;
        }
    }

    /**
     * Private constructor. use {@link #getQueue()} to get singleton instance.
     */
    private TrnAccessUpdateQueue(){
        
    }
    
    /**
     * Get the singleton queue instance.
     * @return
     */
    public synchronized static TrnAccessUpdateQueue getQueue(){
        if (_instance == null){
            _instance = new TrnAccessUpdateQueue();
        }
        return _instance;
    }
    
    /**
     * Puts message in save queue.
     * This also saves time stamp when message was added to the queue without 
     * changing message itself, so no need for clients to update message before calling this method.
     * If there is message with same key already in the queue, 
     * then only time stamp of the message in the queue
     * will be updated and it will not save same message twice. 
     * @param message
     */
    public void put(Message message){
            try {
                queue.put(new DelayedMessage(message));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }
    }
    
    /**
     * Retrieves next message from queue.
     * Retrieved message will be also removed from queue.
     * If there is no messages in queue then this method will block until other thread places new message for processing.
     * Because of this blocking do not call this method from struts tread, only saver thread should call this.
     * @return translation message
     * @throws InterruptedException
     */
    public Message get() throws InterruptedException {
        return queue.take().getMessage();
        //it may be null if notify() was called from other method, not from put(), for example when shutting down saver thread.
        /*if (message != null){
            Timestamp time = map.remove(message);
            message.setLastAccessed(time);
            return message;
        }
        return null;*/
    }
    
    public void clear()
    {
        this.queue.clear();
    }
    
    /**
     * Removes message from save queue.
     * This may be needed when normal save routine is executed from user actions. In such case 
     * it will be latest version of the message and queue version will not overwrite or will not repeat save. 
     * @param message
     */
    /*public synchronized void remove(Message message){
        map.remove(message);
        queue.remove(message);
    }*/
}
