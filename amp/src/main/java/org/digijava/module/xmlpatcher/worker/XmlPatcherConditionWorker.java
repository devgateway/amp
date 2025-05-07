/**
 * XmlPatcherConditionWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import bsh.EvalError;
import bsh.Interpreter;
import org.digijava.module.xmlpatcher.core.XmlPatcherWorkerFactory;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherConditionWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Condition;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.jaxb.Trigger;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Processes a condition entity. A condition entity is a collection of
 *         scripts that define variables plus a test section. The test body is a
 *         bsh script that can be fed directly to the BSH interpreter along with
 *         the variables defined by script entities
 * @see org.digijava.module.xmlpatcher.jaxb.Condition
 */
public class XmlPatcherConditionWorker extends
        XmlPatcherWorker<Condition, Trigger> {

    public XmlPatcherConditionWorker(Condition entity, Trigger parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
    }

    private static Interpreter it = new Interpreter();
    
    @Override
    protected boolean process() throws XmlPatcherWorkerException {
        //Interpreter it = new Interpreter();
        List<Serializable> scripts = getEntity().getContent();
        try {
            String test = null;
            ////System.out.println("scripts.length = " + scripts.size());
            for (Object obj : scripts) {
                //ignore white spaces
                if(obj instanceof String) continue;
                
                JAXBElement element = (JAXBElement) obj;
                if (element.getDeclaredType().equals(String.class)) {
                    test = (String) element.getValue();
                    continue;
                }
                Script script = (Script) element.getValue();
                XmlPatcherWorker<?, ?> worker = XmlPatcherWorkerFactory
                        .createWorker(script, entity, log);
                if (!worker.run())
                    return false;
                if (script.getReturnVar() != null)
                    it.set(script.getReturnVar(), worker.getReturnValue());
            }
            synchronized(it)
            {
                returnValue = it.eval(test);
            }
            return true;
        } catch (EvalError e) {
            throw new XmlPatcherConditionWorkerException(e);
        }
    }

    @Override
    /**
     * Will not execute anything but conditions of type "custom". Please use XSLT transformations
     * to produce only custom type conditions. XSD allows non custom conditions but they need to be transformed
     * into custom, before fed into the worker
     */
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        if (!XmlPatcherConstants.CONDITION_CUSTOM.equals(entity.getType()
                .value()))
            throw new XmlPatcherConditionWorkerException(
                    "Condition entity must always be of type 'custom' when reached the processing step!" +
                    " This means the specified condition type ("+entity.getType()
                    .value()+") is not yet implemented into xmlpatcher.xsl.");
        return true;
    }

}
