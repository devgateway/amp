package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PrimarySectorCells extends HardcodedCells<PercentageTextCell>{

    public PrimarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "110 - EDUCATION", 6236, 0.500000),
            cell("activity 1 with agreement", "112 - BASIC EDUCATION", 6242, 0.500000),
            cell("Activity 2 with multiple agreements", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity Linked With Pledge", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity with both MTEFs and Act.Comms", "110 - EDUCATION", 6236, 0.600000),
            cell("Activity with both MTEFs and Act.Comms", "112 - BASIC EDUCATION", 6242, 0.400000),
            cell("activity with capital spending", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with components", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with contracting agency", "110 - EDUCATION", 6236, 0.600000),
            cell("activity with contracting agency", "112 - BASIC EDUCATION", 6242, 0.100000),
            cell("activity with contracting agency", "120 - HEALTH", 6252, 0.300000),
            cell("activity with directed MTEFs", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with funded components", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with incomplete agreement", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with many MTEFs", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with pipeline MTEFs and act. disb", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity with planned disbursements", "112 - BASIC EDUCATION", 6242, 1.000000),
            cell("activity with primary_program", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity with primary_tertiary_program", "110 - EDUCATION", 6236, 1.000000),
            cell("activity with tertiary_program", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity with Zones", "110 - EDUCATION", 6236, 1.000000),
            cell("Activity With Zones and Percentages", "110 - EDUCATION", 6236, 0.300000),
            cell("Activity With Zones and Percentages", "120 - HEALTH", 6252, 0.700000),
            cell("activity-weird-funding", "110 - EDUCATION", 6236, 1.000000),
            cell("activity-with-unfunded-components", "110 - EDUCATION", 6236, 1.000000),
            cell("activity_with_disaster_response", "110 - EDUCATION", 6236, 0.600000),
            cell("activity_with_disaster_response", "113 - SECONDARY EDUCATION", 6246, 0.400000),
            cell("arrears test", "110 - EDUCATION", 6236, 1.000000),
            cell("crazy funding 1", "110 - EDUCATION", 6236, 1.000000),
            cell("date-filters-activity", "110 - EDUCATION", 6236, 1.000000),
            cell("department/division", "110 - EDUCATION", 6236, 1.000000),
            cell("Eth Water", "110 - EDUCATION", 6236, 1.000000),
            cell("execution rate activity", "110 - EDUCATION", 6236, 1.000000),
            cell("expenditure class", "110 - EDUCATION", 6236, 1.000000),
            cell("mtef activity 1", "110 - EDUCATION", 6236, 1.000000),
            cell("mtef activity 2", "110 - EDUCATION", 6236, 1.000000),
            cell("PID: original", "110 - EDUCATION", 6236, 1.000000),
            cell("PID: original > actual", "110 - EDUCATION", 6236, 1.000000),
            cell("PID: original, actual", "110 - EDUCATION", 6236, 1.000000),
            cell("PID: original, proposed", "110 - EDUCATION", 6236, 1.000000),
            cell("PID: original, proposed, actual", "110 - EDUCATION", 6236, 1.000000),
            cell("pledged 2", "113 - SECONDARY EDUCATION", 6246, 1.000000),
            cell("pledged education activity 1", "110 - EDUCATION", 6236, 1.000000),
            cell("Project with documents", "110 - EDUCATION", 6236, 1.000000),
            cell("Proposed Project Cost 1 - USD", "110 - EDUCATION", 6236, 1.000000),
            cell("Proposed Project Cost 2 - EUR", "110 - EDUCATION", 6236, 1.000000),
            cell("ptc activity 1", "110 - EDUCATION", 6236, 1.000000),
            cell("ptc activity 2", "110 - EDUCATION", 6236, 1.000000),
            cell("Pure MTEF Project", "110 - EDUCATION", 6236, 1.000000),
            cell("Real SSC Activity 1", "110 - EDUCATION", 6236, 1.000000),
            cell("Real SSC Activity 2", "110 - EDUCATION", 6236, 1.000000),
            cell("second with disaster response", "110 - EDUCATION", 6236, 1.000000),
            cell("SSC Project 1", "110 - EDUCATION", 6236, 1.000000),
            cell("SSC Project 2", "112 - BASIC EDUCATION", 6242, 1.000000),
            cell("SubNational no percentages", "110 - EDUCATION", 6236, 1.000000),
            cell("TAC_activity_1", "112 - BASIC EDUCATION", 6242, 1.000000),
            cell("TAC_activity_2", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 6267, 1.000000),
            cell("Test MTEF directed", "110 - EDUCATION", 6236, 1.000000),
            cell("third activity with agreements", "110 - EDUCATION", 6236, 1.000000),
            cell("Unvalidated activity", "110 - EDUCATION", 6236, 1.000000),
            cell("with annual ppc and actual comm", "110 - EDUCATION", 6236, 1.000000),
            cell("with weird currencies", "110 - EDUCATION", 6236, 0.900000),
            cell("with weird currencies", "112 - BASIC EDUCATION", 6242, 0.100000),

            // cells below have been added manually (no AMP correspondent). They should be restored manually in case this file is overwritten by codegen
            cell("custom_1", "112 - BASIC EDUCATION", 6242, 0.400000),
            cell("custom_1", "110 - EDUCATION", 6236, 0.600000),
            
            cell("new activity with contracting", "", ColumnReportData.UNALLOCATED_ID, 1.000000)
);
    }

}
