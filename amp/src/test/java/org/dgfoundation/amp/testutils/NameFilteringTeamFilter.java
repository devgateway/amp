package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.StringGenerator;

import java.util.Arrays;

public class NameFilteringTeamFilter implements StringGenerator {

	public String[] activityNames;
	
	public NameFilteringTeamFilter(String[] activityNames)
	{
		this.activityNames = activityNames;
	}
	
	@Override
	public String getString() {
		return "SELECT amp_activity_id from amp_activity WHERE name IN (" + Util.toCSString(Arrays.asList(activityNames)) + ")";
	}

}
