/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	/**
	 * map<Fundamental pledge column, Fundamental donor column>
	 */
	public final static Map<String, String> GENERIC_PARENT_COLUMNS = Collections.unmodifiableMap(new HashMap<String, String>() {{
		put(ColumnConstants.PLEDGES_SECTORS, ColumnConstants.PRIMARY_SECTOR);
		put(ColumnConstants.PLEDGES_SECONDARY_SECTORS, ColumnConstants.SECONDARY_SECTOR);
		put(ColumnConstants.PLEDGES_TERTIARY_SECTORS, ColumnConstants.TERTIARY_SECTOR);
		
		put(ColumnConstants.PLEDGES_PROGRAMS, ColumnConstants.PRIMARY_PROGRAM);
		put(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS, ColumnConstants.SECONDARY_PROGRAM);
		put(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS, ColumnConstants.TERTIARY_PROGRAM);
		put(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
	}});

	/** map of column key to filter group that it belongs */
	public static final Map<String, String> FILTER_GROUP = buildFilterGroupMap();
	public static final String LOCATION_FILTER = "LOCATION_FILTER";
	
	private static Map<String, String> buildFilterGroupMap() {
		Map<String, String> filterGroupMap = new HashMap<String, String>();
		
		// adding sector groups
		addGroups(filterGroupMap,
				Arrays.asList(
						ColumnConstants.PRIMARY_SECTOR,
						ColumnConstants.PLEDGES_SECTORS,
						
						ColumnConstants.SECONDARY_SECTOR,
						ColumnConstants.PLEDGES_SECONDARY_SECTORS,
						
						ColumnConstants.TERTIARY_SECTOR,
						ColumnConstants.PLEDGES_TERTIARY_SECTORS
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
						ColumnConstants.PLEDGES_PROGRAMS,
						
						ColumnConstants.SECONDARY_PROGRAM,
						ColumnConstants.PLEDGES_SECONDARY_PROGRAMS,
						
						ColumnConstants.TERTIARY_PROGRAM,
						ColumnConstants.PLEDGES_TERTIARY_PROGRAMS,
						
						ColumnConstants.NATIONAL_PLANNING_OBJECTIVES,
						ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES
						),
						programSuffix
				);
		
		List<String> locationColumns = Arrays.asList(
				ColumnConstants.COUNTRY,
				ColumnConstants.REGION,
				ColumnConstants.ZONE,
				ColumnConstants.DISTRICT,
				MoConstants.ATTR_LOCATION_NAME,
				ColumnConstants.LOCATION,
				
				ColumnConstants.PLEDGES_REGIONS,
				ColumnConstants.PLEDGES_ZONES,
				ColumnConstants.PLEDGES_DISTRICTS
			);
		for(String locationColumn:locationColumns)
			filterGroupMap.put(locationColumn, LOCATION_FILTER);
		
		
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_AGENCY);
		filterGroupMap.put(ColumnConstants.DONOR_ID, ColumnConstants.DONOR_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_GROUP);
		filterGroupMap.put(ColumnConstants.PLEDGES_DONOR_GROUP, ColumnConstants.DONOR_GROUP);
		addIdentityMapping(filterGroupMap, ColumnConstants.DONOR_TYPE);
		//filterGroupMap.put(ColumnConstants.PLEDGES_D, ColumnConstants.DONOR_TYPE);
		
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
		
		//ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_COOPERATION, ColumnConstants.TYPE_OF_IMPLEMENTATION
		addIdentityMapping(filterGroupMap, ColumnConstants.FINANCING_INSTRUMENT);
		addIdentityMapping(filterGroupMap, ColumnConstants.MODE_OF_PAYMENT);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.TYPE_OF_ASSISTANCE);
		filterGroupMap.put(ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_ASSISTANCE);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.TYPE_OF_COOPERATION);
		addIdentityMapping(filterGroupMap, ColumnConstants.TYPE_OF_IMPLEMENTATION);
		addIdentityMapping(filterGroupMap, ColumnConstants.FUNDING_STATUS);
		addIdentityMapping(filterGroupMap, ColumnConstants.PROCUREMENT_SYSTEM);
		addIdentityMapping(filterGroupMap, ColumnConstants.PLEDGES_AID_MODALITY);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.PLEDGES_AID_MODALITY);
		addIdentityMapping(filterGroupMap, ColumnConstants.PROCUREMENT_SYSTEM);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.STATUS);
		filterGroupMap.put(ColumnConstants.PLEDGE_STATUS, ColumnConstants.STATUS);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.CONTRACTING_AGENCY);
		filterGroupMap.put(ColumnConstants.CONTRACTING_AGENCY_ID, ColumnConstants.CONTRACTING_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.CONTRACTING_AGENCY_GROUPS);
		
		
		addIdentityMapping(filterGroupMap, ColumnConstants.CONTRACTING_AGENCY);
		filterGroupMap.put(ColumnConstants.CONTRACTING_AGENCY_ID, ColumnConstants.CONTRACTING_AGENCY);
		addIdentityMapping(filterGroupMap, ColumnConstants.CONTRACTING_AGENCY_GROUPS);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.SECTOR_GROUP);
		filterGroupMap.put(ColumnConstants.SECTOR_GROUP_ID, ColumnConstants.SECTOR_GROUP);
		addIdentityMapping(filterGroupMap, ColumnConstants.SECTOR_GROUP_GROUP);

		addIdentityMapping(filterGroupMap, ColumnConstants.REGIONAL_GROUP);
		filterGroupMap.put(ColumnConstants.REGIONAL_GROUP_ID, ColumnConstants.REGIONAL_GROUP);
		addIdentityMapping(filterGroupMap, ColumnConstants.REGIONAL_GROUP_GROUP);		

		addIdentityMapping(filterGroupMap, ColumnConstants.ACTIVITY_ID);
		filterGroupMap.put(ColumnConstants.INTERNAL_USE_ID, ColumnConstants.ACTIVITY_ID);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.TEAM);
		filterGroupMap.put(ColumnConstants.TEAM_ID, ColumnConstants.TEAM);
		
		addIdentityMapping(filterGroupMap, ColumnConstants.ACTIVITY_APPROVED_BY);
		addIdentityMapping(filterGroupMap, ColumnConstants.ACTIVITY_CREATED_BY);
		addIdentityMapping(filterGroupMap, ColumnConstants.ACTIVITY_UPDATED_BY);
		
		return filterGroupMap;
	}
	
	
	private static void addIdentityMapping(Map<String, String> m, String key) {
		m.put(key, key);
	}
		
	
	public static String columnToGenericColumn(String col) {
		if (GENERIC_PARENT_COLUMNS.containsKey(col))
			return GENERIC_PARENT_COLUMNS.get(col);
		return col;
	}
	
	private static void addGroups(Map<String, String> filterGroupMap, List<String> parentList, List<String> suffixList) {
		for (String parent : parentList) {
			if (parent == null)
				throw new RuntimeException("no null parent allowed!");
			String genericParent = columnToGenericColumn(parent);
			for (String idSuffix : idList) {
				//final String group = parent + idSuffix;
				for (String suffix: suffixList) {
					final String current = parent + suffix + idSuffix;
					filterGroupMap.put(current, genericParent);
				}
			}
		}
	}
}
