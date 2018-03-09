/**
 * XmlPatcherPatchWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 4, 2009
 */
package org.digijava.module.xmlpatcher.worker;

import java.util.Iterator;

import org.digijava.module.xmlpatcher.core.XmlPatcherWorkerFactory;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.jaxb.ScriptGroup;
import org.digijava.module.xmlpatcher.jaxb.Trigger;

/**
 * XmlPatcherPatchWorker
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 4, 2009
 */
public class XmlPatcherPatchWorker extends XmlPatcherWorker<Patch,Object> {
    
    
    /**
     * @param entity
     * @param log
     */
    public XmlPatcherPatchWorker(Patch entity,  Object parentEntity,AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
        // TODO Auto-generated constructor stub
    }

    /**
     * Get all script entities inside apply entity and invoke workers for them.
     * If any of the workers fail, then then entire apply entity fails
     * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#process()
     */
    @Override
    protected boolean process() throws XmlPatcherWorkerException {
        ScriptGroup apply = entity.getApply();
        Iterator<Script> i=apply.getScript().iterator();
        while(i.hasNext()) {
            Script script = i.next();
            XmlPatcherWorker<?,?> worker = XmlPatcherWorkerFactory.createWorker(script,entity, log);
            if(!worker.run()) return false;
        }
    return true;    
    }

    /**
     * This will check the trigger entity of the patch, and decide if execution may continue
     * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#runTimeCheck()
     */
    @Override
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        Trigger trigger = getEntity().getTrigger();
        if(trigger==null) return true;
        XmlPatcherWorker<?,?> worker = XmlPatcherWorkerFactory.createWorker(trigger,entity, log);
        if(!worker.run()) return false;
        return (Boolean) worker.getReturnValue();
    }

}
