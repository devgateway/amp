package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;


public class FinancingInstrumentCells extends HardcodedCells<TextCell>{

	public FinancingInstrumentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim) {
		super(activityNames, entityNames, degenerate(dim, "fin_instr"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "default financing instrument"),
			cell("Activity 2 with multiple agreements", "second financing instrument"),
			cell("Activity Linked With Pledge", "default financing instrument"),
			cell("Activity With Zones and Percentages", "default financing instrument"),
			cell("Activity with Zones", "default financing instrument"),
			cell("Activity with both MTEFs and Act.Comms", "second financing instrument"),
			cell("Activity with planned disbursements", "default financing instrument"),
			cell("Activity with primary_tertiary_program", "default financing instrument"),
			cell("Eth Water", "default financing instrument"),
			cell("Eth Water", "second financing instrument"),
			cell("Pure MTEF Project", "default financing instrument"),
			cell("SSC Project 1", "default financing instrument"),
			cell("SSC Project 2", "default financing instrument"),
			cell("SubNational no percentages", "default financing instrument"),
			cell("TAC_activity_1", "default financing instrument"),
			cell("TAC_activity_2", "default financing instrument"),
			cell("Test MTEF directed", "default financing instrument"),
			cell("Unvalidated activity", "second financing instrument"),
			cell("activity 1 with agreement", "default financing instrument"),
			cell("activity with capital spending", "second financing instrument"),
			cell("activity with contracting agency", "second financing instrument"),
			cell("activity with directed MTEFs", "default financing instrument"),
			cell("activity with directed MTEFs", "second financing instrument"),
			cell("activity with funded components", "second financing instrument"),
			cell("activity with incomplete agreement", "second financing instrument"),
			cell("activity with many MTEFs", "default financing instrument"),
			cell("activity with many MTEFs", "second financing instrument"),
			cell("activity with pipeline MTEFs and act. disb", "second financing instrument"),
			cell("activity with primary_program", "default financing instrument"),
			cell("activity with tertiary_program", "default financing instrument"),
			cell("activity-with-unfunded-components", "second financing instrument"),
			cell("activity_with_disaster_response", "second financing instrument"),
			cell("crazy funding 1", "default financing instrument"),
			cell("crazy funding 1", "second financing instrument"),
			cell("date-filters-activity", "default financing instrument"),
			cell("execution rate activity", "second financing instrument"),
			cell("mtef activity 1", "default financing instrument"),
			cell("mtef activity 2", "default financing instrument"),
			cell("new activity with contracting", "default financing instrument"),
			cell("pledged 2", "second financing instrument"),
			cell("pledged education activity 1", "default financing instrument"),
			cell("ptc activity 1", "default financing instrument"),
			cell("ptc activity 2", "default financing instrument"),
			cell("third activity with agreements", "default financing instrument"),
			cell("with weird currencies", "second financing instrument")
);
	}

}
