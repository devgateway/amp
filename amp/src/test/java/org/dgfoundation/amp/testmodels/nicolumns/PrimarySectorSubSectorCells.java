package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class PrimarySectorSubSectorCells extends HardcodedCells<PercentageTextCell>{

	public PrimarySectorSubSectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "111 - Education, level unspecified", 1.000000),
			createCell("Activity Linked With Pledge", "111 - Education, level unspecified", 1.000000),
			createCell("Activity With Zones and Percentages", "", 0.700000),
			createCell("Activity With Zones and Percentages", "111 - Education, level unspecified", 0.300000),
			createCell("Activity with Zones", "111 - Education, level unspecified", 1.000000),
			createCell("Activity with both MTEFs and Act.Comms", "111 - Education, level unspecified", 0.600000),
			createCell("Activity with both MTEFs and Act.Comms", "11240 - Early childhood education", 0.400000),
			createCell("Activity with planned disbursements", "", 1.000000),
			createCell("Activity with primary_tertiary_program", "111 - Education, level unspecified", 1.000000),
			createCell("Eth Water", "111 - Education, level unspecified", 1.000000),
			createCell("Project with documents", "111 - Education, level unspecified", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "111 - Education, level unspecified", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "111 - Education, level unspecified", 1.000000),
			createCell("Pure MTEF Project", "111 - Education, level unspecified", 1.000000),
			createCell("SSC Project 1", "111 - Education, level unspecified", 1.000000),
			createCell("SSC Project 2", "11220 - Primary education", 1.000000),
			createCell("SubNational no percentages", "", 1.000000),
			createCell("TAC_activity_1", "", 1.000000),
			createCell("TAC_activity_2", "", 1.000000),
			createCell("Test MTEF directed", "111 - Education, level unspecified", 1.000000),
			createCell("Unvalidated activity", "111 - Education, level unspecified", 1.000000),
			createCell("activity 1 with agreement", "111 - Education, level unspecified", 0.500000),
			createCell("activity 1 with agreement", "11230 - Basic life skills for youth and adults", 0.230000),
			createCell("activity 1 with agreement", "11240 - Early childhood education", 0.270000),
			createCell("activity with capital spending", "111 - Education, level unspecified", 1.000000),
			createCell("activity with components", "", 1.000000),
			createCell("activity with contracting agency", "111 - Education, level unspecified", 0.600000),
			createCell("activity with contracting agency", "11220 - Primary education", 0.100000),
			createCell("activity with contracting agency", "121 - Health, general", 0.300000),
			createCell("activity with directed MTEFs", "111 - Education, level unspecified", 1.000000),
			createCell("activity with funded components", "111 - Education, level unspecified", 1.000000),
			createCell("activity with incomplete agreement", "111 - Education, level unspecified", 1.000000),
			createCell("activity with many MTEFs", "111 - Education, level unspecified", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "111 - Education, level unspecified", 1.000000),
			createCell("activity with primary_program", "111 - Education, level unspecified", 1.000000),
			createCell("activity with tertiary_program", "111 - Education, level unspecified", 1.000000),
			createCell("activity-with-unfunded-components", "111 - Education, level unspecified", 1.000000),
			createCell("activity_with_disaster_response", "111 - Education, level unspecified", 0.600000),
			createCell("activity_with_disaster_response", "11320 - Secondary education", 0.400000),
			createCell("crazy funding 1", "111 - Education, level unspecified", 1.000000),
			createCell("date-filters-activity", "111 - Education, level unspecified", 1.000000),
			createCell("execution rate activity", "111 - Education, level unspecified", 1.000000),
			createCell("mtef activity 1", "111 - Education, level unspecified", 1.000000),
			createCell("mtef activity 2", "111 - Education, level unspecified", 1.000000),
			createCell("pledged 2", "", 1.000000),
			createCell("pledged education activity 1", "", 1.000000),
			createCell("ptc activity 1", "111 - Education, level unspecified", 1.000000),
			createCell("ptc activity 2", "111 - Education, level unspecified", 1.000000),
			createCell("third activity with agreements", "111 - Education, level unspecified", 1.000000),
			createCell("with weird currencies", "111 - Education, level unspecified", 0.900000),
			createCell("with weird currencies", "11230 - Basic life skills for youth and adults", 0.100000)
);
	}

}
