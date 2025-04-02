package org.digijava.module.aim.repository;

import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.RESOURCE;
import static org.digijava.kernel.services.sync.model.SyncConstants.Ops.DELETED;

import java.util.Date;
import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOfflineChangelog;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineChangelogRepository {

    public static final AmpOfflineChangelogRepository INSTANCE = new AmpOfflineChangelogRepository();

    @SuppressWarnings("unchecked")
    public List<AmpOfflineChangelog> findRemovedResources(Date lastSyncTime) {
        return PersistenceManager.getSession().createQuery(
                "select c "
                        + "from " + AmpOfflineChangelog.class.getSimpleName() + " c "
                        + "where c.operationTime > :lastSyncTime "
                        + "and c.operationName = :operationName "
                        + "and c.entityName = :entityName")
                .setParameter("lastSyncTime", lastSyncTime)
                .setParameter("operationName", DELETED)
                .setParameter("entityName", RESOURCE)
                .list();
    }
}
