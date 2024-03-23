package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ImplementationLevelCells extends HardcodedCells<TextCell>{

    public ImplementationLevelCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Activity 2 with multiple agreements", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Activity Linked With Pledge", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Activity with both MTEFs and Act.Comms", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with capital spending", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with components", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with contracting agency", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with directed MTEFs", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with funded components", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("activity with incomplete agreement", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("activity with many MTEFs", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity with pipeline MTEFs and act. disb", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Activity with planned disbursements", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("activity with primary_program", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("Activity with primary_tertiary_program", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("activity with tertiary_program", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("Activity with Zones", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Activity With Zones and Percentages", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity-weird-funding", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity-with-unfunded-components", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("activity_with_disaster_response", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("arrears test", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("crazy funding 1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("date-filters-activity", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("department/division", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Eth Water", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("execution rate activity", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("expenditure class", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("mtef activity 1", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("mtef activity 2", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("PID: original", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("PID: original > actual", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("PID: original, actual", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("PID: original, proposed", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("PID: original, proposed, actual", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("pledged 2", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("pledged education activity 1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Project with documents", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Proposed Project Cost 1 - USD", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Proposed Project Cost 2 - EUR", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("ptc activity 1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("ptc activity 2", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Pure MTEF Project", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Real SSC Activity 1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Real SSC Activity 2", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("second with disaster response", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("SSC Project 1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("SSC Project 2", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("SubNational no percentages", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("TAC_activity_1", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("TAC_activity_2", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Test MTEF directed", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("third activity with agreements", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("Unvalidated activity", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))),
            cell("with annual ppc and actual comm", "Provincial", 69,
                coos(
                    entry("cats", "Implementation Level", 1, 69))),
            cell("with weird currencies", "National", 70,
                coos(
                    entry("cats", "Implementation Level", 1, 70))));
    }

}
