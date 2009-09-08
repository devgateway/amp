/**
 * XmlPatcherWorkerFactory.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.worker.XmlPatcherBSHLangWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherHQLLangWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherNativeLangWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherPatchWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherSQLLangWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherScriptWorker;
import org.digijava.module.xmlpatcher.worker.XmlPatcherWorker;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherWorkerFactory {
	public static XmlPatcherWorker createWorker(Object xmlEntity,
			AmpXmlPatchLog log) {
		if (xmlEntity instanceof Script)
			return new XmlPatcherScriptWorker((Script) xmlEntity, log);
		if (xmlEntity instanceof Patch)
			return new XmlPatcherPatchWorker((Patch) xmlEntity, log);
		if (xmlEntity instanceof Lang) {
			Lang lang = (Lang) xmlEntity;

			// try to identify generic langs and invoke specific workers
			if (XmlPatcherConstants.ScriptLangs.BSH.equals(lang.getType()
					.value()))
				return new XmlPatcherBSHLangWorker(lang, log);
			if (XmlPatcherConstants.ScriptLangs.SQL.equals(lang.getType()
					.value()))
				return new XmlPatcherSQLLangWorker(lang, log);
			if (XmlPatcherConstants.ScriptLangs.HQL.equals(lang.getType()
					.value()))
				return new XmlPatcherHQLLangWorker(lang, log);

			// no generic language found, it means it uses native SQL, invoking
			// the native worker
			return new XmlPatcherNativeLangWorker(lang, log);
		}
		throw new RuntimeException("No worker associated with class "
				+ xmlEntity.getClass());
	}
}
