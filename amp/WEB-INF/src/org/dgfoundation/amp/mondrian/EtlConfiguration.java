package org.dgfoundation.amp.mondrian;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.Util;

/**
 * the configuration data of an ETL run, e.g. which entities it should touch
 * @author Dolghier Constantin
 *
 */
public final class EtlConfiguration {
	
	public final Map<String, String> queryStorage;
		
	/**
	 * activities to add/remove from all the relevant tables
	 */
	public final Set<Long> activityIds;

	/**
	 * pledges to add/remove from all the relevant tables
	 */
	public final Set<Long> pledgeIds;
	
	/**
	 * components to add/remove from all the relevant tables
	 */
	public final Set<Long> componentIds;

	/**
	 * components to add/remove from all the relevant tables
	 */
	public final Set<Long> agreementIds;

	
	/**
	 * Julian date codes to add/remove from the relevant tables. null means "all"
	 */
	public final Set<Long> dateCodes;
	
	public final boolean fullEtl;

	
	public EtlConfiguration(Set<Long> activityIds, Set<Long> pledgeIds, Set<Long> componentIds, Set<Long> agreementIds, Set<Long> dateCodes, boolean fullEtl) {
		this.activityIds = Collections.unmodifiableSet(activityIds);
		this.pledgeIds = Collections.unmodifiableSet(pledgeIds);
		this.componentIds = Collections.unmodifiableSet(componentIds);
		this.agreementIds = Collections.unmodifiableSet(agreementIds);
		this.dateCodes = dateCodes;
		this.fullEtl = fullEtl;
		this.queryStorage = Collections.unmodifiableMap(new HashMap<String, String>() {{
			put("activityIds", Util.toCSStringForIN(EtlConfiguration.this.activityIds));
			put("pledgeIds", Util.toCSStringForIN(EtlConfiguration.this.pledgeIds));
			put("componentIds", Util.toCSStringForIN(EtlConfiguration.this.componentIds));
			put("agreementIds", Util.toCSStringForIN(EtlConfiguration.this.agreementIds));
			put("allEntityIds", Util.toCSStringForIN(getAllEntityIds()));
		}});
		System.out.println("debug only");
	}
	
	
	/**
	 * builds an SQL subfragment which filters based on the activity IDs
	 * @param prefix
	 * @return
	 */
	public String activityIdsIn(String prefix) {
		return String.format("%s IN (%s)", prefix, queryStorage.get("activityIds"));
	}
	
	/**
	 * builds an SQL subfragment which filters based on the pledge IDs
	 * @param prefix
	 * @return
	 */
	public String pledgeIdsIn(String prefix) {
		return String.format("%s IN (%s)", prefix, queryStorage.get("pledgeIds"));
	}
	
	/**
	 * builds an SQL subfragment which filters based on the component IDs
	 * @param prefix
	 * @return
	 */
	public String componentIdsIn(String prefix) {
		return String.format("%s IN (%s)", prefix, queryStorage.get("componentIds"));
	}
	
	/**
	 * builds an SQL subfragment which filters based on the agreement IDs
	 * @param prefix
	 * @return
	 */
	public String agreementIdsIn(String prefix) {
		return String.format("%s IN (%s)", prefix, queryStorage.get("agreementIds"));
	}
	
	/**
	 * builds an SQL subfragment which filters based on the entity IDs (activity IDs + shifted pledge IDs)
	 * @param prefix
	 * @return
	 */
	public String entityIdsIn(String prefix) {
		return String.format("%s IN (%s)", prefix, queryStorage.get("allEntityIds"));
	}
	
	public Set<Long> getAllEntityIds() {
		Set<Long> res = new java.util.HashSet<>(this.activityIds);
		for(long pledgeId:this.pledgeIds)
			res.add(pledgeId + MondrianETL.PLEDGE_ID_ADDER);
		return res;
	}
	
	@Override public String toString() {
		return String.format("activities: <%s>; pledges: <%s>; components: <%s>; agreements: <%s>; dates: <%s>", 
				activityIds.size() > 20 ? String.format("[%d] activities", activityIds.size()) : activityIds.toString(),
				pledgeIds.size() > 20 ? String.format("[%d] pledges", pledgeIds.size()) : pledgeIds.toString(),
				componentIds.size() > 20 ? String.format("[%d] components", componentIds.size()) : componentIds.toString(),
				agreementIds.size() > 20 ? String.format("[%d] agreements", agreementIds.size()) : agreementIds.toString(),
				dateCodes == null ? "(all)" : dateCodes.toString());
	}
}
