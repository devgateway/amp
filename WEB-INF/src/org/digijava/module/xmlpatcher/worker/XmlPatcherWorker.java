/**
 * XmlPatcherWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.apache.log4j.Logger;
import org.digijava.module.xmlpatcher.dbentity.XmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org The worker
 *         abstraction for all patcher worker entities. Each worker has the log
 *         object passed around. There is only one log object at a time in the
 *         patcher.
 */
public abstract class XmlPatcherWorker<T> {
	protected static Logger logger = Logger.getLogger(XmlPatcherWorker.class);
	protected XmlPatchLog log;
	protected T entity;
	protected Object returnValue;
	
	/**
	 * Gets the entity that this worker wraps. This is the entity that the worker will process
	 * @return the entity provided in the constructor.
	 */
	public T getEntity() {
		return entity;
	}
	
	public XmlPatcherWorker(T entity, XmlPatchLog log) {
		this.log = log;
		this.entity = entity;
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
			logger.error(e);
			log.appendToLog(e);
		}
		return false;
	}
}
