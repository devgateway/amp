package org.dgfoundation.amp.testmodels.nicolumns;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.TextCell;


public class DonorGroupCells extends HardcodedCells<TextCell>{

	public DonorGroupCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
		cell("activity 1 with agreement", "Default Group", 17),
		cell("Activity 2 with multiple agreements", "American", 19),
		cell("Activity 2 with multiple agreements", "International", 20),
		cell("Activity Linked With Pledge", "Default Group", 17),
		cell("Activity with both MTEFs and Act.Comms", "Default Group", 17),
		cell("activity with capital spending", "Default Group", 17),
		cell("activity with contracting agency", "Default Group", 17),
		cell("activity with directed MTEFs", "National", 21),
		cell("activity with funded components", "International", 20),
		cell("activity with incomplete agreement", "Default Group", 17),
		cell("activity with many MTEFs", "American", 19),
		cell("activity with many MTEFs", "Default Group", 17),
		cell("activity with pipeline MTEFs and act. disb", "International", 20),
		cell("Activity with planned disbursements", "American", 19),
		cell("Activity with planned disbursements", "European", 18),
		cell("activity with primary_program", "International", 20),
		cell("Activity with primary_tertiary_program", "International", 20),
		cell("activity with tertiary_program", "National", 21),
		cell("Activity with Zones", "Default Group", 17),
		cell("Activity With Zones and Percentages", "European", 18),
		cell("activity-with-unfunded-components", "Default Group", 17),
		cell("activity_with_disaster_response", "Default Group", 17),
		cell("crazy funding 1", "Default Group", 17),
		cell("crazy funding 1", "Default Group", 17),
		cell("date-filters-activity", "National", 21),
		cell("Eth Water", "American", 19),
		cell("Eth Water", "Default Group", 17),
		cell("Eth Water", "European", 18),
		cell("execution rate activity", "National", 21),
		cell("mtef activity 1", "International", 20),
		cell("mtef activity 2", "Default Group", 17),
		cell("new activity with contracting", "Default Group", 17),
		cell("pledged 2", "American", 19),
		cell("pledged education activity 1", "American", 19),
		cell("ptc activity 1", "Default Group", 17),
		cell("ptc activity 2", "American", 19),
		cell("Pure MTEF Project", "National", 21),
		cell("SSC Project 1", "Default Group", 17),
		cell("SSC Project 2", "American", 19),
		cell("SubNational no percentages", "National", 21),
		cell("TAC_activity_1", "International", 20),
		cell("TAC_activity_2", "American", 19),
		cell("Test MTEF directed", "National", 21),
		cell("third activity with agreements", "National", 21),
		cell("Unvalidated activity", "International", 20),
		cell("with weird currencies", "National", 21)
);
	}

}
