package org.dgfoundation.amp.testmodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardcodedActivities {

	private final Map<Long, String> actNames;
	private final Map<String, Long> actIds;
	
	private Map<Long, String> hiddenActNames = new HashMap<Long, String>();
	private Map<String, Long> hiddenActIds = new HashMap<String, Long>();
	

	public HardcodedActivities() {
		populateMaps();
		actNames = Collections.unmodifiableMap(hiddenActNames);
		actIds = Collections.unmodifiableMap(hiddenActIds);
	}
	
	private void activity(String title, long id) {
		hiddenActNames.put(id, title);
		hiddenActIds.put(title, id);
	}
	
	private void populateMaps() {
		activity("activity 1 with agreement", 65);
		activity("Activity 2 with multiple agreements", 66);
		activity("Activity Linked With Pledge", 41);
		activity("Activity with both MTEFs and Act.Comms", 70);
		activity("activity with capital spending", 50);
		activity("activity with components", 21);
		activity("activity with contracting agency", 52);
		activity("activity with directed MTEFs", 73);
		activity("activity with funded components", 63);
		activity("activity with incomplete agreement", 68);
		activity("activity with many MTEFs", 78);
		activity("activity with pipeline MTEFs and act. disb", 76);
		activity("Activity with planned disbursements", 69);
		activity("activity with primary_program", 44);
		activity("Activity with primary_tertiary_program", 43);
		activity("activity with tertiary_program", 45);
		activity("Activity with Zones", 33);
		activity("Activity With Zones and Percentages", 36);
		activity("activity-with-unfunded-components", 61);
		activity("activity_with_disaster_response", 71);
		activity("crazy funding 1", 32);
		activity("date-filters-activity", 26);
		activity("Eth Water", 24);
		activity("execution rate activity", 77);
		activity("mtef activity 1", 25);
		activity("mtef activity 2", 27);
		activity("new activity with contracting", 53);
		activity("pledged 2", 48);
		activity("pledged education activity 1", 46);
		activity("Project with documents", 23);
		activity("Proposed Project Cost 1 - USD", 15);
		activity("Proposed Project Cost 2 - EUR", 17);
		activity("ptc activity 1", 28);
		activity("ptc activity 2", 29);
		activity("Pure MTEF Project", 19);
		activity("SSC Project 1", 30);
		activity("SSC Project 2", 31);
		activity("SubNational no percentages", 40);
		activity("TAC_activity_1", 12);
		activity("TAC_activity_2", 13);
		activity("Test MTEF directed", 18);
		activity("third activity with agreements", 67);
		activity("Unvalidated activity", 64);
		activity("with weird currencies", 79);
	}
	
	public Map<Long, String> getActNamesMap() {
		return actNames;
	}
	
	public List<String> getActNamesList() {
		return new ArrayList<String>(actNames.values());
	}
	
	
	public Map<String, Long> getActIdsMap() {
		return actIds;
	}
	
}
