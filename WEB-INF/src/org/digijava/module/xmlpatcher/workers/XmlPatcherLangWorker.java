/**
 * XmlPatcherLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.workers;

import org.digijava.module.xmlpatcher.dbentity.XmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public abstract class XmlPatcherLangWorker extends XmlPatcherWorker {

	/**
	 * Gets the entity of this worker as a Lang object
	 * @return
	 */
	public Lang getLang() {
		return (Lang) entity;
	}
	
	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherLangWorker(Object entity, XmlPatchLog log) {
		super(entity, log);
		// TODO Auto-generated constructor stub
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.digijava.module.xmlpatcher.workers.XmlPatcherWorker#runTimeCheck()
	 */
	@Override
	protected boolean runTimeCheck() throws XmlPatcherWorkerException {
		//check if the language type is supported by the xmlpatcher
		boolean langFound=false;
		for(int i=0;i<XmlPatcherConstants.ScriptLangs.all.length;i++) {
			if(XmlPatcherConstants.ScriptLangs.all[i].equals(getLang().getType().value())) return langFound=true;
		}
		if(!langFound) throw new XmlPatcherLangWorkerException("Unsupported language "+getLang().getType());
		
		return true;
	}
}
