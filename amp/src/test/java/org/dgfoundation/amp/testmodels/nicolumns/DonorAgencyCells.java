package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;


public class DonorAgencyCells extends HardcodedCells<TextCell>{

	public DonorAgencyCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			cell("Activity 2 with multiple agreements", "UNDP"),
			cell("Activity 2 with multiple agreements", "USAID"),
			cell("Activity Linked With Pledge", "Finland"),
			cell("Activity With Zones and Percentages", "Norway"),
			cell("Activity with Zones", "Finland"),
			cell("Activity with both MTEFs and Act.Comms", "Finland"),
			cell("Activity with planned disbursements", "Norway"),
			cell("Activity with planned disbursements", "USAID"),
			cell("Activity with primary_tertiary_program", "UNDP"),
			cell("Eth Water", "Finland"),
			cell("Eth Water", "Norway"),
			cell("Eth Water", "USAID"),
			cell("Pure MTEF Project", "Ministry of Finance"),
			cell("SSC Project 1", "Finland"),
			cell("SSC Project 2", "Water Org"),
			cell("SubNational no percentages", "Ministry of Economy"),
			cell("TAC_activity_1", "World Bank"),
			cell("TAC_activity_2", "Water Foundation"),
			cell("Test MTEF directed", "Ministry of Economy"),
			cell("Unvalidated activity", "UNDP"),
			cell("activity 1 with agreement", "Finland"),
			cell("activity with capital spending", "Finland"),
			cell("activity with contracting agency", "Finland"),
			cell("activity with directed MTEFs", "Ministry of Economy"),
			cell("activity with funded components", "UNDP"),
			cell("activity with incomplete agreement", "Finland"),
			cell("activity with many MTEFs", "Finland"),
			cell("activity with many MTEFs", "USAID"),
			cell("activity with pipeline MTEFs and act. disb", "UNDP"),
			cell("activity with primary_program", "World Bank"),
			cell("activity with tertiary_program", "Ministry of Economy"),
			cell("activity-with-unfunded-components", "Finland"),
			cell("activity_with_disaster_response", "Finland"),
			cell("crazy funding 1", "Finland"),
			cell("crazy funding 1", "Finland"),
			cell("date-filters-activity", "Ministry of Finance"),
			cell("execution rate activity", "Ministry of Finance"),
			cell("mtef activity 1", "UNDP"),
			cell("mtef activity 2", "Finland"),
			cell("new activity with contracting", "Finland"),
			cell("pledged 2", "USAID"),
			cell("pledged education activity 1", "USAID"),
			cell("ptc activity 1", "Finland"),
			cell("ptc activity 2", "USAID"),
			cell("third activity with agreements", "Ministry of Finance"),
			cell("with weird currencies", "Ministry of Finance")
);
	}

}
