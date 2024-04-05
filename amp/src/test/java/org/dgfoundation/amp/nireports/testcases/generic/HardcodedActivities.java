package org.dgfoundation.amp.nireports.testcases.generic;

import java.util.*;

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
        activity("activity 1 with indicators", 95);
        activity("activity 2 with indicators", 96);
        activity("Activity 2 with multiple agreements", 66);
        activity("Activity Linked With Pledge", 41);
        activity("Activity with both MTEFs and Act.Comms", 70);
        activity("activity with capital spending", 50);
        activity("activity with components", 21);
        activity("activity with contracting agency", 52);
        activity("activity with directed MTEFs", 73);
        activity("activity with funded components", 63);
        activity("activity with funded components 2", 99);
        activity("activity with incomplete agreement", 68);
        activity("activity with many MTEFs", 78);
        activity("activity with pipeline MTEFs and act. disb", 76);
        activity("Activity with planned disbursements", 69);
        activity("activity with primary_program", 44);
        activity("Activity with primary_tertiary_program", 43);
        activity("activity with tertiary_program", 45);
        activity("Activity with Zones", 33);
        activity("Activity With Zones and Percentages", 36);
        activity("activity-weird-funding", 88);
        activity("activity-with-unfunded-components", 61);
        activity("activity_with_disaster_response", 71);
        activity("arrears test", 80);
        activity("crazy funding 1", 32);
        activity("date-filters-activity", 26);
        activity("department/division", 90);
        activity("Eth Water", 24);
        activity("execution rate activity", 77);
        activity("expenditure class", 87);
        activity("mtef activity 1", 25);
        activity("mtef activity 2", 27);
        activity("new activity with contracting", 53);
        activity("PID: original", 84);
        activity("PID: original > actual", 83);
        activity("PID: original, actual", 82);
        activity("PID: original, proposed", 85);
        activity("PID: original, proposed, actual", 81);
        activity("pledged 2", 48);
        activity("pledged education activity 1", 46);
        activity("Project with documents", 23);
        activity("Proposed Project Cost 1 - USD", 15);
        activity("Proposed Project Cost 2 - EUR", 17);
        activity("ptc activity 1", 28);
        activity("ptc activity 2", 29);
        activity("Pure MTEF Project", 19);
        activity("Real SSC Activity 1", 39);
        activity("Real SSC Activity 2", 38);
        activity("regional funding activity 1", 100);
        activity("regional funding activity 2", 101);
        activity("regional funding activity 3", 102);
        activity("second with disaster response", 92);
        activity("SSC Project 1", 30);
        activity("SSC Project 2", 31);
        activity("SubNational no percentages", 40);
        activity("TAC_activity_1", 12);
        activity("TAC_activity_2", 13);
        activity("Test MTEF directed", 18);
        activity("third activity with agreements", 67);
        activity("Unvalidated activity", 64);
        activity("with annual ppc and actual comm", 94);
        activity("with weird currencies", 79);

        // cells below have been added manually (no AMP correspondent). They should be restored manually in case this file is overwritten by codegen
        activity("custom_1", 700);
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

