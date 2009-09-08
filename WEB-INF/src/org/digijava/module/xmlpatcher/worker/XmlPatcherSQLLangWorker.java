/**
 * XmlPatcherSQLLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides support to
 *         process generic SQL lang-type Use this with all generic SQL scripts,
 *         that do not require special server-specific queries
 */
public class XmlPatcherSQLLangWorker extends XmlPatcherLangWorker {

	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherSQLLangWorker(Lang entity, AmpXmlPatchLog log) {
		super(entity, log);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#process()
	 */
	@Override
	protected boolean process() throws XmlPatcherWorkerException {
		// TODO Auto-generated method stub
		return false;
	}

}
