/**
 * XmlPatcherScriptWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.core.XmlPatcherWorkerFactory;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherScriptWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Responsible for
 *         processing patch script entities
 */
public class XmlPatcherScriptWorker extends XmlPatcherWorker<Script,Object> {
    private Lang generic;

    public XmlPatcherScriptWorker(Script script,  Object parentEntity, AmpXmlPatchLog log) {
        super(script, parentEntity, log);
        generic = null;
    }

    @Override
    protected boolean process() throws XmlPatcherWorkerException {
        // always get non generic entities first and process them
        Script script = (Script) entity;
        List<Lang> langs = script.getLang();
        Iterator<Lang> iterator = langs.iterator();
        while (iterator.hasNext()) {
            Lang next = iterator.next();
            if (next.equals(generic))
                continue;
            XmlPatcherWorker<?, ?> worker = XmlPatcherWorkerFactory.createWorker(
                    next, entity, log);
            if (worker.run()){
                returnValue=worker.getReturnValue();
                return true;
            }
        }
        // if we reached here, it means we were unable to find any non-generic
        // language to execute
        // so we proceed with the generic, if any
        if (generic == null)
            return false;
        XmlPatcherWorker<?,?> worker = XmlPatcherWorkerFactory.createWorker(generic, entity,
                log);
        
        if(!worker.run()) return false;
        returnValue=worker.getReturnValue();
        return true;
    }

    @Override
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        Script script = (Script) entity;
        List<Lang> langs = script.getLang();

        // check if there are type duplicate languages added
        HashMap<String, Lang> langTypes = new HashMap<String, Lang>();
        Iterator<Lang> i = langs.iterator();
        while (i.hasNext()) {
            Lang next = i.next();
            langTypes.put(next.getType().value(), next);
        }
        if (langTypes.size() != langs.size())
            throw new XmlPatcherScriptWorkerException(
                    "The script needs to hold lang entities with distinct types");

        // check if there is more than one generic language specified. also set
        // the generic, if any
        for (int x = 0; x < XmlPatcherConstants.ScriptLangs.generics.length; x++) {
            String genericStr = XmlPatcherConstants.ScriptLangs.generics[x];
            if (langTypes.keySet().contains(genericStr))
                if (langTypes.size() < langs.size()) // if sizes differ it means
                                                        // this is the second
                                                        // found generic
                    throw new XmlPatcherScriptWorkerException(
                            "Inside one script entity you may specify only one of the generic language types: "
                                    + XmlPatcherConstants.ScriptLangs.generics);
                else
                    generic = langTypes.remove(genericStr);
        }
        return true;

    }

}
