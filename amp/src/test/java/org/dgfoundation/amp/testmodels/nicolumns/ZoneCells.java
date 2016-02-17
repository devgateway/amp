package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class ZoneCells extends HardcodedCells<PercentageTextCell>{

	public ZoneCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "", 1.000000),
			cell("Activity Linked With Pledge", "", 1.000000),
			cell("Activity With Zones and Percentages", "Dolboaca", 0.200000),
			cell("Activity With Zones and Percentages", "Glodeni", 0.800000),
			cell("Activity with Zones", "Bulboaca", 0.500000),
			cell("Activity with Zones", "Glodeni", 0.500000),
			cell("Activity with both MTEFs and Act.Comms", "", 0.300000),
			cell("Activity with both MTEFs and Act.Comms", "", 0.700000),
			cell("Activity with planned disbursements", "", 1.000000),
			cell("Activity with primary_tertiary_program", "", 1.000000),
			cell("Eth Water", "", 1.000000),
			cell("Project with documents", "", 1.000000),
			cell("Proposed Project Cost 1 - USD", "", 1.000000),
			cell("Proposed Project Cost 2 - EUR", "", 1.000000),
			cell("Pure MTEF Project", "", 1.000000),
			cell("SSC Project 1", "", 1.000000),
			cell("SSC Project 2", "", 1.000000),
			cell("SubNational no percentages", "", 0.500000),
			cell("SubNational no percentages", "Glodeni", 0.500000),
			cell("TAC_activity_1", "", 1.000000),
			cell("TAC_activity_2", "", 1.000000),
			cell("Test MTEF directed", "", 1.000000),
			cell("Unvalidated activity", "", 1.000000),
			cell("activity 1 with agreement", "Glodeni", 1.000000),
			cell("activity with capital spending", "", 1.000000),
			cell("activity with components", "", 1.000000),
			cell("activity with contracting agency", "Apareni", 0.550000),
			cell("activity with contracting agency", "Slobozia", 0.450000),
			cell("activity with directed MTEFs", "", 1.000000),
			cell("activity with funded components", "", 1.000000),
			cell("activity with incomplete agreement", "", 1.000000),
			cell("activity with many MTEFs", "", 1.000000),
			cell("activity with pipeline MTEFs and act. disb", "", 1.000000),
			cell("activity with primary_program", "", 1.000000),
			cell("activity with tertiary_program", "", 1.000000),
			cell("activity-with-unfunded-components", "Tiraspol", 1.000000),
			cell("activity_with_disaster_response", "", 1.000000),
			cell("crazy funding 1", "", 1.000000),
			cell("date-filters-activity", "", 1.000000),
			cell("execution rate activity", "", 0.500000),
			cell("execution rate activity", "", 0.500000),
			cell("mtef activity 1", "", 1.000000),
			cell("mtef activity 2", "", 1.000000),
			cell("pledged 2", "", 1.000000),
			cell("pledged education activity 1", "", 1.000000),
			cell("ptc activity 1", "", 1.000000),
			cell("ptc activity 2", "", 1.000000),
			cell("third activity with agreements", "", 1.000000),
			cell("with weird currencies", "", 1.000000)
);
	}

}
