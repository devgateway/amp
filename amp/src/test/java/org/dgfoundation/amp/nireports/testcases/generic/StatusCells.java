package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class StatusCells extends HardcodedCells<TextCell>{

    public StatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity 2 with multiple agreements", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("Activity Linked With Pledge", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity with both MTEFs and Act.Comms", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("activity with capital spending", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with components", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with contracting agency", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with directed MTEFs", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with funded components", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with incomplete agreement", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with many MTEFs", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with pipeline MTEFs and act. disb", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity with planned disbursements", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with primary_program", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity with primary_tertiary_program", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity with tertiary_program", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity with Zones", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Activity With Zones and Percentages", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity-weird-funding", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("activity-with-unfunded-components", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("activity_with_disaster_response", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("arrears test", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("crazy funding 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("date-filters-activity", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("department/division", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Eth Water", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("execution rate activity", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("expenditure class", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("mtef activity 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("mtef activity 2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("new activity with contracting", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("PID: original", "second status", 2118,
                coos(
                    entry("cats", "Status", 1, 2118))),
            cell("PID: original > actual", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("PID: original, actual", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("PID: original, proposed", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("PID: original, proposed, actual", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("pledged 2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("pledged education activity 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Project with documents", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Proposed Project Cost 1 - USD", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Proposed Project Cost 2 - EUR", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("ptc activity 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("ptc activity 2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Pure MTEF Project", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Real SSC Activity 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Real SSC Activity 2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("second with disaster response", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("SSC Project 1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("SSC Project 2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("SubNational no percentages", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("TAC_activity_1", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("TAC_activity_2", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Test MTEF directed", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("third activity with agreements", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("Unvalidated activity", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("with annual ppc and actual comm", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))),
            cell("with weird currencies", "default status", 2117,
                coos(
                    entry("cats", "Status", 1, 2117))));
    }

}
