package org.digijava.kernel.ampapi.mondrian.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.Work;

/**
 * set(org group) -> set(org type) expander
 * @author Constantin Dolghier
 *
 */
public class OrgGrpIdsExpander extends IdsExpander {
	
	public OrgGrpIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(final List<Long> values) {
		final Set<Long> res = new HashSet<>(values);
		PersistenceManager.getSession().doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				res.addAll(SQLUtils.fetchLongs(connection, "SELECT amp_org_id FROM amp_organisation WHERE org_grp_id IN (" + Util.toCSStringForIN(values) + ")"));
			}
		});
		return res;
	}
}
