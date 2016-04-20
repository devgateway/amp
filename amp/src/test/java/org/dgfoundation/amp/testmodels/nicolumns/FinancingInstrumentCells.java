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
			cell("activity 1 with agreement", "default financing instrument", 2120),
			cell("Activity 2 with multiple agreements", "default financing instrument", 2120),
			cell("Activity 2 with multiple agreements", "second financing instrument", 2125),
			cell("Activity Linked With Pledge", "default financing instrument", 2120),
			cell("Activity with both MTEFs and Act.Comms", "second financing instrument", 2125),
			cell("activity with capital spending", "second financing instrument", 2125),
			cell("activity with contracting agency", "second financing instrument", 2125),
			cell("activity with directed MTEFs", "default financing instrument", 2120),
			cell("activity with directed MTEFs", "second financing instrument", 2125),
			cell("activity with funded components", "second financing instrument", 2125),
			cell("activity with incomplete agreement", "second financing instrument", 2125),
			cell("activity with many MTEFs", "default financing instrument", 2120),
			cell("activity with many MTEFs", "second financing instrument", 2125),
			cell("activity with pipeline MTEFs and act. disb", "second financing instrument", 2125),
			cell("Activity with planned disbursements", "default financing instrument", 2120),
			cell("activity with primary_program", "default financing instrument", 2120),
			cell("Activity with primary_tertiary_program", "default financing instrument", 2120),
			cell("activity with tertiary_program", "default financing instrument", 2120),
			cell("Activity with Zones", "default financing instrument", 2120),
			cell("Activity With Zones and Percentages", "default financing instrument", 2120),
			cell("activity-with-unfunded-components", "second financing instrument", 2125),
			cell("activity_with_disaster_response", "second financing instrument", 2125),
			cell("crazy funding 1", "default financing instrument", 2120),
			cell("crazy funding 1", "second financing instrument", 2125),
			cell("date-filters-activity", "default financing instrument", 2120),
			cell("Eth Water", "default financing instrument", 2120),
			cell("Eth Water", "second financing instrument", 2125),
			cell("execution rate activity", "second financing instrument", 2125),
			cell("mtef activity 1", "default financing instrument", 2120),
			cell("mtef activity 2", "default financing instrument", 2120),
			cell("new activity with contracting", "default financing instrument", 2120),
			cell("pledged 2", "second financing instrument", 2125),
			cell("pledged education activity 1", "default financing instrument", 2120),
			cell("ptc activity 1", "default financing instrument", 2120),
			cell("ptc activity 2", "default financing instrument", 2120),
			cell("Pure MTEF Project", "default financing instrument", 2120),
			cell("SSC Project 1", "default financing instrument", 2120),
			cell("SSC Project 2", "default financing instrument", 2120),
			cell("SubNational no percentages", "default financing instrument", 2120),
			cell("TAC_activity_1", "default financing instrument", 2120),
			cell("TAC_activity_2", "default financing instrument", 2120),
			cell("Test MTEF directed", "default financing instrument", 2120),
			cell("third activity with agreements", "default financing instrument", 2120),
			cell("Unvalidated activity", "second financing instrument", 2125),
			cell("with weird currencies", "second financing instrument", 2125)
);
	}

}
