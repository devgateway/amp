package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;


public class StatusCells extends HardcodedCells<TextCell>{

	public StatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dimension) {
		super(activityNames, entityNames, degenerate(dimension, "status"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "second status"),
			cell("Activity Linked With Pledge", "default status"),
			cell("Activity With Zones and Percentages", "default status"),
			cell("Activity with Zones", "default status"),
			cell("Activity with both MTEFs and Act.Comms", "second status"),
			cell("Activity with planned disbursements", "default status"),
			cell("Activity with primary_tertiary_program", "default status"),
			cell("Eth Water", "second status"),
			cell("Project with documents", "default status"),
			cell("Proposed Project Cost 1 - USD", "default status"),
			cell("Proposed Project Cost 2 - EUR", "default status"),
			cell("Pure MTEF Project", "default status"),
			cell("SSC Project 1", "default status"),
			cell("SSC Project 2", "default status"),
			cell("SubNational no percentages", "default status"),
			cell("TAC_activity_1", "default status"),
			cell("TAC_activity_2", "default status"),
			cell("Test MTEF directed", "default status"),
			cell("Unvalidated activity", "default status"),
			cell("activity 1 with agreement", "default status"),
			cell("activity with capital spending", "default status"),
			cell("activity with components", "default status"),
			cell("activity with contracting agency", "default status"),
			cell("activity with directed MTEFs", "default status"),
			cell("activity with funded components", "default status"),
			cell("activity with incomplete agreement", "default status"),
			cell("activity with many MTEFs", "default status"),
			cell("activity with pipeline MTEFs and act. disb", "default status"),
			cell("activity with primary_program", "default status"),
			cell("activity with tertiary_program", "default status"),
			cell("activity-with-unfunded-components", "default status"),
			cell("activity_with_disaster_response", "default status"),
			cell("crazy funding 1", "default status"),
			cell("date-filters-activity", "default status"),
			cell("execution rate activity", "second status"),
			cell("mtef activity 1", "default status"),
			cell("mtef activity 2", "default status"),
			cell("new activity with contracting", "second status"),
			cell("pledged 2", "default status"),
			cell("pledged education activity 1", "default status"),
			cell("ptc activity 1", "default status"),
			cell("ptc activity 2", "default status"),
			cell("third activity with agreements", "default status"),
			cell("with weird currencies", "default status")
);
	}

}
