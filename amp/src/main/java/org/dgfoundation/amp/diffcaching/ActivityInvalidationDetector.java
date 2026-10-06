package org.dgfoundation.amp.diffcaching;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * a supplier for {@link ExpiringCacher} which triggers a cache invalidate when any kind of non-incremental mutating event is logged in the AMP in-db changelog
 * @author Dolghier Constantin
 *
 */
public class ActivityInvalidationDetector implements Supplier<Boolean> {
    
    protected long lastProcessedFullEtl = -1; 
    
    @Override
    public Boolean get() {
        long lastFullEtl = PersistenceManager.getSession().doReturningWork(this::getLastFullEtl);
        boolean res = lastFullEtl > lastProcessedFullEtl;
        this.lastProcessedFullEtl = lastFullEtl;
        return res;
    }
    
    protected long getLastFullEtl(Connection conn) {
        return SQLUtils.getLong(conn, String.format("SELECT COALESCE(max(event_id), %d) FROM amp_etl_changelog WHERE event_id > %d and entity_name NOT IN ('activity', 'pledge', 'etl') ", lastProcessedFullEtl, lastProcessedFullEtl));
    }
    
    public long getLastProcessedFullEtl() {
        return lastProcessedFullEtl;
    }
}
