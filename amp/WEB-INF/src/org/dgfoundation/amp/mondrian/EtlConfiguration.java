package org.dgfoundation.amp.mondrian;

import java.util.Set;

import org.dgfoundation.amp.Util;

public class EtlConfiguration {
	
	/**
	 * activities to add/remove from all the relevant tables
	 */
	public final Set<Long> activityIds;

	/**
	 * pledges to add/remove from all the relevant tables
	 */
	public final Set<Long> pledgeIds;
	
	/**
	 * Julian date codes to add/remove from the relevant tables. null means "all"
	 */
	public final Set<Long> dateCodes;
	
	public final boolean fullEtl;

	
	public EtlConfiguration(Set<Long> activityIds, Set<Long> pledgeIds, Set<Long> dateCodes, boolean fullEtl) {
		this.activityIds = activityIds;
		this.pledgeIds = pledgeIds;
		this.dateCodes = dateCodes;
		this.fullEtl = fullEtl;
	}
	
	protected String activityIdsInQuery;
	public String activityIdsIn(String prefix) {
		//if (fullEtl) return "1=1"; 
		if (activityIdsInQuery == null)
			activityIdsInQuery = " IN (" + Util.toCSStringForIN(activityIds) + ")";
		return prefix + activityIdsInQuery;
	}
	
	protected String pledgeIdsInQuery;
	public String pledgeIdsIn(String prefix) {
		//if (fullEtl) return "1=1"; 
		if (pledgeIdsInQuery == null)
			pledgeIdsInQuery = " IN (" + Util.toCSStringForIN(pledgeIds) + ")";
		return prefix + pledgeIdsInQuery;
	}
	
	@Override public String toString() {
		return String.format("activities: <%s>; pledges: <%s>; dates: <%s>", 
				activityIds.size() > 20 ? String.format("[%d] activities", activityIds.size()) : activityIds.toString(),
				pledgeIds.size() > 20 ? String.format("[%d] pledges", pledgeIds.size()) : pledgeIds.toString(),
				dateCodes == null ? "(all)" : dateCodes.toString());
	}
}
