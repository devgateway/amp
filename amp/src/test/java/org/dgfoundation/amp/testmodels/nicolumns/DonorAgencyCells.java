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
			cell("activity 1 with agreement", "Finland", 21698),
			cell("Activity 2 with multiple agreements", "UNDP", 21695),
			cell("Activity 2 with multiple agreements", "USAID", 21696),
			cell("Activity Linked With Pledge", "Finland", 21698),
			cell("Activity with both MTEFs and Act.Comms", "Finland", 21698),
			cell("activity with capital spending", "Finland", 21698),
			cell("activity with contracting agency", "Finland", 21698),
			cell("activity with directed MTEFs", "Ministry of Economy", 21700),
			cell("activity with funded components", "UNDP", 21695),
			cell("activity with incomplete agreement", "Finland", 21698),
			cell("activity with many MTEFs", "Finland", 21698),
			cell("activity with many MTEFs", "USAID", 21696),
			cell("activity with pipeline MTEFs and act. disb", "UNDP", 21695),
			cell("Activity with planned disbursements", "Norway", 21694),
			cell("Activity with planned disbursements", "USAID", 21696),
			cell("activity with primary_program", "World Bank", 21697),
			cell("Activity with primary_tertiary_program", "UNDP", 21695),
			cell("activity with tertiary_program", "Ministry of Economy", 21700),
			cell("Activity with Zones", "Finland", 21698),
			cell("Activity With Zones and Percentages", "Norway", 21694),
			cell("activity-with-unfunded-components", "Finland", 21698),
			cell("activity_with_disaster_response", "Finland", 21698),
			cell("crazy funding 1", "Finland", 21698),
			cell("crazy funding 1", "Finland", 21698),
			cell("date-filters-activity", "Ministry of Finance", 21699),
			cell("Eth Water", "Finland", 21698),
			cell("Eth Water", "Norway", 21694),
			cell("Eth Water", "USAID", 21696),
			cell("execution rate activity", "Ministry of Finance", 21699),
			cell("mtef activity 1", "UNDP", 21695),
			cell("mtef activity 2", "Finland", 21698),
			cell("new activity with contracting", "Finland", 21698),
			cell("pledged 2", "USAID", 21696),
			cell("pledged education activity 1", "USAID", 21696),
			cell("ptc activity 1", "Finland", 21698),
			cell("ptc activity 2", "USAID", 21696),
			cell("Pure MTEF Project", "Ministry of Finance", 21699),
			cell("SSC Project 1", "Finland", 21698),
			cell("SSC Project 2", "Water Org", 21701),
			cell("SubNational no percentages", "Ministry of Economy", 21700),
			cell("TAC_activity_1", "World Bank", 21697),
			cell("TAC_activity_2", "Water Foundation", 21702),
			cell("Test MTEF directed", "Ministry of Economy", 21700),
			cell("third activity with agreements", "Ministry of Finance", 21699),
			cell("Unvalidated activity", "UNDP", 21695),
			cell("with weird currencies", "Ministry of Finance", 21699));
	}

}
