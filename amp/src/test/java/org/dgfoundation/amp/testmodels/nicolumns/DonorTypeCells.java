package org.dgfoundation.amp.testmodels.nicolumns;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import org.dgfoundation.amp.nireports.TextCell;


public class DonorTypeCells extends HardcodedCells<TextCell>{

	public DonorTypeCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
		cell("activity 1 with agreement", "Default", 38),
		cell("Activity 2 with multiple agreements", "Default", 38),
		cell("Activity 2 with multiple agreements", "Default", 38),
		cell("Activity Linked With Pledge", "Default", 38),
		cell("Activity with both MTEFs and Act.Comms", "Default", 38),
		cell("activity with capital spending", "Default", 38),
		cell("activity with contracting agency", "Default", 38),
		cell("activity with directed MTEFs", "Default", 38),
		cell("activity with funded components", "Default", 38),
		cell("activity with incomplete agreement", "Default", 38),
		cell("activity with many MTEFs", "Default", 38),
		cell("activity with many MTEFs", "Default", 38),
		cell("activity with pipeline MTEFs and act. disb", "Default", 38),
		cell("Activity with planned disbursements", "Default", 38),
		cell("Activity with planned disbursements", "Default", 38),
		cell("activity with primary_program", "Default", 38),
		cell("Activity with primary_tertiary_program", "Default", 38),
		cell("activity with tertiary_program", "Default", 38),
		cell("Activity with Zones", "Default", 38),
		cell("Activity With Zones and Percentages", "Default", 38),
		cell("activity-with-unfunded-components", "Default", 38),
		cell("activity_with_disaster_response", "Default", 38),
		cell("crazy funding 1", "Default", 38),
		cell("crazy funding 1", "Default", 38),
		cell("date-filters-activity", "Default", 38),
		cell("Eth Water", "Default", 38),
		cell("Eth Water", "Default", 38),
		cell("Eth Water", "Default", 38),
		cell("execution rate activity", "Default", 38),
		cell("mtef activity 1", "Default", 38),
		cell("mtef activity 2", "Default", 38),
		cell("new activity with contracting", "Default", 38),
		cell("pledged 2", "Default", 38),
		cell("pledged education activity 1", "Default", 38),
		cell("ptc activity 1", "Default", 38),
		cell("ptc activity 2", "Default", 38),
		cell("Pure MTEF Project", "Default", 38),
		cell("SSC Project 1", "Default", 38),
		cell("SSC Project 2", "Default", 38),
		cell("SubNational no percentages", "Default", 38),
		cell("TAC_activity_1", "Default", 38),
		cell("TAC_activity_2", "Default", 38),
		cell("Test MTEF directed", "Default", 38),
		cell("third activity with agreements", "Default", 38),
		cell("Unvalidated activity", "Default", 38),
		cell("with weird currencies", "Default", 38)
);
	}

}
