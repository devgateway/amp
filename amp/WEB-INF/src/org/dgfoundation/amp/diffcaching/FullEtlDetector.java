package org.dgfoundation.amp.diffcaching;

import java.sql.Connection;
import java.util.function.Supplier;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

public class FullEtlDetector implements Supplier<Boolean> {
	
	protected long lastProcessedFullEtl = -1; 
	
	@Override
	public Boolean get() {
		long lastFullEtl = PersistenceManager.getSession().doReturningWork(this::getLastFullEtl);
		boolean res = lastFullEtl > lastProcessedFullEtl;
		this.lastProcessedFullEtl = lastFullEtl;
		return res;
	}
	
	protected long getLastFullEtl(Connection conn) {
		return SQLUtils.getLong(conn, String.format("SELECT COALESCE(max(event_id), %d) FROM amp_etl_changelog WHERE event_id > %d and entity_name = 'full_etl_request'", lastProcessedFullEtl, lastProcessedFullEtl));
	}
}
