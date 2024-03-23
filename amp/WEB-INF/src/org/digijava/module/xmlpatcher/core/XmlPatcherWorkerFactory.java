/**
 * XmlPatcherWorkerFactory.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.jaxb.*;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.worker.*;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherWorkerFactory {
    public static XmlPatcherWorker<?, ?> createWorker(Object xmlEntity,Object parentEntity,
            AmpXmlPatchLog log) {
        if (xmlEntity instanceof Script)
            return new XmlPatcherScriptWorker((Script) xmlEntity,parentEntity, log);
        if (xmlEntity instanceof Patch)
            return new XmlPatcherPatchWorker((Patch) xmlEntity,parentEntity, log);
        if (xmlEntity instanceof Condition)
            return new XmlPatcherConditionWorker((Condition) xmlEntity,(Trigger) parentEntity, log);
        if (xmlEntity instanceof Trigger)
            return new XmlPatcherTriggerWorker((Trigger) xmlEntity,(Patch) parentEntity, log);
    
        if (xmlEntity instanceof Lang) {
            Lang lang = (Lang) xmlEntity;
            Script parentScript=(Script) parentEntity;
            // try to identify generic langs and invoke specific workers
            if (XmlPatcherConstants.ScriptLangs.BSH.equals(lang.getType()
                    .value()))
                return new XmlPatcherBSHLangWorker(lang, parentScript, log);
            if (XmlPatcherConstants.ScriptLangs.SQL.equals(lang.getType()
                    .value()))
                return new XmlPatcherSQLLangWorker(lang,parentScript, log);
            if (XmlPatcherConstants.ScriptLangs.HQL.equals(lang.getType()
                    .value()))
                return new XmlPatcherHQLLangWorker(lang,parentScript, log);

            // no generic language found, it means it uses native SQL, invoking
            // the native worker
            return new XmlPatcherNativeLangWorker(lang,parentScript, log);
        }
        throw new RuntimeException("No worker associated with class "
                + xmlEntity.getClass());
    }
}
