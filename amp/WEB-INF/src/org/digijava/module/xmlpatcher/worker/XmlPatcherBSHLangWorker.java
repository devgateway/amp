/**
 * XmlPatcherBSHLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.WrappedCheckedException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.hibernate.Session;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides support to
 *         execute BSH lang type entities
 * @see http://beanshell.org
 */
public class XmlPatcherBSHLangWorker extends XmlPatcherLangWorker {

    /**
     * @param entity
     * @param log
     */
    public XmlPatcherBSHLangWorker(Lang entity, Script parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean processSelectStatement() throws XmlPatcherLangWorkerException {
        try {
            return PersistenceManager.doInTransaction(s -> {
                try {
                    return processSelectStatementWrapped(s);
                } catch (XmlPatcherLangWorkerException e) {
                    throw new WrappedCheckedException(e);
                }
            });
        } catch (WrappedCheckedException e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

    private boolean processSelectStatementWrapped(Session session) throws XmlPatcherLangWorkerException {
        Interpreter it = new Interpreter();
        try {
            it.set("session", session);
            returnValue=it.eval(entity.getValue());
            return true;
        } catch (EvalError e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

    @Override
    protected boolean processUpdateStatement() throws XmlPatcherLangWorkerException {
        try {
            return PersistenceManager.doInTransaction(s -> {
                try {
                    return processUpdateStatementWrapped(s);
                } catch (XmlPatcherLangWorkerException e) {
                    throw new WrappedCheckedException(e);
                }
            });
        } catch (WrappedCheckedException e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

    protected boolean processUpdateStatementWrapped(Session session) throws XmlPatcherLangWorkerException {
        Interpreter it = new Interpreter();
        try {
            it.set("session", session);
            it.eval(entity.getValue());
            return true;
        } catch (EvalError e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

}
