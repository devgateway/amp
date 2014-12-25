package org.dgfoundation.amp.testutils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.digijava.module.esrigis.helpers.DbHelper;

/**
 * only useful for mondrian-reports pledges id filtering (because of PLEDGE_ID_ADDER)
 * @author Constantin Dolghier
 *
 */
public class PledgeIdsFetcher implements IdsGeneratorSource {
	
	public final List<String> pledgeNames;
	
	/**
	 * 
	 * @param activityNames if null, then "get all" 
	 */
	public PledgeIdsFetcher(List<String> pledgeNames) {
		this.pledgeNames = pledgeNames;
	}
	
	@Override public Set<Long> getIds() {
		try {
			String whereQuery = pledgeNames == null ? "" : ("WHERE title IN (" + Util.toCSString(pledgeNames) + ")");
			String query = "SELECT pledge_id + " + MondrianETL.PLEDGE_ID_ADDER + " from v_pledges_titles " + whereQuery;
			return new TreeSet<Long>(DbHelper.getInActivities(query));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
