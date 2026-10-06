/**
 * XmlPatcherNativeLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Worker invoked for native based SQL languages
 */
public class XmlPatcherNativeLangWorker extends XmlPatcherSQLLangWorker {

    /**
     * @param entity
     * @param log
     */
    public XmlPatcherNativeLangWorker(Lang entity, Script parentEntity, AmpXmlPatchLog log) {
        super(entity, parentEntity,log);
        // TODO Auto-generated constructor stub
    }

    /**
     * Check if this native
     * 
     * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#runTimeCheck()
     */
    @Override
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        // check if the language type is supported by the xmlpatcher
        if (!super.runTimeCheck())
            return false;
        boolean langFound = false;
        for (int i = 0; i < XmlPatcherConstants.ScriptLangs.natives.length; i++) {
            if (XmlPatcherConstants.ScriptLangs.natives[i].equals(getEntity()
                    .getType().value()))
            langFound = true;
        }
        if (!langFound)
            throw new XmlPatcherLangWorkerException(
                    "Unsupported native language " + getEntity().getType());

        // checks if the SQL is compatible with the server
        if (!XmlPatcherUtil.isSQLCompatible(getEntity().getType().value()))
            return false;

        return true;

    }

}
