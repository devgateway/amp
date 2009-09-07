/**
 * XmlPatcherPatchWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 4, 2009
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.dbentity.XmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Patch;

/**
 * XmlPatcherPatchWorker
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 4, 2009
 */
public class XmlPatcherPatchWorker extends XmlPatcherWorker<Patch> {
	
	
	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherPatchWorker(Patch entity, XmlPatchLog log) {
		super(entity, log);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get all script entities inside apply entities and invoke workers for them
	 * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#process()
	 */
	@Override
	protected boolean process() throws XmlPatcherWorkerException {
		return true;
	}

	/**
	 * This will check the trigger entity of the patch, and decide if execution may continue
	 * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#runTimeCheck()
	 */
	@Override
	protected boolean runTimeCheck() throws XmlPatcherWorkerException {
		// TODO Auto-generated method stub
		return true;
	}

}
