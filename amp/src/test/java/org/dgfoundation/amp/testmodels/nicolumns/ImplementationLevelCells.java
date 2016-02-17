package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;


public class ImplementationLevelCells extends HardcodedCells<TextCell>{

	public ImplementationLevelCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim) {
		super(activityNames, entityNames, degenerate(dim, "impl_level"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "Provincial"),
			cell("Activity Linked With Pledge", "Provincial"),
			cell("Activity With Zones and Percentages", "Provincial"),
			cell("Activity with Zones", "Provincial"),
			cell("Activity with both MTEFs and Act.Comms", "Provincial"),
			cell("Activity with planned disbursements", "National"),
			cell("Activity with primary_tertiary_program", "National"),
			cell("Eth Water", "Provincial"),
			cell("Project with documents", "Provincial"),
			cell("Proposed Project Cost 1 - USD", "Provincial"),
			cell("Proposed Project Cost 2 - EUR", "Provincial"),
			cell("Pure MTEF Project", "Provincial"),
			cell("SSC Project 1", "Provincial"),
			cell("SSC Project 2", "Provincial"),
			cell("SubNational no percentages", "Provincial"),
			cell("TAC_activity_1", "Provincial"),
			cell("TAC_activity_2", "Provincial"),
			cell("Test MTEF directed", "Provincial"),
			cell("Unvalidated activity", "National"),
			cell("activity 1 with agreement", "Provincial"),
			cell("activity with capital spending", "Provincial"),
			cell("activity with components", "Provincial"),
			cell("activity with contracting agency", "Provincial"),
			cell("activity with directed MTEFs", "Provincial"),
			cell("activity with funded components", "National"),
			cell("activity with incomplete agreement", "National"),
			cell("activity with many MTEFs", "Provincial"),
			cell("activity with pipeline MTEFs and act. disb", "Provincial"),
			cell("activity with primary_program", "National"),
			cell("activity with tertiary_program", "National"),
			cell("activity-with-unfunded-components", "Provincial"),
			cell("activity_with_disaster_response", "National"),
			cell("crazy funding 1", "Provincial"),
			cell("date-filters-activity", "National"),
			cell("execution rate activity", "Provincial"),
			cell("mtef activity 1", "National"),
			cell("mtef activity 2", "Provincial"),
			cell("pledged 2", "Provincial"),
			cell("pledged education activity 1", "Provincial"),
			cell("ptc activity 1", "Provincial"),
			cell("ptc activity 2", "Provincial"),
			cell("third activity with agreements", "Provincial"),
			cell("with weird currencies", "National")
);
	}

}
