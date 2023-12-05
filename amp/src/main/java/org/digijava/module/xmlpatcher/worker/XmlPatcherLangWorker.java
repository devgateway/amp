/**
 * XmlPatcherLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Worker for all lang entities inside the patch entity. This will be
 *         extended by all lang entities that require special processing. The
 *         runtime check will ensure the language is supported by the system
 */
public abstract class XmlPatcherLangWorker extends
        XmlPatcherWorker<Lang, Script> {

    /**
     * @param entity
     * @param log
     */
    public XmlPatcherLangWorker(Lang entity, Script parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
        // TODO Auto-generated constructor stub
    }

    /**
     * Checks if this lang worker is supposed to execute update queries. It
     * basically checks if the parent script entity does have any returnVar set.
     * If it has it means the current lang worker is supposed to execute SELECT
     * queries, and not update
     * 
     * @return true if the lang worker is supposed to execute update queries
     */
    public boolean isUpdateQueryType() {
        if (parentEntity != null && parentEntity.getReturnVar() != null)
            return false;
        return true;
    }

    @Override
    protected boolean process() throws XmlPatcherWorkerException {
        if (isUpdateQueryType())
            return processUpdateStatement();
        else
            return processSelectStatement();
    }

    /**
     * Method of execution of the enclosing lang entity as an UPDATE statement
     * (or any other kind of statements that do not return a resultSet. DDLs and
     * DMLs enter this category.
     * 
     * @return true if execution is successful
     * @throws XmlPatcherLangWorkerException 
     */
    protected abstract boolean processUpdateStatement() throws XmlPatcherLangWorkerException;

    /**
     * Method of execution of the enclosing lang entity as a SELECT statement.
     * SQL SELECT queries or procedure invocations can be used here.
     * 
     * @return true if execution is successful
     * @throws XmlPatcherLangWorkerException 
     */
    protected abstract boolean processSelectStatement() throws XmlPatcherLangWorkerException;

    @Override
    /**
     * Checks if the lang is supported by the patcher. This is double checked by the XSD validation eariler.
     */
    protected boolean runTimeCheck() throws XmlPatcherWorkerException {
        // check if the language type is supported by the xmlpatcher
        boolean langFound = false;
        for (int i = 0; i < XmlPatcherConstants.ScriptLangs.all.length; i++) {
            if (XmlPatcherConstants.ScriptLangs.all[i].equals(getEntity()
                    .getType().value()))
                return langFound = true;
        }
        if (!langFound)
            throw new XmlPatcherLangWorkerException("Unsupported language "
                    + getEntity().getType());

        return true;
    }
}
