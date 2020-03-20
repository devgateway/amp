/**
 * XmlPatcherWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.apache.log4j.Logger;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         The worker abstraction for all patcher worker entities. Each worker
 *         has the log object passed around. There is only one log object at a
 *         time in the patcher which gathers all log information for the entire
 *         patch execution. The log is later persisted to the db. All functional
 *         patch entities have workers of their own, descendants of
 *         XmlPatcherWorker The execution of a worker is split into two
 *         functional steps
 *         <p>
 *         <li>
 *         <ul>
 *         the runtime check that verifies if the worker can proceed @see
 *         {@link #runTimeCheck()}
 *         <ul>
 *         the processing method that actually does the job @see
 *         {@link #process()}</li>
 *         @param <T> the type of the processed entity
 *         @param <P> the parent type of the processed entity
 */
public abstract class XmlPatcherWorker<T,P> {
    protected static Logger logger = Logger.getLogger(XmlPatcherWorker.class);
    protected AmpXmlPatchLog log;
    protected T entity;
    protected P parentEntity;
    protected Object returnValue;

    /**
     * Gets the entity that this worker wraps. This is the entity that the
     * worker will process
     * 
     * @return the entity provided in the constructor.
     */
    public T getEntity() {
        return entity;
    }
    
    public P getParentEntity() {
        return parentEntity;
    }

    public XmlPatcherWorker(T entity, P parentEntity, AmpXmlPatchLog log) {
        this.log = log;
        this.entity = entity;
        this.parentEntity=parentEntity;
        logger.debug("Worker initialized for " + entity);
    }

    /**
     * performs runtime check over the data to detect possible problems
     * 
     * @return true if the check is successful
     */
    protected abstract boolean runTimeCheck() throws XmlPatcherWorkerException;

    /**
     * the processing part of the worker, where the job is actually coded
     * 
     * @return true if job was success
     */
    protected abstract boolean process() throws XmlPatcherWorkerException;

    /**
     * Invoked externally to start the worker
     * 
     * @return true if successful
     */
    public boolean run() {
        try {
            if (runTimeCheck())
                return process();
        } catch (XmlPatcherWorkerException e) {
            logger.error(e.getMessage(), e);
            log.appendToLog(e);
        }
        return false;
    }

    /**
     * @return the returnValue
     */
    public Object getReturnValue() {
        return returnValue;
    }
    

}
