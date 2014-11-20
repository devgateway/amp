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
public class IdentityExpander extends IdsExpander {
	
	public IdentityExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(final List<Long> values) {
		final Set<Long> res = new HashSet<>(values);
		return res;
	}
}
