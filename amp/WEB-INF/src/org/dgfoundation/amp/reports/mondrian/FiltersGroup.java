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

/**
 * Defines the columns to filter group 
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersGroup {
	private static final String[] idList = new String[]{"", " Id"};
		
	/** map of column key to filter group that it belongs */
	public static final Map<String, String> FILTER_GROUP = buildFilterGroupMap();
	
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
		return filterGroupMap;
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
