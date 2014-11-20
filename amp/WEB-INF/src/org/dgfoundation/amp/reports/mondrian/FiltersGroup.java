/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * Defines the columns to filter group 
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersGroup {
	private static final String[] idList = new String[]{"", " Id"};
		
	/** map of column key to filter group that it belongs */
	public static final Map<String, String> FILTER_GROUP = buildFilterGroupMap();
	public static final String LOCATION_FILTER = "LOCATION_FILTER";
	
	private static Map<String, String> buildFilterGroupMap() {
		Map<String, String> filterGroupMap = new HashMap<String, String>();
		
		// adding sector groups
		addGroups(filterGroupMap, 
				Arrays.asList(
						ColumnConstants.PRIMARY_SECTOR,
						ColumnConstants.SECONDARY_SECTOR, 
						ColumnConstants.TERTIARY_SECTOR
						),
				Arrays.asList("", " Sub-Sector", " Sub-Sub-Sector")
				);
		
		// adding program groups
		List<String> programSuffix = new ArrayList<String>();
		programSuffix.add("");
		programSuffix.add(" Detail");
		programSuffix.add(" Normal");
		for (int idx = 1; idx < 9; idx ++)
			programSuffix.add(" Level " + idx);
		addGroups(filterGroupMap, 
				Arrays.asList(
						ColumnConstants.PRIMARY_PROGRAM,
						ColumnConstants.SECONDARY_PROGRAM, 
						ColumnConstants.TERTIARY_PROGRAM,
						ColumnConstants.NATIONAL_PLANNING_OBJECTIVES
						),
						programSuffix
				);
		
		filterGroupMap.put(ColumnConstants.COUNTRY, LOCATION_FILTER);
		filterGroupMap.put(ColumnConstants.REGION, LOCATION_FILTER);
		filterGroupMap.put(ColumnConstants.ZONE, LOCATION_FILTER);
		filterGroupMap.put(ColumnConstants.DISTRICT, LOCATION_FILTER);
		filterGroupMap.put(MoConstants.ATTR_LOCATION_NAME, LOCATION_FILTER);
		
		
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_AGENCY);
		filterGroupMap.put(ColumnConstants.DONOR_ID, ColumnConstants.DONOR_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_GROUP);
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_TYPE);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.EXECUTING_AGENCY);
		filterGroupMap.put(ColumnConstants.EXECUTING_AGENCY_ID, ColumnConstants.EXECUTING_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.EXECUTING_AGENCY_GROUPS);
		addIdentityMapping(filterGroupMap, ColumnConstants.EXECUTING_AGENCY_TYPE);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.BENEFICIARY_AGENCY);
		filterGroupMap.put(ColumnConstants.BENEFICIARY_AGENCY_ID, ColumnConstants.BENEFICIARY_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.BENEFICIARY_AGENCY_GROUPS);


		addIdentityMapping(filterGroupMap, ColumnConstants.IMPLEMENTING_AGENCY);
		filterGroupMap.put(ColumnConstants.IMPLEMENTING_AGENCY_ID, ColumnConstants.IMPLEMENTING_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS);
		addIdentityMapping(filterGroupMap, ColumnConstants.IMPLEMENTING_AGENCY_TYPE);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		filterGroupMap.put(ColumnConstants.RESPONSIBLE_ORGANIZATION_ID, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		addIdentityMapping(filterGroupMap, ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.IMPLEMENTATION_LEVEL);

		return filterGroupMap;
	}
	
	
	private static void addIdentityMapping(Map<String, String> m, String key) {
		m.put(key, key);
	}
	
	private static void addGroups(Map<String, String> filterGroupMap, 
			List<String> parentList, 
			List<String> suffixList) {
		for (String parent : parentList) {
			for (String idSuffix : idList) {
				final String group = parent + idSuffix;
				for (String suffix: suffixList) {
					final String current = parent + suffix + idSuffix;
					filterGroupMap.put(current, group);
				}
			}
		}
	}
}
