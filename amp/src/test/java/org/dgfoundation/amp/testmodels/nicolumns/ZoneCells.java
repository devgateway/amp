package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class ZoneCells extends HardcodedCells<PercentageTextCell>{

	public ZoneCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "", 1.000000),
			createCell("Activity Linked With Pledge", "", 1.000000),
			createCell("Activity With Zones and Percentages", "Dolboaca", 0.200000),
			createCell("Activity With Zones and Percentages", "Glodeni", 0.800000),
			createCell("Activity with Zones", "Bulboaca", 0.500000),
			createCell("Activity with Zones", "Glodeni", 0.500000),
			createCell("Activity with both MTEFs and Act.Comms", "", 0.300000),
			createCell("Activity with both MTEFs and Act.Comms", "", 0.700000),
			createCell("Activity with planned disbursements", "", 1.000000),
			createCell("Activity with primary_tertiary_program", "", 1.000000),
			createCell("Eth Water", "", 1.000000),
			createCell("Project with documents", "", 1.000000),
			createCell("Proposed Project Cost 1 - USD", "", 1.000000),
			createCell("Proposed Project Cost 2 - EUR", "", 1.000000),
			createCell("Pure MTEF Project", "", 1.000000),
			createCell("SSC Project 1", "", 1.000000),
			createCell("SSC Project 2", "", 1.000000),
			createCell("SubNational no percentages", "", 0.500000),
			createCell("SubNational no percentages", "Glodeni", 0.500000),
			createCell("TAC_activity_1", "", 1.000000),
			createCell("TAC_activity_2", "", 1.000000),
			createCell("Test MTEF directed", "", 1.000000),
			createCell("Unvalidated activity", "", 1.000000),
			createCell("activity 1 with agreement", "Glodeni", 1.000000),
			createCell("activity with capital spending", "", 1.000000),
			createCell("activity with components", "", 1.000000),
			createCell("activity with contracting agency", "Apareni", 0.550000),
			createCell("activity with contracting agency", "Slobozia", 0.450000),
			createCell("activity with directed MTEFs", "", 1.000000),
			createCell("activity with funded components", "", 1.000000),
			createCell("activity with incomplete agreement", "", 1.000000),
			createCell("activity with many MTEFs", "", 1.000000),
			createCell("activity with pipeline MTEFs and act. disb", "", 1.000000),
			createCell("activity with primary_program", "", 1.000000),
			createCell("activity with tertiary_program", "", 1.000000),
			createCell("activity-with-unfunded-components", "Tiraspol", 1.000000),
			createCell("activity_with_disaster_response", "", 1.000000),
			createCell("crazy funding 1", "", 1.000000),
			createCell("date-filters-activity", "", 1.000000),
			createCell("execution rate activity", "", 0.500000),
			createCell("execution rate activity", "", 0.500000),
			createCell("mtef activity 1", "", 1.000000),
			createCell("mtef activity 2", "", 1.000000),
			createCell("pledged 2", "", 1.000000),
			createCell("pledged education activity 1", "", 1.000000),
			createCell("ptc activity 1", "", 1.000000),
			createCell("ptc activity 2", "", 1.000000),
			createCell("third activity with agreements", "", 1.000000),
			createCell("with weird currencies", "", 1.000000)
);
	}

}
