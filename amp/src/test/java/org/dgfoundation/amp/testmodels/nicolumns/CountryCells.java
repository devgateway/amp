package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class CountryCells extends HardcodedCells<PercentageTextCell>{

	public CountryCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "Moldova", 1.000000),
			createCell("Activity Linked With Pledge", "Moldova", 1.000000),
			createCell("Activity With Zones and Percentages", "Moldova", 1.000000),
			createCell("Activity with Zones", "Moldova", 1.000000),
			createCell("Activity with both MTEFs and Act.Comms", "Moldova", 1.000000),
			createCell("Activity with planned disbursements", "Moldova", 1.000000),
			createCell("Activity with primary_tertiary_program", "Moldova", 1.000000),
			createCell("Eth Water", "Moldova", 1.000000),
			createCell("Project with documents", "Moldova", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "Moldova", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "Moldova", 1.000000),
			createCell("Pure MTEF Project", "Moldova", 1.000000),
			createCell("SSC Project 1", "Moldova", 1.000000),
			createCell("SSC Project 2", "Moldova", 1.000000),
			createCell("SubNational no percentages", "Moldova", 1.000000),
			createCell("TAC_activity_1", "Moldova", 1.000000),
			createCell("TAC_activity_2", "Moldova", 1.000000),
			createCell("Test MTEF directed", "Moldova", 1.000000),
			createCell("Unvalidated activity", "Moldova", 1.000000),
			createCell("activity 1 with agreement", "Moldova", 1.000000),
			createCell("activity with capital spending", "Moldova", 1.000000),
			createCell("activity with components", "Moldova", 1.000000),
			createCell("activity with contracting agency", "Moldova", 1.000000),
			createCell("activity with directed MTEFs", "Moldova", 1.000000),
			createCell("activity with funded components", "Moldova", 1.000000),
			createCell("activity with incomplete agreement", "Moldova", 1.000000),
			createCell("activity with many MTEFs", "Moldova", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "Moldova", 1.000000),
			createCell("activity with primary_program", "Moldova", 1.000000),
			createCell("activity with tertiary_program", "Moldova", 1.000000),
			createCell("activity-with-unfunded-components", "Moldova", 1.000000),
			createCell("activity_with_disaster_response", "Moldova", 1.000000),
			createCell("crazy funding 1", "Moldova", 1.000000),
			createCell("date-filters-activity", "Moldova", 1.000000),
			createCell("execution rate activity", "Moldova", 1.000000),
			createCell("mtef activity 1", "Moldova", 1.000000),
			createCell("mtef activity 2", "Moldova", 1.000000),
			createCell("pledged 2", "Moldova", 1.000000),
			createCell("pledged education activity 1", "Moldova", 1.000000),
			createCell("ptc activity 1", "Moldova", 1.000000),
			createCell("ptc activity 2", "Moldova", 1.000000),
			createCell("third activity with agreements", "Moldova", 1.000000),
			createCell("with weird currencies", "Moldova", 1.000000)
);
	}

}
