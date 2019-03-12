package org.dgfoundation.amp.nireports.testcases.generic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class RegionCells extends HardcodedCells<PercentageTextCell>{

    public RegionCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "Balti County", 9086, 1.000000),
            cell("Activity 2 with multiple agreements", "Chisinau County", 9089, 1.000000),
            cell("Activity Linked With Pledge", "Chisinau City", 9088, 1.000000),
            cell("Activity with both MTEFs and Act.Comms", "Balti County", 9086, 0.300000),
            cell("Activity with both MTEFs and Act.Comms", "Drochia County", 9090, 0.700000),
            cell("activity with capital spending", "Chisinau County", 9089, 1.000000),
            cell("activity with components", "Anenii Noi County", 9085, 1.000000),
            cell("activity with contracting agency", "Balti County", 9086, 0.550000),
            cell("activity with contracting agency", "Transnistrian Region", 9105, 0.450000),
            cell("activity with directed MTEFs", "Chisinau City", 9088, 1.000000),
            cell("activity with funded components", "", -8977, 1.000000),
            cell("activity with incomplete agreement", "", -8977, 1.000000),
            cell("activity with many MTEFs", "Drochia County", 9090, 1.000000),
            cell("activity with pipeline MTEFs and act. disb", "Chisinau County", 9089, 1.000000),
            cell("Activity with planned disbursements", "", -8977, 1.000000),
            cell("activity with primary_program", "", -8977, 1.000000),
            cell("Activity with primary_tertiary_program", "", -8977, 1.000000),
            cell("activity with tertiary_program", "", -8977, 1.000000),
            cell("Activity with Zones", "Anenii Noi County", 9085, 0.500000),
            cell("Activity with Zones", "Balti County", 9086, 0.500000),
            cell("Activity With Zones and Percentages", "Anenii Noi County", 9085, 0.200000),
            cell("Activity With Zones and Percentages", "Balti County", 9086, 0.800000),
            cell("activity-weird-funding", "Chisinau City", 9088, 0.400000),
            cell("activity-weird-funding", "Edinet County", 9092, 0.600000),
            cell("activity-with-unfunded-components", "Transnistrian Region", 9105, 1.000000),
            cell("activity_with_disaster_response", "", -8977, 1.000000),
            cell("arrears test", "Anenii Noi County", 9085, 1.000000),
            cell("crazy funding 1", "Balti County", 9086, 1.000000),
            cell("date-filters-activity", "", -8977, 1.000000),
            cell("department/division", "Telenesti County", 9103, 1.000000),
            cell("Eth Water", "Anenii Noi County", 9085, 1.000000),
            cell("execution rate activity", "Chisinau City", 9088, 0.500000),
            cell("execution rate activity", "Dubasari County", 9091, 0.500000),
            cell("expenditure class", "Chisinau City", 9088, 1.000000),
            cell("mtef activity 1", "", -8977, 1.000000),
            cell("mtef activity 2", "Anenii Noi County", 9085, 1.000000),
            cell("new activity with contracting", "", -999999999, 1.000000),
            cell("PID: original", "Balti County", 9086, 1.000000),
            cell("PID: original > actual", "", -8977, 1.000000),
            cell("PID: original, actual", "Balti County", 9086, 1.000000),
            cell("PID: original, proposed", "Anenii Noi County", 9085, 1.000000),
            cell("PID: original, proposed, actual", "Balti County", 9086, 1.000000),
            cell("pledged 2", "Cahul County", 9087, 1.000000),
            cell("pledged education activity 1", "Chisinau County", 9089, 1.000000),
            cell("Project with documents", "Balti County", 9086, 1.000000),
            cell("Proposed Project Cost 1 - USD", "Drochia County", 9090, 1.000000),
            cell("Proposed Project Cost 2 - EUR", "Anenii Noi County", 9085, 1.000000),
            cell("ptc activity 1", "Anenii Noi County", 9085, 1.000000),
            cell("ptc activity 2", "Anenii Noi County", 9085, 1.000000),
            cell("Pure MTEF Project", "Cahul County", 9087, 1.000000),
            cell("Real SSC Activity 1", "Balti County", 9086, 1.000000),
            cell("Real SSC Activity 2", "", -8977, 1.000000),
            cell("second with disaster response", "Cahul County", 9087, 1.000000),
            cell("SSC Project 1", "Anenii Noi County", 9085, 1.000000),
            cell("SSC Project 2", "Edinet County", 9092, 1.000000),
            cell("SubNational no percentages", "Anenii Noi County", 9085, 0.500000),
            cell("SubNational no percentages", "Balti County", 9086, 0.500000),
            cell("TAC_activity_1", "Dubasari County", 9091, 1.000000),
            cell("TAC_activity_2", "Falesti County", 9093, 1.000000),
            cell("Test MTEF directed", "Anenii Noi County", 9085, 1.000000),
            cell("third activity with agreements", "Chisinau City", 9088, 1.000000),
            cell("Unvalidated activity", "", -8977, 1.000000),
            cell("with annual ppc and actual comm", "Balti County", 9086, 1.000000),
            cell("with weird currencies", "", -8977, 1.000000));
    }

}
