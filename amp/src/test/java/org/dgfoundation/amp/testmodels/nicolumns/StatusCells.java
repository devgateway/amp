package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class StatusCells extends HardcodedCells<TextCell>{

	public StatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "second status"),
			createCell("Activity Linked With Pledge", "default status"),
			createCell("Activity With Zones and Percentages", "default status"),
			createCell("Activity with Zones", "default status"),
			createCell("Activity with both MTEFs and Act.Comms", "second status"),
			createCell("Activity with planned disbursements", "default status"),
			createCell("Activity with primary_tertiary_program", "default status"),
			createCell("Eth Water", "second status"),
			createCell("Project with documents", "default status"),
			createCell("Proposed Project Cost 1 - USD", "default status"),
			createCell("Proposed Project Cost 2 - EUR", "default status"),
			createCell("Pure MTEF Project", "default status"),
			createCell("SSC Project 1", "default status"),
			createCell("SSC Project 2", "default status"),
			createCell("SubNational no percentages", "default status"),
			createCell("TAC_activity_1", "default status"),
			createCell("TAC_activity_2", "default status"),
			createCell("Test MTEF directed", "default status"),
			createCell("Unvalidated activity", "default status"),
			createCell("activity 1 with agreement", "default status"),
			createCell("activity with capital spending", "default status"),
			createCell("activity with components", "default status"),
			createCell("activity with contracting agency", "default status"),
			createCell("activity with directed MTEFs", "default status"),
			createCell("activity with funded components", "default status"),
			createCell("activity with incomplete agreement", "default status"),
			createCell("activity with many MTEFs", "default status"),
			createCell("activity with pipeline MTEFs and act. disb", "default status"),
			createCell("activity with primary_program", "default status"),
			createCell("activity with tertiary_program", "default status"),
			createCell("activity-with-unfunded-components", "default status"),
			createCell("activity_with_disaster_response", "default status"),
			createCell("crazy funding 1", "default status"),
			createCell("date-filters-activity", "default status"),
			createCell("execution rate activity", "second status"),
			createCell("mtef activity 1", "default status"),
			createCell("mtef activity 2", "default status"),
			createCell("new activity with contracting", "second status"),
			createCell("pledged 2", "default status"),
			createCell("pledged education activity 1", "default status"),
			createCell("ptc activity 1", "default status"),
			createCell("ptc activity 2", "default status"),
			createCell("third activity with agreements", "default status"),
			createCell("with weird currencies", "default status")
);
	}

}
