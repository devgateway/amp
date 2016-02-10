package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.TextCell;


public class DonorAgencyCells extends HardcodedCells<TextCell>{

	public DonorAgencyCells(Map<String, Long> activityNames, Map<String, Long> entityNames) {
		super(activityNames, entityNames);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
			createCell("Activity 2 with multiple agreements", "UNDP"),
			createCell("Activity 2 with multiple agreements", "USAID"),
			createCell("Activity Linked With Pledge", "Finland"),
			createCell("Activity With Zones and Percentages", "Norway"),
			createCell("Activity with Zones", "Finland"),
			createCell("Activity with both MTEFs and Act.Comms", "Finland"),
			createCell("Activity with planned disbursements", "Norway"),
			createCell("Activity with planned disbursements", "USAID"),
			createCell("Activity with primary_tertiary_program", "UNDP"),
			createCell("Eth Water", "Finland"),
			createCell("Eth Water", "Norway"),
			createCell("Eth Water", "USAID"),
			createCell("Pure MTEF Project", "Ministry of Finance"),
			createCell("SSC Project 1", "Finland"),
			createCell("SSC Project 2", "Water Org"),
			createCell("SubNational no percentages", "Ministry of Economy"),
			createCell("TAC_activity_1", "World Bank"),
			createCell("TAC_activity_2", "Water Foundation"),
			createCell("Test MTEF directed", "Ministry of Economy"),
			createCell("Unvalidated activity", "UNDP"),
			createCell("activity 1 with agreement", "Finland"),
			createCell("activity with capital spending", "Finland"),
			createCell("activity with contracting agency", "Finland"),
			createCell("activity with directed MTEFs", "Ministry of Economy"),
			createCell("activity with funded components", "UNDP"),
			createCell("activity with incomplete agreement", "Finland"),
			createCell("activity with many MTEFs", "Finland"),
			createCell("activity with many MTEFs", "USAID"),
			createCell("activity with pipeline MTEFs and act. disb", "UNDP"),
			createCell("activity with primary_program", "World Bank"),
			createCell("activity with tertiary_program", "Ministry of Economy"),
			createCell("activity-with-unfunded-components", "Finland"),
			createCell("activity_with_disaster_response", "Finland"),
			createCell("crazy funding 1", "Finland"),
			createCell("crazy funding 1", "Finland"),
			createCell("date-filters-activity", "Ministry of Finance"),
			createCell("execution rate activity", "Ministry of Finance"),
			createCell("mtef activity 1", "UNDP"),
			createCell("mtef activity 2", "Finland"),
			createCell("new activity with contracting", "Finland"),
			createCell("pledged 2", "USAID"),
			createCell("pledged education activity 1", "USAID"),
			createCell("ptc activity 1", "Finland"),
			createCell("ptc activity 2", "USAID"),
			createCell("third activity with agreements", "Ministry of Finance"),
			createCell("with weird currencies", "Ministry of Finance")
);
	}

}
