/**
 * XmlPatcherWorkerFactory.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import org.digijava.module.xmlpatcher.dbentity.XmlPatchLog;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.workers.XmlPatcherBSHLangWorker;
import org.digijava.module.xmlpatcher.workers.XmlPatcherHQLLangWorker;
import org.digijava.module.xmlpatcher.workers.XmlPatcherNativeLangWorker;
import org.digijava.module.xmlpatcher.workers.XmlPatcherSQLLangWorker;
import org.digijava.module.xmlpatcher.workers.XmlPatcherScriptWorker;
import org.digijava.module.xmlpatcher.workers.XmlPatcherWorker;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherWorkerFactory {
	public static XmlPatcherWorker createWorker(Object xmlEntity,
			XmlPatchLog log) {
		if (xmlEntity instanceof Script)
			return new XmlPatcherScriptWorker(xmlEntity, log);
		if (xmlEntity instanceof Lang) {
			Lang lang = (Lang) xmlEntity;

			// try to identify generic langs and invoke specific workers
			if (XmlPatcherConstants.ScriptLangs.BSH.equals(lang.getType()
					.value()))
				return new XmlPatcherBSHLangWorker(xmlEntity, log);
			if (XmlPatcherConstants.ScriptLangs.SQL.equals(lang.getType()
					.value()))
				return new XmlPatcherSQLLangWorker(xmlEntity, log);
			if (XmlPatcherConstants.ScriptLangs.HQL.equals(lang.getType()
					.value()))
				return new XmlPatcherHQLLangWorker(xmlEntity, log);

			// no generic language found, it means it uses native SQL, invoking
			// the native worker
			return new XmlPatcherNativeLangWorker(xmlEntity, log);
		}
		throw new RuntimeException("No worker associated with class "
				+ xmlEntity.getClass());
	}
}
