/**
 * XmlPatcherHQLLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import java.sql.SQLException;
import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Provides support to execute Hibernate Query Language (HQL) lang-type
 *         scripts.
 * @see https://www.hibernate.org/hib_docs/nhibernate/html/queryhql.html
 */
public class XmlPatcherHQLLangWorker extends XmlPatcherLangWorker {

    /**
     * @param entity
     * @param log
     */
    public XmlPatcherHQLLangWorker(Lang entity, Script parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
    }

    

    @Override
    protected boolean processSelectStatement()
            throws XmlPatcherLangWorkerException {
        try {
            Session session = PersistenceManager.getSession();
            Query query = session.createQuery(entity.getValue());
            List<?> list = query.list();
            returnValue = list.get(0);
            return true;
        } catch (HibernateException e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

    @Override
    protected boolean processUpdateStatement()
            throws XmlPatcherLangWorkerException {
        Session session = XmlPatcherUtil.getHibernateSession();
        try {
            Query query = session.createQuery(entity.getValue());
            query.executeUpdate();
            return true;
        } catch (HibernateException e) {
            throw new XmlPatcherLangWorkerException(e);
        } finally {
            XmlPatcherUtil.closeHibernateSession(session);
        }
    }

}
