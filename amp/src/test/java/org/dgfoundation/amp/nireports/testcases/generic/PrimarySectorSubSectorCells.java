package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PrimarySectorSubSectorCells extends HardcodedCells<PercentageTextCell>{

    public PrimarySectorSubSectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "111 - Education, level unspecified", 6237, 0.500000),
            cell("activity 1 with agreement", "11230 - Basic life skills for youth and adults", 6244, 0.230000),
            cell("activity 1 with agreement", "11240 - Early childhood education", 6245, 0.270000),
            cell("Activity 2 with multiple agreements", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity Linked With Pledge", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity with both MTEFs and Act.Comms", "111 - Education, level unspecified", 6237, 0.600000),
            cell("Activity with both MTEFs and Act.Comms", "11240 - Early childhood education", 6245, 0.400000),
            cell("activity with capital spending", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with components", "", -6236, 1.000000),
            cell("activity with contracting agency", "111 - Education, level unspecified", 6237, 0.600000),
            cell("activity with contracting agency", "11220 - Primary education", 6243, 0.100000),
            cell("activity with contracting agency", "121 - Health, general", 6253, 0.300000),
            cell("activity with directed MTEFs", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with funded components", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with incomplete agreement", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with many MTEFs", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with pipeline MTEFs and act. disb", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity with planned disbursements", "", -6242, 1.000000),
            cell("activity with primary_program", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity with primary_tertiary_program", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity with tertiary_program", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity with Zones", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Activity With Zones and Percentages", "", -6252, 0.700000),
            cell("Activity With Zones and Percentages", "111 - Education, level unspecified", 6237, 0.300000),
            cell("activity-weird-funding", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity-with-unfunded-components", "111 - Education, level unspecified", 6237, 1.000000),
            cell("activity_with_disaster_response", "111 - Education, level unspecified", 6237, 0.600000),
            cell("activity_with_disaster_response", "11320 - Secondary education", 6247, 0.400000),
            cell("arrears test", "111 - Education, level unspecified", 6237, 1.000000),
            cell("crazy funding 1", "111 - Education, level unspecified", 6237, 1.000000),
            cell("date-filters-activity", "111 - Education, level unspecified", 6237, 1.000000),
            cell("department/division", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Eth Water", "111 - Education, level unspecified", 6237, 1.000000),
            cell("execution rate activity", "111 - Education, level unspecified", 6237, 1.000000),
            cell("expenditure class", "111 - Education, level unspecified", 6237, 1.000000),
            cell("mtef activity 1", "111 - Education, level unspecified", 6237, 1.000000),
            cell("mtef activity 2", "111 - Education, level unspecified", 6237, 1.000000),
            cell("PID: original", "111 - Education, level unspecified", 6237, 1.000000),
            cell("PID: original > actual", "111 - Education, level unspecified", 6237, 1.000000),
            cell("PID: original, actual", "111 - Education, level unspecified", 6237, 1.000000),
            cell("PID: original, proposed", "111 - Education, level unspecified", 6237, 1.000000),
            cell("PID: original, proposed, actual", "111 - Education, level unspecified", 6237, 1.000000),
            cell("pledged 2", "", -6246, 1.000000),
            cell("pledged education activity 1", "", -6236, 1.000000),
            cell("Project with documents", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Proposed Project Cost 1 - USD", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Proposed Project Cost 2 - EUR", "111 - Education, level unspecified", 6237, 1.000000),
            cell("ptc activity 1", "111 - Education, level unspecified", 6237, 1.000000),
            cell("ptc activity 2", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Pure MTEF Project", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Real SSC Activity 1", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Real SSC Activity 2", "111 - Education, level unspecified", 6237, 1.000000),
            cell("second with disaster response", "111 - Education, level unspecified", 6237, 1.000000),
            cell("SSC Project 1", "111 - Education, level unspecified", 6237, 1.000000),
            cell("SSC Project 2", "11220 - Primary education", 6243, 1.000000),
            cell("SubNational no percentages", "", -6236, 1.000000),
            cell("TAC_activity_1", "", -6242, 1.000000),
            cell("TAC_activity_2", "", -6267, 1.000000),
            cell("Test MTEF directed", "111 - Education, level unspecified", 6237, 1.000000),
            cell("third activity with agreements", "111 - Education, level unspecified", 6237, 1.000000),
            cell("Unvalidated activity", "111 - Education, level unspecified", 6237, 1.000000),
            cell("with annual ppc and actual comm", "111 - Education, level unspecified", 6237, 1.000000),
            cell("with weird currencies", "111 - Education, level unspecified", 6237, 0.900000),
            cell("with weird currencies", "11230 - Basic life skills for youth and adults", 6244, 0.100000));
    }

}
