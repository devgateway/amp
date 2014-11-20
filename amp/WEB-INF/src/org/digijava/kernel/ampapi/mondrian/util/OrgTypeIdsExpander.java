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
 * set(org type) -> set(org type) expander
 * @author Constantin Dolghier
 *
 */
public class OrgTypeIdsExpander extends IdsExpander {
	
	public OrgTypeIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(final List<Long> values) {
		final Set<Long> res = new HashSet<>();
		PersistenceManager.getSession().doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				/**
				 * select ao.amp_org_id, ao.org_grp_id, aot.* from 
					amp_organisation ao 
						JOIN amp_org_group aog ON aog.amp_org_grp_id = ao.org_grp_id
						JOIN amp_org_type aot ON aot.amp_org_type_id = aog.org_type
				 */
				String query = "SELECT ao.amp_org_id FROM " +  
						"amp_organisation ao " +  
						"JOIN amp_org_group aog ON aog.amp_org_grp_id = ao.org_grp_id " + 
						"WHERE aog.org_type IN (" + Util.toCSStringForIN(values) + ")";
				res.addAll(SQLUtils.fetchLongs(connection, query));
			}
		});
		return res;
	}
}
