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
	
	List<String> activityNames;
	
	public ActivityIdsFetcher(String...activityNames) {
		this.activityNames = Arrays.asList(activityNames);
	}
	
	@Override public Set<Long> getIds() {
		try {
			String query = "SELECT amp_activity_id from amp_activity WHERE name IN (" + Util.toCSString(activityNames) + ")";
			return new TreeSet<Long>(DbHelper.getInActivities(query));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
