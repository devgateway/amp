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
			cell("activity 1 with agreement", "Provincial", 69),
			cell("Activity 2 with multiple agreements", "Provincial", 69),
			cell("Activity Linked With Pledge", "Provincial", 69),
			cell("Activity with both MTEFs and Act.Comms", "Provincial", 69),
			cell("activity with capital spending", "Provincial", 69),
			cell("activity with components", "Provincial", 69),
			cell("activity with contracting agency", "Provincial", 69),
			cell("activity with directed MTEFs", "Provincial", 69),
			cell("activity with funded components", "National", 70),
			cell("activity with incomplete agreement", "National", 70),
			cell("activity with many MTEFs", "Provincial", 69),
			cell("activity with pipeline MTEFs and act. disb", "Provincial", 69),
			cell("Activity with planned disbursements", "National", 70),
			cell("activity with primary_program", "National", 70),
			cell("Activity with primary_tertiary_program", "National", 70),
			cell("activity with tertiary_program", "National", 70),
			cell("Activity with Zones", "Provincial", 69),
			cell("Activity With Zones and Percentages", "Provincial", 69),
			cell("activity-with-unfunded-components", "Provincial", 69),
			cell("activity_with_disaster_response", "National", 70),
			cell("crazy funding 1", "Provincial", 69),
			cell("date-filters-activity", "National", 70),
			cell("Eth Water", "Provincial", 69),
			cell("execution rate activity", "Provincial", 69),
			cell("mtef activity 1", "National", 70),
			cell("mtef activity 2", "Provincial", 69),
			cell("pledged 2", "Provincial", 69),
			cell("pledged education activity 1", "Provincial", 69),
			cell("Project with documents", "Provincial", 69),
			cell("Proposed Project Cost 1 - USD", "Provincial", 69),
			cell("Proposed Project Cost 2 - EUR", "Provincial", 69),
			cell("ptc activity 1", "Provincial", 69),
			cell("ptc activity 2", "Provincial", 69),
			cell("Pure MTEF Project", "Provincial", 69),
			cell("SSC Project 1", "Provincial", 69),
			cell("SSC Project 2", "Provincial", 69),
			cell("SubNational no percentages", "Provincial", 69),
			cell("TAC_activity_1", "Provincial", 69),
			cell("TAC_activity_2", "Provincial", 69),
			cell("Test MTEF directed", "Provincial", 69),
			cell("third activity with agreements", "Provincial", 69),
			cell("Unvalidated activity", "National", 70),
			cell("with weird currencies", "National", 70)
);
	}

}
