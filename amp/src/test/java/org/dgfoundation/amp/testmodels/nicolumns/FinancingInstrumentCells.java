package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class FinancingInstrumentCells extends HardcodedCells<TextCell>{

	public FinancingInstrumentCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "default financing instrument"),
			createCell("Activity 2 with multiple agreements", "second financing instrument"),
			createCell("Activity Linked With Pledge", "default financing instrument"),
			createCell("Activity With Zones and Percentages", "default financing instrument"),
			createCell("Activity with Zones", "default financing instrument"),
			createCell("Activity with both MTEFs and Act.Comms", "second financing instrument"),
			createCell("Activity with planned disbursements", "default financing instrument"),
			createCell("Activity with primary_tertiary_program", "default financing instrument"),
			createCell("Eth Water", "default financing instrument"),
			createCell("Eth Water", "second financing instrument"),
			createCell("Pure MTEF Project", "default financing instrument"),
			createCell("SSC Project 1", "default financing instrument"),
			createCell("SSC Project 2", "default financing instrument"),
			createCell("SubNational no percentages", "default financing instrument"),
			createCell("TAC_activity_1", "default financing instrument"),
			createCell("TAC_activity_2", "default financing instrument"),
			createCell("Test MTEF directed", "default financing instrument"),
			createCell("Unvalidated activity", "second financing instrument"),
			createCell("activity 1 with agreement", "default financing instrument"),
			createCell("activity with capital spending", "second financing instrument"),
			createCell("activity with contracting agency", "second financing instrument"),
			createCell("activity with directed MTEFs", "default financing instrument"),
			createCell("activity with directed MTEFs", "second financing instrument"),
			createCell("activity with funded components", "second financing instrument"),
			createCell("activity with incomplete agreement", "second financing instrument"),
			createCell("activity with many MTEFs", "default financing instrument"),
			createCell("activity with many MTEFs", "second financing instrument"),
			createCell("activity with pipeline MTEFs and act. disb", "second financing instrument"),
			createCell("activity with primary_program", "default financing instrument"),
			createCell("activity with tertiary_program", "default financing instrument"),
			createCell("activity-with-unfunded-components", "second financing instrument"),
			createCell("activity_with_disaster_response", "second financing instrument"),
			createCell("crazy funding 1", "default financing instrument"),
			createCell("crazy funding 1", "second financing instrument"),
			createCell("date-filters-activity", "default financing instrument"),
			createCell("execution rate activity", "second financing instrument"),
			createCell("mtef activity 1", "default financing instrument"),
			createCell("mtef activity 2", "default financing instrument"),
			createCell("new activity with contracting", "default financing instrument"),
			createCell("pledged 2", "second financing instrument"),
			createCell("pledged education activity 1", "default financing instrument"),
			createCell("ptc activity 1", "default financing instrument"),
			createCell("ptc activity 2", "default financing instrument"),
			createCell("third activity with agreements", "default financing instrument"),
			createCell("with weird currencies", "second financing instrument")
);
	}

}
