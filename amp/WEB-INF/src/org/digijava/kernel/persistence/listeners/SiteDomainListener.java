package org.digijava.kernel.persistence.listeners;

import org.digijava.kernel.jobs.RegisterWithAmpRegistryJob;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Monitors changes to SiteDomain entities.
 *
 * @author Octavian Ciubotaru
 */
public class SiteDomainListener implements PostUpdateEventListener, PostDeleteEventListener, PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        handleEvent(event.getEntity());
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        handleEvent(event.getEntity());
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        handleEvent(event.getEntity());
    }

    private void handleEvent(Object entity) {
        if (entity instanceof SiteDomain) {
            QuartzJobUtils.runJobIfNotPaused(RegisterWithAmpRegistryJob.NAME);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return PostUpdateEventListener.super.requiresPostCommitHandling(persister);
    }
}
