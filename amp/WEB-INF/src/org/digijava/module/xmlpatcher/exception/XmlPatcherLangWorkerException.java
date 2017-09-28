/**
 * XmlPatcherLangWorkerException.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.exception;

import java.sql.BatchUpdateException;

import org.apache.log4j.Logger;
import org.digijava.module.xmlpatcher.core.XmlPatcherService;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherLangWorkerException extends XmlPatcherWorkerException {
    private static Logger logger = Logger.getLogger(XmlPatcherLangWorkerException.class);
    /**
     * 
     */
    public XmlPatcherLangWorkerException() {
        // TODO Auto-generated constructor stub
    }
    

    /**
     * @param arg0
     */
    public XmlPatcherLangWorkerException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @throws XmlPatcherLangWorkerException 
     * @throws XmlPatcherConditionWorkerException 
     */
    public XmlPatcherLangWorkerException(Throwable arg0) throws XmlPatcherLangWorkerException  {
        super(arg0);
        if(arg0 instanceof BatchUpdateException) {
            BatchUpdateException bue=(BatchUpdateException) arg0;
            logger.error(this.getMessage());
            throw new XmlPatcherLangWorkerException(bue.getNextException());
        }
    }

    /**
     * @param arg0
     * @param arg1
     */
    public XmlPatcherLangWorkerException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}
