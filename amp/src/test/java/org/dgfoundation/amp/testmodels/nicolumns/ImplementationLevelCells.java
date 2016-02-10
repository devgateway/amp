package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class ImplementationLevelCells extends HardcodedCells<TextCell>{

	public ImplementationLevelCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "Provincial"),
			createCell("Activity Linked With Pledge", "Provincial"),
			createCell("Activity With Zones and Percentages", "Provincial"),
			createCell("Activity with Zones", "Provincial"),
			createCell("Activity with both MTEFs and Act.Comms", "Provincial"),
			createCell("Activity with planned disbursements", "National"),
			createCell("Activity with primary_tertiary_program", "National"),
			createCell("Eth Water", "Provincial"),
			createCell("Project with documents", "Provincial"),
			createCell("Proposed Project Cost 1 - USD", "Provincial"),
			createCell("Proposed Project Cost 2 - EUR", "Provincial"),
			createCell("Pure MTEF Project", "Provincial"),
			createCell("SSC Project 1", "Provincial"),
			createCell("SSC Project 2", "Provincial"),
			createCell("SubNational no percentages", "Provincial"),
			createCell("TAC_activity_1", "Provincial"),
			createCell("TAC_activity_2", "Provincial"),
			createCell("Test MTEF directed", "Provincial"),
			createCell("Unvalidated activity", "National"),
			createCell("activity 1 with agreement", "Provincial"),
			createCell("activity with capital spending", "Provincial"),
			createCell("activity with components", "Provincial"),
			createCell("activity with contracting agency", "Provincial"),
			createCell("activity with directed MTEFs", "Provincial"),
			createCell("activity with funded components", "National"),
			createCell("activity with incomplete agreement", "National"),
			createCell("activity with many MTEFs", "Provincial"),
			createCell("activity with pipeline MTEFs and act. disb", "Provincial"),
			createCell("activity with primary_program", "National"),
			createCell("activity with tertiary_program", "National"),
			createCell("activity-with-unfunded-components", "Provincial"),
			createCell("activity_with_disaster_response", "National"),
			createCell("crazy funding 1", "Provincial"),
			createCell("date-filters-activity", "National"),
			createCell("execution rate activity", "Provincial"),
			createCell("mtef activity 1", "National"),
			createCell("mtef activity 2", "Provincial"),
			createCell("pledged 2", "Provincial"),
			createCell("pledged education activity 1", "Provincial"),
			createCell("ptc activity 1", "Provincial"),
			createCell("ptc activity 2", "Provincial"),
			createCell("third activity with agreements", "Provincial"),
			createCell("with weird currencies", "National")
);
	}

}
