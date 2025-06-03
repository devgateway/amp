package org.dgfoundation.amp.onepager;

import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

public class HibernateRequestCycleListener extends AbstractRequestCycleListener {

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        Session session = PersistenceManager.openNewSession();
        session.beginTransaction();
        PersistenceManager.setCurrentSession(session);
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        Session session = PersistenceManager.getSession();
        try {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().commit();
            }
        } catch (Throwable t) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Error during session commit", t);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            PersistenceManager.clearCurrentSession();
        }
    }
}
