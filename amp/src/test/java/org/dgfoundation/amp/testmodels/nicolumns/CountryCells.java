package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class CountryCells extends HardcodedCells<PercentageTextCell>{

	public CountryCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			cell("activity 1 with agreement", "Moldova", 8977, 1.000000),
			cell("Activity 2 with multiple agreements", "Moldova", 8977, 1.000000),
			cell("Activity Linked With Pledge", "Moldova", 8977, 1.000000),
			cell("Activity with both MTEFs and Act.Comms", "Moldova", 8977, 1.000000),
			cell("activity with capital spending", "Moldova", 8977, 1.000000),
			cell("activity with components", "Moldova", 8977, 1.000000),
			cell("activity with contracting agency", "Moldova", 8977, 1.000000),
			cell("activity with directed MTEFs", "Moldova", 8977, 1.000000),
			cell("activity with funded components", "Moldova", 8977, 1.000000),
			cell("activity with incomplete agreement", "Moldova", 8977, 1.000000),
			cell("activity with many MTEFs", "Moldova", 8977, 1.000000),
			cell("activity with pipeline MTEFs and act. disb", "Moldova", 8977, 1.000000),
			cell("Activity with planned disbursements", "Moldova", 8977, 1.000000),
			cell("activity with primary_program", "Moldova", 8977, 1.000000),
			cell("Activity with primary_tertiary_program", "Moldova", 8977, 1.000000),
			cell("activity with tertiary_program", "Moldova", 8977, 1.000000),
			cell("Activity with Zones", "Moldova", 8977, 1.000000),
			cell("Activity With Zones and Percentages", "Moldova", 8977, 1.000000),
			cell("activity-with-unfunded-components", "Moldova", 8977, 1.000000),
			cell("activity_with_disaster_response", "Moldova", 8977, 1.000000),
			cell("crazy funding 1", "Moldova", 8977, 1.000000),
			cell("date-filters-activity", "Moldova", 8977, 1.000000),
			cell("Eth Water", "Moldova", 8977, 1.000000),
			cell("execution rate activity", "Moldova", 8977, 1.000000),
			cell("mtef activity 1", "Moldova", 8977, 1.000000),
			cell("mtef activity 2", "Moldova", 8977, 1.000000),
			cell("pledged 2", "Moldova", 8977, 1.000000),
			cell("pledged education activity 1", "Moldova", 8977, 1.000000),
			cell("Project with documents", "Moldova", 8977, 1.000000),
			cell("Proposed Project Cost 1 - USD", "Moldova", 8977, 1.000000),
			cell("Proposed Project Cost 2 - EUR", "Moldova", 8977, 1.000000),
			cell("ptc activity 1", "Moldova", 8977, 1.000000),
			cell("ptc activity 2", "Moldova", 8977, 1.000000),
			cell("Pure MTEF Project", "Moldova", 8977, 1.000000),
			cell("SSC Project 1", "Moldova", 8977, 1.000000),
			cell("SSC Project 2", "Moldova", 8977, 1.000000),
			cell("SubNational no percentages", "Moldova", 8977, 1.000000),
			cell("TAC_activity_1", "Moldova", 8977, 1.000000),
			cell("TAC_activity_2", "Moldova", 8977, 1.000000),
			cell("Test MTEF directed", "Moldova", 8977, 1.000000),
			cell("third activity with agreements", "Moldova", 8977, 1.000000),
			cell("Unvalidated activity", "Moldova", 8977, 1.000000),
			cell("with weird currencies", "Moldova", 8977, 1.000000));
	}

}
