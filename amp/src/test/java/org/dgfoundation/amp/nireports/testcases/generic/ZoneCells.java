package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ZoneCells extends HardcodedCells<PercentageTextCell>{

    public ZoneCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "Glodeni", 9111, 1.000000),
            cell("Activity 2 with multiple agreements", "", -9089, 1.000000),
            cell("Activity Linked With Pledge", "", -9088, 1.000000),
            cell("Activity with both MTEFs and Act.Comms", "", -9086, 0.300000),
            cell("Activity with both MTEFs and Act.Comms", "", -9090, 0.700000),
            cell("activity with capital spending", "", -9089, 1.000000),
            cell("activity with components", "", -9085, 1.000000),
            cell("activity with contracting agency", "Apareni", 9113, 0.550000),
            cell("activity with contracting agency", "Slobozia", 9115, 0.450000),
            cell("activity with directed MTEFs", "", -9088, 1.000000),
            cell("activity with funded components", "", -8977, 1.000000),
            cell("activity with incomplete agreement", "", -8977, 1.000000),
            cell("activity with many MTEFs", "", -9090, 1.000000),
            cell("activity with pipeline MTEFs and act. disb", "", -9089, 1.000000),
            cell("Activity with planned disbursements", "", -8977, 1.000000),
            cell("activity with primary_program", "", -8977, 1.000000),
            cell("Activity with primary_tertiary_program", "", -8977, 1.000000),
            cell("activity with tertiary_program", "", -8977, 1.000000),
            cell("Activity with Zones", "Bulboaca", 9108, 0.500000),
            cell("Activity with Zones", "Glodeni", 9111, 0.500000),
            cell("Activity With Zones and Percentages", "Dolboaca", 9110, 0.200000),
            cell("Activity With Zones and Percentages", "Glodeni", 9111, 0.800000),
            cell("activity-weird-funding", "", -9088, 0.400000),
            cell("activity-weird-funding", "", -9092, 0.600000),
            cell("activity-with-unfunded-components", "Tiraspol", 9114, 1.000000),
            cell("activity_with_disaster_response", "", -8977, 1.000000),
            cell("arrears test", "Dolboaca", 9110, 1.000000),
            cell("crazy funding 1", "", -9086, 1.000000),
            cell("date-filters-activity", "", -8977, 1.000000),
            cell("department/division", "", -9103, 1.000000),
            cell("Eth Water", "", -9085, 1.000000),
            cell("execution rate activity", "", -9088, 0.500000),
            cell("execution rate activity", "", -9091, 0.500000),
            cell("expenditure class", "", -9088, 1.000000),
            cell("mtef activity 1", "", -8977, 1.000000),
            cell("mtef activity 2", "", -9085, 1.000000),
            cell("new activity with contracting", "", -999999999, 1.000000),
            cell("PID: original", "", -9086, 1.000000),
            cell("PID: original > actual", "", -8977, 1.000000),
            cell("PID: original, actual", "", -9086, 1.000000),
            cell("PID: original, proposed", "Dolboaca", 9110, 1.000000),
            cell("PID: original, proposed, actual", "", -9086, 1.000000),
            cell("pledged 2", "", -9087, 1.000000),
            cell("pledged education activity 1", "", -9089, 1.000000),
            cell("Project with documents", "", -9086, 1.000000),
            cell("Proposed Project Cost 1 - USD", "", -9090, 1.000000),
            cell("Proposed Project Cost 2 - EUR", "", -9085, 1.000000),
            cell("ptc activity 1", "", -9085, 1.000000),
            cell("ptc activity 2", "", -9085, 1.000000),
            cell("Pure MTEF Project", "", -9087, 1.000000),
            cell("Real SSC Activity 1", "", -9086, 1.000000),
            cell("Real SSC Activity 2", "", -8977, 1.000000),
            cell("second with disaster response", "", -9087, 1.000000),
            cell("SSC Project 1", "", -9085, 1.000000),
            cell("SSC Project 2", "", -9092, 1.000000),
            cell("SubNational no percentages", "", -9085, 0.500000),
            cell("SubNational no percentages", "Glodeni", 9111, 0.500000),
            cell("TAC_activity_1", "", -9091, 1.000000),
            cell("TAC_activity_2", "", -9093, 1.000000),
            cell("Test MTEF directed", "", -9085, 1.000000),
            cell("third activity with agreements", "", -9088, 1.000000),
            cell("Unvalidated activity", "", -8977, 1.000000),
            cell("with annual ppc and actual comm", "", -9086, 1.000000),
            cell("with weird currencies", "", -8977, 1.000000));
    }

}
