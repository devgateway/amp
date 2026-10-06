/**
 * XmlPatcherTriggerWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.core.XmlPatcherWorkerFactory;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Condition;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.digijava.module.xmlpatcher.jaxb.Trigger;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherTriggerWorker extends XmlPatcherWorker<Trigger, Patch> {

    public XmlPatcherTriggerWorker(Trigger entity, Patch parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
    }

    
    /**
     * Iterates over all conditions entities and checks their return value (a boolean).
     * If the trigger is of type ALL the trigger will return as value true only when all conditions are met,
     * otherwise it will return true if any of the conditions is met.
     * @return
     * @throws XmlPatcherWorkerException
     */
    protected boolean internalProcess() throws XmlPatcherWorkerException {
        boolean entityTypeAll = getEntity().getType().value().equals(
                XmlPatcherConstants.TriggerTypes.ALL);
        for (Condition condition : getEntity().getCondition()) {
            XmlPatcherWorker<?, ?> worker = XmlPatcherWorkerFactory
                    .createWorker(condition, entity, log);
            if (!worker.run())
                return false;
            Boolean conditionRet = (Boolean) worker.getReturnValue();
            if(condition.isInverted()) 
                conditionRet=!conditionRet;
            if (entityTypeAll && conditionRet == false) {
                returnValue = false;
                return true;
            }
            if (!entityTypeAll && conditionRet == true) {
                returnValue = true;
                return true;
            }
        }
        if (entityTypeAll)
            returnValue = true;
        else
            returnValue = false;
        return true;
    }

    @Override
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        return true;
    }



    @Override
    protected boolean process() throws XmlPatcherWorkerException {
        if(getEntity().isInverted()) return !internalProcess(); else return internalProcess();
    }

}
