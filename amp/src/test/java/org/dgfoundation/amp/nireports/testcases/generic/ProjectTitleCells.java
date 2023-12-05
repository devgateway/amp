package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.TextCell;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ProjectTitleCells extends HardcodedCells<TextCell>{

    public ProjectTitleCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
        super(activityNames, entityNames, null);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "activity 1 with agreement", 65,
                coos(
                    entry("acts", "acts", 0, 65))),
            cell("Activity 2 with multiple agreements", "Activity 2 with multiple agreements", 66,
                coos(
                    entry("acts", "acts", 0, 66))),
            cell("Activity Linked With Pledge", "Activity Linked With Pledge", 41,
                coos(
                    entry("acts", "acts", 0, 41))),
            cell("Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", 70,
                coos(
                    entry("acts", "acts", 0, 70))),
            cell("activity with capital spending", "activity with capital spending", 50,
                coos(
                    entry("acts", "acts", 0, 50))),
            cell("activity with components", "activity with components", 21,
                coos(
                    entry("acts", "acts", 0, 21))),
            cell("activity with contracting agency", "activity with contracting agency", 52,
                coos(
                    entry("acts", "acts", 0, 52))),
            cell("activity with directed MTEFs", "activity with directed MTEFs", 73,
                coos(
                    entry("acts", "acts", 0, 73))),
            cell("activity with funded components", "activity with funded components", 63,
                coos(
                    entry("acts", "acts", 0, 63))),
            cell("activity with incomplete agreement", "activity with incomplete agreement", 68,
                coos(
                    entry("acts", "acts", 0, 68))),
            cell("activity with many MTEFs", "activity with many MTEFs", 78,
                coos(
                    entry("acts", "acts", 0, 78))),
            cell("activity with pipeline MTEFs and act. disb", "activity with pipeline MTEFs and act. disb", 76,
                coos(
                    entry("acts", "acts", 0, 76))),
            cell("Activity with planned disbursements", "Activity with planned disbursements", 69,
                coos(
                    entry("acts", "acts", 0, 69))),
            cell("activity with primary_program", "activity with primary_program", 44,
                coos(
                    entry("acts", "acts", 0, 44))),
            cell("Activity with primary_tertiary_program", "Activity with primary_tertiary_program", 43,
                coos(
                    entry("acts", "acts", 0, 43))),
            cell("activity with tertiary_program", "activity with tertiary_program", 45,
                coos(
                    entry("acts", "acts", 0, 45))),
            cell("Activity with Zones", "Activity with Zones", 33,
                coos(
                    entry("acts", "acts", 0, 33))),
            cell("Activity With Zones and Percentages", "Activity With Zones and Percentages", 36,
                coos(
                    entry("acts", "acts", 0, 36))),
            cell("activity-weird-funding", "activity-weird-funding", 88,
                coos(
                    entry("acts", "acts", 0, 88))),
            cell("activity-with-unfunded-components", "activity-with-unfunded-components", 61,
                coos(
                    entry("acts", "acts", 0, 61))),
            cell("activity_with_disaster_response", "activity_with_disaster_response", 71,
                coos(
                    entry("acts", "acts", 0, 71))),
            cell("arrears test", "arrears test", 80,
                coos(
                    entry("acts", "acts", 0, 80))),
            cell("crazy funding 1", "crazy funding 1", 32,
                coos(
                    entry("acts", "acts", 0, 32))),
            cell("date-filters-activity", "date-filters-activity", 26,
                coos(
                    entry("acts", "acts", 0, 26))),
            cell("department/division", "department/division", 90,
                coos(
                    entry("acts", "acts", 0, 90))),
            cell("Eth Water", "Eth Water", 24,
                coos(
                    entry("acts", "acts", 0, 24))),
            cell("execution rate activity", "execution rate activity", 77,
                coos(
                    entry("acts", "acts", 0, 77))),
            cell("expenditure class", "expenditure class", 87,
                coos(
                    entry("acts", "acts", 0, 87))),
            cell("mtef activity 1", "mtef activity 1", 25,
                coos(
                    entry("acts", "acts", 0, 25))),
            cell("mtef activity 2", "mtef activity 2", 27,
                coos(
                    entry("acts", "acts", 0, 27))),
            cell("new activity with contracting", "new activity with contracting", 53,
                coos(
                    entry("acts", "acts", 0, 53))),
            cell("PID: original", "PID: original", 84,
                coos(
                    entry("acts", "acts", 0, 84))),
            cell("PID: original > actual", "PID: original > actual", 83,
                coos(
                    entry("acts", "acts", 0, 83))),
            cell("PID: original, actual", "PID: original, actual", 82,
                coos(
                    entry("acts", "acts", 0, 82))),
            cell("PID: original, proposed", "PID: original, proposed", 85,
                coos(
                    entry("acts", "acts", 0, 85))),
            cell("PID: original, proposed, actual", "PID: original, proposed, actual", 81,
                coos(
                    entry("acts", "acts", 0, 81))),
            cell("pledged 2", "pledged 2", 48,
                coos(
                    entry("acts", "acts", 0, 48))),
            cell("pledged education activity 1", "pledged education activity 1", 46,
                coos(
                    entry("acts", "acts", 0, 46))),
            cell("Project with documents", "Project with documents", 23,
                coos(
                    entry("acts", "acts", 0, 23))),
            cell("Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", 15,
                coos(
                    entry("acts", "acts", 0, 15))),
            cell("Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", 17,
                coos(
                    entry("acts", "acts", 0, 17))),
            cell("ptc activity 1", "ptc activity 1", 28,
                coos(
                    entry("acts", "acts", 0, 28))),
            cell("ptc activity 2", "ptc activity 2", 29,
                coos(
                    entry("acts", "acts", 0, 29))),
            cell("Pure MTEF Project", "Pure MTEF Project", 19,
                coos(
                    entry("acts", "acts", 0, 19))),
            cell("Real SSC Activity 1", "Real SSC Activity 1", 39,
                coos(
                    entry("acts", "acts", 0, 39))),
            cell("Real SSC Activity 2", "Real SSC Activity 2", 38,
                coos(
                    entry("acts", "acts", 0, 38))),
            cell("second with disaster response", "second with disaster response", 92,
                coos(
                    entry("acts", "acts", 0, 92))),
            cell("SSC Project 1", "SSC Project 1", 30,
                coos(
                    entry("acts", "acts", 0, 30))),
            cell("SSC Project 2", "SSC Project 2", 31,
                coos(
                    entry("acts", "acts", 0, 31))),
            cell("SubNational no percentages", "SubNational no percentages", 40,
                coos(
                    entry("acts", "acts", 0, 40))),
            cell("TAC_activity_1", "TAC_activity_1", 12,
                coos(
                    entry("acts", "acts", 0, 12))),
            cell("TAC_activity_2", "TAC_activity_2", 13,
                coos(
                    entry("acts", "acts", 0, 13))),
            cell("Test MTEF directed", "Test MTEF directed", 18,
                coos(
                    entry("acts", "acts", 0, 18))),
            cell("third activity with agreements", "third activity with agreements", 67,
                coos(
                    entry("acts", "acts", 0, 67))),
            cell("Unvalidated activity", "Unvalidated activity", 64,
                coos(
                    entry("acts", "acts", 0, 64))),
            cell("with annual ppc and actual comm", "with annual ppc and actual comm", 94,
                coos(
                    entry("acts", "acts", 0, 94))),
            cell("with weird currencies", "with weird currencies", 79,
                coos(
                    entry("acts", "acts", 0, 79))),

            // cells below have been added manually (no AMP correspondent). They should be restored manually in case this file is overwritten by codegen
            cell("custom_1", "custom_1", 700,
                coos(
                    entry("acts", "acts", 0, 700))));
    }

}
