package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class TypeOfAssistanceCells extends HardcodedCells<TextCell>{

	public TypeOfAssistanceCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "default type of assistance"),
			createCell("Activity Linked With Pledge", "default type of assistance"),
			createCell("Activity With Zones and Percentages", "default type of assistance"),
			createCell("Activity with Zones", "default type of assistance"),
			createCell("Activity with both MTEFs and Act.Comms", "default type of assistance"),
			createCell("Activity with planned disbursements", "default type of assistance"),
			createCell("Activity with primary_tertiary_program", "default type of assistance"),
			createCell("Eth Water", "default type of assistance"),
			createCell("Eth Water", "second type of assistance"),
			createCell("Pure MTEF Project", "default type of assistance"),
			createCell("SSC Project 1", "default type of assistance"),
			createCell("SSC Project 2", "default type of assistance"),
			createCell("SubNational no percentages", "default type of assistance"),
			createCell("TAC_activity_1", "default type of assistance"),
			createCell("TAC_activity_2", "default type of assistance"),
			createCell("Test MTEF directed", "default type of assistance"),
			createCell("Unvalidated activity", "default type of assistance"),
			createCell("activity 1 with agreement", "default type of assistance"),
			createCell("activity with capital spending", "second type of assistance"),
			createCell("activity with contracting agency", "default type of assistance"),
			createCell("activity with directed MTEFs", "default type of assistance"),
			createCell("activity with directed MTEFs", "second type of assistance"),
			createCell("activity with funded components", "default type of assistance"),
			createCell("activity with incomplete agreement", "default type of assistance"),
			createCell("activity with many MTEFs", "default type of assistance"),
			createCell("activity with pipeline MTEFs and act. disb", "default type of assistance"),
			createCell("activity with primary_program", "default type of assistance"),
			createCell("activity with tertiary_program", "default type of assistance"),
			createCell("activity-with-unfunded-components", "second type of assistance"),
			createCell("activity_with_disaster_response", "default type of assistance"),
			createCell("crazy funding 1", "default type of assistance"),
			createCell("crazy funding 1", "second type of assistance"),
			createCell("date-filters-activity", "default type of assistance"),
			createCell("execution rate activity", "default type of assistance"),
			createCell("mtef activity 1", "default type of assistance"),
			createCell("mtef activity 2", "default type of assistance"),
			createCell("new activity with contracting", "default type of assistance"),
			createCell("pledged 2", "second type of assistance"),
			createCell("pledged education activity 1", "default type of assistance"),
			createCell("ptc activity 1", "default type of assistance"),
			createCell("ptc activity 2", "default type of assistance"),
			createCell("third activity with agreements", "default type of assistance"),
			createCell("with weird currencies", "default type of assistance")
);
	}

}
