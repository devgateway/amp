package org.dgfoundation.amp.testmodels.nicolumns;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.TextCell;


public class TypeOfAssistanceCells extends HardcodedCells<TextCell>{

	public TypeOfAssistanceCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim) {
		super(activityNames, entityNames, degenerate(dim, "type_of_assistance"));
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("activity 1 with agreement", "default type of assistance", 2119),
			cell("Activity 2 with multiple agreements", "default type of assistance", 2119),
			cell("Activity Linked With Pledge", "default type of assistance", 2119),
			cell("Activity with both MTEFs and Act.Comms", "default type of assistance", 2119),
			cell("activity with capital spending", "second type of assistance", 2124),
			cell("activity with contracting agency", "default type of assistance", 2119),
			cell("activity with directed MTEFs", "default type of assistance", 2119),
			cell("activity with directed MTEFs", "second type of assistance", 2124),
			cell("activity with funded components", "default type of assistance", 2119),
			cell("activity with incomplete agreement", "default type of assistance", 2119),
			cell("activity with many MTEFs", "default type of assistance", 2119),
			cell("activity with pipeline MTEFs and act. disb", "default type of assistance", 2119),
			cell("Activity with planned disbursements", "default type of assistance", 2119),
			cell("activity with primary_program", "default type of assistance", 2119),
			cell("Activity with primary_tertiary_program", "default type of assistance", 2119),
			cell("activity with tertiary_program", "default type of assistance", 2119),
			cell("Activity with Zones", "default type of assistance", 2119),
			cell("Activity With Zones and Percentages", "default type of assistance", 2119),
			cell("activity-with-unfunded-components", "second type of assistance", 2124),
			cell("activity_with_disaster_response", "default type of assistance", 2119),
			cell("crazy funding 1", "default type of assistance", 2119),
			cell("crazy funding 1", "second type of assistance", 2124),
			cell("date-filters-activity", "default type of assistance", 2119),
			cell("Eth Water", "default type of assistance", 2119),
			cell("Eth Water", "second type of assistance", 2124),
			cell("execution rate activity", "default type of assistance", 2119),
			cell("mtef activity 1", "default type of assistance", 2119),
			cell("mtef activity 2", "default type of assistance", 2119),
			cell("new activity with contracting", "default type of assistance", 2119),
			cell("pledged 2", "second type of assistance", 2124),
			cell("pledged education activity 1", "default type of assistance", 2119),
			cell("ptc activity 1", "default type of assistance", 2119),
			cell("ptc activity 2", "default type of assistance", 2119),
			cell("Pure MTEF Project", "default type of assistance", 2119),
			cell("SSC Project 1", "default type of assistance", 2119),
			cell("SSC Project 2", "default type of assistance", 2119),
			cell("SubNational no percentages", "default type of assistance", 2119),
			cell("TAC_activity_1", "default type of assistance", 2119),
			cell("TAC_activity_2", "default type of assistance", 2119),
			cell("Test MTEF directed", "default type of assistance", 2119),
			cell("third activity with agreements", "default type of assistance", 2119),
			cell("Unvalidated activity", "default type of assistance", 2119),
			cell("with weird currencies", "default type of assistance", 2119)
);
	}

}