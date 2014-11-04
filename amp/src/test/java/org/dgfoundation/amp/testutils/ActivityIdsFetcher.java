package org.dgfoundation.amp.testutils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.digijava.module.esrigis.helpers.DbHelper;

/**
 * 
 * @author Constantin Dolghier
 *
 */
public class ActivityIdsFetcher implements IdsGeneratorSource {
	
	public final List<String> activityNames;
	
	/**
	 * 
	 * @param activityNames if null, then "get all" 
	 */
	public ActivityIdsFetcher(List<String> activityNames) {
		this.activityNames = activityNames;
	}
	
	@Override public Set<Long> getIds() {
		try {
			String whereQuery = activityNames == null ? "" : ("WHERE name IN (" + Util.toCSString(activityNames) + ")");
			String query = "SELECT amp_activity_id from amp_activity " + whereQuery;
			return new TreeSet<Long>(DbHelper.getInActivities(query));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
