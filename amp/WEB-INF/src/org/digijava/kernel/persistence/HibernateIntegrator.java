package org.digijava.kernel.persistence;

import org.digijava.kernel.persistence.listeners.SiteDomainListener;
import org.hibernate.boot.Metadata;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * Used to register listeners. Because hibernate.cfg.xml is no longer supported as means of specifying listeners.
 *
 * @author Octavian Ciubotaru
 */
public class HibernateIntegrator implements Integrator {

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        SiteDomainListener siteDomainListener = new SiteDomainListener();

        final EventListenerRegistry eventRegistry = sessionFactoryServiceRegistry.getService(EventListenerRegistry.class);
        eventRegistry.appendListeners(EventType.POST_COMMIT_INSERT, siteDomainListener);
        eventRegistry.appendListeners(EventType.POST_COMMIT_UPDATE, siteDomainListener);
        eventRegistry.appendListeners(EventType.POST_COMMIT_DELETE, siteDomainListener);
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
    }
}
