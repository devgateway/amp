package org.dgfoundation.amp.diffcaching;

import java.sql.Connection;
import java.util.function.Supplier;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * a supplier for {@link ExpiringCacher} which triggers a cache invalidate when any kind of mutating event is logged in the AMP in-db changelog
 * @author Dolghier Constantin
 *
 */
public class DatabaseChangedDetector implements Supplier<Boolean> {
    
    protected long lastProcessedFullEtl = -1; 
    
    @Override
    public Boolean get() {
        Session session =PersistenceManager.getSession();
        Transaction tx = session.getTransaction();
        long lastFullEtl = session.doReturningWork(this::getLastFullEtl);
        boolean res = lastFullEtl > lastProcessedFullEtl;
        ExpiringCacher.logger.debug(String.format("DBCD: lastFullEtl = %d, lastProcessedFullETL = %d, returning: %b\n", lastFullEtl, lastProcessedFullEtl, res));
        this.lastProcessedFullEtl = lastFullEtl;
        tx.commit();
        return res;
    }
    
    protected long getLastFullEtl(Connection conn) {
        return SQLUtils.getLong(conn, String.format("SELECT COALESCE(max(event_id), %d) FROM amp_etl_changelog WHERE event_id > %d and entity_name != 'etl'", lastProcessedFullEtl, lastProcessedFullEtl));
    }
}
